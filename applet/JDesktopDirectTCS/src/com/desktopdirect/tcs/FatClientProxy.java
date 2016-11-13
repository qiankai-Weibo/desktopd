package com.desktopdirect.tcs;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;


public class FatClientProxy {
	protected String username, password, domain;
	
	protected String session_id;
	protected String session_cookie;
	protected String mod_id;
	protected String server_name, proxy_host;
	protected int server_port, proxy_port;
	
	protected int m_LastIdleTimer = 0;
	protected int m_LastLifetimeTimer = 0;

	
	public Hashtable m_Conns;
	protected int m_LastPort;
	
	protected Thread server_thread;
	public FCP_KeepAlive keeper;

	protected short[] ciphers;
	protected SSLSocket sslsock;

	protected InputStream sp_in;
	protected OutputStream sp_out;
	protected BufferedOutputStream buff_out;

	protected boolean valid_session;
	protected boolean conn_alive;
	protected boolean was_started;
	protected boolean do_authentication;
	protected boolean need_credentials;
	protected boolean ready_notified;
	protected boolean m_Ready;
	
	protected PipedOutputStream m_ResolveHostResult = null;
	protected PipedOutputStream m_GetTimersResult = null;
	protected PipedOutputStream m_GetUsernameResult = null;
	protected PipedOutputStream m_GetPasswordResult = null;
	protected PipedOutputStream m_AddConnResultOut = null;
	protected PipedInputStream m_AddConnResultIn = null;
	
	public byte importance_threshold;

	public String m_NetType = "";
	
	public static final int MAX_LOCAL_MSS = 1024; //4096; // MacOS TCP STACK SUX0RZ!!!
	
	// these are used for internal print_message controls
	public static final byte INFO_ALL = 0;
	public static final byte INFO_NOTICE = 1;
	public static final byte INFO_WARNING = 2;
	public static final byte INFO_ERROR = 3;
	public static final byte INFO_CRITICAL = 4;
	public static final byte INFO_FAILURE = 5;
	public static final byte INFO_STATUS = 6;
	public static final byte INFO_NONE = 7;
	
	// Log message level
	public static final byte LOG_EMERG                  = 0;
	public static final byte LOG_ALERT                  = 1;
	public static final byte LOG_CRIT                   = 2;
	public static final byte LOG_ERR                    = 3;
	public static final byte LOG_WARNING                = 4;
	public static final byte LOG_NOTICE                 = 5;
	public static final byte LOG_INFO                   = 6;
	public static final byte LOG_DEBUG                  = 7;
	
	public static final String LOCALHOST_ADDR = "127.0.0.1";
	public static final int INTERNAL_PORT = 135;

	public FatClientProxy(String prmServer, int prmPort, String prmSessionID,
			String prmSessionCookie, String prmModID) {

		server_name = prmServer;
		server_port = prmPort;
		session_id = prmSessionID;
		mod_id = prmModID;
		session_cookie = prmSessionCookie;
		m_LastPort = 1;
		m_Ready = false;

		m_Conns = new Hashtable((int) 5, (float) 0.9);
		
	}
	
	public void init() {
	}
	
	public void setDefaultCredentials(String prmUsername, String prmPassword,
			String prmDomain) {
		username = prmUsername;
		password = prmPassword;
		domain = prmDomain;
	}

	public void setSessionCookie(String prmCookie) {
		session_cookie = prmCookie;

		int i = session_cookie.indexOf("=");
		if (i > -1) {
			session_id = session_cookie.substring(i + 1);
		}
	}

	// display a message in the event log
	public void print_message(String msg, byte importance) {		
		//System.err.println(msg);
	}

	// avoid String.getBytes due to utf-8 bug in jdk 1.1
	public static byte[] str2bytes(String str) {
		char[] chars = str.toCharArray();
		byte[] bytes = new byte[chars.length];

		int i;
		for (i = 0; i < chars.length; i++) {
			bytes[i] = (byte) (chars[i] & 0xFF);
		}

		return bytes;
	}

	// convert C-string (1 byte char) into Java-string (2 byte char)
	public static String bytes2str(byte[] bytes, int offset, int len) {
		int i;
		byte[] temp = new byte[1];
		String return_str = new String("");

		for (i = offset; i < offset + len; i++) {
			temp[0] = bytes[i];
			if (temp[0] == 0) {
				break; // reached null terminator
			}
			return_str += new String(temp);
		}

		return return_str;
	}

	// convert from int to bytes in network order (BE)
	public static byte[] int2bytes(int num, int len) {
		int i;
		byte[] bytes = new byte[len];

		for (i = (len - 1) * 8; i >= 0; i -= 8) {
			bytes[(len - 1) - i / 8] = (byte) ((num >> i) & 0xFF);
		}

		return bytes;
	}
	
	// convert from bytes in network order (BE) to int
	public int bytes2int(byte[] bytes, int offset, int len) {
		int i, num = 0;
		int[] mask = new int[4];

		mask[0] = 0xFF000000;
		mask[1] = 0x00FF0000;
		mask[2] = 0x0000FF00;
		mask[3] = 0x000000FF;

		for (i = (len - 1) * 8; i >= 0; i -= 8) {
			num |= ((int) bytes[offset + (len - 1) - i / 8] << i)
					& mask[3 - i / 8];
		}

		return num;
	}
	
	public synchronized int nextAvailPort() {
		int i = m_LastPort;

		while (true) {
			if (m_Conns.get(new Integer(i)) == null) {
				m_LastPort = i + 1;
				return i;
			}
			i++;
			if (i > 60000) {
				i = 1;
			}
		}

	}
	
	public boolean isAddConnOK() {
		try {
			return (m_AddConnResultIn.available() > 0);
		} catch (Exception e) {

		}

		return false;
	}
	
	public synchronized int getIdleTimer() {

		return m_LastIdleTimer;
	}

	public synchronized int getLifetimeTimer() {

		return m_LastLifetimeTimer;
	}
	
	public boolean isReady() {
		return m_Ready;
	}

	public String getSessID() {
		return session_id;
	}

	public String getSessCookie() {
		return session_cookie;
	}
	

	public boolean isValidIP(String prmIP) {
		String[] parts = prmIP.split("\\.");
		int i;

		for (String s : parts) {
			try {
				i = Integer.parseInt(s);
			} catch (Exception e) {
				return false;
			}

			if (i < 0 || i > 255) {
				return false;
			}
		}
		return true;
	}
	
	public synchronized int addConn(String prmDestIP, int prmDestPort,
			int prmSrcPort) {
	  return 0;
	}
	public synchronized int addOtherConnAG(String prmDestIP, int prmDestPort,
			int prmSrcPort, ServerSocket serverSock, int listenPort, FCP_AcceptThreadAG conn){
		return 0;
	}
	public synchronized int addOtherConnSPX(String prmDestIP, int prmDestPort,
			int prmSrcPort, ServerSocket serverSock, int listenPort, FCP_AcceptThreadSPX conn){
		return 0;
	}
	public synchronized void sendLog(int prmLogLevel, String prmProtocol, int prmDest, String prmDestName, String prmMsg) {}
	
	public synchronized String resolveHost(String prmHost) {
	  return "";
	}
	
	public void logout() {}
	public void close_server(int local_ip, int listen_port, int remote_port){};
	public synchronized void removeAG(FCP_AcceptThreadAG listener){};
	public synchronized void removeSPX(FCP_AcceptThreadSPX listener){};
	public void winredir_connect(int dest_ip, int dest_port, int id) {};
	public void winredir_connect_spx(int local_ip, int listen_port,
			int remote_port, int len, byte[] data){};
	public void send(int local_ip, int listen_port, int remote_port, int len,
			byte[] data) {}
	public synchronized String getUsername() {
		return "";
	}
	
	public synchronized String getPassword() {
		return "";
	}
	public String getNetworkType() {
	    return "";
	}
	public void keepalive() {}
}

