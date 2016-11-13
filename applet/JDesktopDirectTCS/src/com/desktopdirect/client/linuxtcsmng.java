/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.desktopdirect.client;

/**
 *
 * @author lrapport
 */

import java.util.*;
import java.io.*;

import com.desktopdirect.tcs.*;
import java.net.*;
import java.security.*;

public class linuxtcsmng extends tcsmng {

    
     private FatClientProxy m_Protocol;
     private CfgManager     m_DBMng;
     private tcsgui         m_Owner;
     private ARTInterface   m_ARTInterface;
     private InitThd        m_InitThd = null;
     private ARTInterface.HostResults m_Hosts = null;
     private Global         m_Global;
     private Folders.Folder m_CurFolder;

     public Map getDesktopIndexMap(){
 		return null;
 	}
     public Map getVDIServerSockMap(){
     	return null;
     }
     public Map getVDIConnMap(){
     	return null;
     }
     public Map getVDIPortMap(){
     	return null;
     }
     public Map getAppServerSockMap(){
      	return null;
      }
      public Map getAppConnMap(){
      	return null;
      }
      public Map getAppPortMap(){
      	return null;
      }
      public boolean isReady() {
          return true;
      }
      public void init(String prmSPX, int prmPort, String prmSPCookie, String prmSessID, String prmModID, String prmCfg,String farmList,String desktopList, int prmCommPort,int hideDesks) {
          m_DBMng = new CfgManager(prmCfg);
          m_DBMng.setSPX(prmSPX+":"+Integer.toString(prmPort));
          m_Protocol = new FatClientProxy(prmSPX, prmPort, prmSessID, prmSPCookie, prmModID);
          m_Owner.initDone();
          Folders f = new Folders();
          m_CurFolder = f.new Folder("", null);
          m_ARTInterface = new ARTInterface(this, m_CurFolder,m_Owner, m_Protocol, m_DBMng, m_Global);
      }
      public void initTCS() {
        m_InitThd  = new InitThd(m_Owner, m_Protocol, m_DBMng, this);
        m_InitThd.start();
      }
      public void finish() {

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

          if (prmParam.compareTo("MenuAnim") == 0){
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

          System.err.println("Get parameter - "+prmParam+" not found !!!");
          return "";
      }
      public void logout(){
    	  
      }
      public void beforeConnect(int prmHostIndex) {
          Host host;

          if (m_Hosts != null) {
              if (prmHostIndex < m_Hosts.getList().size()) {
                  host = (Host)m_Hosts.getList().get(prmHostIndex) ;
                  if (m_DBMng.GetEnablePowerMng()) {
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
                  host = (Host)m_Hosts.getList().get(prmHostIndex) ;
                  host.powerup();
              }
          }
      }
      public void launchDesktop(
              String prmDest,
              int prmHostIndex,
              int prmWidth,
              int prmHeight,
              int prmColorDepth,
              int prmRedirDrives,
              int prmRedirPrinters,
              int prmRedirPorts,
              int prmRedirSmartCards,
              int prmRedirClipboard,
              int prmRedirPOSDevices,
              int prmBitmapCaching,
              int prmPerfFlags,
              int prmSound,
              int prmConnectToConsole,
              int prmSpan,
              int prmSSO,
              String prmStartApp,
              String prmWorkDir) {

          System.out.println("TCSMNG LaunchDesktop "+prmDest+","+prmHostIndex);

          LaunchDesktopThread thd = new LaunchDesktopThread(
              m_Protocol,
              m_DBMng,
              m_Owner,
              prmDest,
              prmHostIndex,
              prmWidth,
              prmHeight,
              prmColorDepth,
              prmRedirDrives,
              prmRedirPrinters,
              prmRedirPorts,
              prmRedirSmartCards,
              prmRedirClipboard,
              prmRedirPOSDevices,
              prmBitmapCaching,
              prmPerfFlags,
              prmSound,
              prmConnectToConsole,
              prmSpan,
              prmSSO,
              prmStartApp,
              prmWorkDir);

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
                  host = (Host)m_Hosts.getList().get(prmHostIndex) ;
                  return host.GetHost();
              }
          }
          return "";
      }
      public String getHostDesc(int prmHostIndex) {
          Host host;

          if (m_Hosts != null) {
              if (prmHostIndex < m_Hosts.getList().size()) {
                  host = (Host)m_Hosts.getList().get(prmHostIndex) ;
                  return host.GetDesc();
              }
          }
          return "";
      }
      public int getHostTTL(int prmHostIndex) {
          Host host;

          if (m_Hosts != null) {
              if (prmHostIndex < m_Hosts.getList().size()) {
                  host = (Host)m_Hosts.getList().get(prmHostIndex) ;
                  return host.GetDaysLeft();
              }
          }
          return 0;

      }
      public int getHostState(int prmHostIndex) {
          return 0;
      }


      public String getCurAppName() {
          return "";
      }
      public String getCurAppIconURL() {
          return "";
      }
      public void launchCurApp() {

      }
      public void popCurFolder() {

      }
      public void setCurFolder(int prmIndex) {

      }
      public void setCurSubFolder(int prmIndex) {

      }
      public int getHasParentFolder() {
          return 0;
      }
      public int  getSubFoldersCount() {
          return 0;
      }
      public String getCurFolder() {
          return "";
      }
      public String getCurSubFolder() {
          return "";
      }
      public int getAppsCount() {
          return 0;
      }
      public void setCurApp(int prmIndex) {

      }

      public linuxtcsmng(tcsgui prmOwner, Global prmGlobal) {
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
          private tcsgui         m_Owner;
          private FatClientProxy m_Protocol;
          private CfgManager     m_DBMng;
          private linuxtcsmng    m_TCSMng;

          public InitThd(tcsgui prmOwner, FatClientProxy prmProtocol, CfgManager prmDBMng, linuxtcsmng prmTCSMng) {
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

              // !!! Missing check ART server proxy
              m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_RET_HOSTS, 0, "");
              hosts = m_TCSMng.getARTInterface().getAutoHost();
              if (hosts != null) {
                  // TCS is ready
                  m_TCSMng.setHosts(hosts);
                  m_Owner.tcsStateNotify(m_Owner.TCS_STATUS_READY, 0, "");
              } else {
                  // Failed to retrieve desktops
                  m_Owner.reportError(m_Owner.ERR_FAILED_RET_DESKTOPS, "");
              }

          }
      }

      private int extractPort(String prmHost) {
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

      private String extractServer(String prmHost) {
          int i = prmHost.indexOf(":");
          if (i > 0) {
              try {
                  return prmHost.substring(0, i-1);
              } catch (Exception e) {
                  return prmHost;
              }
          } else {
              return prmHost;
          }
      }

  class LaunchDesktopThread extends Thread {
    private FatClientProxy m_Protocol;
     private CfgManager     m_DBMng;
     private tcsgui         m_Owner;
     private String m_Dest;
     private int    m_HostIndex;
     private int    m_Width;
     private int    m_Height;
     private int    m_ColorDepth;
     private int    m_RedirDrives;
     private int    m_RedirPrinters;
     private int    m_RedirPorts;
     private int    m_RedirSmartCards;
     private int    m_RedirClipboard;
     private int    m_RedirPOSDevices;
     private int    m_BitmapCaching;
     private int    m_PerfFlags;
     private int    m_Sound;
     private int    m_ConnectToConsole;
     private int    m_Span;
     private int    m_SSO;
     private String m_StartApp;
     private String m_WorkDir;


      public LaunchDesktopThread(
              FatClientProxy prmProtocol,
              CfgManager     prmDBMng,
              tcsgui         prmOwner,
              String prmDest,
              int prmHostIndex,
              int prmWidth,
              int prmHeight,
              int prmColorDepth,
              int prmRedirDrives,
              int prmRedirPrinters,
              int prmRedirPorts,
              int prmRedirSmartCards,
              int prmRedirClipboard,
              int prmRedirPOSDevices,
              int prmBitmapCaching,
              int prmPerfFlags,
              int prmSound,
              int prmConnectToConsole,
              int prmSpan,
              int prmSSO,
              String prmStartApp,
              String prmWorkDir) {


         m_Protocol = prmProtocol;
         m_DBMng = prmDBMng;
         m_Owner = prmOwner;
         m_Dest = prmDest;
         m_HostIndex = prmHostIndex;
         m_Width =prmWidth;
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

        System.out.println("Thread LaunchDesktop "+m_Dest+","+m_HostIndex);

          LaunchDesktopAction la = new LaunchDesktopAction(
              m_Protocol,
              m_DBMng,
              m_Owner,
              m_Dest,
              m_HostIndex,
              m_Width,
              m_Height,
              m_ColorDepth,
              m_RedirDrives,
              m_RedirPrinters,
              m_RedirPorts,
              m_RedirSmartCards,
              m_RedirClipboard,
              m_RedirPOSDevices,
              m_BitmapCaching,
              m_PerfFlags,
              m_Sound,
              m_ConnectToConsole,
              m_Span,
              m_SSO,
              m_StartApp,
              m_WorkDir);

          AccessController.doPrivileged(la);
    }
  }

  class LaunchDesktopAction implements PrivilegedAction {

     private FatClientProxy m_Protocol;
     private CfgManager     m_DBMng;
     private tcsgui         m_Owner;
     private String m_Dest;
     private int    m_HostIndex;
     private int    m_Width;
     private int    m_Height;
     private int    m_ColorDepth;
     private int    m_RedirDrives;
     private int    m_RedirPrinters;
     private int    m_RedirPorts;
     private int    m_RedirSmartCards;
     private int    m_RedirClipboard;
     private int    m_RedirPOSDevices;
     private int    m_BitmapCaching;
     private int    m_PerfFlags;
     private int    m_Sound;
     private int    m_ConnectToConsole;
     private int    m_Span;
     private int    m_SSO;
     private String m_StartApp;
     private String m_WorkDir;


      public LaunchDesktopAction(
              FatClientProxy prmProtocol,
              CfgManager     prmDBMng,
              tcsgui         prmOwner,
              String prmDest,
              int prmHostIndex,
              int prmWidth,
              int prmHeight,
              int prmColorDepth,
              int prmRedirDrives,
              int prmRedirPrinters,
              int prmRedirPorts,
              int prmRedirSmartCards,
              int prmRedirClipboard,
              int prmRedirPOSDevices,
              int prmBitmapCaching,
              int prmPerfFlags,
              int prmSound,
              int prmConnectToConsole,
              int prmSpan,
              int prmSSO,
              String prmStartApp,
              String prmWorkDir) {


         m_Protocol = prmProtocol;
         m_DBMng = prmDBMng;
         m_Owner = prmOwner;
         m_Dest = prmDest;
         m_HostIndex = prmHostIndex;
         m_Width =prmWidth;
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

          Host host;
          int  rdpPort;
          String hostSt;
          String uname = "";
          String pwd = "";
          String hostIP = "";

          System.out.println("Action LaunchDesktop "+m_Dest+","+m_HostIndex);

          if (m_HostIndex > -1) {
              System.out.println("Action LaunchDesktop HostIndex is "+m_HostIndex);
              // The desktop to launch is from the list of registered desktops
              if ((m_Hosts == null) || (m_HostIndex > m_Hosts.getList().size())) {
                  // Invalid host index
                  return null;
              }
              host = (Host)m_Hosts.getList().get(m_HostIndex) ;
              System.out.println("Action LaunchDesktop desc="+host.GetDesc()+", id="+host.GetID()+", host="+host.GetHost());
              hostSt = host.GetHost();
              rdpPort = m_DBMng.GetRDPPort();
          } else {
              // The desktop to launch is an arbitrary host
              host = null;
              hostSt = extractServer(m_Dest);
              rdpPort = extractPort(m_Dest);
              if (rdpPort == -1) {
                  // Invalid port number was specified
                  m_Owner.reportError(tcsgui.ERR_INVALID_PORT, "");
                  return null;
              }
          }

          // Check if SSO is requested
          if (m_SSO == 1) {
              // SSO is request, but is it enabled
              if (m_DBMng.GetSSO()) {
                  if (host != null) {
                      // If the user is launching a registered desktop
                      // we check if Host SSO applies to the host
                      if (host.GetUsername().compareTo("") != 0) {
                          // Host SSO is specified for this desktop
                          uname = host.GetUsername();
                          pwd = host.GetPwd();
                      } else {
                          // Use session SSO
                          uname = m_Protocol.getUsername();
                          pwd = m_Protocol.getPassword();
                      }
                  } else {
                      uname = m_Protocol.getUsername();
                      pwd = m_Protocol.getPassword();
                  }
              }
          }

          /* !!!! Missing
          if not(m_Protocol.IsConnected) then
          begin
            m_UI.ReportError(ERR_FAILED_TO_CONNECT_TO_SPX, '');
            Exit;
          end;*/

          System.out.println("hostSt 1 = "+hostSt);

          if (hostSt.compareTo("") == 0) {
              m_Owner.reportError(tcsgui.ERR_HOSTNAME_MISSING, "");
              return null;
          }

          try {
            if (InetAddress.getByName(hostSt).toString().compareTo(hostSt) == 0) {
              hostIP = hostSt;
            }
          } catch (Exception e) {

          }

          System.out.println("hostSt 2 = "+hostSt);
          System.out.println("hostIP 1 = "+hostIP);

          if (hostIP.length() == 0) {
              // Resolve the host name
              hostIP = m_Protocol.resolveHost(hostSt);
              System.out.println("hostIP 2 = "+hostIP);
              if ((hostIP.compareTo("0.0.0.0") == 0) || (hostIP.compareTo("255.255.255.255") == 0)) {
                  // Failed to resolve hostname
                  m_Owner.reportError(tcsgui.ERR_FAILED_TO_RESOLVE_HOSTNAME, hostSt);
                  return null;
              }
          }

          System.out.println("hostIP 3 = "+hostIP);

          int listenPort = m_Protocol.addConn(hostIP, rdpPort, 0);
          if (listenPort == -1) {
              m_Owner.reportError(tcsgui.ERR_FAILED_TO_CONNECT_TO_HOST, hostSt);
              return null;
	  }

          if (host != null) {
            host.SetStatus(ARTInterface.HOST_STATUS_CONNECTED);
          }

	  try {

	      Process p;
              if (m_Height == -1) {
                String[] rdpcmd = {"usr/bin/rdesktop", "-u", uname, "-d", m_DBMng.GetDomain(), "-p", pwd, "-f", "-a", Integer.toString(m_ColorDepth), FatClientProxy.LOCALHOST_ADDR+":"+listenPort};
                p = Runtime.getRuntime().exec(rdpcmd);
              } else {
                String[] rdpcmd = {"usr/bin/rdesktop", "-u", uname, "-d", m_DBMng.GetDomain(), "-p", pwd, "-g", Integer.toString(m_Width)+"x"+Integer.toString(m_Height), "-a", Integer.toString(m_ColorDepth), FatClientProxy.LOCALHOST_ADDR+":"+listenPort};
                p = Runtime.getRuntime().exec(rdpcmd);
              }
	      try {
                  p.waitFor(); // Wait for the open command to complete
              } catch (InterruptedException e) {
                  System.out.println("Unable to wait for application to run");
                  m_Owner.reportError(tcsgui.ERR_FAILED_TO_CONN_GENERIC, "Unable to wait for application to run");
	      }
	  } catch (IOException e) {
              System.out.println("Unable to open (run) application");
	  }

          if (host != null) {
            host.SetStatus(ARTInterface.HOST_STATUS_DISCONNECTED);
          }

          return null;
      }
  }
    
}
