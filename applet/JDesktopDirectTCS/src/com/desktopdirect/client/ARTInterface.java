/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.desktopdirect.client;

import com.desktopdirect.tcs.*;
import java.text.SimpleDateFormat;
import java.net.*;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import org.xml.sax.SAXException;
import org.apache.commons.lang3.StringEscapeUtils;

import java.security.PrivilegedActionException;
import javax.swing.*;

public class ARTInterface {

	public static final int DEV_TYPE_ANDROID = 4;
	public static final int DEV_TYPE_MACOS = 5;
	protected static final String ARP_ROOT = "ARP";
	protected static final String ARP_HOSTS = "Hosts";
	protected static final String ARP_HOST = "Host";
	protected static final String ARP_APPS = "Apps";
	protected static final String ARP_APP = "App";
	protected static final String ARP_STATE_SESS = "StateSess";
	protected static final String ARP_OBJ_PRE_LAUNCH = "ObjPreLaunch";
	protected static final String ARP_LIC = "license";
	protected static final String ARP_PROXY = "Proxy";
	protected static final String ART_ERROR = "Error";
	protected static final String ART_SERVER = "Server";
	protected static final String ART_VERSION = "Version";
	protected static final String ART_INST = "Instance";
	protected static final String ART_SETTINGS = "Settings";
	protected static final String ART_VDIAUTH = "vdiauth";
	protected static final String ART_KEEPALIVE = "keepalive";

	protected static final String ATTR_ERR_INFO = "Info";

	protected static final String ATTR_HOSTS_RDP_PORT = "RDPPort";

	protected static final String ATTR_SERVER_PORT = "Port";

	protected static final String ATTR_HOST_DESC = "Desc";
	protected static final String ATTR_HOST_DAYS_LEFT = "Days";
	protected static final String ATTR_HOST_SESS_INFO = "SessInfo";
	protected static final String ATTR_HOST_SESS_LEN = "SessLen";
	protected static final String ATTR_HOST_PARAMS = "Params";
	protected static final String ATTR_HOST_PROTOCOL = "Protocol";
	protected static final String ATTR_HOST_PROVIDER = "Provider";
	protected static final String ATTR_HOST_ID = "ID";
	protected static final String ATTR_HOST_STATE = "State";
	protected static final String ATTR_HOST_PROVIDER_ID = "ProviderID";
	protected static final String ATTR_HOST_DESKTOP_ID = "desktopID";
	protected static final String ATTR_HOST_HOST = "Host";
	protected static final String ATTR_HOST_VDIUSER = "VDIUser";
	protected static final String ATTR_HOST_VDIPWD = "VDIPwd";

	protected static final String ATTR_APP_NAME = "Name";
	protected static final String ATTR_APP_DESC = "Desc";
	protected static final String ATTR_APP_LOC = "Loc";
	protected static final String ATTR_APP_DIR = "Dir";
	protected static final String ATTR_APP_HEIGHT = "Height";
	protected static final String ATTR_APP_WIDTH = "Width";
	protected static final String ATTR_APP_PUBLISHER = "Publisher";
	protected static final String ATTR_APP_FOLDER = "Folder";
	protected static final String ATTR_APP_ICON_UPDATED = "IconUpdated";

	protected static final String ATTR_STATE_SESS_ID = "ID";

	protected static final String URL_PORTAL_BASE = "/portal";
	protected static final String URL_QUERY_BASE = "/query";
	protected static final String URL_OBJECT_BASE = "/object";
	protected static final String URL_ADMIN_BASE = "/admin";
	protected static final String URL_TEST_BASE = "/test";
	protected static final String URL_PORTAL_REQ = URL_PORTAL_BASE + "/req";
	protected static final String URL_PORTAL_REQ_COMP = URL_PORTAL_BASE
			+ "/comp";
	protected static final String URL_PORTAL_RES = URL_PORTAL_BASE + "/res";
	protected static final String URL_PORTAL_ERR = URL_PORTAL_BASE + "/error";
	protected static final String URL_PORTAL_LOGIN = URL_PORTAL_BASE + "/login";
	protected static final String URL_PORTAL_CONFIRM_OVERRIDE = URL_PORTAL_BASE
			+ "/confirm";

	protected static final String URL_QUERY_HOSTS = "/hosts";
	protected static final String URL_QUERY_HOST_AVAIL = "/avail";
	protected static final String URL_QUERY_PROXY = "/proxy";
	protected static final String URL_QUERY_LICENSE = "/license";
	protected static final String URL_QUERY_CLIENT_VERI2 = "/clientverification2";
	protected static final String URL_QUERY_REG_SCRIPT = URL_QUERY_BASE
			+ "/regscript";
	protected static final String URL_QUERY_QUERY_APP_SERVER = URL_QUERY_BASE
			+ "/appserver";
	protected static final String URL_QUERY_QUERY_APP_ICON = URL_QUERY_BASE
			+ "/appicon";
	protected static final String URL_QUERY_INFO = URL_QUERY_BASE + "/info";
	protected static final String URL_OBJECT_POWER_UP = "/powerup";
	protected static final String URL_OBJECT_NEW_STATE_SESS = "/newstatesess";
	protected static final String URL_OBJECT_PRE_LAUNCH = URL_OBJECT_BASE
			+ "/prelaunch";

	protected static final String URL_ADMIN_REG = URL_ADMIN_BASE + "/register";
	protected static final String URL_ADMIN_REG_COMPLETE = URL_ADMIN_BASE
			+ "/regcomplete";
	protected static final String URL_ADMIN_RES = URL_ADMIN_BASE + "/res";
	protected static final String URL_ADMIN_ERR = URL_PORTAL_BASE + "/error";

	protected static final String URL_TEST_DEL_USER = URL_TEST_BASE
			+ "/deluser";
	protected static final String BODY_PORTAL_HEADER = "header";
	protected static final String BODY_PORTAL_FOOTER = "footer";
	protected static final String BODY_PORTAL_REQ_REQ = "reg_req";
	protected static final String BODY_PORTAL_REQ_COMP = "reg_comp";

	protected static final String BODY_PORTAL_LOGIN = "login";
	protected static final String BODY_PORTAL_ERROR = "err";
	protected static final String BODY_PORTAL_CONFIRM = "reg_confirm";
	protected static final String BODY_ADMIN_HEADER = "header";
	protected static final String BODY_ADMIN_FOOTER = "footer";
	protected static final String BODY_ADMIN_REG = "reg";
	protected static final String BODY_ADMIN_REG_COMPLETE = "reg_complete";
	protected static final String PARAM_MSG = "_msg";
	protected static final String PARAM_OBJ_DESC = "_desc";
	protected static final String PARAM_OBJ_SIG = "_signature";
	protected static final String PARAM_UNAME = "_uname";
	protected static final String PARAM_PWD = "_id";
	protected static final String PARAM_PWD2 = "_pid";
	protected static final String PARAM_SESS_ID = "_sessid";
	protected static final String PARAM_PROVIDER = "_prov";
	protected static final String PARAM_PROVIDER_ID = "_provid";
	protected static final String PARAM_OBJ_ID = "_objid";
	protected static final String PARAM_OBJ_EXT_PARAMS = "_extraparams";
	protected static final String PARAM_DB_SESS = "_dbsess";
	protected static final String PARAM_OVERRIDE_CONFIRM = "_override";
	protected static final String PARAM_PARAMS = "_params";
	protected static final String PARAM_INSTID = "_instid";
	protected static final String PARAM_DEVICEID = "_id";
	protected static final String PARAM_DEVICETYPE = "_devtype";
	protected static final String PARAM_DESKTOP_ID = "_deskid";
	protected static final String PARAM_PORT = "_port";
	protected static final String PARAM_APP_ID = "_appid";
	protected static final String PARAM_VDIUSER_ID = "_vdiuser";
	protected static final String PARAM_VDIPWD_ID = "_vdipwd";

	protected static final String TAG_ERROR_MSG = "ERROR_MSG";
	protected static final String TAG_AID_VER = "AIDVER";
	protected static final String TAG_MSG = "MSG_";
	protected static final String TAG_PARAMS = "PARAMS";
	protected static final String ENC_DEC_KEY = "%^UNR#%^UEB$%@$";
	protected static final int OBJ_PROTO_NONE = 0;
	protected static final int OBJ_PROTO_RDP = 1;
	protected static final int OBJ_PROTO_ICA = 2;
	protected static final int OBJ_STATE_UNKNOWN = 0;
	protected static final int OBJ_STATE_DOWN = 1;
	protected static final int OBJ_STATE_WAKING = 2;
	protected static final int OBJ_STATE_UP = 3;
	protected static final int OBJ_STATE_WAKING_FAILED = 4;

	protected static final String OBJ_MAC_ADDRS = "_AMA";
	protected static final String OBJ_PARAMS_DELIM = "&";
	protected static final String COMPONENT_RET_VAL_NAME = "RetCode";
	protected static final String GET_ART_HOSTS_REQ = "GET %s%s?%s=%s&%s=%s&%s=%d HTTP/1.1\r\nHost: %s\r\n\r\n";
	protected static final String GET_ART_NEWSTATE_SESSION_REQ = "GET %s%s?%s=%s&%s=%s&%s=%s&%s=%s HTTP/1.1\r\nHost: %s:%d\r\n\r\n";
	protected static final String GET_ART_HOST_REFRESH_STATE_REQ = "GET %s%s?%s=%d&%s=%s&%s=%s HTTP/1.1\r\nHost: %s:%d\r\n\r\n";
	protected static final String GET_ART_HOST_POWERUP_REQ = "GET %s%s?%s=%s&%s=%s&%s=%s&%s=%s HTTP/1.1\r\nHost: %s:%d\r\n\r\n";
	protected static final String GET_ART_PROXY_REQ = "GET %s%s?%s=%s HTTP/1.1\r\nHost: %s\r\n\r\n";
	protected static final String GET_ART_ENTERPRISE_LICENSE_REQ = "GET %s%s?%s=%s&%s=%s&%s=%s&%s=%d HTTP/1.1\r\nHost: %s\r\n\r\n";
	protected static final String CLV_XML_RULES = "Rules";
	protected static final String CLV_XML_RULE = "Rule";
	protected static final String CLV_XML_TRIGS = "Triggers";
	protected static final String CLV_XML_TRIG = "Trigger";
	protected static final String CLV_XML_BODY = "Body";
	protected static final String CLV_XML_CONDITION = "Condition";
	protected static final String CLV_XML_EVAL = "Eval";
	protected static final String CLV_XML_CASE = "Case";

	protected static final String CLV_TRIG_PRE_LOGIN = "prelogin";
	protected static final String CLV_TRIG_POST_LOGIN = "postlogin";
	protected static final String CLV_TRIG_TIMER = "timer";
	protected static final int HOST_STATUS_UNKNOWN = 0;
	protected static final int HOST_STATUS_AVAIL = 1;
	protected static final int HOST_STATUS_NOT_AVAIL = 2;
	protected static final int HOST_STATUS_POWER_UP = 3;
	protected static final int HOST_STATUS_CONNECTING = 4;
	protected static final int HOST_STATUS_CONNECTED = 5;
	protected static final int HOST_STATUS_DISCONNECTING = 6;
	protected static final int HOST_STATUS_DISCONNECTED = 7;
	protected static final int HOST_STATUS_CHECK_AVAIL = 8;
	protected static final int HOST_STATUS_POWER_UP_FAILED = 9;

	protected static final int ERR_COMM_UNKNOWN = -1;
	protected static final int ERR_COMM_NO_TSAPP_SERVER = -2;
	String m_username = "", m_password = "";
	protected static boolean VMViewSSO = false;

	// protected static final String ;
	// protected static final int ;
	private FatClientProxy m_TCSprotocol;
	private CfgManager m_CfgManager;
	private tcsgui m_Owner;
	private tcsmng m_TCS;
	private int m_Port;
	private Global m_Global;
	private Folders.Folder m_CurFolder;
	private String m_ARTServerVersion;
	private int appNums;
	private String m_NetworkType;
	private String sessionUser="";
	private String sessionPass="";
	public DatagramSocket sock;
	public SocketAddress address;
	public ServerSocket otherServerSock;
	int otherListenPort;
	public FCP_AcceptThreadAG conn;
	public FCP_AcceptThreadSPX connSPX;
	

	public ARTInterface(tcsmng prmTCS, Folders.Folder folder, tcsgui prmOwner,
			FatClientProxy prmTCSprotocol, CfgManager prmCfgManager,
			Global prmGlobal) {// prmDeviceType do we
								// need it?

		m_TCSprotocol = prmTCSprotocol;
		m_CfgManager = prmCfgManager;
		m_Owner = prmOwner;
		m_Global = prmGlobal;
		m_CurFolder = folder;
		m_TCS = prmTCS;
		m_Port = m_TCSprotocol.addConn(m_CfgManager.GetARPServerAdd(),
				m_CfgManager.GetARPServerPort(), 0);
		
	}

	public class HostResults {
		private ArrayList m_List;
		private Folders.Folder m_RootFolder;
		private boolean m_Failed;
		private int m_FailReason;

		public HostResults() {
			m_List = null;

			Folders f = new Folders();
			m_RootFolder = f.new Folder("", null);
			m_Failed = false;
			m_FailReason = 0;
		}

		public void setList(ArrayList prmList) {
			m_List = prmList;
		}

		public void setFailure(int prmReason) {
			m_Failed = true;
			m_FailReason = prmReason;
		}

		public ArrayList getList() {
			return m_List;
		}

		public Folders.Folder getRootFolder() {
			return m_RootFolder;
		}

		public boolean getFailed() {
			return m_Failed;
		}

		public int getFailReason() {
			return m_FailReason;
		}

	}

	private class ARTPrivilegedExceptionAction implements
			PrivilegedExceptionAction {

		private String m_Request;

		public ARTPrivilegedExceptionAction(String prmRequest) {
			m_Request = prmRequest;
		}

		public Object run() throws Exception {
			 int lc = m_TCSprotocol.addConn(m_CfgManager.GetARPServerAdd(),m_CfgManager.GetARPServerPort(), 0);

			URL url = new URL("http://" + FatClientProxy.LOCALHOST_ADDR + ":" + Integer.toString(lc) + m_Request);
			Document doc = null;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			try {
				InputStream in = new BufferedInputStream(conn.getInputStream());
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();

				doc = db.parse(in);
			} finally {
				conn.disconnect();
			}

			return doc;
		}

	}

	Document getARTResponse(String prmRequest, int port) {
		try {
			String st;			
			return (Document) AccessController.doPrivileged(new ARTPrivilegedExceptionAction(prmRequest));
		} catch (PrivilegedActionException ex) {
			return null;
		}

	}

	public int extractPort(String prmHost) {
		int i = prmHost.indexOf(":");
		if (i > -1) {
			try {
				return Integer.parseInt(prmHost.substring(i));
			} catch (Exception e) {
				return -1;
			}
		} else {
			return 3389;
		}
	}

	public String extractServer(String prmHost) {
		int i = prmHost.indexOf(":");
		if (i > 0) {
			try {
				return prmHost.substring(0, i - 1);
			} catch (Exception e) {
				return prmHost;
			}
		} else {
			return prmHost;
		}
	}

	class passArtInfo extends Thread {

		private HostResults hosts;
		
		public passArtInfo(HostResults prmHosts) {
			hosts = prmHosts;
			
		}

		public void run() {
			passArtInfoAction la = new passArtInfoAction(hosts);

			AccessController.doPrivileged(la);
		}
	}

	class passArtInfoAction implements PrivilegedAction {

		private HostResults m_Hosts;
		public passArtInfoAction(HostResults prmHosts) {
			m_Hosts = prmHosts;
			
		}

		public Object run() {

			Host host = null;
			int rdpPort;
			String hostSt;
			String uname = "";
			String pwd = "";
			String hostIP = "";
			String m_Dest = "";
			int d;
			Integer i;
			int m_Width;
			int m_Height;
			int m_ColorDepth = 0;
			int m_RedirDrives;
			int m_RedirPrinters;
			int m_RedirPorts;
			int m_RedirSmartCards;
			int m_RedirClipboard;
			int m_RedirPOSDevices;
			int m_BitmapCaching;
			int m_PerfFlags = 0;
			int m_Sound = 1;
			int m_ConnectToConsole = 0;
			int m_Span = 2;
			int m_SSO = 1;
			String m_StartApp = "";
			String m_WorkDir = "";
			Apps.TSApp m_App;
			TSAppServer AppServer;
			int listenPort;
			ServerSocket VDIServerSock;
			int VDIListenPort;
			ServerSocket APPServerSock;
			int APPListenPort;
			
			m_RedirPrinters = Integer.parseInt(m_TCS
					.getDBMngParam("RedirPrinters"));
			m_RedirClipboard = Integer.parseInt(m_TCS
					.getDBMngParam("RedirClipboard"));
			m_RedirDrives = Integer
					.parseInt(m_TCS.getDBMngParam("RedirDrives"));
			m_RedirPorts = Integer.parseInt(m_TCS.getDBMngParam("RedirPorts"));
			m_ConnectToConsole = Integer.parseInt(m_TCS.getDBMngParam("console"));
			m_RedirPOSDevices = Integer.parseInt(m_TCS
					.getDBMngParam("RedirPOS"));
			m_RedirSmartCards = Integer.parseInt(m_TCS
					.getDBMngParam("RedirSmartCards"));
			d = Integer.parseInt(m_TCS.getDBMngParam("DefColorDepth"));
			switch (d) {
			// 8 bit color depth
			case 1:
				m_ColorDepth = 8;
				break;
			// 16 bit color depth
			case 2:
				m_ColorDepth = 16;
				break;
			// 24 bit color depth
			case 3:
				m_ColorDepth = 24;
				break;
			// 32 bit color depth
			case 4:
				m_ColorDepth = 32;
				break;
			}
			d = Integer.parseInt(m_TCS.getDBMngParam("DefSize"));
			if (d == 4) {
				// User selected custom size
				i = Integer.parseInt(m_TCS.getDBMngParam("DefWidth"));
				if (i == null) {
					// reportError(ERR_INVALID_WIDTH, "");
					return null;
				}
				m_Width = i;
				i = Integer.parseInt(m_TCS.getDBMngParam("DefHeight"));
				if (i == null) {
					// reportError(ERR_INVALID_HEIGHT, "");
					return null;
				}
				m_Height = i;
			} else {
				// User selected a pre-defined size
				m_Width = -1;
				m_Height = -1;
				switch (d) {
				// 640X480
				case 1:
					m_Width = 640;
					m_Height = 480;
					break;
				// 800X600
				case 2:
					m_Width = 800;
					m_Height = 600;
					break;
				// 1024X768
				case 3:
					m_Width = 1024;
					m_Height = 768;
					break;
				}
			}
			if (m_TCS.getDBMngParam("DesktopWallpaper").equalsIgnoreCase("1")) {
				m_PerfFlags += 0x1;
			}
			if (m_TCS.getDBMngParam("FullWindowDrag").equalsIgnoreCase("1")) {
				m_PerfFlags += 0x2;
			}
			if (m_TCS.getDBMngParam("MenuAnim").equalsIgnoreCase("1")) {
				m_PerfFlags += 0x4;
			}
			if (m_TCS.getDBMngParam("Theme").equalsIgnoreCase("1")) {
				m_PerfFlags += 0x8;
			}
			// DatagramSocket sock;
			try {
				InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 0);
				sock = new DatagramSocket(addr);
			} catch (Exception e) {
				return null;
			}
			int resNum = appNums + m_Hosts.getList().size();
			if (m_CfgManager.AllowCustomDest() == true) {
				resNum++;
			}
			String clientPath = System.getProperty("user.home") +"/.ArrayDD/DesktopDirect ";
			String cmdPath = clientPath + sock.getLocalPort() + " " + resNum;
			MacOSCommandThread thd = new MacOSCommandThread(cmdPath, m_Owner);
			thd.start();
			try {
				System.out.println("call DesktopDirect");
			} catch (Exception e) {

			}
			byte[] rbuf = new byte[10240];
			DatagramPacket rpacket = new DatagramPacket(rbuf, 4096);
			DatagramPacket spacket = new DatagramPacket(rbuf, 4096);
			int mType;
			try {
				sock.receive(rpacket);
				address = rpacket.getSocketAddress();
			} catch (Exception e) {
				System.out.println("LaunchDesktop - Receive Exception "
						+ e.getMessage());
			}

			ByteArrayInputStream bis = new ByteArrayInputStream(rbuf);
			DataInputStream in = new DataInputStream(bis);

			// First read message type
			int m_HostIndex;
			mType = -1;
			int m_ResourceIndex = 0;
			Map hostMap = m_TCS.getDesktopIndexMap();
			Map vdiSockMap = m_TCS.getVDIServerSockMap();
			Map vdiPortMap = m_TCS.getVDIPortMap();
			Map vdiConnMap = m_TCS.getVDIConnMap();
			Map appSockMap = m_TCS.getAppServerSockMap();
			Map appPortMap = m_TCS.getAppPortMap();
			Map appConnMap = m_TCS.getAppConnMap();
			try {
				mType = in.readInt();
				if (mType == 1) {
					// Client requests launch information
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(bos);
					for (int folderNum = 0; folderNum < m_CurFolder.getItems()
							.size(); folderNum++) {
						Folders.Folder f = (Folders.Folder) m_CurFolder
								.getItems().get(folderNum);
						for (m_HostIndex = 0; m_HostIndex < f.getApps().size(); m_HostIndex++) {
							bos.reset();
							hostIP = "";
							m_StartApp = "";
							m_StartApp = ((Apps.App) (f.getApps()
									.get(m_HostIndex))).getLocation();
							m_App = (Apps.TSApp) (f.getApps().get(m_HostIndex));
							m_ResourceIndex++;
							hostMap.put(m_App.getName(), m_ResourceIndex);
							APPServerSock = new ServerSocket(0, 5,
									InetAddress.getByName("127.0.0.1"));
							APPListenPort = APPServerSock.getLocalPort();
							
							listenPort = APPListenPort;
							if ((m_Owner.getPlatform() != null)
									&& (m_Owner.getPlatform().compareTo("AG") == 0)){
								FCP_AcceptThreadAG appConn = new FCP_AcceptThreadAG(m_TCSprotocol,
										"127.0.0.1", APPServerSock, APPListenPort);
								appSockMap.put(m_App.getName(), APPServerSock);
								appPortMap.put(m_App.getName(), APPListenPort);
								appConnMap.put(m_App.getName(), appConn);
							} else{
								FCP_AcceptThreadSPX appConn = new FCP_AcceptThreadSPX(m_TCSprotocol,
										"127.0.0.1", APPServerSock, APPListenPort);
								appSockMap.put(m_App.getName(), APPServerSock);
								appPortMap.put(m_App.getName(), APPListenPort);
								appConnMap.put(m_App.getName(), appConn);
							}

							// Destination Port
							out.writeInt(listenPort);
							// Width
							out.writeInt(m_Width);
							// Height
							out.writeInt(m_Height);
							// Color Depth
							out.writeInt(m_ColorDepth);
							// Redir options
							out.writeInt(m_RedirDrives);
							out.writeInt(m_RedirPrinters);
							out.writeInt(m_RedirPorts);
							out.writeInt(m_RedirSmartCards);
							out.writeInt(m_RedirClipboard);
							out.writeInt(m_RedirPOSDevices);
							// User experience options
							out.writeInt(m_PerfFlags);
							// Sound
							out.writeInt(m_Sound);
							// Console connection
							out.writeInt(m_ConnectToConsole);						
							// SSO
							if (m_CfgManager.GetSSO()) {
								out.writeInt(1);
							} else {
								out.writeInt(0);
							}

							// Check if SSO is requested
							if (m_CfgManager.GetSSO()) {
								// SSO is request, but is it enabled
								
								// Username
								out.writeInt(sessionUser.getBytes().length);
								out.write(sessionUser.getBytes());
								// Password
								out.writeInt(sessionPass.getBytes().length);
								out.write(sessionPass.getBytes());
							}

							// Domain
							if (m_CfgManager.GetDomain() != null) {
								out.writeInt(m_CfgManager.GetDomain().length());
								if (m_CfgManager.GetDomain().length() > 0) {
									out.write(m_CfgManager.GetDomain()
											.getBytes());
								}
							} else {
								out.writeInt(0);
							}

							// Start App
							System.out.println("m_StartApp is " + m_StartApp);
							out.writeInt(m_StartApp.length());
							if (m_StartApp.length() > 0) {
								out.write(m_StartApp.getBytes());
							}

							// Work Dir
							out.writeInt(m_WorkDir.length());
							if (m_StartApp.length() > 0) {
								out.write(m_WorkDir.getBytes());
							}

							if (m_App.getName() != null) {
								out.writeInt(m_App.getName().length());
								out.write(m_App.getName().getBytes());
							} else {
								out.writeInt(0);
							}
							// spacket = new DatagramPacket(bos.toByteArray(),
							// bos.size());
							spacket.setData(bos.toByteArray(), 0, bos.size());
							spacket.setSocketAddress(address);
							sock.send(spacket);
						}
					}
					for (m_HostIndex = 0; m_HostIndex < m_CurFolder.getApps()
							.size(); m_HostIndex++) {
						bos.reset();
						hostIP = "";
						m_StartApp = "";
						m_StartApp = ((Apps.App) (m_CurFolder.getApps()
								.get(m_HostIndex))).getLocation();
						m_App = (Apps.TSApp) (m_CurFolder.getApps()
								.get(m_HostIndex));
						m_ResourceIndex++;
						hostMap.put(m_App.getName(), m_ResourceIndex);
						APPServerSock = new ServerSocket(0, 5,
								InetAddress.getByName("127.0.0.1"));
						APPListenPort = APPServerSock.getLocalPort();
						
						listenPort = APPListenPort;
						if ((m_Owner.getPlatform() != null)
								&& (m_Owner.getPlatform().compareTo("AG") == 0)){
							FCP_AcceptThreadAG appConn = new FCP_AcceptThreadAG(m_TCSprotocol,
									"127.0.0.1", APPServerSock, APPListenPort);
							appSockMap.put(m_App.getName(), APPServerSock);
							appPortMap.put(m_App.getName(), APPListenPort);
							appConnMap.put(m_App.getName(), appConn);
						} else{
							FCP_AcceptThreadSPX appConn = new FCP_AcceptThreadSPX(m_TCSprotocol,
									"127.0.0.1", APPServerSock, APPListenPort);
							appSockMap.put(m_App.getName(), APPServerSock);
							appPortMap.put(m_App.getName(), APPListenPort);
							appConnMap.put(m_App.getName(), appConn);
						}
						// Destination Port
						out.writeInt(listenPort);
						// Width
						out.writeInt(m_Width);
						// Height
						out.writeInt(m_Height);
						// Color Depth
						out.writeInt(m_ColorDepth);
						// Redir options
						out.writeInt(m_RedirDrives);
						out.writeInt(m_RedirPrinters);
						out.writeInt(m_RedirPorts);
						out.writeInt(m_RedirSmartCards);
						out.writeInt(m_RedirClipboard);
						out.writeInt(m_RedirPOSDevices);
						// User experience options
						out.writeInt(m_PerfFlags);
						// Sound
						out.writeInt(m_Sound);
						// Console connection
						out.writeInt(m_ConnectToConsole);
						// SSO
						if (m_CfgManager.GetSSO()) {
							out.writeInt(1);
						} else {
							out.writeInt(0);
						}

						// Check if SSO is requested
						if (m_CfgManager.GetSSO()) {
							// SSO is request, but is it enabled
							
							// Username
							out.writeInt(sessionUser.getBytes().length);
							out.write(sessionUser.getBytes());
							// Password
							out.writeInt(sessionPass.getBytes().length);
							out.write(sessionPass.getBytes());
						}

						// Domain
						if (m_CfgManager.GetDomain() != null) {
							out.writeInt(m_CfgManager.GetDomain().length());
							if (m_CfgManager.GetDomain().length() > 0) {
								out.write(m_CfgManager.GetDomain().getBytes());
							}
						} else {
							out.writeInt(0);
						}

						// Start App
						System.out.println("m_StartApp is " + m_StartApp);
						out.writeInt(m_StartApp.length());
						if (m_StartApp.length() > 0) {
							out.write(m_StartApp.getBytes());
						}

						// Work Dir
						out.writeInt(m_WorkDir.length());
						if (m_StartApp.length() > 0) {
							out.write(m_WorkDir.getBytes());
						}

						if (m_App.getName() != null) {
							out.writeInt(m_App.getName().length());
							out.write(m_App.getName().getBytes());
						} else {
							out.writeInt(0);
						}
						spacket.setData(bos.toByteArray(), 0, bos.size());
						spacket.setSocketAddress(address);
						sock.send(spacket);
					}
					for (m_HostIndex = 0; m_HostIndex < m_Hosts.getList()
							.size(); m_HostIndex++) {
						bos.reset();
						hostSt = "";
						hostIP = "";
						m_StartApp = "";
						// checkVDIResources(m_HostIndex, m_Hosts);
						if (m_HostIndex > -1) {
							// The desktop to launch is from the list of
							// registered desktops
							if ((m_Hosts == null)
									|| (m_HostIndex > m_Hosts.getList().size())) {
								// Invalid host index
								break;
							}
							host = (Host) m_Hosts.getList().get(m_HostIndex);
							m_ResourceIndex++;
							hostMap.put(host.GetDesc(), m_ResourceIndex);
							hostSt = host.GetHost();
							rdpPort = host.GetPort();
							// System.out.println("hostSt is " + hostSt
							// + ",rdpPort is " + rdpPort);
						} else {
							// The desktop to launch is an arbitrary host
							host = null;
							hostSt = extractServer(m_Dest);
							rdpPort = extractPort(m_Dest);
						}
						
						if (hostSt.compareTo("") == 0) {
							m_Owner.reportError(tcsgui.ERR_HOSTNAME_MISSING, "");
						}

						if (!m_CfgManager.GetSSO()) {
							m_SSO = 0;
						}
							VDIServerSock = new ServerSocket(0, 5,
									InetAddress.getByName("127.0.0.1"));
							VDIListenPort = VDIServerSock.getLocalPort();
							
							listenPort = VDIListenPort;
							if ((m_Owner.getPlatform() != null)
									&& (m_Owner.getPlatform().compareTo("AG") == 0)){
								FCP_AcceptThreadAG conn = new FCP_AcceptThreadAG(m_TCSprotocol,
										"127.0.0.1", VDIServerSock, VDIListenPort);
								vdiSockMap.put(host.GetDesc(), VDIServerSock);
								vdiPortMap.put(host.GetDesc(), VDIListenPort);
								vdiConnMap.put(host.GetDesc(), conn);
							}else {
								FCP_AcceptThreadSPX conn = new FCP_AcceptThreadSPX(m_TCSprotocol,
										"127.0.0.1", VDIServerSock, VDIListenPort);
								vdiSockMap.put(host.GetDesc(), VDIServerSock);
								vdiPortMap.put(host.GetDesc(), VDIListenPort);
								vdiConnMap.put(host.GetDesc(), conn);
							}
						if (listenPort == -1) {
							m_Owner.reportError(
									tcsgui.ERR_FAILED_TO_CONNECT_TO_HOST,
									hostSt);
						}
						// Destination Port
						out.writeInt(listenPort);
						// Width
						out.writeInt(m_Width);
						// Height
						out.writeInt(m_Height);
						// Color Depth
						out.writeInt(m_ColorDepth);
						// Redir options
						out.writeInt(m_RedirDrives);
						out.writeInt(m_RedirPrinters);
						out.writeInt(m_RedirPorts);
						out.writeInt(m_RedirSmartCards);
						out.writeInt(m_RedirClipboard);
						out.writeInt(m_RedirPOSDevices);
						// User experience options
						out.writeInt(m_PerfFlags);
						// Sound
						out.writeInt(m_Sound);
						// Console connection
						out.writeInt(m_ConnectToConsole);					
						// SSO
						out.writeInt(m_SSO);
						// Check if SSO is requested
						if (m_SSO == 1) {
							// SSO is request, but is it enabled
							if (m_CfgManager.GetSSO()) {
								// If the user is launching a registered
								// desktop
								// we check if Host SSO applies to the
								// host
								if(host.GetProvider().toString().equalsIgnoreCase("VMView") &&
										m_username.length()>0 && m_password.length()>0){
									uname = m_username;
									pwd = m_password;
								} else if (host.GetUsername().compareTo("") != 0) {
									// Host SSO is specified for this
									// desktop
									uname = host.GetUsername();
									pwd = host.GetPwd();
								} else {
									// Use session SSO									
									uname = sessionUser;
									pwd = sessionPass;
								}
							}
							// Username
							out.writeInt(uname.getBytes().length);
							out.write(uname.getBytes());

							// Password
							out.writeInt(pwd.getBytes().length);
							out.write(pwd.getBytes());
						}

						// Domain
						if (m_CfgManager.GetDomain() != null) {
							out.writeInt(m_CfgManager.GetDomain().length());
							if (m_CfgManager.GetDomain().length() > 0) {
								out.write(m_CfgManager.GetDomain().getBytes());
							}
						} else {
							out.writeInt(0);
						}

						// Start App
						out.writeInt(m_StartApp.length());
						if (m_StartApp.length() > 0) {
							out.write(m_StartApp.getBytes());
						}
						// Work Dir
						out.writeInt(m_WorkDir.length());
						if (m_StartApp.length() > 0) {
							out.write(m_WorkDir.getBytes());
						}
						out.writeInt(host.GetDesc().length());
						out.write(host.GetDesc().getBytes());

						spacket.setData(bos.toByteArray(), 0, bos.size());
						spacket.setSocketAddress(address);
						sock.send(spacket);

					}
					if (m_CfgManager.AllowCustomDest() == true) {
						bos.reset();
						// Destination Port
						otherServerSock = new ServerSocket(0, 5,
								InetAddress.getByName("127.0.0.1"));
						otherListenPort = otherServerSock.getLocalPort();
						m_ResourceIndex++;
						hostMap.put("other", m_ResourceIndex);
						out.writeInt(otherListenPort);
						// Width
						out.writeInt(m_Width);
						// Height
						out.writeInt(m_Height);
						// Color Depth,
						out.writeInt(m_ColorDepth);
						// Redir options
						out.writeInt(m_RedirDrives);
						out.writeInt(m_RedirPrinters);
						out.writeInt(m_RedirPorts);
						out.writeInt(m_RedirSmartCards);
						out.writeInt(m_RedirClipboard);
						out.writeInt(m_RedirPOSDevices);
						// User experience options
						out.writeInt(m_PerfFlags);
						// Sound
						out.writeInt(m_Sound);
						// Console connection
						out.writeInt(m_ConnectToConsole);		
						// SSO
						if (!m_CfgManager.GetSSO()) {
							m_SSO = 0;
						}
						out.writeInt(m_SSO);
						// Check if SSO is requested
						if (m_SSO == 1) {
							// SSO is request, but is it enabled
							if (m_CfgManager.GetSSO()) {

								// Use session SSO
								uname = sessionUser;
								pwd = sessionPass;

							}

							// Username
							out.writeInt(uname.getBytes().length);
							out.write(uname.getBytes());

							// Password
							out.writeInt(pwd.getBytes().length);
							out.write(pwd.getBytes());
						}
						// Domain
						if (m_CfgManager.GetDomain() != null) {
							out.writeInt(m_CfgManager.GetDomain().length());
							if (m_CfgManager.GetDomain().length() > 0) {
								out.write(m_CfgManager.GetDomain().getBytes());
							}
						} else {
							out.writeInt(0);
						}

						// Start App
						m_StartApp = "";
						out.writeInt(m_StartApp.length());
						if (m_StartApp.length() > 0) {
							out.write(m_StartApp.getBytes());
						}
						// Work Dir
						m_WorkDir = "";
						out.writeInt(m_WorkDir.length());
						if (m_StartApp.length() > 0) {
							out.write(m_WorkDir.getBytes());
						}
						String desc = "other";
						out.writeInt(desc.length());
						out.write(desc.getBytes());
						if ((m_Owner.getPlatform() != null)
								&& (m_Owner.getPlatform().compareTo("AG") == 0)){
						conn = new FCP_AcceptThreadAG(m_TCSprotocol,
								"127.0.0.1", otherServerSock, otherListenPort);
						}else {
							connSPX = new FCP_AcceptThreadSPX(m_TCSprotocol,
									"127.0.0.1", otherServerSock, otherListenPort);
						}
						spacket = new DatagramPacket(bos.toByteArray(), bos.size());
						spacket.setSocketAddress(address);
						sock.send(spacket);

					}
					m_ResourceIndex++;
					hostMap.put("other_as", m_ResourceIndex);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("pass art config to DD - Exception "
						+ e.getMessage());
			}
			if (m_Hosts != null) {
				// TCS is ready
				try {
					Thread.sleep(1000 * 4);//wait for config take effect
				} catch (Exception e) {
					
				}
				m_TCS.setHosts(m_Hosts);
				m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_READY, 0, "");
			} else {
				// Failed to retrieve desktops
				m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_READY, 0, "");
			}
			
			return null;
		}
	}

	public void passArtConfig(HostResults m_Hosts) {
		passArtInfo thd = new passArtInfo(m_Hosts);
		thd.start();
	}

	public HostResults getAutoHost() {
		Document doc;
		String userName = "";
		String password = "";
		HostResults hres = new HostResults();

		m_Global.writeLog(Global.LOG_LEVEL_DEBUG, "GetAUtoHost - Begin ");
		ArrayList hosts = new ArrayList();
		int daysLeft, protocol;
		try {

			if (VMViewSSO) {
				doc = getARTResponse(
						URL_QUERY_BASE
								+ URL_QUERY_HOSTS
								+ "?"
								+ PARAM_UNAME
								+ "="
								+ URLEncoder.encode(
										sessionUser, "utf-8")
								+ "&"
								+ PARAM_PWD2
								+ "="
								+ URLEncoder.encode(
										sessionPass, "utf-8")
								+ "&"
								+ PARAM_DEVICEID
								+ "="
								+ URLEncoder.encode(Global.m_DevID, "utf-8")
								+ "&"
								+ PARAM_DEVICETYPE
								+ "="
								+ DEV_TYPE_MACOS
								+ "&"
								+ PARAM_INSTID
								+ "="
								+ URLEncoder.encode(m_CfgManager.GetInstID(),
										"utf-8") + "&" + PARAM_VDIUSER_ID + "="
								+ URLEncoder.encode(m_username, "utf-8") + "&"
								+ PARAM_VDIPWD_ID + "="
								+ URLEncoder.encode(m_password, "utf-8"),
						m_Port);
			} else {
				doc = getARTResponse(
						URL_QUERY_BASE
								+ URL_QUERY_HOSTS
								+ "?"
								+ PARAM_INSTID
								+ "="
								+ URLEncoder.encode(m_CfgManager.GetInstID(),
										"utf-8")
								+ "&"
								+ PARAM_UNAME
								+ "="
								+ URLEncoder.encode(
										sessionUser, "utf-8")
								+ "&"
								+ PARAM_PWD2
								+ "="
								+ URLEncoder.encode(
										sessionPass, "utf-8")
								+ "&" + PARAM_DEVICETYPE + "=" + DEV_TYPE_MACOS
								+ "&" + PARAM_DEVICEID + "="
								+ URLEncoder.encode(Global.m_DevID, "utf-8"),
						m_Port);
			}
			NodeList nodes;

			nodes = doc.getElementsByTagName("ClientVerification");
			if (nodes.getLength() > 0) {
				if (getCharacterDataFromElement((Element) nodes.item(0))
						.equals("Failed")) {
					int devidres;
					try {
						devidres = Integer.parseInt(((Element) nodes.item(0))
								.getAttribute("DeviceID"));
					} catch (Exception e) {
						devidres = 0;
					}
					if (devidres == 2) {
						m_Owner.reportError(tcsgui.ERR_DEV_ID_PEND, "");
					} else {
						m_Owner.reportError(tcsgui.ERR_DEV_ID_FAILED, "");
					}
					hres.setFailure(devidres);
				}
			}

			int rdpPort;
			nodes = doc.getElementsByTagName(ARP_HOSTS);
			if ((nodes != null) && (nodes.getLength() > 0)) {
				try {
					Element hostsNode = (Element) nodes.item(0);
					rdpPort = Integer.parseInt(hostsNode
							.getAttribute(ATTR_HOSTS_RDP_PORT));
					m_CfgManager.SetRDPPort(rdpPort);
				} catch (Exception e) {

				}
			}

			nodes = doc.getElementsByTagName("App");
			appNums = nodes.getLength();
			m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
					"GetAutoHost - Apps count = " + nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++) {

				Element appNode = (Element) nodes.item(i);
				if (i > 0) {
					Element prevAppNode = (Element) nodes.item(i - 1);
					String appName = appNode.getAttribute("Name");
					String prevAppName = prevAppNode.getAttribute("Name");
					if (appName.compareTo(prevAppName) == 0) {
						continue;
					}
				}
				m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
						"GetAutoHost - App node = " + appNode.getTextContent());

				Apps apps = new Apps();
				// Apps.TSApp app = apps.new TSApp(appNode,
				// hres.getRootFolder());
				Apps.TSApp app = apps.new TSApp(appNode, m_CurFolder);
			}

			nodes = doc.getElementsByTagName("Host");
			m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
					"GetAutoHost - Nodes count = " + nodes.getLength());

			for (int i = 0; i < nodes.getLength(); i++) {
				userName = "";
				password = "";
				Element host = (Element) nodes.item(i);
				m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
						"GetAutoHost - Host node = " + host.getTextContent());

				try {
					daysLeft = Integer.parseInt(host
							.getAttribute(ATTR_HOST_DAYS_LEFT));
				} catch (Exception e) {
					daysLeft = 0;
				}

				try {
					protocol = Integer.parseInt(host
							.getAttribute(ATTR_HOST_PROTOCOL));
				} catch (Exception e) {
					protocol = OBJ_PROTO_RDP;
				}
				// get SessInfo
				String sessInfo = host.getAttribute(ATTR_HOST_SESS_INFO);
				String sessLen = null;
				try {
					sessLen = host.getAttribute(ATTR_HOST_SESS_LEN);
				} catch (Exception e) {

				}

				// get userName password decode
				if (sessInfo != null && sessInfo.trim().length() != 0) {
					// acquire userName
					userName = getUserName(sessInfo);
					StringBuilder builder = new StringBuilder();

					// MaskUnmaskStr
					int length;
					if (sessLen == null || sessLen.length() == 0) {
						length = userName.length();
					} else {
						length = getUserNameLen(sessLen);
					}
					int keyLength = ENC_DEC_KEY.length();
					for (int k = 0; k < length; k++) {
						char newChar = (char) ((userName.charAt(k) ^ ENC_DEC_KEY
								.charAt(k % keyLength)));
						builder.append(newChar);
					}

					userName = builder.toString();
					builder.delete(0, builder.length());

					// acquire password
					password = getPassword(sessInfo);

					// MaskUnmaskStr
					if (sessLen == null || sessLen.length() == 0) {
						length = password.length();
					} else {
						length = getPasswordLen(sessLen);
					}

					for (int k = 0; k < length; k++) {
						char newChar = (char) ((password.charAt(k) ^ ENC_DEC_KEY
								.charAt(k % keyLength)));
						builder.append(newChar);
					}
					password = builder.toString();
				}
				Host tmpHost = new Host(m_Owner, m_TCSprotocol, m_CfgManager,
						this, i, daysLeft, protocol,
						host.getAttribute(ATTR_HOST_ID),
						StringEscapeUtils.unescapeHtml4(host.getAttribute(
								ATTR_HOST_DESC).toString()),
						getCharacterDataFromElement(host), userName,
						password,
						host.getAttribute(ATTR_HOST_PROVIDER),
						host.getAttribute(ATTR_HOST_PROVIDER_ID),
						"", // !!! Provider parameters not yet implemented
						host.getAttribute(ATTR_HOST_DESKTOP_ID),
						host.getAttribute(ATTR_HOST_PARAMS),
						m_CfgManager.GetInstID(), -1);

				hres.getRootFolder().addItem(tmpHost);
				hosts.add(tmpHost);
			}

			hres.setList(hosts);
			boolean Prt, Drv, Clip, Rsc, Ports;
			nodes = doc.getElementsByTagName("DPP");
			if ((nodes != null) && (nodes.getLength() > 0)) {
				try {
					Element hostsNode = (Element) nodes.item(0);
					Prt = hostsNode.getAttribute("RPrt").startsWith("1");
					Drv = hostsNode.getAttribute("RDrv").startsWith("1");
					Ports = hostsNode.getAttribute("RPorts").startsWith("1");
					Clip = hostsNode.getAttribute("RClip").startsWith("1");
					Rsc = hostsNode.getAttribute("RSC").startsWith("1");
					m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
							"GetAutoHost RPrt " + Prt);
					m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
							"GetAutoHost RClip " + Clip);
					m_CfgManager.SetRedir(Drv, Prt, Ports, Rsc, Clip);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO, "GetAutoHost - Exception "
					+ e.getMessage());
			return null;
		} finally {

		}
		return hres;
	}

	public String getStateSess(Host prmHost, String prmObjID,
			String prmDesktopID, String prmProvider, String prmProviderID) {

		String sessID = "";
		String hostIP = "";
		Document doc = null;
		m_Global.writeLog(Global.LOG_LEVEL_DEBUG, "GetStateSess " + prmObjID
				+ ", " + prmDesktopID + ", " + prmProvider + ", "
				+ prmProviderID);

		if (isValidIP(prmObjID)) {
			hostIP = prmObjID;
		} else if ((prmHost.GetProvider().toString().compareTo("VMView") != 0)
				&& (prmHost.GetProvider().toString().compareTo("XenDesktop") != 0)) {
			hostIP = m_TCSprotocol.resolveHost(prmObjID);
		}
		try {
			if ((prmHost.GetProvider().toString().compareTo("VMView") == 0)
					|| (prmHost.GetProvider().toString()
							.compareTo("XenDesktop") == 0)) {
				doc = getARTResponse(
						URL_OBJECT_BASE
								+ URL_OBJECT_NEW_STATE_SESS
								+ "?"
								+ PARAM_INSTID
								+ "="
								+ URLEncoder.encode(m_CfgManager.GetInstID(),
										"utf-8")
								+ "&"
								+ PARAM_OBJ_ID
								+ "="
								+ URLEncoder.encode(prmObjID, "utf-8")
								+ "&"
								+ PARAM_UNAME
								+ "="
								+ URLEncoder.encode(
										sessionUser, "utf-8")
								+ "&" + PARAM_PROVIDER + "="
								+ URLEncoder.encode(prmProvider, "utf-8") + "&"
								+ PARAM_PROVIDER_ID + "="
								+ URLEncoder.encode(prmProviderID, "utf-8")
								+ "&" + PARAM_DESKTOP_ID + "="
								+ URLEncoder.encode(prmDesktopID, "utf-8"),
						m_Port);
			} else {
				doc = getARTResponse(
						URL_OBJECT_BASE
								+ URL_OBJECT_NEW_STATE_SESS
								+ "?"
								+ PARAM_INSTID
								+ "="
								+ URLEncoder.encode(m_CfgManager.GetInstID(),
										"utf-8")
								+ "&"
								+ PARAM_OBJ_ID
								+ "="
								+ URLEncoder.encode(hostIP, "utf-8")
								+ "&"
								+ PARAM_UNAME
								+ "="
								+ URLEncoder.encode(
										sessionUser, "utf-8")
								+ "&" + PARAM_PROVIDER + "="
								+ URLEncoder.encode(prmProvider, "utf-8") + "&"
								+ PARAM_PROVIDER_ID + "="
								+ URLEncoder.encode(prmProviderID, "utf-8")
								+ "&" + PARAM_DESKTOP_ID + "="
								+ URLEncoder.encode(prmDesktopID, "utf-8"),
						m_Port);
			}
			NodeList nodes = doc.getElementsByTagName(ARP_STATE_SESS);

			if (nodes.getLength() > 0) {
				Element sess = (Element) nodes.item(0);
				sessID = sess.getAttribute(ATTR_STATE_SESS_ID);
			}

		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO,
					"GetStateSess - Exception " + e.getMessage());
			return null;
		} finally {

		}

		return sessID;
	}

	class MacOSCommandThread extends Thread {

		private String m_Cmd;
		private tcsgui m_Owner;

		public MacOSCommandThread(String prmCmd, tcsgui prmOwner) {
			m_Cmd = prmCmd;
			m_Owner = prmOwner;
		}

		public void run() {

			// System.out.println("Thread LaunchDesktop " + m_Dest + "," +
			// m_HostIndex);

			MacOSCommandAction la = new MacOSCommandAction(m_Cmd);

			AccessController.doPrivileged(la);
		}
	}

	class MacOSCommandAction implements PrivilegedAction {

		private String m_Cmd;
		private tcsgui m_Owner;

		public MacOSCommandAction(String prmCmd) {
			m_Cmd = prmCmd;
		}

		public Object run() {

			String[] shellcmd = { "/bin/sh", "-c", m_Cmd };

			try {

				Process p = Runtime.getRuntime().exec(shellcmd);
				try {
					p.waitFor(); // Wait for the open command to complete
				} catch (InterruptedException e) {
					System.out.println("Unable to wait for application to run");
					m_Owner.reportError(tcsgui.ERR_FAILED_TO_CONN_GENERIC,
							"Unable to wait for application to run");
				}
			} catch (IOException e) {
				System.out.println("Unable to open (run) application");
			}

			return null;
		}
	}

	public class ARTError {
		private int m_Code;
		private String m_Extra;

		ARTError(int prmCode, String prmExtra) {
			m_Code = prmCode;
			m_Extra = prmExtra;
		}

		public int getCode() {
			return m_Code;
		}

		public String getExtra() {
			return m_Extra;
		}
	}

	public class TSAppServer {
		private String m_Server;
		private int m_Port;
		private ARTError m_Err;

		TSAppServer(String prmServer, int prmPort) {
			m_Server = prmServer;
			m_Port = prmPort;
			m_Err = null;
		}

		TSAppServer(ARTError prmErr) {
			m_Server = null;
			m_Port = -1;
			m_Err = prmErr;
		}

		public String getServer() {
			return m_Server;
		}

		public int getPort() {
			return m_Port;
		}

		public ARTError getError() {
			return m_Err;
		}
	}

	public ARTError checkForError(Document prmDoc) {
		int ecode = 0;
		String extra;

		NodeList nodes = prmDoc.getElementsByTagName(ART_ERROR);

		if (nodes.getLength() > 0) {
			Element enode = (Element) nodes.item(0);

			try {
				if (enode.hasChildNodes()) {
					ecode = Integer.parseInt(enode.getChildNodes().item(0)
							.getNodeValue());
				}

			} catch (Exception e) {

			}

			extra = enode.getAttribute(ATTR_ERR_INFO);

			return new ARTError(ecode, extra);
		}

		return null;
	}

	public TSAppServer getTSAppServer(Apps.TSApp prmApp) {

		String sessID = "";

		m_Global.writeLog(Global.LOG_LEVEL_DEBUG,
				"GetTSAppServer " + prmApp.getID());

		try {
			Document doc = getARTResponse(URL_QUERY_QUERY_APP_SERVER + "?"
					+ PARAM_APP_ID + "=" + prmApp.getID(), m_Port);

			ARTError err = checkForError(doc);
			if (err != null) {
				return new TSAppServer(err);
			}

			NodeList nodes = doc.getElementsByTagName(ART_SERVER);

			if (nodes.getLength() > 0) {
				Element servNode = (Element) nodes.item(0);
				String serv = "";
				if (servNode.hasChildNodes()) {
					serv = servNode.getChildNodes().item(0).getNodeValue();
				}

				int port = 3389;

				try {
					port = Integer.parseInt(servNode
							.getAttribute(ATTR_SERVER_PORT));
				} catch (Exception e) {

				}
				System.out.println("terminal server ip is " + serv
						+ ",port is " + port);
				return new TSAppServer(serv, port);
			}

		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO,
					"GetTSAppServer - Exception " + e.getMessage());
			return null;
		} finally {

		}

		return null;
	}

	public String powerupDesktop(String prmInstID, String prmObjID,
			String prmDesktopID, String prmProvider, String prmProviderID,
			String prmDesc) {

		String sessID = "";

		m_Global.writeLog(Global.LOG_LEVEL_DEBUG, "PowerupDesktop - "
				+ prmInstID + ", " + prmObjID + ", " + prmDesktopID + ", "
				+ prmProvider + ", " + prmProviderID + ", " + prmDesc);

		try {
			Document doc = getARTResponse(
					URL_OBJECT_BASE
							+ URL_OBJECT_POWER_UP
							+ "?"
							+ PARAM_INSTID
							+ "="
							+ URLEncoder.encode(prmInstID, "utf-8")
							+ "&"
							+ PARAM_OBJ_ID
							+ "="
							+ prmObjID
							+ "&"
							+ PARAM_UNAME
							+ "="
							+ URLEncoder.encode(sessionUser,
									"utf-8") + "&" + PARAM_PROVIDER + "="
							+ prmProvider + "&" + PARAM_PROVIDER_ID + "="
							+ prmProviderID + "&" + PARAM_DESKTOP_ID + "="
							+ prmDesktopID + "&" + PARAM_OBJ_DESC + "="
							+ URLEncoder.encode(prmDesc, "utf-8"), m_Port);

			NodeList nodes = doc.getElementsByTagName(ARP_STATE_SESS);

			if (nodes.getLength() > 0) {
				Element sess = (Element) nodes.item(0);
				sessID = sess.getAttribute(ATTR_STATE_SESS_ID);
			}

		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO,
					"PowerupDesktop - Exception " + e.getMessage());
			return null;
		} finally {

		}

		return sessID;
	}

	public int queryHostAvail(String prmSessID, String prmObjID, int prmRDPPort,
			String prmDesktopID, String prmProvider, String prmProviderID,
			StringBuffer prmHost, StringBuffer prmVDIUser,
			StringBuffer prmVDIPwd, boolean monitorThd) {

		int state = OBJ_STATE_UNKNOWN;
		String host = "";
		String vdiUser = "";
		String vdiPwd = "";
		Document doc = null;
		m_Global.writeLog(Global.LOG_LEVEL_DEBUG, "QueryHostAvail " + prmSessID
				+ ", " + Integer.toString(prmRDPPort) + ", " + prmDesktopID
				+ ", " + prmProvider + ", " + prmProviderID);

		try {
			if (monitorThd) {
				doc = getARTResponse(
					URL_QUERY_BASE
							+ URL_QUERY_HOST_AVAIL
							+ "?"
							+ PARAM_INSTID
							+ "="
							+ URLEncoder.encode(m_CfgManager.GetInstID(),
									"utf-8")
							+ "&"
							+ PARAM_SESS_ID
							+ "="
							+ prmSessID
							+ "&"
							+ PARAM_OBJ_ID
							+ "="
							+ prmObjID
							+ "&"
							+ PARAM_PORT
							+ "="
							+ Integer.toString(prmRDPPort)
							+ "&"
							+ PARAM_PROVIDER
							+ "="
							+ URLEncoder.encode(prmProvider, "utf-8")
							+ "&"
							+ PARAM_PROVIDER_ID
							+ "="
							+ prmProviderID
							+ "&"
							+ PARAM_DESKTOP_ID
							+ "="
							+ prmDesktopID
							+ "&"
							+ PARAM_UNAME
							+ "="
							+ URLEncoder.encode(sessionUser,
									"utf-8")
							+ "&"
							+ PARAM_PWD2
							+ "="
							+ URLEncoder.encode(sessionPass,
									"utf-8"), m_Port);
			
			} else {
				doc = getARTResponse(
					URL_QUERY_BASE
							+ URL_QUERY_HOST_AVAIL
							+ "?"
							+ PARAM_INSTID
							+ "="
							+ URLEncoder.encode(m_CfgManager.GetInstID(),
									"utf-8")
							+ "&"
							+ PARAM_SESS_ID
							+ "="
							+ prmSessID
							+ "&"
							+ PARAM_PORT
							+ "="
							+ Integer.toString(prmRDPPort)
							+ "&"
							+ PARAM_PROVIDER
							+ "="
							+ URLEncoder.encode(prmProvider, "utf-8")
							+ "&"
							+ PARAM_PROVIDER_ID
							+ "="
							+ prmProviderID
							+ "&"
							+ PARAM_DESKTOP_ID
							+ "="
							+ prmDesktopID
							+ "&"
							+ PARAM_UNAME
							+ "="
							+ URLEncoder.encode(sessionUser,
									"utf-8")
							+ "&"
							+ PARAM_PWD2
							+ "="
							+ URLEncoder.encode(sessionPass,
									"utf-8"), m_Port);
			}
			
			NodeList nodes = doc.getElementsByTagName(ARP_STATE_SESS);

			if (nodes.getLength() > 0) {
				Element sess = (Element) nodes.item(0);
				state = Integer.parseInt(sess.getAttribute(ATTR_HOST_STATE));
				host = sess.getAttribute(ATTR_HOST_HOST);
				vdiUser = sess.getAttribute(ATTR_HOST_VDIUSER);
				vdiPwd = sess.getAttribute(ATTR_HOST_VDIPWD);
				if ((host != null) && (host.length() > 0)) {
					prmHost.append(host);
				}
				if ((vdiUser != null) && (vdiUser.length() > 0)) {
					prmVDIUser.append(vdiUser);
				}
				if ((vdiPwd != null) && (vdiPwd.length() > 0)) {
					prmVDIPwd.append(vdiPwd);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			m_Global.writeLog(Global.LOG_LEVEL_INFO,
					"QueryHostAvail - Exception " + e.getMessage());
		}

		return state;
	}

	public String getProxy() {
		try {
			Document doc = getARTResponse(
					URL_QUERY_BASE
							+ URL_QUERY_PROXY
							+ "?"
							+ PARAM_INSTID
							+ "="
							+ URLEncoder.encode(m_CfgManager.GetInstID(),
									"utf-8"), m_Port);

			NodeList nodes = doc.getElementsByTagName(ARP_PROXY);

			if (nodes.getLength() > 0) {
				return getCharacterDataFromElement((Element) nodes.item(0));
			}

		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO, "GetLicense - Exception "
					+ e.getMessage());
			return "";
		} finally {

		}

		return "";
	}

	public boolean getARTInfo() {
		try {
			Document doc = getARTResponse(
					URL_QUERY_INFO
							+ "?"
							+ PARAM_DEVICETYPE
							+ "="
							+ DEV_TYPE_MACOS
							+ "&"
							+ PARAM_SESS_ID
							+ "="
							+ m_TCSprotocol.getSessID()
							+ "&"
							+ PARAM_UNAME
							+ "="
							+ URLEncoder.encode(sessionUser,
									"utf-8"), m_Port);

			NodeList nodes = doc.getElementsByTagName(ART_VERSION);
			if (nodes.getLength() > 0) {
				m_ARTServerVersion = getCharacterDataFromElement((Element) nodes
						.item(0));
			}

			nodes = doc.getElementsByTagName(ART_KEEPALIVE);
			boolean keepAlive = true;
			long aliveDelay = 60000;

			if (nodes.getLength() > 0) {
				String aliveValue = getCharacterDataFromElement((Element) nodes
						.item(0));
				if (aliveValue.compareTo("0") == 0) {
					keepAlive = false;
				} else {
					aliveDelay = Long.parseLong(aliveValue.concat("000"));
				}
			}
			if (keepAlive) {
				try {
					m_TCSprotocol.keeper = new FCP_KeepAlive(m_TCSprotocol,
							aliveDelay, keepAlive);
					m_TCSprotocol.keeper.start();
				} catch (Exception e) {
					System.out.println("Could not start Keep-Alive timer!\n");
				}
			}

			nodes = doc.getElementsByTagName(ART_INST);

			if (nodes.getLength() > 0) {
				m_CfgManager
						.SetInstID(getCharacterDataFromElement((Element) nodes
								.item(0)));

				nodes = doc.getElementsByTagName(ART_SETTINGS);
				if (nodes.getLength() > 0) {
					m_CfgManager.setOrigXML(doc);
					m_CfgManager.parseXMLConfig((Element) nodes.item(0));
				} else {
					m_Global.writeLog(Global.LOG_LEVEL_INFO,
							"getARTInfo - No settings found");
				}
				nodes = doc.getElementsByTagName(ART_VDIAUTH);
				if (getCharacterDataFromElement((Element) nodes.item(0))
						.equals("1")) {
					VMViewSSO = true;
					CredentialsDlg dlg = new CredentialsDlg();
					dlg.setTitle("VDI Authentication");
					if (dlg.showDialog() == CredentialsDlg.Result_OK) {
						m_username = dlg.getUserName();
						m_password = dlg.getPasswd();
					}
				}
			} else {
				m_Global.writeLog(Global.LOG_LEVEL_ERROR,
						"getARTInfo - Instance node is missing");
			}

		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO, "getARTInfo - Exception "
					+ e.getMessage());
			return false;
		} finally {

		}

		return true;
	}

	public int GetPostLoginClientVerificationRules() {
		Document doc;
		int ret;
		try {
			doc = getARTResponse(
					URL_QUERY_BASE
							+ URL_QUERY_CLIENT_VERI2
							+ "?"
							+ PARAM_INSTID
							+ "="
							+ URLEncoder.encode(m_CfgManager.GetInstID(),
									"utf-8")
							+ "&"
							+ PARAM_UNAME
							+ "="
							+ URLEncoder.encode(sessionUser,
									"utf-8"), m_Port);

			NodeList nodes = doc.getElementsByTagName(CLV_XML_BODY);
			m_NetworkType = m_TCSprotocol.m_NetType;
			System.out.println("networktype is:" + m_NetworkType);
			for (int k = 0; k < nodes.getLength(); k++) {
				NodeList nodesOfBody = nodes.item(k).getChildNodes();
				// for process condition tag under body tag
				for (int i = 0; i < nodesOfBody.getLength(); i++) {
					if ((ret = ProcRule(nodesOfBody.item(i))) != 0) {
						return ret;
					}
				}
			}
		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_INFO,
					"getClientVerification Rules - " + e.getMessage());
		}
		return 0;
	}

	public int CLVExecuteAction(Node node) {
		int ret;
		String Msg1 = "", Msg2 = "";
		try {
			if (node.getNodeName().equals("alertuser")) {
				JOptionPane.showMessageDialog(null,
						getCharacterDataFromElement((Element) node), "Alert",
						JOptionPane.WARNING_MESSAGE);
			} else if (node.getNodeName().equals("termsess")) {
				return 2;
			} else if (node.getNodeName().equals("promptuser")) {
				Msg1 = ((Element) node).getAttribute("LogTxt") + " <"
						+ ((Element) node).getAttribute("Option1") + ">";
				Msg2 = ((Element) node).getAttribute("LogTxt") + " <"
						+ ((Element) node).getAttribute("Option2") + ">";
				Object[] options = { ((Element) node).getAttribute("Option2"),
						((Element) node).getAttribute("Option1") };
				ret = JOptionPane.showOptionDialog(null,
						getCharacterDataFromElement((Element) node), "Prompt",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if (ret == 1) {
					m_TCSprotocol.sendLog(FatClientProxy.LOG_INFO, "", 0, "",
							Msg1);
				} else if (ret == 0) {
					m_TCSprotocol.sendLog(FatClientProxy.LOG_INFO, "", 0, "",
							Msg2);
				}
			} else if (node.getNodeName().equals("endrule")) {
				return 1;
			}
		} catch (Exception e) {

		}
		return 0;
	}

	public int ProcRule(Node node) {
		int ret;
		NodeList nodes2 = node.getChildNodes();
		if (nodes2.item(0).getFirstChild().getNodeName().equals("ostype")) {
			for (int j = 0; j < nodes2.getLength(); j++) {
				if (nodes2.item(j).getNodeName().equals(CLV_XML_CASE)) {
					if (((Element) nodes2.item(j)).getAttribute("Value")
							.equals("MacOS")) {
						NodeList nodes3 = nodes2.item(j).getChildNodes();
						// process case tag child nodes
						for (int k = 0; k < nodes3.getLength(); k++) {
							if (!nodes3.item(k).getNodeName()
									.equals(CLV_XML_CONDITION)) {
								if ((ret = CLVExecuteAction(nodes3.item(k))) != 0) {
									return ret;
								}
							} else {
								// process case tag has condition tag
								return ProcRule(nodes3.item(k));
							}
						}
					}
				}
			}
		}
		if (nodes2.item(0).getFirstChild().getNodeName().equals("networktype")) {
			for (int j = 0; j < nodes2.getLength(); j++) {
				if (nodes2.item(j).getNodeName().equals(CLV_XML_CASE)) {
					if (((Element) nodes2.item(j)).getAttribute("Value")
							.equals(m_NetworkType)) {
						NodeList nodes3 = nodes2.item(j).getChildNodes();
						// process case tag child nodes
						for (int k = 0; k < nodes3.getLength(); k++) {
							if (!nodes3.item(k).getNodeName()
									.equals(CLV_XML_CONDITION)) {
								if ((ret = CLVExecuteAction(nodes3.item(k))) != 0) {
									return ret;
								}
							} else {
								// process case tag has condition tag
								return ProcRule(nodes3.item(k));
							}
						}
					}
				}
			}
		}
		return 0;
	}

	public boolean getLicense(int prmDevType, String prmDevID) {

		try {
			Document doc = getARTResponse(URL_QUERY_BASE + URL_QUERY_LICENSE
					+ "?" + PARAM_DEVICEID + "=" + prmDevID + "&"
					+ PARAM_DEVICETYPE + "=" + prmDevType, m_Port);

			NodeList nodes = doc.getElementsByTagName(ARP_LIC);

			if (nodes.getLength() > 0) {
				return getCharacterDataFromElement((Element) nodes.item(0))
						.equals("1");
			}

		} catch (Exception e) {
			m_Global.writeLog(Global.LOG_LEVEL_DEBUG, "GetLicense - Exception "
					+ e.getMessage());
			return false;
		} finally {

		}

		return false;
	}

	/*
	 * public String getFile(String prmURL) { BufferedReader reader; String rst
	 * = ""; String line;
	 * 
	 * try { DefaultHttpClient httpclient = new
	 * DefaultHttpClient(m_ClientConnectionManager, m_HTTPParams);
	 * 
	 * HttpGet httpReq = new
	 * HttpGet("https://"+m_CfgManager.getSPX()+"/prx/000/"+prmURL);
	 * httpReq.addHeader("Cookie", m_TCSprotocol.getSessCookie()); HttpResponse
	 * response = httpclient.execute(httpReq); HttpEntity entity =
	 * response.getEntity();
	 * 
	 * reader = new BufferedReader(new InputStreamReader(entity.getContent()));
	 * 
	 * while ((line = reader.readLine()) != null) { rst += line; }
	 * 
	 * return rst;
	 * 
	 * 
	 * } catch (Exception e) { m_Global.writeLog(Global.LOG_LEVEL_INFO,
	 * "GetFile - Exception "+e.getMessage()); return ""; } }
	 */

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}

	public boolean getFileFromServer() {

		return false;
	}

	public void setGUI(tcsgui prmGUI) {
		m_Owner = prmGUI;
	}
	public void setSessionUserPass(String uname, String pass) {
		sessionUser = uname;
		sessionPass = pass;
	}
	private String getUserName(String sessionInfo) {
		String result = "";
		int index = sessionInfo.indexOf("@");
		String userName = sessionInfo.substring(0, index);

		if (userName != null && userName.length() != 0) {
			result = com.desktopdirect.tcs.Base64.base64_decode(userName);
		}
		return result;
	}

	private String getPassword(String sessionInfo) {
		String result = "";
		int index = sessionInfo.indexOf("@");
		String password = sessionInfo
				.substring(index + 1, sessionInfo.length());

		if (password != null && password.length() != 0) {
			result = com.desktopdirect.tcs.Base64.base64_decode(password);
		}
		return result;
	}

	private int getUserNameLen(String sessionLen) {
		int length = 0;
		int index = sessionLen.indexOf("@");

		String userNameLen = sessionLen.substring(0, index);
		if (userNameLen != null && userNameLen.length() != 0) {
			length = Integer.parseInt(userNameLen);
		}
		return length;
	}

	private int getPasswordLen(String sessionLen) {
		int length = 0;
		int index = sessionLen.indexOf("@");

		String passwordLen = sessionLen.substring(index + 1,
				sessionLen.length());
		if (passwordLen != null && passwordLen.length() != 0) {
			length = Integer.parseInt(passwordLen);
		}
		return length;
	}

	public void checkVDIResources(int prmHostIndex, HostResults m_Hosts) {
		Host host;
		if (m_Hosts != null) {
			host = (Host) m_Hosts.getList().get(prmHostIndex);
			if ((host.GetProvider().toString().compareTo("VMView") == 0)
					|| (host.GetProvider().toString().compareTo("XenDesktop") == 0)) {
				if (host.GetStatus() != ARTInterface.HOST_STATUS_CONNECTED) {
					host.SetStatus(ARTInterface.HOST_STATUS_CHECK_AVAIL);
					host.refreshState();
				} else {
					host.SetStatus(ARTInterface.HOST_STATUS_AVAIL);
				}
			}
		}
	}

	private boolean isValidIP(String prmIp) {
		String[] Ar;
		int length = 0;
		try {
			Ar = prmIp.split("\\.");
			length = Ar.length;
			if (length != 4) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				if (Integer.parseInt(Ar[i]) > 255
						|| Integer.parseInt(Ar[i]) < 0) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public ServerSocket getOtherSock() {
		return otherServerSock;
	}

	public int getOtherPort() {
		return otherListenPort;
	}

	public FCP_AcceptThreadAG getOtherConn() {
		return conn;
	}
	public FCP_AcceptThreadSPX getOtherConnSPX() {
		return connSPX;
	}
}