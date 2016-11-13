/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.desktopdirect.client;

import com.desktopdirect.tcs.*;

import java.net.*;
import java.util.*;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.io.*;

/**
 * 
 * @author chenyue
 */
public class Host {

	private tcsgui m_Owner;
	private FatClientProxy m_TCSprotocol;
	private CfgManager m_CfgManager;
	private ARTInterface m_ARTInterface;

	private int m_Index;
	private int m_DaysLeft;
	private int m_RdpProtocal;
	private int m_State;
	private int m_SessID;
	private int m_Status;
	private String m_ID;
	private String m_Desc;
	private String m_Host;
	private String m_Username;
	private String m_Pwd;
	private String m_Provider;
	private String m_ProviderID;
	private String m_DesktopID;
	private Map m_Params;
	private String m_InstID;

	public boolean m_WakeUP;
	public boolean m_PowerFlag;

	Host(tcsgui prmOwner, FatClientProxy prmTCSprotocol,
			CfgManager prmCfgManager, ARTInterface prmARTInterface,
			final int prmIndex, final int prmDaysLeft,
			final int prmRdpProtocal, final String prmID, final String prmDesc,
			final String prmHost, final String prmUsername,
			final String prmPwd, final String prmProvider,
			final String prmProviderID, final String prmProviderParams,
			final String prmDesktopID, final String prmParameters,// attention!!!
			final String prmInstID, final int prmState) {
		m_WakeUP = false;

		m_TCSprotocol = prmTCSprotocol;
		m_CfgManager = prmCfgManager;
		m_Owner = prmOwner;
		m_ARTInterface = prmARTInterface;

		m_Index = -1;
		m_DaysLeft = -1;
		m_RdpProtocal = -1;
		m_State = -1;
		m_SessID = -1;
		m_Status = ARTInterface.HOST_STATUS_NOT_AVAIL;

		m_ID = prmID;
		m_Desc = prmDesc;
		m_Host = prmHost;
		m_Username = prmUsername;
		m_Pwd = prmPwd;
		m_Provider = prmProvider;
		m_ProviderID = prmProviderID;
		m_DesktopID = prmDesktopID;
		m_Params = new HashMap<String, String>();
		parseParams(prmParameters);
		m_InstID = prmInstID;

		m_Index = prmIndex;
		m_DaysLeft = prmDaysLeft;
		m_RdpProtocal = prmRdpProtocal;
		m_State = prmState;

		System.out.println("host m_Index = " + m_Index);
		switch (m_State) {
		case ARTInterface.OBJ_STATE_UNKNOWN:
			m_Status = ARTInterface.HOST_STATUS_NOT_AVAIL;
			break;
		case ARTInterface.OBJ_STATE_DOWN:
			m_Status = ARTInterface.HOST_STATUS_NOT_AVAIL;
			break;
		case ARTInterface.OBJ_STATE_WAKING:
			m_Status = ARTInterface.HOST_STATUS_POWER_UP;
		case ARTInterface.OBJ_STATE_UP:
			m_Status = ARTInterface.HOST_STATUS_AVAIL;

			break;
		default:
			break;
		}

	}

	public String GetID() {
		return m_ID;
	}

	public String GetDesc() {
		return m_Desc;
	}

	public String GetHost() {
		return m_Host;
	}

	public void SetHost(final String prmHost) {		
		m_Host = prmHost;
	}

	public void SetVDIUser(final String prmvdiuser) {
		System.out.println("SetVdiuser - " + prmvdiuser + " (length "
				+ prmvdiuser.length() + ")");
		m_Username = prmvdiuser;
	}

	public void SetVDIPwd(final String prmvdipwd) {
		System.out.println("SetVdipwd - " + prmvdipwd + " (length "
				+ prmvdipwd.length() + ")");
		m_Pwd = prmvdipwd;
	}

	public int GetDaysLeft() {
		return m_DaysLeft;
	}

	public String GetUsername() {
		return m_Username;
	}

	public String GetPwd() {
		return m_Pwd;
	}

	int GetRdpProtocol() {
		return 0;
	}

	public String GetProvider() {
		return m_Provider;
	}

	public String GetParameters() {
		return "";
	}

	public int GetState() {
		return m_State;
	}

	public void SetState(final int prmState) {
		m_State = prmState;
	}

	public void SetSessID(final int prmSessID) {
		m_SessID = prmSessID;
	}

	public int GetSessID() {
		return m_SessID;
	}

	public void SetStatus(final int prmStatus) {
		m_Status = prmStatus;
		m_Owner.hostStateNotify(m_Index, prmStatus);
	}
	public void SetPowerUpFlag(boolean prmFlag) {
		m_PowerFlag = true;
	}
	public boolean GetPowerUpFlag() {
		return m_PowerFlag;
	}

	public int GetStatus() {
		return m_Status;
	}

	public int GetIndex() {
		return m_Index;
	}

	public int GetStateSess() {
		int lisPort = m_TCSprotocol.addConn(m_CfgManager.GetARPServerAdd(),
				m_CfgManager.GetARPServerPort(), 0);

		if (lisPort <= 0) {
			return -1;
		}
		try {
			Socket sock = new Socket("localhost", lisPort);
			InputStream sp_in;
			OutputStream sp_out;
			BufferedOutputStream buff_out;
			BufferedReader reader;

			sp_in = sock.getInputStream();
			sp_out = sock.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(sp_in));
			buff_out = new BufferedOutputStream(sp_out, 1460);

			String request = "GET " + ARTInterface.URL_OBJECT_BASE
					+ ARTInterface.URL_OBJECT_NEW_STATE_SESS + "?"
					+ ARTInterface.PARAM_OBJ_ID + "=" + m_ID + "&"
					+ ARTInterface.PARAM_UNAME + "=" + m_Username + "&"
					+ ARTInterface.PARAM_PROVIDER_ID + "=" + m_Provider + "&"
					+ ARTInterface.PARAM_INSTID + "="
					+ m_CfgManager.GetInstID() + " HTTP/1.1\r\nHost: "
					+ m_CfgManager.GetARPServerWithPort() + "\r\n\r\n";
			System.out.println(request);
			buff_out.write(FatClientProxy.str2bytes(request), 0,
					request.length()); // since we use 1-byte chars
			// buff_out.write(FatClientProxy.str2bytes(keepalive), 0,
			// keepalive.length()); //since we use 1-byte chars
			buff_out.flush(); // no more to write

			String header = "";
			String reponse = "";
			while ((header = reader.readLine()) != null) {
				// System.out.println(header);
				reponse += header;
			}
			// System.out.println(reponse);
			// System.out.println(reponse.substring(reponse.length()-120));
			reponse = reponse.substring(reponse.indexOf("<?xml"));
			// System.out.println(reponse);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(reponse));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("StateSess");
			// System.out.println(nodes.getLength());

			for (int i = 0; i < nodes.getLength(); i++) {// it should be only 1
															// result

				Element StateSess = (Element) nodes.item(i);
				System.out.println("ID: " + StateSess.getAttribute("ID"));
				m_SessID = Integer.parseInt(StateSess.getAttribute("ID"));
				System.out.println("=============");
				return 1;
			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return -1;
		} finally {

		}
		return -1;
	}

	public void DoWakeup() {
		System.out.println("DoWakeup1111");
		int lisPort = m_TCSprotocol.addConn(m_CfgManager.GetARPServerAdd(),
				m_CfgManager.GetARPServerPort(), 0);
		System.out.println("DoWakeup lisPort=" + lisPort);
		if (lisPort <= 0) {
			return;
		}
		try {
			Socket sock = new Socket("localhost", lisPort);
			InputStream sp_in;
			OutputStream sp_out;
			BufferedOutputStream buff_out;
			BufferedReader reader;

			sp_in = sock.getInputStream();
			sp_out = sock.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(sp_in));
			buff_out = new BufferedOutputStream(sp_out, 1460);

			String request = "GET " + ARTInterface.URL_OBJECT_BASE
					+ ARTInterface.URL_OBJECT_POWER_UP + "?"
					+ ARTInterface.PARAM_OBJ_ID + "=" + m_ID + "&"
					+ ARTInterface.PARAM_UNAME + "=" + m_Username + "&"
					+ ARTInterface.PARAM_PROVIDER_ID + "=" + m_Provider + "&"
					+ ARTInterface.PARAM_INSTID + "="
					+ m_CfgManager.GetInstID() + " HTTP/1.1\r\nHost: "
					+ m_CfgManager.GetARPServerWithPort() + "\r\n\r\n";
			System.out.println(request);
			buff_out.write(FatClientProxy.str2bytes(request), 0,
					request.length()); // since we use 1-byte chars
			// buff_out.write(FatClientProxy.str2bytes(keepalive), 0,
			// keepalive.length()); //since we use 1-byte chars
			buff_out.flush(); // no more to write

		} catch (Exception e) {
			System.out.println("dowakeup  Exception " + e.getMessage());
			return;
		} finally {

		}

	}

	public boolean DoRefreshState() {
		// int nReturnSize = host->GetStateSess(artadd, artport, buf, nBufSize);
		// nReturnSize = host->DoRefreshState(artadd, artport, buf2, nBufSize);

		GetStateSess();
		refreshState();
		return false;
	}

	public int refreshState() {
		RefreshStateThd thd = new RefreshStateThd(m_Owner, m_CfgManager, this);
		thd.start();

		return 0;
	}

	public void powerup() {
		String stateSessID;
		String host = "";
		int state;
		MonitorWakeupThd thd;

		try {

			stateSessID = m_ARTInterface.powerupDesktop(m_InstID, m_ID,
					m_DesktopID, m_Provider, m_ProviderID, m_Desc);
			try {
				SetSessID(Integer.parseInt(stateSessID));
				SetStatus(ARTInterface.HOST_STATUS_POWER_UP);
				thd = new MonitorWakeupThd(m_Owner, m_CfgManager, this);
				thd.start();
			} catch (Exception e) {
				m_Owner.reportError(tcsgui.ERR_FAILED_TO_WAKEUP_DESKTOP,
						"Failed to obtain state session ID (1)");
				return;
			}

		} catch (Exception e) {
			m_Owner.reportError(tcsgui.ERR_FAILED_TO_WAKEUP_DESKTOP,
					"Failed to obtain state session ID (2)");
			return;
		}

	}

	public void onHostStateChanged(int prmState) {
		System.out.println("onHostStateChanged");
		System.out.println("host m_Index = " + m_Index);
		m_Owner.hostStateNotify(m_Index, prmState);

		if (GetState() == ARTInterface.OBJ_STATE_UP) {
			System.out.println("luanch desktop = " + GetDesc() + ", index = "
					+ m_Index);
			m_Owner.LaunchDesktop(m_Index, 1);
		}
	}

	class RefreshStateThd extends Thread {
		private CfgManager m_DBMng;
		private Host m_Host;
		private tcsgui m_GUI;

		public RefreshStateThd(tcsgui prmGUI, CfgManager prmDBMng, Host prmHost) {
			m_DBMng = prmDBMng;
			m_Host = prmHost;
			m_GUI = prmGUI;
		}

		public void run() {
			String stateSessID;
			StringBuffer host = new StringBuffer("");
			StringBuffer vdiuser = new StringBuffer("");
			StringBuffer vdipwd = new StringBuffer("");
			int state;

			try {

				stateSessID = m_ARTInterface.getStateSess(m_Host, m_ID,
						m_DesktopID, m_Provider, m_ProviderID);
				try {
					m_Host.SetSessID(Integer.parseInt(stateSessID));
				} catch (Exception e) {
					m_GUI.reportError(tcsgui.ERR_GENERIC_ERROR,
							"Failed to obtain state session ID (1)");
					return;
				}

			} catch (Exception e) {
				m_GUI.reportError(tcsgui.ERR_GENERIC_ERROR,
						"Failed to obtain state session ID (2)");
				return;
			}

			try {
				state = m_ARTInterface.queryHostAvail(stateSessID, "",
						m_DBMng.GetRDPPort(), m_DesktopID, m_Provider,
						m_ProviderID, host, vdiuser, vdipwd,false);
				switch (state) {
				case ARTInterface.OBJ_STATE_DOWN:
					m_Host.SetStatus(ARTInterface.HOST_STATUS_NOT_AVAIL);
					m_Host.SetPowerUpFlag(true);
					break;

				case ARTInterface.OBJ_STATE_UP:
					if (host.length() > 0) {
						m_Host.SetHost(host.toString());
					}
					if (vdiuser.length() > 0) {
						m_Host.SetVDIUser(vdiuser.toString());
					}
					if (vdipwd.length() > 0) {
						m_Host.SetVDIPwd(vdipwd.toString());
					}
//					if ((m_Host.GetProvider().toString().compareTo("VMView") != 0)
//							&& (m_Host.GetProvider().toString()
//									.compareTo("XenDesktop") != 0)) {
						m_Host.SetStatus(ARTInterface.HOST_STATUS_AVAIL);
//					}
					break;

				default:
					m_Host.SetStatus(ARTInterface.HOST_STATUS_UNKNOWN);
				}

				m_Host.SetSessID(-1);

			} catch (Exception e) {
				m_Host.SetStatus(ARTInterface.HOST_STATUS_UNKNOWN);
			}

		}
	}

	class MonitorWakeupThd extends Thread {
		private CfgManager m_DBMng;
		private Host m_Host;
		private tcsgui m_GUI;

		public MonitorWakeupThd(tcsgui prmGUI, CfgManager prmDBMng, Host prmHost) {
			m_DBMng = prmDBMng;
			m_Host = prmHost;
			m_GUI = prmGUI;
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
		public void run() {
			int state;
			StringBuffer host = new StringBuffer("");
			StringBuffer vdiuser = new StringBuffer("");
			StringBuffer vdipwd = new StringBuffer("");
			String hostIP = "";
			if (isValidIP(m_ID) || 
				(m_Host.GetProvider().toString().compareTo("VMView") == 0) ||
				(m_Host.GetProvider().toString().compareTo("XenDesktop") == 0)) {
				hostIP = m_ID;
			} else {
				hostIP = m_TCSprotocol.resolveHost(m_ID);
			}
			while (true) {
				try {
					state = m_ARTInterface.queryHostAvail(
							Integer.toString(m_Host.GetSessID()),
							hostIP,
							m_DBMng.GetRDPPort(), m_DesktopID, m_Provider,
							m_ProviderID, host, vdiuser, vdipwd, true);

					switch (state) {
					case ARTInterface.OBJ_STATE_WAKING:
						break;

					case ARTInterface.OBJ_STATE_UP:
						if (host.length() > 0) {
							m_Host.SetHost(host.toString());
						}
						m_Host.SetStatus(ARTInterface.HOST_STATUS_AVAIL);
						m_Host.SetSessID(-1);
						return;

					case ARTInterface.OBJ_STATE_DOWN:
					case ARTInterface.OBJ_STATE_UNKNOWN:
					case ARTInterface.OBJ_STATE_WAKING_FAILED:
						m_Host.SetStatus(ARTInterface.HOST_STATUS_POWER_UP_FAILED);
						m_Host.SetSessID(-1);
						return;

					default:
						m_Host.SetStatus(ARTInterface.HOST_STATUS_UNKNOWN);
					}

				} catch (Exception e) {
					m_Host.SetStatus(ARTInterface.HOST_STATUS_UNKNOWN);
				}

				try {
					this.sleep(1000 * 5);
				} catch (Exception e) {

				}
			}
		}

	}

	private void parseParams(String prmParams) {

		String[] items = prmParams.split(",");

		for (int i = 0; i < items.length; i++) {
			String[] item = items[i].split("=");
			if (item.length > 1) {
				m_Params.put(item[0], item[1]);
				m_Params.get(1);
			}
		}
	}

	public int GetPort() {
		int port;

		String portSt = (String) m_Params.get("_PORT");
		try {
			port = Integer.parseInt(portSt);
		} catch (Exception e) {
			port = -1;
		}

		if (port == -1) {
			return m_CfgManager.GetRDPPort();
		} else {
			return port;
		}

	}

}
