package com.desktopdirect.tcs;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.*;
import javax.net.ssl.*;

import java.security.cert.X509Certificate;

public class FatClientProxyAG extends FatClientProxy implements Runnable {

	// these must sync with server side fcp_server.h #defines
	private final byte FCP_VERSION_ID = 9;

	private final byte CLIENT_INITIATE = 0;
	private final byte VALIDATE_VERSION = 1;
	private final byte SET_MAPPED_HOSTS = 2;
	private final byte SET_LISTEN_PORTS = 3;
	// private final byte CONNECTION_OPENED = 4;
	private final byte CONNECTION_READY = 5;
	private final byte SEND_DATA = 6;
	private final byte CONNECTION_CLOSED = 7;
	private final byte START_WINREDIR = 8;
	private final byte WINREDIR_OPENED = 9;
	private final byte WINREDIR_RESOLVE = 10;
	private final byte SESSION_TIMEOUT = 11;
	private final byte SESSION_INVALID = 12;
	private final byte SESSION_KEEPALIVE = 13;
	private final byte FCP_HEADER_ACK = 14;
	private final byte START_TCS = 15;
	private final byte IDENTIFY_MODULE = 16;
	private final byte GET_SITEID = 17;
	private final byte GET_USERNAME = 18;
	private final byte GET_PASSWORD = 19;
	private final byte RESOLVE_HOSTNAME = 20;
	private final byte ACCEPT_CONNECTION = 21;
	private final byte FCP_GEN_LOG = 22;
	private final byte GET_TIME_INFO = 23;
	private final byte RESET_TIMERS = 25;

	private final byte FCP_HEADER_LEN = 16;

	private final byte ACTION_CODE_OFFSET = 0;
	private final byte LOCALHOSTIP_OFFSET = 4;
	private final byte LISTEN_PORT_OFFSET = 8;
	private final byte REMOTE_PORT_OFFSET = 10;
	private final byte DATA_LENGTH_OFFSET = 12;
	private final byte DATA_REGION_OFFSET = 15;

	// these values are from Windows (Visual Studio 6)
	public static final byte IPPROTO_TCP = 6; // always this for now
	public static final byte IPPROTO_UDP = 17;

	// these must sync with the winredir constants
	public static final byte WINREDIR_DATA_PORT = 0;
	public static final byte WINREDIR_DNS_HOSTS = 1; // not used yet
	public static final byte WINREDIR_IP_PORTS = 2;
	public static final byte WINREDIR_EXE_MD5 = 3;
	public static final byte WINREDIR_NEW_CONN = 4;
	public static final byte WINREDIR_KEEPALIVE = 5;
	public static final byte WINREDIR_SHUTDOWN = 6;
	public static final byte WINREDIR_CONFIGEND = 7;
	public static final byte WINREDIR_SP_IP = 8;
	public static final byte WINREDIR_HOSTNAME = 9;
	public static final byte WINREDIR_ADDRESS = 10;

	private String m_DevID;

	// tcsmng m_TCSMng;

	// convert from bytes in network order (BE) to int
	public int byteArrayToInt(byte[] bytes, int offset, int len, boolean rev) {
		int i;

		byte[] tmp = new byte[len];

		for (i = 0; i < len; i++) {
			tmp[i] = bytes[i + offset];
		}

		// return new BigInteger(tmp).intValue();
		if (rev) {
			return Integer.reverseBytes(bytes2int(bytes, offset, len));
		} else {
			return bytes2int(bytes, offset, len);
		}
	}

	public byte[] intToByteArray(int a, int len, boolean rev) {

		// byte[] tmp = BigInteger.valueOf(a).toByteArray();
		byte[] tmp;
		if (rev) {
			tmp = int2bytes(Integer.reverseBytes(a), len);
		} else {
			tmp = int2bytes(a, len);
		}
		int i;
		byte[] res = new byte[len];

		for (i = 0; i < tmp.length; i++) {
			res[i] = tmp[i];
		}

		while (i < len) {
			res[i] = 0;
			i++;
		}

		return res;
	}

	public FatClientProxyAG(String prmServer, int prmPort, String prmSessionID,
			String prmSessionCookie, String prmDevID) {

		super(prmServer, prmPort, prmSessionID, prmSessionCookie, "");

		m_DevID = prmDevID;
		// m_TCSMng = prmTCS;
	}

	public void init() {
		// initialize global variables to default values
		sslsock = null;
		keeper = null;

		username = null;
		password = null;
		domain = null;
		do_authentication = false;
		need_credentials = false;

		was_started = false;
		conn_alive = false;
		valid_session = true;
		ready_notified = false;

		importance_threshold = INFO_CRITICAL;

		// do general startup operations
		try {
			print_message("Initializing SSL...\n", INFO_NOTICE);

			// ready a thread for execution - this would be FCP_ServerThread
			// print_message("Spooling Up...\n", INFO_NOTICE);
			// server_thread = new Thread();

			server_thread = new Thread(this); // the thread to read from the SP
			server_thread.start();
			// server_thread.setPriority(Thread.MAX_PRIORITY);
			was_started = true; // the try succeeded
		} catch (Exception e) {
			print_message("Failure because: " + e.getMessage() + "\n",
					INFO_FAILURE);
		}
	}

	// construct the packet header
	private byte[] make_header(byte action_code, int local_ip, int listen_port,
			int remote_port, int len) {
		byte[] packet_header;
		byte[] temp;

		packet_header = new byte[16];
		packet_header[0] = action_code;
		packet_header[1] = 0;
		packet_header[2] = 0;
		packet_header[3] = 0;

		// temp = htonl2(local_ip);
		temp = intToByteArray(local_ip, 4, false);
		
		packet_header[4] = temp[3];
		packet_header[5] = temp[2];
		packet_header[6] = temp[1];
		packet_header[7] = temp[0];

		// temp = htons(listen_port);
		temp = intToByteArray(listen_port, 2, false);
		packet_header[8] = temp[0];
		packet_header[9] = temp[1];

		// temp = htons(remote_port);
		temp = intToByteArray(remote_port, 2, false);
		packet_header[10] = temp[0];
		packet_header[11] = temp[1];

		// temp = htonl(len);
		temp = intToByteArray(len, 4, false);
		packet_header[12] = temp[0];
		packet_header[13] = temp[1];
		packet_header[14] = temp[2];
		packet_header[15] = temp[3];

		// print_message(action_code+" l="+len+" "+temp[0]+" "+temp[1]+" "+temp[2]+" "+temp[3],
		// INFO_NOTICE);

		return packet_header;
	}

	public SSLSocket init_control_connection() {
		SSLSocket sock; // we could use the global one but...
		int status = 0;

		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			SocketFactory factory = sc.getSocketFactory();
			// SocketFactory factory = SSLSocketFactory.getDefault();
			System.out.println("init_control_connection - server_name="
					+ server_name + ", server_port=" + server_port);
			sock = (SSLSocket) factory.createSocket(server_name, server_port);
			m_NetType = this.getNetworkType(sock);
		} catch (Exception e) {
			print_message("Couldn't create socket: " + e.getMessage() + "\n",
					INFO_NOTICE);
			return null;
		}

		return sock;
	}

	public String getNetworkType(SSLSocket sock) {
		try {
			if (NetworkInterface.getByInetAddress(sock.getLocalAddress())
					.getDisplayName().equals("en1")) {
				return "WiFi";
			}
		} catch (Exception e) {
			print_message("Couldn't get networktype: " + e.getMessage() + "\n",
					INFO_NOTICE);
		}
		return "Wired";
	}

	public void start() {
		if (!was_started) {
			print_message(
					"An error occurred and the Applet cannot be started!\n",
					INFO_STATUS);
		}
	}

	public void run() {

		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction() {

				public Object run() {
					// Hashtable listeners;
					// FCP_AcceptThread listener;
					FCP_AcceptThreadAG conn;
					int iterations;

					long time = (new Date()).getTime();
					int starttime = (int) (time / 1000); // convert miliseconds
															// to seconds

					int len;
					int tmpcode;
					byte[] data_header, data_payload;
					byte[] ip_arr;

					byte action_code;
					int local_ip;
					int listen_port, remote_port;
					int data_length;

					data_header = new byte[FCP_HEADER_LEN];
					data_payload = new byte[MAX_LOCAL_MSS];

					iterations = 0;

					print_message("Connecting to Server...\n", INFO_STATUS);
					while (valid_session) {
						try {
							if (!conn_alive) {
								byte[] sessid_array;

								if (starttime == 0) {
									time = (new Date()).getTime();
									starttime = (int) (time / 1000); // convert
																		// miliseconds
																		// to
																		// seconds
								}

								// open a connection to the SP on the control
								// port
								// sslsock = init_control_connection();
								sslsock = (SSLSocket) AccessController
										.doPrivileged(new PrivilegedExceptionAction() {
											public Object run() {
												return init_control_connection();
											}
										});
								if (sslsock == null) {
									throw new Exception(
											"Unable to open control connection");
								}

								sp_in = sslsock.getInputStream();
								sp_out = sslsock.getOutputStream();
								buff_out = new BufferedOutputStream(sp_out,
										1460);

								/*
								 * String cookies = ""; //attempt to extract all
								 * SP set cookies from the browser try { cookies
								 * = (String)win.call("get_cookies",null); }
								 * catch (Exception ignored) { } if (cookies ==
								 * null) { cookies = ""; } cookies.replace('\r',
								 * ' '); cookies.replace('\n', ' ');
								 */

								// send the request to initiate the clientapp
								// control
								// connection
								String capp_request = "GET /vpntunnel HTTP/1.1\r\n"
										+ "Connection: Keep-Alive\r\n"
										+ "Host: "
										+ server_name
										+ "\r\n"
										+ "Cookie: "
										+ session_cookie
										+ "\r\n"
										+ "appid: DesktopDirect\r\n"
										+ "clientid: " + m_DevID + "\r\n\r\n";
								buff_out.write(str2bytes(capp_request), 0,
										capp_request.length());
								buff_out.flush();

								int tcount = 0;
								byte b[] = new byte[1];

								while (tcount < 4) {
									if (sp_in.read(b, 0, 1) == -1) {
										break;
									}
									if ((b[0] == 10) || (b[0] == 13)) {
										tcount++;
									} else {
										tcount = 0;
									}
								}

								// finally start our internal initiation
								// sequence
								action_code = START_TCS;

								// strip off the siteid and + delimiter
								// XXX need to fix: see bug 8732
								sessid_array = str2bytes(session_id
										.substring(session_id.indexOf("+") + 1));
								data_header = make_header(action_code, 0, 0, 0,
										0);

								buff_out.write(data_header, 0,
										data_header.length);
								// buff_out.write(sessid_array, 0,
								// sessid_array.length);
								buff_out.flush();

								conn_alive = true;
								iterations = 0;
								starttime = 0;

							}

							// don't starve other threads
							iterations++;
							if (iterations > 10) {
								server_thread.yield();
								iterations = 0;
							}

							// read in the packet header
							len = 0;
							while (len < FCP_HEADER_LEN) {
								len += sp_in.read(data_header, len,
										FCP_HEADER_LEN - len);
							}
							// extract the values
							// action_code = data_header[ACTION_CODE_OFFSET];

							// tmpcode = ibytes2int(data_header,
							// ACTION_CODE_OFFSET, 4);
							action_code = data_header[ACTION_CODE_OFFSET];

							// !@!local_ip = ntohl(data_header,
							// LOCALHOSTIP_OFFSET);
							local_ip = byteArrayToInt(data_header,
									LOCALHOSTIP_OFFSET, 4, false);

							// !@!listen_port = ntohs(data_header,
							// LISTEN_PORT_OFFSET);
							listen_port = byteArrayToInt(data_header,
									LISTEN_PORT_OFFSET, 2, false);

							// !@!remote_port = htons(data_header,
							// REMOTE_PORT_OFFSET);
							remote_port = byteArrayToInt(data_header,
									REMOTE_PORT_OFFSET, 2, false);

							// !@!data_length = ntohl(data_header,
							// DATA_LENGTH_OFFSET);
							data_length = byteArrayToInt(data_header,
									DATA_LENGTH_OFFSET, 4, false);

							/*
							 * print_message("action_code="+Integer.toString(
							 * action_code), INFO_ERROR);
							 * print_message("local_ip="
							 * +Integer.toString(local_ip), INFO_ERROR);
							 * print_message
							 * ("listen_port="+Integer.toString(listen_port),
							 * INFO_ERROR);
							 * print_message("remote_port="+Integer.
							 * toString(remote_port), INFO_ERROR);
							 * print_message(
							 * "data_length="+Integer.toString(data_length),
							 * INFO_ERROR);
							 */

							int read_len;
							String tmps;
							String resSt = "";

							// decide what to do with it
							// action_code = (byte) (tmpcode & 0xFF);
							switch (action_code) {
							case SESSION_KEEPALIVE:
								break;
							case FCP_HEADER_ACK:
								print_message("Server ACK: (" + action_code
										+ "," + listen_port + "," + remote_port
										+ "," + data_length + ")\n",
										INFO_NOTICE);
								break;

							case CONNECTION_READY:
								if (m_AddConnResultOut != null) {
									m_AddConnResultOut.write(1);
								}
								break;

							case RESOLVE_HOSTNAME:
								if (m_ResolveHostResult != null) {
									m_ResolveHostResult.write(
											intToByteArray(local_ip, 4, false),
											0, 4);
									// m_ResolveHostResult.write(int2bytes(local_ip,
									// 4), 0, 4);
								}

								break;

							case GET_TIME_INFO:
								if (m_GetTimersResult != null) {
									m_GetTimersResult.write(
											int2bytes(local_ip, 4), 0, 4);
									m_GetTimersResult.write(
											int2bytes(data_length, 4), 0, 4);
									data_length = 0;
								}

								break;
							case GET_USERNAME:

								// read in the packet payload (data)
								while (data_length > 0) {
									if (data_length > MAX_LOCAL_MSS) {
										read_len = MAX_LOCAL_MSS;
									} else {
										read_len = data_length;
									}
									len = sp_in.read(data_payload, 0, read_len);
									tmps = new String(data_payload, 0, len);
									resSt = resSt + tmps;
									data_length -= len;
								}
								if (m_GetUsernameResult != null) {
									m_GetUsernameResult.write(resSt.getBytes(),
											0, resSt.length());
									m_GetUsernameResult.write(0);
								}
								break;
							case GET_PASSWORD:

								// read in the packet payload (data)
								while (data_length > 0) {
									if (data_length > MAX_LOCAL_MSS) {
										read_len = MAX_LOCAL_MSS;
									} else {
										read_len = data_length;
									}
									len = sp_in.read(data_payload, 0, read_len);
									tmps = new String(data_payload, 0, len);
									resSt = resSt + tmps;
									data_length -= len;
								}
								if (m_GetPasswordResult != null) {
									m_GetPasswordResult.write(resSt.getBytes(),
											0, resSt.length());
									m_GetPasswordResult.write(0);
								}

								break;

							case SEND_DATA:
								conn = (FCP_AcceptThreadAG) m_Conns
										.get(new Integer(remote_port));
								if (conn != null) {

									// !!! Start
									FCP_ClientThreadAG cl = conn
											.get_client(remote_port);
									// !!! End

									// read in the packet payload (data)
									while (data_length > 0) {

										/*
										 * iterations++; if (iterations > 10) {
										 * server_thread.yield(); iterations =
										 * 0; }
										 */

										if (data_length > MAX_LOCAL_MSS) {
											read_len = MAX_LOCAL_MSS;
										} else {
											read_len = data_length;
										}
										len = sp_in.read(data_payload, 0,
												read_len);

										// !!! Start
										if (cl != null) {
											cl.send(len, data_payload);
										}
										// !!! End
										// !!!conn.send_to_client(remote_port,
										// len, data_payload);
										data_length -= len;
									}
								}
								break;
							case CONNECTION_CLOSED:
								conn = (FCP_AcceptThreadAG) m_Conns
										.get(new Integer(remote_port));
								if (conn != null) {
									conn.close_client(remote_port);
								}
								break;
							case WINREDIR_ADDRESS:
								byte[] domain = null;

								if (data_length > 0) {
									domain = new byte[data_length];
									// read in the packet payload (data)
									len = 0;
									while (len < data_length) {
										len += sp_in.read(domain, len,
												data_length - len);
									}
								}
								ip_arr = int2bytes(local_ip, 4);
								print_message(
										"Resolved "
												+ bytes2str(domain, 0,
														data_length)
												+ "to "
												+ new String(
														((int) ip_arr[0] & 0xFF)
																+ "."
																+ ((int) ip_arr[1] & 0xFF)
																+ "."
																+ ((int) ip_arr[2] & 0xFF)
																+ "."
																+ ((int) ip_arr[3] & 0xFF))
												+ " for Query ID "
												+ listen_port + " SPI port "
												+ remote_port + "\n",
										INFO_NOTICE);
								// overload listen_port as the query id
								break;

							case START_TCS:
								m_Ready = true;
								break;
							case SESSION_TIMEOUT:
								valid_session = false;
								terminate();
								logout();
								print_message("Your session has timed out.\n"
										+ "Please close this window and "
										+ "log back in.", INFO_STATUS);
								break;
							case SESSION_INVALID:
								valid_session = false;
								terminate();
								logout();
								print_message("Your session is invalid!\n"
										+ "Please close this window and "
										+ "log back in.", INFO_STATUS);
								break;
							default: // should never happen
								valid_session = false;
								terminate();
								print_message(
										"Could not understand the Server!\n"
												+ "Please close this window, clear your\n"
												+ "browser cache, and try again.\n",
										INFO_STATUS);
								break;
							}
						} catch (Exception e) {
							print_message("Problem with Server connection: "
									+ e.getMessage() + "\n", INFO_CRITICAL);
							terminate(); // all our connections are hosed now
											// anyway
							conn_alive = false; // we'll try to reopen the next
												// loop

							time = (new Date()).getTime();
							int currtime = (int) (time / 1000); // convert
																// miliseconds
																// to
																// seconds
							if (starttime != 0 && currtime - starttime > 120) {
								valid_session = false; // give up after 120
														// seconds
								print_message(
										"Cannot connect to Server at this time.\n"
												+ "Please close this window and try again\n"
												+ "later; if this problem persists, contact\n"
												+ "your network administrator.",
										INFO_STATUS);
							}
							try {
								if (sslsock != null)
									sslsock.close();
							} catch (Exception ignored) {
							}
							try {
								// sleep 5 seconds
								print_message(
										"Retrying Server connection...\n",
										INFO_STATUS);
								server_thread.sleep((long) 5000);
							} catch (Exception ignored) {
							}
						}
					}
					return null;
				}
			});
		} catch (PrivilegedActionException ex) {
			// Logger.getLogger(FatClientProxy.class.getName()).log(Level.SEVERE,
			// null, ex);
		}

	}

	private synchronized void generic_send(byte action_code, int local_ip,
			int listen_port, int remote_port, int len, byte[] data) {
		byte[] data_header;
		byte[] temp;

		try {
			// send the data
			data_header = make_header(action_code, local_ip, listen_port,
					remote_port, len);
			buff_out.write(data_header, 0, data_header.length);
			if (data != null) {
				buff_out.write(data, 0, len);
			}
			buff_out.flush(); // no more to write
		} catch (Exception e) {
			print_message("Error during send: " + e.getMessage() + "\n",
					INFO_ERROR);
		}
	}

	public synchronized int addConn(String prmDestIP, int prmDestPort,
			int prmSrcPort) {

		m_AddConnResultOut = new PipedOutputStream();
		m_AddConnResultIn = new PipedInputStream();

		print_message("Creating accesspt thread \n", INFO_NOTICE);
		FCP_AcceptThreadAG conn = new FCP_AcceptThreadAG(this, LOCALHOST_ADDR,
				null, 0);
		print_message("Accept thread created \n", INFO_NOTICE);
		if (conn.getListenPort() > 0) {
			print_message("Listen port is ok \n", INFO_NOTICE);
			// m_Conns.put(new Integer(c), conn);
			// conn.start();

			try {
				print_message("Connecting to result pipe \n", INFO_NOTICE);
				m_AddConnResultIn.connect(m_AddConnResultOut);
				print_message("Creating WinRedirEntry \n", INFO_NOTICE);

				FCP_WinRedirEntryAG entry = new FCP_WinRedirEntryAG(0,
						prmDestPort, byteArrayToInt(
								InetAddress.getByName(prmDestIP).getAddress(),
								0, 4, false));
				print_message("Setting connection entry \n", INFO_NOTICE);
				conn.setEntry(entry);
				print_message("Starting connection \n", INFO_NOTICE);
				conn.start();
				// winredir_connect(bytes2int(InetAddress.getByName(LOCALHOST_ADDR).getAddress(),
				// 0, 4),INTERNAL_PORT, c, entry.encode().length,
				// entry.encode());

				return conn.getListenPort();

			} catch (Exception e) {
				// m_Conns.remove(new Integer(c));
			}
		}

		return -1;
	}
	public synchronized int addOtherConnAG(String prmDestIP, int prmDestPort,
			int prmSrcPort, ServerSocket serverSock, int listenPort, FCP_AcceptThreadAG conn) {

		m_AddConnResultOut = new PipedOutputStream();
		m_AddConnResultIn = new PipedInputStream();

		print_message("Creating accesspt thread \n", INFO_NOTICE);
		
		print_message("Accept thread created \n", INFO_NOTICE);
		
		if (conn.getListenPort() > 0) {
			print_message("Listen port is ok \n", INFO_NOTICE);
			// m_Conns.put(new Integer(c), conn);
			// conn.start();
			try {
				print_message("Connecting to result pipe \n", INFO_NOTICE);
				m_AddConnResultIn.connect(m_AddConnResultOut);
				print_message("Creating WinRedirEntry \n", INFO_NOTICE);

				FCP_WinRedirEntryAG entry = new FCP_WinRedirEntryAG(0,
						prmDestPort, byteArrayToInt(
								InetAddress.getByName(prmDestIP).getAddress(),
								0, 4, false));
				print_message("Setting connection entry \n", INFO_NOTICE);
				conn.setEntry(entry);
				print_message("Starting connection \n", INFO_NOTICE);
				conn.start();
				// winredir_connect(bytes2int(InetAddress.getByName(LOCALHOST_ADDR).getAddress(),
				// 0, 4),INTERNAL_PORT, c, entry.encode().length,
				// entry.encode());
				return conn.getListenPort();

			} catch (Exception e) {
				
			}
		}
		return -1;
	}
	public synchronized String resolveHost(String prmHost) {

		// Creates the output and input pipes needed to get the result to the
		// requesting class
		int i, c;
		// byte resIP[] = new byte[4];
		// String resIP = "";
		String resSt = "";
		m_ResolveHostResult = new PipedOutputStream();
		PipedInputStream res = new PipedInputStream();

		try {
			res.connect(m_ResolveHostResult);
			generic_send(RESOLVE_HOSTNAME, 0, 0, 0, prmHost.length(),
					prmHost.getBytes());

			Integer ai;
			c = 0;
			do {
				i = res.read();
				if (i == -1) {
					return "";
				}

				ai = new Integer(i);
				resSt = resSt + ai.toString();
				if (c < 3) {
					resSt = resSt + ".";
				}
				// resIP[c] = (byte)i;
				c++;
			} while (c < 4);

			/*
			 * resSt = InetAddress.getByName(resIP).toString(); if
			 * (resSt.length() > 1) { resSt = resSt.substring(1); }
			 */
		} catch (Exception e) {

		}

		return resSt;
	}

	public synchronized void sendLog(int prmLogLevel, String prmProtocol,
			int prmDest, String prmDestName, String prmMsg) {

		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		byte[] blank = { 0, 0, 0, 0 };
		try {
			buf.write(prmLogLevel);

			buf.write(prmProtocol.length());
			buf.write(prmProtocol.getBytes());

			buf.write(blank);

			// buf.write(ByteBuffer.allocate(4).putInt(prmDest).array());
			buf.write(int2bytes(prmDest, 4));

			buf.write(blank);

			buf.write(prmDestName.length());
			buf.write(prmDestName.getBytes());
			// buf.write(str2bytes(prmDestName));

			buf.write(0);
			buf.write(0);
			buf.write(blank);
			buf.write(blank);
			// buf.write(blank);

			String msg;
			if (prmMsg.length() > 120) {
				msg = prmMsg.substring(0, 120);
			} else {
				msg = prmMsg.substring(0);
			}

			buf.write(msg.length());
			buf.write(msg.getBytes());
			// buf.write(str2bytes(msg));

			generic_send(FCP_GEN_LOG, 0, 0, 0, buf.size(), buf.toByteArray());
		} catch (Exception e) {

		}

	}

	public synchronized void updateTimers() {
		// Creates the output and input pipes needed to get the result to the
		// requesting class
		int i = 0;
		int c;
		byte b[] = new byte[4];
		m_GetTimersResult = new PipedOutputStream();
		PipedInputStream res = new PipedInputStream();

		try {
			res.connect(m_GetTimersResult);
			generic_send(GET_TIME_INFO, 0, 0, 0, 0, (byte[]) null);

			c = 3;
			do {
				i = res.read();
				if (i == -1) {
					return;
				}
				b[c] = (byte) i;
				c--;
			} while (c >= 0);

			m_LastIdleTimer = bytes2int(b, 0, 4);

			c = 0;
			do {
				i = res.read();
				if (i == -1) {
					return;
				}

				b[c] = (byte) i;
				c++;
			} while (c < 4);

			m_LastLifetimeTimer = bytes2int(b, 0, 4);

		} catch (Exception e) {

		}

		return;
	}

	public synchronized void resetTimers(int prmSecs) {

		generic_send(RESET_TIMERS, prmSecs, 0, 0, 0, (byte[]) null);

	}

	public synchronized String getUsername() {

		// Creates the output and input pipes needed to get the result to the
		// requesting class
		int i;
		String resSt = "";
		m_GetUsernameResult = new PipedOutputStream();
		PipedInputStream res = new PipedInputStream();

		try {
			res.connect(m_GetUsernameResult);
			generic_send(GET_USERNAME, 0, 0, 0, 0, (byte[]) null);

			while (true) {
				i = res.read();
				if ((i == -1) || (i == 0)) {
					return resSt;
				}
				resSt = resSt + String.valueOf((char) i);
			}

		} catch (Exception e) {

		}

		return resSt;
	}

	public synchronized String getPassword() {

		// Creates the output and input pipes needed to get the result to the
		// requesting class
		int i;
		String resSt = "";
		m_GetPasswordResult = new PipedOutputStream();
		PipedInputStream res = new PipedInputStream();

		try {
			res.connect(m_GetPasswordResult);
			generic_send(GET_PASSWORD, 0, 0, 0, 0, (byte[]) null);

			while (true) {
				i = res.read();
				if ((i == -1) || (i == 0)) {
					return resSt;
				}
				resSt = resSt + String.valueOf((char) i);
			}

		} catch (Exception e) {

		}

		return resSt;
	}

	public void keepalive() {
		byte action_code = SESSION_KEEPALIVE;
		print_message("Keep-Alive triggered...\n", INFO_NOTICE);
		generic_send(action_code, (int) 0, (int) 0, (int) 0, (int) 0,
				(byte[]) null);
	}

	/*
	 * public void client_connect(int local_ip, int listen_port, int
	 * remote_port) { byte action_code = CONNECTION_OPENED;
	 * generic_send(action_code, local_ip, listen_port, remote_port, (int)0,
	 * (byte[])null); }
	 */

	public void winredir_connect(int dest_ip, int dest_port, int id) {
		byte action_code = WINREDIR_OPENED;
		generic_send(action_code, dest_ip, dest_port, id, 0, null);
	}

	public void winredir_resolve(int queryid, int remote_port, byte[] hostname) {
		byte action_code = WINREDIR_RESOLVE;
		generic_send(action_code, (int) 0, queryid, remote_port,
				hostname.length, hostname);
	}

	public void send(int local_ip, int listen_port, int remote_port, int len,
			byte[] data) {
		byte action_code = SEND_DATA;
		if (keeper != null) {
			keeper.need_keepalive = false;
		}
		generic_send(action_code, local_ip, listen_port, remote_port, len, data);

	}

	public void close_server(int local_ip, int listen_port, int remote_port) {
		byte action_code = CONNECTION_CLOSED;

		int lip = Integer.reverse(local_ip);
		int lp = Integer.reverse(listen_port);
		int rp = Integer.reverse(remote_port);
		generic_send(action_code, lip, lp, rp, (int) 0, (byte[]) null);
		/*
		 * generic_send(action_code, local_ip, listen_port, remote_port, (int)
		 * 0, (byte[]) null);
		 */
	}

	public synchronized void remove(FCP_AcceptThreadAG listener) {
		Hashtable listeners;
		try {
			/*
			 * listeners = (Hashtable)localhosts.get(new
			 * Integer(listener.local_ip)); if (listeners != null) {
			 * listeners.remove(new Integer(listener.listen_port)); } else {
			 * throw new Exception("Local IP not found in hosts table."); }
			 */
		} catch (Exception e) {
			print_message("While deleting accept: " + e.getMessage() + "\n",
					INFO_WARNING);
		}
	}

	/*
	 * private void logout() { try { //open a connection to the SP (assumes the
	 * virtual site port) print_message("Connecting to Server...\n",
	 * INFO_STATUS); sslsock = init_control_connection(); if (sslsock == null) {
	 * return; }
	 * 
	 * sp_in = sslsock.getInputStream(); sp_out = sslsock.getOutputStream();
	 * buff_out = new BufferedOutputStream(sp_out, 1460);
	 * 
	 * String cookies = ""; //attempt to extract all SP set cookies from the
	 * browser try { //!!!cookies = (String)win.call("get_cookies",null); }
	 * catch (Exception ignored) { } if (cookies == null) { cookies = ""; }
	 * cookies.replace('\r', ' '); cookies.replace('\n', ' '); //remove the
	 * session cookie for SP2 in dual SP mode (or SP1 in single) int pos =
	 * cookies.indexOf(session_id); String prefix = cookies.substring(0, pos);
	 * String suffix = cookies.substring(pos + session_id.length()); cookies =
	 * prefix + "fakeid" + suffix;
	 * 
	 * //send the request to initiate the clientapp control connection String
	 * capp_request = "GET /prx/000/http/localhost/logout HTTP/1.0\r\n" +
	 * "Connection: Close\r\n" + "Cookie: " + cookies + "\r\n\r\n";
	 * buff_out.write(str2bytes(capp_request), 0, capp_request.length());
	 * buff_out.flush();
	 * 
	 * //close the connection - discard response sslsock.close();
	 * 
	 * } catch (Exception ignored) { } }
	 */

	public synchronized void terminate() {
		FCP_AcceptThreadAG listener;
		importance_threshold = INFO_NONE; // stop all further output
		String msg = "ExitDD";
		byte[] buf = msg.getBytes();
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");
			int port = 6666;

			DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length,
					address, port);

			DatagramSocket socket = new DatagramSocket();
			socket.send(dataGramPacket);
			System.out.println("socket send ag terminate");
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (keeper != null) {
				keeper.terminate();
			}
		} catch (Exception ignored) {
		}
		try {
			sp_in.close();
		} catch (Exception ignored) {
		}
		try {
			sp_out.close();
		} catch (Exception ignored) {
		}
		try {
			sslsock.close();
		} catch (Exception ignored) {
		}
		try {
			Enumeration e, k;
			Object temp_e;
			Hashtable listeners;
			// clear out the hostnames
			/*
			 * for (e = hostnames.keys(); e.hasMoreElements(); ) {
			 * hostnames.remove(e.nextElement()); } //clear out the accept
			 * lists/threads for (e = localhosts.keys(); e.hasMoreElements(); )
			 * { temp_e = e.nextElement(); listeners =
			 * (Hashtable)localhosts.get(temp_e); for (k = listeners.keys();
			 * k.hasMoreElements(); ) { listener =
			 * (FCP_AcceptThread)listeners.get(k.nextElement());
			 * listener.terminate(); //calls remove() }
			 * localhosts.remove(temp_e); }
			 */
		} catch (Exception ignored) {
		}
		conn_alive = false;

		importance_threshold = INFO_CRITICAL;
	}

	public void stop() {
		// leave the thread running so we can work in the background
	}

	public void destroy() {
		// stop the server thread and then do cleanup
		// this is what we should do instead of calling stop(), but
		// currently the sp_in.read() will potentially block forever,
		// so since we don't hold any locks and we're shutting down,
		// just call stop() for now.
		// valid_session = false;
		// try {
		// server_thread.join();
		// } catch (Exception e) {
		// //could not do clean shutdown; oh well
		// }
		server_thread.stop();
		terminate();
	}

	public String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	/*
	 * public ClientConnectionManager getClientConnMng() { return
	 * m_ClientConnectionManager; } public HttpContext getHTTPContext() { return
	 * m_HTTPContext; } public HttpParams getHTTPParams() { return m_HTTPParams;
	 * }
	 */

} // end FatClientProxyAG class

