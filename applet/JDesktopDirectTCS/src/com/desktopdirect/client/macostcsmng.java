/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.desktopdirect.client;

/**
 *
 * @author lrapport
 */
import java.io.*;

import com.desktopdirect.client.ARTInterface.TSAppServer;
import com.desktopdirect.client.Folders.Folder;
import com.desktopdirect.tcs.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.net.*;
import java.security.*;
import java.util.HashMap;

public class macostcsmng extends tcsmng {

	private FatClientProxy m_Protocol;
	private CfgManager m_DBMng;
	private tcsgui m_Owner;
	private ARTInterface m_ARTInterface;
	private InitThd m_InitThd = null;
	private ARTInterface.HostResults m_Hosts = null;
	private Global m_Global;
	private Folders.Folder m_CurFolder;
	private int m_CurSubFolder;
	private int m_CurApp;
	private String scriptPath = "";
	public Map m_Map = new HashMap();
	public Map vdi_SockMap = new HashMap();
	public Map vdi_ConnMap = new HashMap();
	public Map vdi_PortMap = new HashMap();
	public Map appSockMap = new HashMap();
	public Map appConnMap = new HashMap();
	public Map appPortMap = new HashMap();
	
	public Map getDesktopIndexMap(){
		return m_Map;
	}
	public Map getVDIServerSockMap(){
		return vdi_SockMap;
	}
	public Map getVDIConnMap(){
		return vdi_ConnMap;
	}
	public Map getVDIPortMap(){
		return vdi_PortMap;
	}
	public Map getAppServerSockMap(){
		return appSockMap;
	}
	public Map getAppConnMap(){
		return appConnMap;
	}
	public Map getAppPortMap(){
		return appPortMap;
	}
	native public void testIT(String prmParam);

    public boolean isReady() {
        return true;
    }

    public void init(String prmSPX, int prmPort, String prmSPCookie, String prmSessID, String prmModID, String prmCfg,String farmList, String desktopList, int prmCommPort,int hideDesks) {

		if ((m_Owner.getPlatform() != null) && (m_Owner.getPlatform().compareTo("AG") == 0)) {
			m_DBMng = new CfgManager();
			m_DBMng.setSPX(prmSPX + ":" + Integer.toString(prmPort));
			m_Protocol = new FatClientProxyAG(prmSPX, prmPort, prmSessID, prmSPCookie, prmModID);
		} else {
			m_DBMng = new CfgManager(prmCfg);
			m_DBMng.setSPX(prmSPX + ":" + Integer.toString(prmPort));
			m_Protocol = new FatClientProxySPX(prmSPX, prmPort, prmSessID, prmSPCookie, prmModID);
		}
		String homePath = System.getProperty("user.home");
		scriptPath = "/usr/bin/osascript "+ homePath +"/.ArrayDD/start.applescript ";
		m_Owner.initDone();
		Folders f = new Folders();
		m_CurFolder = f.new Folder("", null);
		m_ARTInterface = new ARTInterface(this, m_CurFolder, m_Owner, m_Protocol, m_DBMng, m_Global);
	}

	public void initTCS() {
		m_InitThd = new InitThd(m_Owner, m_Protocol, m_DBMng, this);
		m_InitThd.start();

	}

	public void finish() {
		String msg = "ExitDD";

		byte[] buf = msg.getBytes();
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");
			int port = 6666;

			DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length,
					address, port);

			DatagramSocket socket = new DatagramSocket();
			socket.send(dataGramPacket);
			System.out.println("socket send macos tcs mng finish");
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logout() {
		String msg = "ExitDD";

		byte[] buf = msg.getBytes();
		System.out.println("macostcsmng finish func,bytes num " + buf.length);
		try {
			InetAddress address = InetAddress.getByName("127.0.0.1");
			int port = 6666;

			DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length,
					address, port);

			DatagramSocket socket = new DatagramSocket();
			socket.send(dataGramPacket);
			System.out.println("socket send macos tcs mng logout");
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int boolAsInt(boolean prmVal) {
		if (prmVal) {
			return 1;
		} else {
			return 0;
		}
	}

	public String getDBMngParam(String prmParam) {

		if (prmParam.compareTo("ARTServer") == 0) {
			return m_DBMng.GetARPServerAdd();
		}

		if (prmParam.compareTo("DefSize") == 0) {
			return Integer.toString(m_DBMng.GetDefSize());
		}

		if (prmParam.compareTo("DefColorDepth") == 0) {
			return Integer.toString(m_DBMng.GetDefColorDepth());
		}

		if (prmParam.compareTo("DefHeight") == 0) {
			return Integer.toString(m_DBMng.GetDefHeight());
		}

		if (prmParam.compareTo("DefWidth") == 0) {
			return Integer.toString(m_DBMng.GetDefWidth());
		}

		if (prmParam.compareTo("GetEnableGUI") == 0) {
			if (m_DBMng.GetEnableDefaultGUI()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("ARTEnabled") == 0) {
			if (m_DBMng.ARPEnabled()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("AllowCustomDest") == 0) {
			if (m_DBMng.AllowCustomDest()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("EnablePowerMng") == 0) {
			if (m_DBMng.GetEnablePowerMng()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("RedirDrives") == 0) {
			if (m_DBMng.GetRedirDrives()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("RedirPrinters") == 0) {
			if (m_DBMng.GetRedirPrinters()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("RedirPorts") == 0) {
			if (m_DBMng.GetRedirPorts()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("RedirSmartCards") == 0) {
			if (m_DBMng.GetRedirSmartCards()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("RedirClipboard") == 0) {
			if (m_DBMng.GetRedirClipboard()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("RedirPOS") == 0) {
			if (m_DBMng.GetRedirPOS()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("BitmapCache") == 0) {
			if (m_DBMng.GetBitmapCache()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("DesktopWallpaper") == 0) {
			if (m_DBMng.GetWallpaper()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("FullWindowDrag") == 0) {
			if (m_DBMng.GetFullWindowDrag()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("MenuAnim") == 0) {
			if (m_DBMng.GetMenuAnim()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("Theme") == 0) {
			if (m_DBMng.GetTheme()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("AllowConnectToConsole") == 0) {
			if (m_DBMng.GetAllowConnectToConsole()) {
				return "1";
			} else {
				return "0";
			}
		}

		if (prmParam.compareTo("AllowRedirControl") == 0) {
			if (m_DBMng.GetAllowRedirConfig()) {
				return "1";
			} else {
				return "0";
			}
		}
		if (prmParam.compareTo("console") == 0) {
			if (m_DBMng.GetAllowConnectToConsole()) {
				return "1";
			} else {
				return "0";
			}
		}
		System.err.println("Get parameter - " + prmParam + " not found !!!");
		return "";
	}

	public void beforeConnect(int prmHostIndex) {
		Host host;
		if (m_Hosts != null) {
			if (prmHostIndex < m_Hosts.getList().size()) {
				host = (Host) m_Hosts.getList().get(prmHostIndex);
				if ((host.GetProvider().toString().compareTo("VMView") == 0)
						|| (host.GetProvider().toString()
								.compareTo("XenDesktop") == 0)) {
					if (host.GetStatus() != ARTInterface.HOST_STATUS_CONNECTED) {
						host.SetStatus(ARTInterface.HOST_STATUS_CHECK_AVAIL);
						host.refreshState();
					} else {
						host.SetStatus(ARTInterface.HOST_STATUS_AVAIL);
					}
				} else if (m_DBMng.GetEnablePowerMng()) {
					if (host.GetStatus() != ARTInterface.HOST_STATUS_CONNECTED) {
						host.SetStatus(ARTInterface.HOST_STATUS_CHECK_AVAIL);
						host.refreshState();
					} else {
						host.SetStatus(ARTInterface.HOST_STATUS_AVAIL);
					}
				} else {
					host.SetStatus(ARTInterface.HOST_STATUS_AVAIL);
				}
			}
		}
	}

	public void powerupDesktop(int prmHostIndex) {
		Host host;

		if (m_Hosts != null) {
			if (prmHostIndex < m_Hosts.getList().size()) {
				host = (Host) m_Hosts.getList().get(prmHostIndex);
				host.powerup();
			}
		}
	}

	public void launchDesktop(String prmDest, int prmHostIndex, int prmWidth,
			int prmHeight, int prmColorDepth, int prmRedirDrives,
			int prmRedirPrinters, int prmRedirPorts, int prmRedirSmartCards,
			int prmRedirClipboard, int prmRedirPOSDevices,
			int prmBitmapCaching, int prmPerfFlags, int prmSound,
			int prmConnectToConsole, int prmSpan, int prmSSO,
			String prmStartApp, String prmWorkDir) {

		// System.out.println("TCSMNG LaunchDesktop " + prmDest + "," +
		// prmHostIndex+","+prmPerfFlags);

		LaunchDesktopThread thd = new LaunchDesktopThread(m_Protocol, m_DBMng,
				m_Owner, prmDest, prmHostIndex, prmWidth, prmHeight,
				prmColorDepth, prmRedirDrives, prmRedirPrinters, prmRedirPorts,
				prmRedirSmartCards, prmRedirClipboard, prmRedirPOSDevices,
				prmBitmapCaching, prmPerfFlags, prmSound, prmConnectToConsole,
				prmSpan, prmSSO, prmStartApp, prmWorkDir);

		thd.start();

	}

	public int getHostsCount() {
		if (m_Hosts != null) {
			return m_Hosts.getList().size();
		} else {
			return 0;
		}
	}

	public String getHostHost(int prmHostIndex) {
		Host host;

		if (m_Hosts != null) {
			if (prmHostIndex < m_Hosts.getList().size()) {
				host = (Host) m_Hosts.getList().get(prmHostIndex);
				return host.GetHost();
			}
		}
		return "";
	}

	public String getHostDesc(int prmHostIndex) {
		Host host;

		if (m_Hosts != null) {
			if (prmHostIndex < m_Hosts.getList().size()) {
				host = (Host) m_Hosts.getList().get(prmHostIndex);
				return host.GetDesc();
			}
		}
		return "";
	}

	public int getHostTTL(int prmHostIndex) {
		Host host;

		if (m_Hosts != null) {
			if (prmHostIndex < m_Hosts.getList().size()) {
				host = (Host) m_Hosts.getList().get(prmHostIndex);
				return host.GetDaysLeft();
			}
		}
		return 0;

	}

	public int getHostState(int prmHostIndex) {
		return 0;
	}

	public String getCurAppName() {
		return ((Apps.TSApp) m_CurFolder.getApps().get(m_CurApp)).getName();
	}

	public String getCurAppIconURL() {
		return "/prx/000/http/localhost:9090/query/appicon?_appid="
				+ ((Apps.TSApp) m_CurFolder.getApps().get(m_CurApp)).getID();
	}

	public void launchCurApp() {
		int w, h, d;
		Integer i;
		int colord;
		int perfFlags;

		d = Integer.parseInt(this.getDBMngParam("DefSize"));
		if (d == 4) {
			// User selected custom size
			i = Integer.parseInt(this.getDBMngParam("DefWidth"));
			if (i == null) {
				// reportError(ERR_INVALID_WIDTH, "");
				return;
			}
			w = i;

			i = Integer.parseInt(this.getDBMngParam("DefHeight"));
			if (i == null) {
				// reportError(ERR_INVALID_HEIGHT, "");
				return;
			}
			h = i;
		} else {
			// User selected a pre-defined size
			w = -1;
			h = -1;

			switch (d) {
			// 640X480
			case 1:
				w = 640;
				h = 480;
				break;

			// 800X600
			case 2:
				w = 800;
				h = 600;
				break;

			// 1024X768
			case 3:
				w = 1024;
				h = 768;
				break;
			}
		}

		colord = 0;
		d = Integer.parseInt(this.getDBMngParam("DefColorDepth"));
		switch (d) {
		// 8 bit color depth
		case 1:
			colord = 8;
			break;

		// 16 bit color depth
		case 2:
			colord = 16;
			break;

		// 24 bit color depth
		case 3:
			colord = 24;
			break;

		// 32 bit color depth
		case 4:
			colord = 32;
			break;
		}

		perfFlags = 0;

		if (this.getDBMngParam("DesktopWallpaper").equalsIgnoreCase("1")) {
			perfFlags += 0x1;
		}

		if (this.getDBMngParam("FullWindowDrag").equalsIgnoreCase("1")) {
			perfFlags += 0x2;
		}

		if (this.getDBMngParam("MenuAnim").equalsIgnoreCase("1")) {
			perfFlags += 0x4;
		}

		if (this.getDBMngParam("Theme").equalsIgnoreCase("1")) {
			perfFlags += 0x8;
		}
		int RedirPrinters = Integer.parseInt(this
				.getDBMngParam("RedirPrinters"));
		int RedirClipboard = Integer.parseInt(this
				.getDBMngParam("RedirClipboard"));
		int RedirDrives = Integer.parseInt(this.getDBMngParam("RedirDrives"));
		int RedirPorts = Integer.parseInt(this.getDBMngParam("RedirPorts"));
		int RedirPOS = Integer.parseInt(this.getDBMngParam("RedirPOS"));
		int RedirSmartCards = Integer.parseInt(this
				.getDBMngParam("RedirSmartCards"));
		LaunchAppThread thd = new LaunchAppThread(m_Protocol, m_DBMng, m_Owner,
				"", m_CurApp, w, h, colord, RedirDrives, RedirPrinters,
				RedirPorts, RedirSmartCards, RedirClipboard, RedirPOS,
				boolAsInt(this.getDBMngParam("BitmapCache").equalsIgnoreCase(
						"1")), perfFlags, 0, 0, 2,
				((Apps.App) (m_CurFolder.getApps().get(m_CurApp)))
						.getLocation(), "",
				(Apps.TSApp) (m_CurFolder.getApps().get(m_CurApp)), this);
		thd.start();

	}

	public void popCurFolder() {
		if (m_CurFolder.getParent() != null) {
			m_CurFolder = m_CurFolder.getParent();
		} else {
			return;
		}
	}

	public void setCurFolder(int prmIndex) {
		if ((prmIndex > -1) && (prmIndex < m_CurFolder.getItems().size())) {
			m_CurFolder = (Folder) m_CurFolder.getItems().get(prmIndex);
		}

	}

	public void setCurSubFolder(int prmIndex) {
		if ((prmIndex > -1) && (prmIndex < m_CurFolder.getItems().size()))
			m_CurSubFolder = prmIndex;
	}

	public int getHasParentFolder() {
		if (m_CurFolder.getParent() != null) {
			return 1;
		} else {
			return 0;
		}
	}

	public int getSubFoldersCount() {
		return m_CurFolder.getItems().size();
	}

	public String getCurFolder() {
		return m_CurFolder.getName();
	}

	public String getCurSubFolder() {
		return ((Folder) m_CurFolder.getItems().get(m_CurSubFolder)).getName();
	}

	public int getAppsCount() {
		return m_CurFolder.getApps().size();
	}

	public void setCurApp(int prmIndex) {
		if ((prmIndex > -1) && (prmIndex < m_CurFolder.getApps().size())) {
			m_CurApp = prmIndex;
		}
	}

	public macostcsmng(tcsgui prmOwner, Global prmGlobal) {
		m_Owner = prmOwner;
		m_Global = prmGlobal;
	}

	public void setHosts(ARTInterface.HostResults prmHosts) {
		m_Hosts = prmHosts;
	}

	public ARTInterface getARTInterface() {
		return m_ARTInterface;
	}

	private class InitThd extends Thread {

		private tcsgui m_Owner;
		private FatClientProxy m_Protocol;
		private CfgManager m_DBMng;
		private macostcsmng m_TCSMng;

		public InitThd(tcsgui prmOwner, FatClientProxy prmProtocol,
				CfgManager prmDBMng, macostcsmng prmTCSMng) {
			m_Owner = prmOwner;
			m_Protocol = prmProtocol;
			m_DBMng = prmDBMng;
			m_TCSMng = prmTCSMng;
		}

		public void run() {
			int timeout;
			ARTInterface.HostResults hosts;

			m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_CONNECTING, 0, "");

			try {
				m_Protocol.init();
			} catch (Exception e) {
				m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_CONNECT_FAILED, 0, "");
				m_Owner.reportError(m_Owner.ERR_FAILED_TO_CONN_GENERIC, "");
				return;
			}

			timeout = 60; // 30 seconds * 2 (500 milliseconds)

			while (timeout > 0) {
				if (m_Protocol.isReady()) {
					// The TCS connection is ready
					break;
				}

				try {
					this.sleep(500);
				} catch (Exception e) {
				}
				timeout--;
			}

			// Check if the connection is established
			if (timeout == 0) {
				// Time out passed connection failed
				m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_CONNECT_FAILED, 0, "");
				m_Owner.reportError(m_Owner.ERR_FAILED_TO_CONN_TIMEOUT, "");
				return;
			}

			m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_CONNECTED, 0, "");
			String uname = m_Protocol.getUsername();
			String pass = m_Protocol.getPassword();
			m_ARTInterface.setSessionUserPass(uname, pass);
			if ((m_Owner.getPlatform() != null)
					&& (m_Owner.getPlatform().compareTo("AG") == 0)) {
				if (m_ARTInterface.getARTInfo() == false) {
					m_Owner.reportError(m_Owner.ERR_FAILED_TO_CONN_GENERIC,
							"Failed to get ART Info");
					return;
				}
				if (m_ARTInterface.GetPostLoginClientVerificationRules() == 2) {
					m_Owner.reportError(tcsgui.ERR_CLV_FAILED, "");
					m_Owner.reportError(tcsgui.TCS_STATUS_REDIRECT, "");
					return;
				}
			}

			// !!! Missing check ART server proxy
			m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_RET_HOSTS, 0, "");
			hosts = m_TCSMng.getARTInterface().getAutoHost();
			m_TCSMng.getARTInterface().passArtConfig(hosts);
			
			// if (hosts != null) {
			// // TCS is ready
			// m_TCSMng.setHosts(hosts);
			// m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_READY, 0, "");
			// } else {
			// // Failed to retrieve desktops
			// m_Owner.reportError(m_Owner.ERR_FAILED_RET_DESKTOPS, "");
			// }

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

	class LaunchDesktopThread extends Thread {

		private FatClientProxy m_Protocol;
		private CfgManager m_DBMng;
		private tcsgui m_Owner;
		private String m_Dest;
		private int m_HostIndex;
		private int m_Width;
		private int m_Height;
		private int m_ColorDepth;
		private int m_RedirDrives;
		private int m_RedirPrinters;
		private int m_RedirPorts;
		private int m_RedirSmartCards;
		private int m_RedirClipboard;
		private int m_RedirPOSDevices;
		private int m_BitmapCaching;
		private int m_PerfFlags;
		private int m_Sound;
		private int m_ConnectToConsole;
		private int m_Span;
		private int m_SSO;
		private String m_StartApp;
		private String m_WorkDir;

		public LaunchDesktopThread(FatClientProxy prmProtocol,
				CfgManager prmDBMng, tcsgui prmOwner, String prmDest,
				int prmHostIndex, int prmWidth, int prmHeight,
				int prmColorDepth, int prmRedirDrives, int prmRedirPrinters,
				int prmRedirPorts, int prmRedirSmartCards,
				int prmRedirClipboard, int prmRedirPOSDevices,
				int prmBitmapCaching, int prmPerfFlags, int prmSound,
				int prmConnectToConsole, int prmSpan, int prmSSO,
				String prmStartApp, String prmWorkDir) {

			m_Protocol = prmProtocol;
			m_DBMng = prmDBMng;
			m_Owner = prmOwner;
			m_Dest = prmDest;
			m_HostIndex = prmHostIndex;
			m_Width = prmWidth;
			m_Height = prmHeight;
			m_ColorDepth = prmColorDepth;
			m_RedirDrives = prmRedirDrives;
			m_RedirPrinters = prmRedirPrinters;
			m_RedirPorts = prmRedirPorts;
			m_RedirSmartCards = prmRedirSmartCards;
			m_RedirClipboard = prmRedirClipboard;
			m_RedirPOSDevices = prmRedirPOSDevices;
			m_BitmapCaching = prmBitmapCaching;
			m_PerfFlags = prmPerfFlags;
			m_Sound = prmSound;
			m_ConnectToConsole = prmConnectToConsole;
			m_Span = prmSpan;
			m_SSO = prmSSO;
			m_StartApp = prmStartApp;
			m_WorkDir = prmWorkDir;
		}

		public void run() {

			// System.out.println("Thread LaunchDesktop " + m_Dest + "," +
			// m_HostIndex);

			LaunchDesktopAction la = new LaunchDesktopAction(m_Protocol,
					m_DBMng, m_Owner, m_Dest, m_HostIndex, m_Width, m_Height,
					m_ColorDepth, m_RedirDrives, m_RedirPrinters, m_RedirPorts,
					m_RedirSmartCards, m_RedirClipboard, m_RedirPOSDevices,
					m_BitmapCaching, m_PerfFlags, m_Sound, m_ConnectToConsole,
					m_Span, m_SSO, m_StartApp, m_WorkDir);

			AccessController.doPrivileged(la);
		}
	}

	class LaunchAppAction implements PrivilegedAction {
		private FatClientProxy m_Protocol;
		private CfgManager m_DBMng;
		private tcsgui m_Owner;
		private String m_Dest;
		private int m_AppIndex;
		private int m_Width;
		private int m_Height;
		private int m_ColorDepth;
		private int m_RedirDrives;
		private int m_RedirPrinters;
		private int m_RedirPorts;
		private int m_RedirSmartCards;
		private int m_RedirClipboard;
		private int m_RedirPOSDevices;
		private int m_BitmapCaching;
		private int m_PerfFlags;
		private int m_Sound;
		private int m_ConnectToConsole;
		private int m_Span;
		// private int m_SSO;
		private String m_StartApp;
		private String m_WorkDir;
		private ARTInterface.TSAppServer m_TSAppServer;

		public LaunchAppAction(FatClientProxy prmProtocol, CfgManager prmDBMng,
				tcsgui prmOwner, String prmDest, int prmAppIndex, int prmWidth,
				int prmHeight, int prmColorDepth, int prmRedirDrives,
				int prmRedirPrinters, int prmRedirPorts,
				int prmRedirSmartCards, int prmRedirClipboard,
				int prmRedirPOSDevices, int prmBitmapCaching, int prmPerfFlags,
				int prmSound, int prmConnectToConsole, int prmSpan,
				// int prmSSO,
				String prmStartApp, String prmWorkDir,
				ARTInterface.TSAppServer prmAppServer) {

			m_Protocol = prmProtocol;
			m_DBMng = prmDBMng;
			m_Owner = prmOwner;
			m_Dest = prmDest;
			m_AppIndex = prmAppIndex;
			m_Width = prmWidth;
			m_Height = prmHeight;
			m_ColorDepth = prmColorDepth;
			m_RedirDrives = prmRedirDrives;
			m_RedirPrinters = prmRedirPrinters;
			m_RedirPorts = prmRedirPorts;
			m_RedirSmartCards = prmRedirSmartCards;
			m_RedirClipboard = prmRedirClipboard;
			m_RedirPOSDevices = prmRedirPOSDevices;
			m_BitmapCaching = prmBitmapCaching;
			m_PerfFlags = prmPerfFlags;
			m_Sound = prmSound;
			m_ConnectToConsole = prmConnectToConsole;
			m_Span = prmSpan;
			// m_SSO = prmSSO;
			m_StartApp = prmStartApp;
			m_WorkDir = prmWorkDir;
			m_TSAppServer = prmAppServer;
		}

		public Object run() {
			int rdpPort;
			String hostIP ="";
			Apps.TSApp m_App;
			TSAppServer AppServer;
			
			m_App = (Apps.TSApp) (m_CurFolder.getApps().get(m_AppIndex));
			AppServer = m_ARTInterface.getTSAppServer(
					m_App);
			hostIP = AppServer.getServer();
			rdpPort = AppServer.getPort();
			try {
				int Port;
				if ((m_Owner.getPlatform() != null)
						&& (m_Owner.getPlatform().compareTo("AG") == 0)) {
					Port = m_Protocol
						.addOtherConnAG(hostIP, rdpPort, 0,
								(ServerSocket) appSockMap.get(m_App
										.getName()), ((Integer) appPortMap
										.get(m_App.getName())).intValue(),
								(FCP_AcceptThreadAG) appConnMap.get(m_App
										.getName()));
				}else {
					Port = m_Protocol
							.addOtherConnSPX(hostIP, rdpPort, 0,
									(ServerSocket) appSockMap.get(m_App
											.getName()), ((Integer) appPortMap
											.get(m_App.getName())).intValue(),
									(FCP_AcceptThreadSPX) appConnMap.get(m_App
											.getName()));
				}
			} catch (Exception e) {
				System.out.println("Exception: Failed to add VDI conn:"
						+ e.getMessage());
				e.printStackTrace();
			}
			System.out.println("app index "+m_Map.get(m_App.getName()));
			MacOSShellThread thd = new MacOSShellThread(scriptPath + m_Map.get(m_App.getName()), m_Owner, null);
			thd.start();
			return null;
		}
	}

	class LaunchDesktopAction implements PrivilegedAction {

		private FatClientProxy m_Protocol;
		private CfgManager m_DBMng;
		private tcsgui m_Owner;
		private String m_Dest;
		private int m_HostIndex;
		private int m_Width;
		private int m_Height;
		private int m_ColorDepth;
		private int m_RedirDrives;
		private int m_RedirPrinters;
		private int m_RedirPorts;
		private int m_RedirSmartCards;
		private int m_RedirClipboard;
		private int m_RedirPOSDevices;
		private int m_BitmapCaching;
		private int m_PerfFlags;
		private int m_Sound;
		private int m_ConnectToConsole;
		private int m_Span;
		private int m_SSO;
		private String m_StartApp;
		private String m_WorkDir;

		public LaunchDesktopAction(FatClientProxy prmProtocol,
				CfgManager prmDBMng, tcsgui prmOwner, String prmDest,
				int prmHostIndex, int prmWidth, int prmHeight,
				int prmColorDepth, int prmRedirDrives, int prmRedirPrinters,
				int prmRedirPorts, int prmRedirSmartCards,
				int prmRedirClipboard, int prmRedirPOSDevices,
				int prmBitmapCaching, int prmPerfFlags, int prmSound,
				int prmConnectToConsole, int prmSpan, int prmSSO,
				String prmStartApp, String prmWorkDir) {

			m_Protocol = prmProtocol;
			m_DBMng = prmDBMng;
			m_Owner = prmOwner;
			m_Dest = prmDest;
			m_HostIndex = prmHostIndex;
			m_Width = prmWidth;
			m_Height = prmHeight;
			m_ColorDepth = prmColorDepth;
			m_RedirDrives = prmRedirDrives;
			m_RedirPrinters = prmRedirPrinters;
			m_RedirPorts = prmRedirPorts;
			m_RedirSmartCards = prmRedirSmartCards;
			m_RedirClipboard = prmRedirClipboard;
			m_RedirPOSDevices = prmRedirPOSDevices;
			m_BitmapCaching = prmBitmapCaching;
			m_PerfFlags = prmPerfFlags;
			m_Sound = prmSound;
			m_ConnectToConsole = prmConnectToConsole;
			m_Span = prmSpan;
			m_SSO = prmSSO;
			m_StartApp = prmStartApp;
			m_WorkDir = prmWorkDir;
		}

		public Object run() {
			Host host = null;
			int rdpPort;
			String hostSt;
			String hostIP = "";
			// if (m_DBMng.GetEnablePowerMng())
			if (m_HostIndex == -1) {
				System.out.println("msso is "+m_SSO);
				hostSt = extractServer(m_Dest);
				rdpPort = extractPort(m_Dest);
				if (rdpPort == -1) {
					// Invalid port number was specified
					m_Owner.reportError(tcsgui.ERR_INVALID_PORT, "");
				}
				if (hostSt.compareTo("") == 0) {
					m_Owner.reportError(tcsgui.ERR_HOSTNAME_MISSING, "");
				}
				if(isValidIP(hostSt)){
					hostIP = hostSt;
				} else {
					// Resolve the host name
					hostIP = m_Protocol.resolveHost(hostSt);
					if ((hostIP.compareTo("0.0.0.0") == 0)
							|| (hostIP.compareTo("255.255.255.255") == 0)) {
						// Failed to resolve hostname
						m_Owner.reportError(
								tcsgui.ERR_FAILED_TO_RESOLVE_HOSTNAME, hostSt);
						return null;
					}
				}
//				FCP_AcceptThreadAG conn = new FCP_AcceptThreadAG(m_Protocol, "127.0.0.1",
//						m_ARTInterface.getOtherSock(), m_ARTInterface.getOtherPort());
				int listenPort;
				if ((m_Owner.getPlatform() != null)
						&& (m_Owner.getPlatform().compareTo("AG") == 0)) {
				listenPort = m_Protocol.addOtherConnAG(hostIP, rdpPort, 0,
						m_ARTInterface.getOtherSock(),
						m_ARTInterface.getOtherPort(), m_ARTInterface.getOtherConn());
				}else {
					listenPort = m_Protocol.addOtherConnSPX(hostIP, rdpPort, 0,
							m_ARTInterface.getOtherSock(),
							m_ARTInterface.getOtherPort(), m_ARTInterface.getOtherConnSPX());
				}
				System.out.println("macostcsmng m_Protocol.addConn,"
						+ "hostIP is:" + hostIP + " rdpPort is:" + rdpPort);
				
				if (m_SSO == 1) {
					System.out.println("desktop index "+m_Map.get("other"));
					MacOSShellThread thd = new MacOSShellThread(scriptPath + m_Map.get("other"), m_Owner, null);
					thd.start();
				} else {
					System.out.println("desktop index "+m_Map.get("other_as"));
					MacOSShellThread thd = new MacOSShellThread(scriptPath + m_Map.get("other_as"), m_Owner, null);
					thd.start();
				}

			} else {
				
				host = (Host) m_Hosts.getList().get(m_HostIndex);
				hostSt = host.GetHost();
				rdpPort = host.GetPort();
				hostIP = "";
				
				if(isValidIP(hostSt)){
					hostIP = hostSt;
				} else {
					// Resolve the host name
					hostIP = m_Protocol.resolveHost(hostSt);
					if ((hostIP.compareTo("0.0.0.0") == 0) || (hostIP.compareTo("255.255.255.255") == 0)) {
                        // Failed to resolve hostname
                        m_Owner.reportError(tcsgui.ERR_FAILED_TO_RESOLVE_HOSTNAME, hostSt);
                        return null;
                      }
				}
				try {
					int Port;
					if ((m_Owner.getPlatform() != null)
							&& (m_Owner.getPlatform().compareTo("AG") == 0)) {
						Port = m_Protocol
							.addOtherConnAG(hostIP, rdpPort, 0,
									(ServerSocket) vdi_SockMap.get(host
											.GetDesc()), ((Integer) vdi_PortMap
											.get(host.GetDesc())).intValue(),
									(FCP_AcceptThreadAG) vdi_ConnMap.get(host
											.GetDesc()));
					} else {
						Port = m_Protocol
								.addOtherConnSPX(hostIP, rdpPort, 0,
										(ServerSocket) vdi_SockMap.get(host
												.GetDesc()), ((Integer) vdi_PortMap
												.get(host.GetDesc())).intValue(),
										(FCP_AcceptThreadSPX) vdi_ConnMap.get(host
												.GetDesc()));
					}
				} catch (Exception e) {
					System.out.println("Exception: Failed to add VDI conn:"
							+ e.getMessage());
					e.printStackTrace();
				}
				System.out.println("desktop index "+m_Map.get(host.GetDesc()));
				MacOSShellThread thd = new MacOSShellThread(scriptPath + m_Map.get(host.GetDesc()), m_Owner, host);
				thd.start();
				if (host != null) {
					// host.SetStatus(ARTInterface.HOST_STATUS_CONNECTING);
				}
			}
			return null;
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
	}

	class LaunchAppThread extends Thread {
		private FatClientProxy m_Protocol;
		private CfgManager m_DBMng;
		private tcsgui m_Owner;
		private String m_Dest;
		private int m_AppIndex;
		private int m_Width;
		private int m_Height;
		private int m_ColorDepth;
		private int m_RedirDrives;
		private int m_RedirPrinters;
		private int m_RedirPorts;
		private int m_RedirSmartCards;
		private int m_RedirClipboard;
		private int m_RedirPOSDevices;
		private int m_BitmapCaching;
		private int m_PerfFlags;
		private int m_Sound;
		private int m_ConnectToConsole;
		private int m_Span;
		// private int m_SSO;
		private String m_StartApp;
		private String m_WorkDir;
		private macostcsmng m_TCSMng;
		private Apps.TSApp m_App;
		public ARTInterface.TSAppServer AppServer;

		public LaunchAppThread(FatClientProxy prmProtocol, CfgManager prmDBMng,
				tcsgui prmOwner, String prmDest, int prmAppIndex, int prmWidth,
				int prmHeight, int prmColorDepth, int prmRedirDrives,
				int prmRedirPrinters, int prmRedirPorts,
				int prmRedirSmartCards, int prmRedirClipboard,
				int prmRedirPOSDevices, int prmBitmapCaching, int prmPerfFlags,
				int prmSound, int prmConnectToConsole, int prmSpan,
				String prmStartApp, String prmWorkDir, Apps.TSApp prmApp,
				macostcsmng prmTCSMng) {
			m_Protocol = prmProtocol;
			m_DBMng = prmDBMng;
			m_Owner = prmOwner;
			m_Dest = prmDest;
			m_AppIndex = prmAppIndex;
			m_Width = prmWidth;
			m_Height = prmHeight;
			m_ColorDepth = prmColorDepth;
			m_RedirDrives = prmRedirDrives;
			m_RedirPrinters = prmRedirPrinters;
			m_RedirPorts = prmRedirPorts;
			m_RedirSmartCards = prmRedirSmartCards;
			m_RedirClipboard = prmRedirClipboard;
			m_RedirPOSDevices = prmRedirPOSDevices;
			m_BitmapCaching = prmBitmapCaching;
			m_PerfFlags = prmPerfFlags;
			m_Sound = prmSound;
			m_ConnectToConsole = prmConnectToConsole;
			m_Span = prmSpan;
			// m_SSO = prmSSO;
			m_StartApp = prmStartApp;
			m_WorkDir = prmWorkDir;
			m_TCSMng = prmTCSMng;
			m_App = prmApp;
		}

		public void run() {
			AppServer = m_TCSMng.getARTInterface().getTSAppServer(m_App);
			LaunchAppAction la = new LaunchAppAction(m_Protocol, m_DBMng,
					m_Owner, m_Dest, m_AppIndex, m_Width, m_Height,
					m_ColorDepth, m_RedirDrives, m_RedirPrinters, m_RedirPorts,
					m_RedirSmartCards, m_RedirClipboard, m_RedirPOSDevices,
					m_BitmapCaching, m_PerfFlags, m_Sound, m_ConnectToConsole,
					m_Span,
					// m_SSO,
					m_StartApp, m_WorkDir, AppServer);

			AccessController.doPrivileged(la);
		}
	}

	class MacOSShellThread extends Thread {

		private String m_Cmd;
		private tcsgui m_Owner;
		private Host m_Host;

		public MacOSShellThread(String prmCmd, tcsgui prmOwner, Host prmHost) {
			m_Cmd = prmCmd;
			m_Owner = prmOwner;
			m_Host = prmHost;
		}

		public void run() {

			// System.out.println("Thread LaunchDesktop " + m_Dest + "," +
			// m_HostIndex);

			MacOSShellAction la = new MacOSShellAction(m_Cmd, m_Owner, m_Host);

			AccessController.doPrivileged(la);
		}
	}

	class MacOSShellAction implements PrivilegedAction {

		private String m_Cmd;
		private tcsgui m_Owner;
		private Host m_Host;

		public MacOSShellAction(String prmCmd, tcsgui prmOwner, Host prmHost) {
			m_Cmd = prmCmd;
			m_Owner = prmOwner;
			m_Host = prmHost;
		}

		public Object run() {

			String[] shellcmd = { "/bin/sh", "-c", m_Cmd };

			try {

				Process p = Runtime.getRuntime().exec(shellcmd);
				try {
					p.waitFor(); // Wait for the open command to complete
					if (m_Host != null) {
						// m_Host.SetStatus(ARTInterface.HOST_STATUS_CONNECTED);
					}
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
	
}
