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

abstract public class tcsmng {

      abstract public boolean isReady();
      abstract public void init(String prmSPX, int prmPort, String prmSPCookie, String prmSessID, String prmModID, String prmCfg,String farmsList,String desktopsList, int prmCommPort,int hideDesks);
      abstract public void initTCS();
      abstract public void finish();
      abstract public void logout();
      abstract public String getDBMngParam(String prmParam);
      abstract public void beforeConnect(int prmHostIndex);
      abstract public void powerupDesktop(int prmHostIndex);
      abstract public void launchDesktop(
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
              String prmWorkDir);
      abstract public int getHostsCount();
      abstract public String getHostHost(int prmHostIndex);
      abstract public String getHostDesc(int prmHostIndex);
      abstract public int getHostTTL(int prmHostIndex);
      abstract public int getHostState(int prmHostIndex);


      abstract public String getCurAppName();
      abstract public String getCurAppIconURL();
      abstract public void launchCurApp();
      abstract public ARTInterface getARTInterface();
      abstract public void popCurFolder();
      abstract public void setCurFolder(int prmIndex);
      abstract void setHosts(ARTInterface.HostResults prmHosts);
      abstract public void setCurSubFolder(int prmIndex);
      abstract public int getHasParentFolder();
      abstract public int  getSubFoldersCount();
      abstract public String getCurFolder();
      abstract public String getCurSubFolder();
      abstract public int getAppsCount();
      abstract public void setCurApp(int prmIndex);
      abstract public Map getDesktopIndexMap();
      abstract public Map getVDIServerSockMap();
      abstract public Map getVDIConnMap();
      abstract public Map getVDIPortMap();
      abstract public Map getAppServerSockMap();
      abstract public Map getAppConnMap();
      abstract public Map getAppPortMap();
}
