/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * tcsgui.java
 *
 * Created on Dec 21, 2009, 3:15:43 PM
 */

package com.desktopdirect.client;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.awt.*;
import netscape.javascript.JSObject;

/**
 *
 * @author lrapport
 */
public class tcsgui extends javax.swing.JApplet {

    class GUIProperty {

        private String m_Val;

        GUIProperty(String prmVal) {
            m_Val = prmVal;
            System.out.println("Property value is "+m_Val);
        }

        GUIProperty(int prmVal) {
            m_Val = String.valueOf(prmVal);
            System.out.println("Property value is "+m_Val);
        }
        
    }

    class CommThread extends Thread {

        private tcsgui         m_Owner;
        private DatagramSocket m_Socket;

        public CommThread(tcsgui prmOwner) {
            m_Owner = prmOwner;
            m_Socket = null;

            try {
              m_Socket = new DatagramSocket();
            }
            catch (Exception e) {
                m_Owner.writeDebug("CommThread", "Failed to create socket ("+e.getMessage()+")", e);
                return;
            }
        }

        public int getLocalPort() {
            if (m_Socket != null) {
                return m_Socket.getLocalPort();
            } else {
                return 0;
            }
        }

        public void closeSocket() {
            m_Socket.close();
        }

        public void run() {
            byte[] buf = new byte[4096];
            DatagramPacket packet = new DatagramPacket(buf, 4096);
            int mType;
            int id;
            long lParam, sParamLen;
           

            String sParam;
            //this.setPriority(Thread.MAX_PRIORITY);

            while (true) {
                try {
                  m_Socket.receive(packet);
                } catch (Exception e) {
                    m_Owner.writeDebug("CommThread", "Socket exception ("+e.getMessage()+")", e);
                    break;
                }
                ByteArrayInputStream bis = new ByteArrayInputStream(buf);
                DataInputStream in = new DataInputStream(bis);
                
                // First read message type
                mType = -1;
                try {
                    mType = in.readInt();
                    switch (mType) {
                        // DD Client initialization done
                        case 0:
                            m_Owner.initDone();
                            break;
                        // TCS State message
                        case 1:
                            // Read ID
                            id = in.readInt();

                            // Read LParam
                            lParam = in.readInt();

                            // Read SParam length
                            sParamLen = in.readInt();

                            char ch;

                            sParam = new String();
                            while (sParamLen > 0) {
                                ch = in.readChar();
                                sParam = sParam + ch;
                                sParamLen--;
                            }

                            try {
                              m_Owner.tcsStateNotify(id, lParam, sParam);
                            } catch (Exception e) {
                                m_Owner.writeDebug("CommThread", "Error while notifying state "+id, e);
                                
                            }
                            break;

                        // Host State message
                        case 2:

                            // Read ID
                            id = in.readInt();

                            // Read LParam
                            lParam = in.readInt();
                            
                            m_Owner.hostStateNotify(id, lParam);
                            break;

                        // Error message
                        case 3:
                            // Read ID
                            id = in.readInt();

                            // Read SParam length
                            sParamLen = in.readInt();

                            sParam = new String();
                            while (sParamLen > 0) {
                                sParam = sParam + in.readChar();
                                sParamLen--;
                            }
                            m_Owner.reportError(id, sParam);
                            break;


                        // Timeout information
                        case 4:
                            // Read timeout type
                            id = in.readInt();

                            // Read seconds left
                            lParam = in.readInt();

                            m_Owner.timeoutAlert(id, lParam);
                            break;

                        default:
                            m_Owner.writeDebug("CommThread", "Received unknown status message type "+mType, null);
                    }
                } catch (Exception e) {
                    m_Owner.writeDebug("CommThread", "Error while reading status message type ("+e.getMessage()+")", e);
                    break;
                }
            }

            m_Owner.writeDebug("CommThread", "Status thread exit", null);
        }


    }


  // TCS State codes
  public static final int TCS_STATUS_CONNECTING      = 1;
  public static final int TCS_STATUS_CONNECT_FAILED  = 2;
  public static final int TCS_STATUS_CONNECTED       = 3;
  public static final int TCS_STATUS_RET_HOSTS       = 4;
  public static final int TCS_STATUS_DOWNLOAD_CLIENT = 5;
  public  final int TCS_STATUS_READY           = 6;
  public static final int TCS_STATUS_CLIENT_VERIFI   = 7;
  public static final int TCS_STATUS_DISCONNECTED    = 8;



    // TCS Error codes
  public  final int ERR_FAILED_RET_DESKTOPS                         = 1;
  public static final int ERR_FAILED_TO_CONN_TIMEOUT                      = 2;
  public static final int ERR_FAILED_TO_CONN_GENERIC                      = 3;
  public static final int ERR_FAILED_TO_INSTALL_RDP_CLIENT                = 4;
  public static final int ERR_PLUGIN_MISSING                              = 5;
  public static final int ERR_INVALID_PORT                                = 6;
  public static final int ERR_FAILED_TO_RESOLVE_HOSTNAME                  = 7;
  public static final int ERR_FAILED_TO_CONNECT_TO_HOST                   = 8;
  public static final int ERR_FAILED_TO_START_RDP_CLIENT                  = 9;
  public static final int ERR_FAILED_TO_CONNECT_TO_SPX                    = 10;
  public static final int ERR_HOSTNAME_MISSING                            = 11;
  public static final int ERR_INVALID_WIDTH                               = 12;
  public static final int ERR_WIDTH_OUT_OF_RANGE                          = 13;
  public static final int ERR_INVALID_HEIGHT                              = 14;
  public static final int ERR_HEIGHT_OUT_OF_RANGE                         = 15;
  public static final int ERR_FAILED_TO_WAKEUP_DESKTOP                    = 16;
  public static final int ERR_GENERIC_ERROR                               = 17;
  public static final int ERR_WAKEUP_FAILED                               = 18;
  public static final int ERR_CLV_FAILED                                  = 19;
  public static final int ERR_ART_COMM                                    = 20;
  public static final int ERR_SA_COMM                                     = 21;
  public static final int ERR_APP_NOT_EXIST                               = 22;
  public static final int ERR_EP_NOT_AVAIL                                = 23;
  public static final int ERR_EP_RELOGIN                                  = 24;
  public static final int ERR_DEV_ID_PEND                                 = 25;
  public static final int TCS_STATUS_REDIRECT                             = 26;
  public static final int ERR_DEV_ID_FAILED                               = 27;
    public GUIProperty testprop;
    public String m_Platform;
    public String m_SPX;
    public int    m_SPXPort;
    public String m_ModID;
    public String m_SessID;
    public String m_SPCookie;
    public String m_Cfg;
	public String m_FarmsList;
	public String m_DesktopsList;
    public int    m_HideDesks;
    private tcsmng m_TCSMng = null;
    private int    m_CurDesktop;
    private CommThread m_CommThread;
    private boolean m_CustomPortal;
    private String  m_ARTServer;
    private boolean m_ARTEnabled;
    private boolean m_AllowCustomDest;
    private boolean m_PowerMngEnabled;
    private int m_AllowRedirControl;
    private Thread m_OwnThread;
    private Global m_Global;

    public int RegDesktopsCount = 0;
    public String DesktopDesc = "";
    public int CurAppID = -1;
    public int ScreenWidth;
    public int ScreenHeight;
    public int ColorDepth;
    public int SoundRedir;
    public int RedirDrives;
    public int RedirPrinters;
    public int RedirClipboard;
    public int RedirPorts;
    public int RedirSmartCards;
    public int RedirPOS;
    public int ConnectToConsole;
    public int EnableBitmapCaching;
    public int EnableDesktopWallpaper;
    public int EnableFullDrag;
    public int EnableMenuAnim;
    public int EnableTheme;
    public int SpanMonitors;
    Thread m_downloadthread = null;

    public void InitTCS() {
        m_TCSMng.initTCS();
    }

    public int AllowNonRegDesktops() {
      if (m_TCSMng.getDBMngParam("AllowCustomDest").equals("1")) {
          return 1;
      } else {
          return 0;
      }
    }

    public void RefreshDesktopState() {
        if ((m_CurDesktop >= 0) && (m_CurDesktop < m_TCSMng.getHostsCount())) {
            m_TCSMng.beforeConnect(m_CurDesktop);
        }
    }
    public void logout() {
            m_TCSMng.logout();
    }
    public void WakeupDesktop() {
        if ((m_CurDesktop >= 0) && (m_CurDesktop < m_TCSMng.getHostsCount())) {
            m_TCSMng.powerupDesktop(m_CurDesktop);
        }
    }

    public void LaunchDesktop(int prmHostIndex, int prmSSO) {


       int w, h, d;
       Integer i;
       int colord;
       int perfFlags;

       
       d = Integer.parseInt(m_TCSMng.getDBMngParam("DefSize"));
       if (d == 4) {
           // User selected custom size
           i = Integer.parseInt(m_TCSMng.getDBMngParam("DefWidth"));
           if (i == null) {
               reportError(ERR_INVALID_WIDTH, "");
               return;
           }
           w = i;

           i = Integer.parseInt(m_TCSMng.getDBMngParam("DefHeight"));
           if (i == null) {
               reportError(ERR_INVALID_HEIGHT, "");
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
       d = Integer.parseInt(m_TCSMng.getDBMngParam("DefColorDepth"));
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


       if (m_TCSMng.getDBMngParam("DesktopWallpaper").equalsIgnoreCase("1")) {
           perfFlags += 0x1;
       }

       if (m_TCSMng.getDBMngParam("FullWindowDrag").equalsIgnoreCase("1")) {
           perfFlags += 0x2;
       }

       if (m_TCSMng.getDBMngParam("MenuAnim").equalsIgnoreCase("1")) {
           perfFlags += 0x4;
       }

       if (m_TCSMng.getDBMngParam("Theme").equalsIgnoreCase("1")) {
           perfFlags += 0x8;
       }
       RedirPrinters= Integer.parseInt(m_TCSMng.getDBMngParam("RedirPrinters"));
       RedirClipboard= Integer.parseInt(m_TCSMng.getDBMngParam("RedirClipboard"));                 
       RedirDrives= Integer.parseInt(m_TCSMng.getDBMngParam("RedirDrives"));                 
       RedirPorts= Integer.parseInt(m_TCSMng.getDBMngParam("RedirPorts"));                 
       RedirPOS= Integer.parseInt(m_TCSMng.getDBMngParam("RedirPOS"));                 
       RedirSmartCards= Integer.parseInt(m_TCSMng.getDBMngParam("RedirSmartCards"));
       int console = Integer.parseInt(m_TCSMng.getDBMngParam("console"));
        m_TCSMng.launchDesktop("", prmHostIndex,
             w, h, colord,
             RedirDrives, RedirPrinters, RedirPorts,
             RedirSmartCards, RedirClipboard, RedirPOS,
             boolAsInt(m_TCSMng.getDBMngParam("BitmapCache").equalsIgnoreCase("1")),
             perfFlags,
             0, console, 2, prmSSO, "", "");
        m_Global.writeLog(Global.LOG_LEVEL_DEBUG,"tcsgui launchDesktop");
    }

    public void SetCurDesktop(int prmIndex) {
        if ((prmIndex >= 0) && (prmIndex < m_TCSMng.getHostsCount())) {
            m_CurDesktop = prmIndex;
            DesktopDesc = m_TCSMng.getHostDesc(m_CurDesktop);
        }
    }


    public int AllowConnectToConsole() {
      if (cbConnectToConsole.isVisible()) {
          return 1;
      } else {
          return 0;
      }
    }

    public int AllowRedirControl() {
      return m_AllowRedirControl;
    }

    public void StartSession(String prmServer, int prmSSO) {

       int perfFlags = 0;
       int cd = 0;

       if (EnableDesktopWallpaper == 1) {
           perfFlags += 0x1;
       }

       if (EnableFullDrag == 1) {
           perfFlags += 0x2;
       }

       if (EnableMenuAnim == 1) {
           perfFlags += 0x4;
       }

       if (EnableTheme == 1) {
           perfFlags += 0x8;
       }

       switch (ColorDepth) {
           case 1:
             cd = 8;
             break;

           case 2:
             cd = 16;
             break;

           case 3:
             cd = 24;
             break;
               
           case 4:
             cd = 32;
             break;

       }

        m_TCSMng.launchDesktop(prmServer, -1, ScreenWidth, ScreenHeight,
                cd, RedirDrives, RedirPrinters, RedirPorts,
                RedirSmartCards, RedirClipboard, RedirPOS, EnableBitmapCaching,
                perfFlags, SoundRedir, ConnectToConsole, SpanMonitors, prmSSO, "", "");
    }


/////////////////////////////////////////////////////////////////////////////////
    public void PopCurFolder() {
      m_TCSMng.popCurFolder();
      AppsCount = m_TCSMng.getAppsCount();
      SubFoldersCount = m_TCSMng.getSubFoldersCount();
      HasParentFolder = m_TCSMng.getHasParentFolder();
    }

    public void SetCurFolder(int prmIndex) {
      m_TCSMng.setCurFolder(prmIndex);
      AppsCount = m_TCSMng.getAppsCount();
      SubFoldersCount = m_TCSMng.getSubFoldersCount();
      HasParentFolder = m_TCSMng.getHasParentFolder();
    }

    public void SetCurSubFolder(int prmIndex) {
      m_TCSMng.setCurSubFolder(prmIndex);
      CurSubFolder = m_TCSMng.getCurSubFolder();
    }

    public void SetCurApp(int prmIndex) {
      if ((prmIndex >= 0) && (prmIndex < m_TCSMng.getAppsCount())) {
            m_TCSMng.setCurApp(prmIndex);
            CurAppName = m_TCSMng.getCurAppName();
            CurAppIconURL = m_TCSMng.getCurAppIconURL();
        }

    }

    public void LaunchCurApp() {
        m_TCSMng.launchCurApp();
    }

    public int SubFoldersCount;
    public String CurSubFolder;
    public int    AppsCount;
    public String CurAppIconURL;
    public String CurAppName;
    public int HasParentFolder;




/////////////////////////////////////////////////////////////////////////////////

    public void initDone() {

        // If default GUI is not used that means the applet is embedded in
        // a custom portal
        m_CustomPortal = m_TCSMng.getDBMngParam("GetEnableGUI").equals("0");
        m_ARTEnabled = m_TCSMng.getDBMngParam("ARTEnabled").equals("1");
        m_ARTServer = m_TCSMng.getDBMngParam("ARTServer");
        m_AllowCustomDest = m_TCSMng.getDBMngParam("AllowCustomDest").equals("1");
        m_PowerMngEnabled = m_TCSMng.getDBMngParam("EnablePowerMng").equals("1");

        System.out.println("tcsgui loadPresets before");
        loadPresets();
        System.out.println("tcsgui loadPresets after");

        if (m_CustomPortal == false) {
            m_TCSMng.initTCS();
        } else {
          cbxSpan.setSelected(true);
        }
    }

    public void tcsStateNotify(int prmID, long prmLParam, String prmSParam) {
      int i;
      String desc;

      switch (prmID) {
          case 1:
              /* Prog vis, Alert off */
              lblInfo.setVisible(true);
              lblInfo.setText("Connecting...");
              break;

          case 2:
              /* Prog off, Alert vis */
              lblInfo.setVisible(true);
              lblInfo.setText("Failed to connect to gateway");
              break;

          case 3:
              /* Prog off, Alert off */
              lblInfo.setVisible(false);
              break;

          case 4:
              /* Prog vis, Alert off */
              lblInfo.setVisible(true);
              lblInfo.setText("Retrieving Desktops ...");
              break;

          case 5:
              if (prmLParam < 100) {
                  /* Prog vis, Alert off */
                  lblInfo.setVisible(true);
                  lblInfo.setText("Retrieving Desktops ...");
                  if (prmLParam > 0) {
                    lblInfo.setText(prmSParam+" ("+prmLParam+")...");
                  } else {
                    lblInfo.setText(prmSParam+"...");
                  }
              } else {
                  /* Prog off, Alert off */
                  lblInfo.setVisible(false);
              }
              break;
          case 6:
              /* Prog off, Alert off */
              RegDesktopsCount = m_TCSMng.getHostsCount();
              AppsCount = m_TCSMng.getAppsCount();
              SubFoldersCount = m_TCSMng.getSubFoldersCount();
              HasParentFolder = 0;

              for (i = 0; i < RegDesktopsCount; i++) {
                  desc = new String(m_TCSMng.getHostDesc(i));
                  cbDest.addItem(desc);
              }

              lblInfo.setVisible(false);
              break;

          case 7:
              /* Prog vis, Alert off */
              lblInfo.setVisible(true);
              lblInfo.setText("Performing Client Verification, please wait...");
              break;

          case 8:
              if (lblInfo.isVisible() == false) {
                  /* Prog off, Alert off */
                  lblInfo.setVisible(true);
                  lblInfo.setText("Lost connection to the gateway");
                  break;
              }

      }

      if (m_CustomPortal) {
        try {
          JSObject win = JSObject.getWindow(this);
          if (win != null) {
              Object args[] = { prmID, prmLParam, prmSParam };
              win.call("TCSStatusChanged", args);
              return;
          }
        } catch (Exception e) {
        }
        
      }
    }

    public void hostStateNotify(int prmID, long prmLParam) {

      if (m_CustomPortal) {
        JSObject win = JSObject.getWindow(this);
        if (win != null) {
            Object args[] = { prmID, prmLParam};
            win.call("HostStatusChanged", args);
            return;
        }
      } else {
          switch ((int)prmLParam) {
              case 0: // Unknown
                  /* Prog off, Alert vis */
                  lblInfo.setVisible(true);
                  if (prmID < RegDesktopsCount) {
                      lblInfo.setText(m_TCSMng.getHostDesc(prmID)+
                              " is in an unknown state");
                  } else {
                      lblInfo.setText("Unknown Error");
                  }
                  panMain.setEnabled(true);
                  pan1.setEnabled(true);
                  break;

              case 1: // Available
                  /* Prog off, Alert off */
                  lblInfo.setVisible(false);
                  if (prmLParam < RegDesktopsCount) {
                      launchCurDesktop(1);
                  }
                  panMain.setEnabled(true);
                  pan1.setEnabled(true);
                  break;

              case 2: // Not Available
                  int n = JOptionPane.showConfirmDialog(
                            this, "It seems the desktop might be turned off\nWould you like to try and turn it on?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        if (prmID < RegDesktopsCount) {
                            m_TCSMng.powerupDesktop(prmID);
                        } else {
                            /* Prog off, Alert off */
                            lblInfo.setVisible(false);
                            panMain.setEnabled(true);
                            pan1.setEnabled(true);
                        }
                    } else {
                         /* Prog off, Alert off */
                         lblInfo.setVisible(false);
                         panMain.setEnabled(true);
                         pan1.setEnabled(true);
                    }
                  break;

              case 3: // Powering up
                  /* Prog vis, Alert off */
                  lblInfo.setVisible(true);
                  lblInfo.setText("Powering Up...");
                  break;

              case 4: // Connecting
                  break;
              case 5: // Connected
                  break;
              case 6: // Disconnecting
                  break;
              case 7: // Disconnected
                  break;

              case 8: // Checking availability
                  /* Prog vis, Alert off */
                  lblInfo.setVisible(true);
                  lblInfo.setText("Verifying Desktop Availability...");
                  break;
              case 9: // Powerup failed
                  /* Prog off, Alert vis */
                  lblInfo.setVisible(true);
                  if (prmID < RegDesktopsCount) {
                      lblInfo.setText("Wakeup failed for "+
                              m_TCSMng.getHostDesc(prmID));
                  } else {
                      lblInfo.setText("Wakeup failed");
                  }
                  panMain.setEnabled(true);
                  pan1.setEnabled(true);
                  break;
          }
      }
    }

    public void reportError(int prmID, String prmInfo) {
        if (m_CustomPortal) {
            JSObject win = JSObject.getWindow(this);
            if (win != null) {
                Object args[] = { prmID, prmInfo};
                win.call("reportError", args);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, prmID+" - "+prmInfo, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void timeoutAlert(int prmTOType, long prmSecondsLeft) {

        long days, hours, mins, secs;
        String st;

        if (m_CustomPortal) {
            JSObject win = JSObject.getWindow(this);
            if (win != null) {
                Object args[] = { prmTOType, prmSecondsLeft};
                win.call("timeoutAlert", args);
                return;
            }
        } else {

            if (prmTOType == 0) {
                // Clear alerts
                /* Prog off, Alert vis */
                lblInfo.setVisible(false);
            } else {
                // Idle or Lifetime alert

                // Converts time left to a string
                st = "";
                if (prmSecondsLeft > -1) {
                    secs = prmSecondsLeft;
                    days = secs / 86400;
                    secs -= (days * 86400);

                    hours = secs / 3600;
                    secs -= (hours * 3600);

                    mins = secs / 60;
                    secs -= (mins * 60);

                    if (days > 0) {
                        st = days+" days(s) "+hours+":"+mins+":"+secs;
                    } else {
                        if (hours > 0) {
                            st = hours+":"+mins+":"+secs;
                        } else {
                            if (mins > 0) {
                                st = mins+":"+secs;
                            } else {
                                st = Long.toString(secs);
                            }

                        }
                    }
                }

                if (prmTOType == 1) {
                    // Idle timeout
                    if (prmSecondsLeft > -1) {
                        lblInfo.setText("Due to inactivity your session will terminate in "+st);
                    } else {
                        lblInfo.setText("Due to inactivity your session expired");
                    }
                } else {
                    //Lifetime timeout
                    if (prmSecondsLeft > -1) {
                        lblInfo.setText("Your session will terminate in "+st+" Please save your work now! ");
                    } else {
                        lblInfo.setText("Your session expired");
                    }
                }

                /* Prog off, Alert vis */
                lblInfo.setVisible(true);
            }
        }
    }

    public void writeDebug(String prmMod, String prmMsg, Exception prmException) {
        if (prmException != null) {
            prmException.printStackTrace();
        }
        System.out.println(prmMod+" - "+prmMsg);
    }

    private void sizeChanged() {
        switch (cbSize.getSelectedIndex()) {
            case 4: //Custom Size
            	System.out.println("tcsgui sizeChanged custom size");
                edHeight.setEnabled(true);
                edHeight.setText(m_TCSMng.getDBMngParam("DefHeight"));
                edWidth.setEnabled(true);
                edWidth.setText(m_TCSMng.getDBMngParam("DefWidth"));
                cbxSpan.setEnabled(false);
                break;

            case 0: // Full Screen
            	System.out.println("tcsgui sizeChanged Full Screen");
                edHeight.setEnabled(false);
                edHeight.setText("");
                edWidth.setEnabled(false);
                edWidth.setText("");
                cbxSpan.setEnabled(true);
                break;

            default:
            	System.out.println("tcsgui sizeChanged default");
                edHeight.setEnabled(false);
                edHeight.setText("");
                edWidth.setEnabled(false);
                edWidth.setText("");
                cbxSpan.setEnabled(false);
        }
    }

    private void perfSettingsChanged() {
        switch (cbConnType.getSelectedIndex()) {
            case 0: // Slow dialup
                cbBitmapCaching.setSelected(false);
                cbDesktopWallpaper.setSelected(true);
                cbFullWindow.setSelected(true);
                cbMenuAnimation.setSelected(true);
                cbThemes.setSelected(true);
                break;

            case 1: // Fast dialup
                cbBitmapCaching.setSelected(false);
                cbDesktopWallpaper.setSelected(true);
                cbFullWindow.setSelected(true);
                cbMenuAnimation.setSelected(true);
                cbThemes.setSelected(false);
                break;

            case 2: // Broadband
                cbBitmapCaching.setSelected(false);
                cbDesktopWallpaper.setSelected(true);
                cbFullWindow.setSelected(false);
                cbMenuAnimation.setSelected(false);
                cbThemes.setSelected(false);
                break;

            case 3: //LAN
                cbBitmapCaching.setSelected(false);
                cbDesktopWallpaper.setSelected(false);
                cbFullWindow.setSelected(false);
                cbMenuAnimation.setSelected(false);
                cbThemes.setSelected(false);
                break;

            default:
                cbBitmapCaching.setSelected(false);
                cbDesktopWallpaper.setSelected(false);
                cbFullWindow.setSelected(false);
                cbMenuAnimation.setSelected(false);
                cbThemes.setSelected(false);
        }
    }

    private void loadPresets() {
        int i;

        if (m_ARTEnabled) {
            // ART is enabled disable edit box for destination and enable a combo box
            cbDest.setEditable(m_AllowCustomDest);
            btConnectAs.setVisible(false);
        } else {
            cbDest.setEditable(true);
            btConnectAs.setVisible(true);
        }


        i = Integer.parseInt(m_TCSMng.getDBMngParam("DefSize"));
        System.out.println("tcsgui loadPresets setSelectedIndex is "+i);
        if (i == -1) {
            cbSize.setSelectedIndex(0);
        } else {
            cbSize.setSelectedIndex(i);
        }
        sizeChanged();
        ScreenWidth = Integer.parseInt(m_TCSMng.getDBMngParam("DefWidth"));
        if(cbSize.getSelectedIndex()==0 && ScreenWidth==0 )
        {
        	ScreenWidth =-1;
        }
		System.out.println("tcsgui loadPresets ScreenWidth is "+ScreenWidth);
        ScreenHeight = Integer.parseInt(m_TCSMng.getDBMngParam("DefHeight"));
        if(cbSize.getSelectedIndex()==0 && ScreenHeight==0 )
        {
        	ScreenHeight =-1;
        }
		System.out.println("tcsgui loadPresets DefHeight is "+ScreenHeight);

        i = Integer.parseInt(m_TCSMng.getDBMngParam("DefColorDepth"));
        ColorDepth = i;
		System.out.println("tcsgui loadPresets DefColorDepth is "+i);
        cbColorDepth.setSelectedIndex(i);

        i = Integer.parseInt(m_TCSMng.getDBMngParam("RedirDrives"));
        cbRedirDrv.setSelected(i == 1);
        RedirDrives = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("RedirPrinters"));
        System.out.println("tcsgui loadPresets RedirPrinters is "+i);
        cbRedirPrinters.setSelected(i == 1);
        RedirPrinters = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("RedirPorts"));
        cbRedirPorts.setSelected(i == 1);
        RedirPorts = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("RedirSmartCards"));
        cbRedirSmartCards.setSelected(i == 1);
        RedirSmartCards = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("RedirClipboard"));
        cbRedirClipboard.setSelected(i == 1);
        RedirClipboard = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("RedirPOS"));
        cbRedirPOSDev.setSelected(i == 1);
        RedirPOS = i;

        SoundRedir = cbSound.getSelectedIndex();

        i = Integer.parseInt(m_TCSMng.getDBMngParam("BitmapCache"));
        cbBitmapCaching.setSelected(i == 1);
        EnableBitmapCaching = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("DesktopWallpaper"));
        cbDesktopWallpaper.setSelected(i == 1);
        EnableDesktopWallpaper = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("FullWindowDrag"));
        cbFullWindow.setSelected(i == 1);
        EnableFullDrag = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("MenuAnim"));
        cbMenuAnimation.setSelected(i == 1);
        EnableMenuAnim = i;

        i = Integer.parseInt(m_TCSMng.getDBMngParam("Theme"));
        cbThemes.setSelected(i == 1);
        EnableTheme = i;
    
        i = Integer.parseInt(m_TCSMng.getDBMngParam("AllowConnectToConsole"));
        if (i == 0) {
            cbConnectToConsole.setVisible(false);
        } else {
            cbConnectToConsole.setVisible(true);
        }
        cbConnectToConsole.setSelected(false);
        ConnectToConsole = 0;

        m_AllowRedirControl = Integer.parseInt(m_TCSMng.getDBMngParam("AllowRedirControl"));
        if (m_AllowRedirControl == 0) {
            panRedir.setVisible(false);
            jLabel7.setVisible(false);
        }
        
        //perfSettingsChanged();

        SpanMonitors = 0;
    }
    private void download_thread() {
		AccessController.doPrivileged(new PrivilegedAction() {
			public Object run() {
				/* get spname spport and downloadpath */
				install_DesktopDirect();
				return null;
			}
		});
	}
    /** Initializes the applet tcsgui */
    public void init() {

        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /*
        m_OwnThread = new Thread(this);
        m_OwnThread.start();
    }
    

   public void run() {*/
       m_Global = new Global();
       
       // Detects which OS is running
       String os = System.getProperty("os.name");
       if(os.startsWith("Mac OS")) {
           // Initialize the MAC OS TCS manager
           m_TCSMng = new macostcsmng(this, m_Global);
       } else {
           if (os.indexOf( "nux") >=0) {
               // Initialize the Linux TCS manager
               m_TCSMng = new linuxtcsmng(this, m_Global);
           } else {
             // Initialize the Windows TCS manager
             m_TCSMng = new wintcsmng();
           }
       }
       

       if (m_TCSMng.isReady() == false) {
         // Interface is not ready load failed
         return;
       }

       m_CommThread = new CommThread(this);
       m_CommThread.start();
       m_SPXPort = m_CommThread.getLocalPort();

       try {
          m_Platform = getParameter("platform");
          if ((m_Platform != null) && (m_Platform.compareTo("AG") == 0)) {
            m_Cfg = "AG";
            //m_ModID = "DesktopDirect";
            if(os.startsWith("Mac OS")) {
            	m_ModID = Global.getDeviceID();
            } else {
            	m_ModID = "DesktopDirect";
            }
          } else {
            m_Cfg = getParameter("config_str");
            m_ModID = getParameter("module_id");
          }

          m_SPX = getParameter("sp_name");     //"spdemo.arraynetworks.net";
          m_SPXPort = new Integer(getParameter("sp_port")).intValue(); //443
          m_SessID = getParameter("session_id");
          m_SPCookie = getParameter("SPCookies");
		  m_FarmsList = getParameter("farmslist");
		   if(m_FarmsList == null)
		  {
			  m_FarmsList= new String();
			  m_FarmsList="-1";
		  }
		  System.out.println(" m_FarmsList:"+m_FarmsList);
		  
		  m_DesktopsList = getParameter("xdfarmslist");
		   if(m_DesktopsList == null)
		  {
			  m_DesktopsList= new String();
			  m_DesktopsList="-1";
		  }
		  System.out.println(" m_DesktopsList:"+m_DesktopsList);
		  
		  String hidesks = getParameter("hidedesks");
		  if(hidesks==null)
		  {
			  m_HideDesks = -1;
		  }
		  else
		  {
			  m_HideDesks = new Integer(hidesks).intValue();
		  }
		  System.out.println(" m_HideDesks:"+m_HideDesks);
          
       } catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
         return;
       }
		if (os.startsWith("Mac OS")) {
			m_downloadthread = new Thread() {
				public void run() {
					download_thread();
				}
			};

			m_downloadthread.start();
		}
       System.out.println("!!!!! init() !!!!!");
       m_TCSMng.init(m_SPX, m_SPXPort, m_SPCookie, m_SessID, m_ModID, m_Cfg,m_FarmsList, m_DesktopsList, m_CommThread.getLocalPort(),m_HideDesks);
     
   }

   public void stop() {
     m_TCSMng.finish();
   }

   public String getPlatform() {
       return m_Platform;
   }

   private int boolAsInt(boolean prmVal) {
       if (prmVal) {
           return 1;
       } else {
           return 0;
       }
   }

   private void launchCurDesktop(int prmSSO) {
       Integer i;
       int w, h;
       int colord;
       int perfFlags;

       if (cbSize.getSelectedIndex() == 4) {
           // User selected custom size
           i = Integer.parseInt(edWidth.getText());
           if (i == null) {
               reportError(ERR_INVALID_WIDTH, "");
               return;
           }
           w = i;
           
           i = Integer.parseInt(edHeight.getText());
           if (i == null) {
               reportError(ERR_INVALID_HEIGHT, "");
               return;
           }
           h = i;
       } else {
           // User selected a pre-defined size
           w = -1;
           h = -1;
           switch (cbSize.getSelectedIndex()) {
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
       switch (cbColorDepth.getSelectedIndex()) {
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

       if (cbDesktopWallpaper.getSelectedObjects() != null) {
           perfFlags += 0x1;
       }

       if (cbFullWindow.getSelectedObjects() != null) {
           perfFlags += 0x2;
       }

       if (cbMenuAnimation.getSelectedObjects() != null) {
           perfFlags += 0x4;
       }

       if (cbThemes.getSelectedObjects() != null) {
           perfFlags += 0x8;
       }

       m_TCSMng.launchDesktop(
             (String)cbDest.getSelectedItem(),
             cbDest.getSelectedIndex(),
             w, h, colord,
             RedirDrives, RedirPrinters, RedirPorts,
             RedirSmartCards, RedirClipboard, RedirPOS,
             boolAsInt(cbBitmapCaching.getSelectedObjects() != null),
             perfFlags,
             cbSound.getSelectedIndex(),
             boolAsInt(cbConnectToConsole.getSelectedObjects() != null),
             boolAsInt(cbxSpan.getSelectedObjects() != null),
             prmSSO, "", "");
   }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        panMain = new javax.swing.JTabbedPane();
        pan1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbSize = new javax.swing.JComboBox();
        btConnect = new javax.swing.JButton();
        btConnectAs = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        edWidth = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        edHeight = new javax.swing.JTextField();
        cbxSpan = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        cbSound = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbColorDepth = new javax.swing.JComboBox();
        panRedir = new javax.swing.JPanel();
        cbRedirDrv = new javax.swing.JCheckBox();
        cbRedirPorts = new javax.swing.JCheckBox();
        cbRedirPrinters = new javax.swing.JCheckBox();
        cbRedirSmartCards = new javax.swing.JCheckBox();
        cbRedirClipboard = new javax.swing.JCheckBox();
        cbRedirPOSDev = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        cbDest = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        cbConnType = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        cbBitmapCaching = new javax.swing.JCheckBox();
        cbDesktopWallpaper = new javax.swing.JCheckBox();
        cbFullWindow = new javax.swing.JCheckBox();
        cbMenuAnimation = new javax.swing.JCheckBox();
        cbThemes = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        cbConnectToConsole = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                formPropertyChange(evt);
            }
        });

        pan1.setLayout(null);

        jLabel1.setText("Host\\IP Address:");
        pan1.add(jLabel1);
        jLabel1.setBounds(20, 10, 100, 14);

        cbSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Full Screen", "640X480", "800X600", "1024X780", "Custom" }));
        cbSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbSizeItemStateChanged(evt);
            }
        });
        pan1.add(cbSize);
        cbSize.setBounds(140, 80, 200, 20);

        btConnect.setText("Connect");
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });
        pan1.add(btConnect);
        btConnect.setBounds(140, 40, 90, 20);

        btConnectAs.setText("Connect As...");
        btConnectAs.setDefaultCapable(false);
        btConnectAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectAsActionPerformed(evt);
            }
        });
        pan1.add(btConnectAs);
        btConnectAs.setBounds(240, 40, 99, 20);

        jLabel2.setText("Size:");
        pan1.add(jLabel2);
        jLabel2.setBounds(20, 80, 70, 14);

        jLabel3.setText("Width:");
        pan1.add(jLabel3);
        jLabel3.setBounds(140, 114, 40, 10);

        edWidth.setText("640");
        edWidth.setEnabled(false);
        pan1.add(edWidth);
        edWidth.setBounds(190, 110, 40, 20);

        jLabel4.setText("Height:");
        pan1.add(jLabel4);
        jLabel4.setBounds(250, 114, 40, 10);

        edHeight.setText("480");
        edHeight.setEnabled(false);
        pan1.add(edHeight);
        edHeight.setBounds(300, 110, 40, 20);

        cbxSpan.setText("Span Monitors");
        cbxSpan.setEnabled(false);
        pan1.add(cbxSpan);
        cbxSpan.setBounds(140, 140, 130, 20);

        jLabel5.setText("Color Depth:");
        pan1.add(jLabel5);
        jLabel5.setBounds(20, 170, 90, 14);

        cbSound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Play Sounds On This Computer", "Play Sounds on The Server", "Do Not Play Any Sounds" }));
        pan1.add(cbSound);
        cbSound.setBounds(140, 210, 200, 20);

        jLabel6.setText("Sound:");
        pan1.add(jLabel6);
        jLabel6.setBounds(20, 210, 100, 14);

        cbColorDepth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default (Server Settings)", "256 Colors", "High Color (16 bits)", "True Color (24 bits)", "Highest Quality (32 bits)" }));
        pan1.add(cbColorDepth);
        cbColorDepth.setBounds(140, 170, 200, 20);

        panRedir.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbRedirDrv.setText("Drives");
        cbRedirDrv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbRedirDrvItemStateChanged(evt);
            }
        });
        cbRedirDrv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedirDrvActionPerformed(evt);
            }
        });

        cbRedirPorts.setText("Ports");
        cbRedirPorts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedirPortsActionPerformed(evt);
            }
        });

        cbRedirPrinters.setText("Printers");
        cbRedirPrinters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedirPrintersActionPerformed(evt);
            }
        });

        cbRedirSmartCards.setText("Smart Cards");
        cbRedirSmartCards.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedirSmartCardsActionPerformed(evt);
            }
        });

        cbRedirClipboard.setText("Clipboard");
        cbRedirClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedirClipboardActionPerformed(evt);
            }
        });

        cbRedirPOSDev.setText("POS Dev.");
        cbRedirPOSDev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRedirPOSDevActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panRedirLayout = new javax.swing.GroupLayout(panRedir);
        panRedir.setLayout(panRedirLayout);
        panRedirLayout.setHorizontalGroup(
            panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRedirLayout.createSequentialGroup()
                .addGroup(panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbRedirDrv)
                    .addComponent(cbRedirPorts))
                .addGap(51, 51, 51)
                .addGroup(panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbRedirPrinters)
                    .addComponent(cbRedirSmartCards))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbRedirClipboard)
                    .addComponent(cbRedirPOSDev))
                .addGap(21, 21, 21))
        );
        panRedirLayout.setVerticalGroup(
            panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRedirLayout.createSequentialGroup()
                .addGroup(panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbRedirDrv)
                    .addComponent(cbRedirPrinters)
                    .addComponent(cbRedirClipboard))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(panRedirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbRedirPorts)
                    .addComponent(cbRedirSmartCards)
                    .addComponent(cbRedirPOSDev))
                .addContainerGap())
        );

        pan1.add(panRedir);
        panRedir.setBounds(20, 270, 310, 70);

        jLabel7.setText("Redirection:");
        pan1.add(jLabel7);
        jLabel7.setBounds(20, 250, 110, 14);

        cbDest.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbDestKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cbDestKeyTyped(evt);
            }
        });
        pan1.add(cbDest);
        cbDest.setBounds(140, 10, 200, 20);

        panMain.addTab("General", pan1);

        cbConnType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Slow Dialup", "Fast  Dialup", "Broadband", "LAN", "Custom" }));
        cbConnType.setSelectedIndex(4);
        cbConnType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbConnTypeItemStateChanged(evt);
            }
        });

        jLabel8.setText("Disable the following options:");

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cbBitmapCaching.setText("Bitmap Caching");

        cbDesktopWallpaper.setText("Desktop Wallpaper");

        cbFullWindow.setText("Full Window Drag");

        cbMenuAnimation.setText("Menu Animation");

        cbThemes.setText("Themes");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbBitmapCaching)
                    .addComponent(cbDesktopWallpaper)
                    .addComponent(cbFullWindow)
                    .addComponent(cbMenuAnimation)
                    .addComponent(cbThemes))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbBitmapCaching)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbDesktopWallpaper)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbFullWindow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbMenuAnimation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbThemes)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(cbConnType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbConnType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        panMain.addTab("Performance", jPanel2);

        cbConnectToConsole.setText("Connect to Console");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(cbConnectToConsole)
                .addContainerGap(357, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(cbConnectToConsole)
                .addContainerGap(310, Short.MAX_VALUE))
        );

        panMain.addTab("Advanced", jPanel3);

        lblInfo.setText("Info");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(233, 233, 233))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblInfo)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panMain)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panMain))
        );

        panMain.getAccessibleContext().setAccessibleName("General");
    }// </editor-fold>//GEN-END:initComponents

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        if (cbDest.getSelectedIndex() > -1) {
            if (m_PowerMngEnabled) {
                panMain.setEnabled(false);
                pan1.setEnabled(false);
                m_TCSMng.beforeConnect(cbDest.getSelectedIndex());
                return;
            }

        }

        launchCurDesktop(1);
    }//GEN-LAST:event_btConnectActionPerformed

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
        System.out.println("Property "+evt.getPropertyName()+" triggered an event");
    }//GEN-LAST:event_formPropertyChange

    private void cbSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbSizeItemStateChanged
        // TODO add your handling code here:
        sizeChanged();
    }//GEN-LAST:event_cbSizeItemStateChanged

    private void cbConnTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbConnTypeItemStateChanged
        // TODO add your handling code here:
        perfSettingsChanged();
    }//GEN-LAST:event_cbConnTypeItemStateChanged

    private void btConnectAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectAsActionPerformed
        launchCurDesktop(0);
    }//GEN-LAST:event_btConnectAsActionPerformed

    private void cbDestKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbDestKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cbDestKeyTyped

    private void cbDestKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbDestKeyPressed
    }//GEN-LAST:event_cbDestKeyPressed

    private void cbRedirDrvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbRedirDrvItemStateChanged
        
    }//GEN-LAST:event_cbRedirDrvItemStateChanged

    private void cbRedirDrvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedirDrvActionPerformed
        if (cbRedirDrv.isSelected()) {
          RedirDrives = 1;
        } else {
          RedirDrives = 0;
        }
    }//GEN-LAST:event_cbRedirDrvActionPerformed

    private void cbRedirPrintersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedirPrintersActionPerformed
        // TODO add your handling code here:
        if (cbRedirPrinters.isSelected()) {
          RedirPrinters = 1;
        } else {
          RedirPrinters = 0;
        }
    }//GEN-LAST:event_cbRedirPrintersActionPerformed

    private void cbRedirClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedirClipboardActionPerformed
        // TODO add your handling code here:
        if (cbRedirClipboard.isSelected()) {
          RedirClipboard = 1;
        } else {
          RedirClipboard = 0;
        }
    }//GEN-LAST:event_cbRedirClipboardActionPerformed

    private void cbRedirPortsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedirPortsActionPerformed
        // TODO add your handling code here:
        if (cbRedirPorts.isSelected()) {
          RedirPorts = 1;
        } else {
          RedirPorts = 0;
        }
    }//GEN-LAST:event_cbRedirPortsActionPerformed

    private void cbRedirSmartCardsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedirSmartCardsActionPerformed
        // TODO add your handling code here:
        if (cbRedirSmartCards.isSelected()) {
          RedirSmartCards = 1;
        } else {
          RedirSmartCards = 0;
        }
    }//GEN-LAST:event_cbRedirSmartCardsActionPerformed

	private void downloadClientFiles(String loadFile) {
		
		String tmp_install_path = System.getProperty("user.home") +"/.ArrayDD/";
		try {			
			File install_path = new File(tmp_install_path);
			boolean bExists = install_path.exists();
			if (!bExists) {
				install_path.mkdir();
			} else if (!install_path.isDirectory()) {
				/* System.out.println("exists!"); */
				install_path.delete();
				install_path.mkdir();
			}
			InputStream inputStream2 = tcsgui.class.getResource(loadFile).openStream();	
			File dllFile2 = new File(tmp_install_path+ loadFile); 
			if(dllFile2.exists()){
				System.out.println("find loadfile and delete existing file");
				dllFile2.delete(); 				
			}				
			FileOutputStream outputStream2 = new FileOutputStream(dllFile2);
			byte[] array2 = new byte[8192];
			for (int i = inputStream2.read(array2); i != -1; i = inputStream2.read(array2)) { 
				outputStream2.write(array2, 0, i); 
			} 
			outputStream2.close();  
			inputStream2.close();
			
		} catch (Exception e) {
			System.out.println("install client exception "
					+ this.getClass().getName() + " "
					+ this.getClass().getMethods()[0].getName() + " "
					+ e.getMessage());
			e.printStackTrace();
			return;
		}
	}
    private void cbRedirPOSDevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRedirPOSDevActionPerformed
        // TODO add your handling code here:
        if (cbRedirPOSDev.isSelected()) {
          RedirPOS = 1;
        } else {
          RedirPOS = 0;
        }
    }//GEN-LAST:event_cbRedirPOSDevActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btConnect;
    private javax.swing.JButton btConnectAs;
    private javax.swing.JCheckBox cbBitmapCaching;
    private javax.swing.JComboBox cbColorDepth;
    private javax.swing.JComboBox cbConnType;
    private javax.swing.JCheckBox cbConnectToConsole;
    private javax.swing.JCheckBox cbDesktopWallpaper;
    private javax.swing.JComboBox cbDest;
    private javax.swing.JCheckBox cbFullWindow;
    private javax.swing.JCheckBox cbMenuAnimation;
    private javax.swing.JCheckBox cbRedirClipboard;
    private javax.swing.JCheckBox cbRedirDrv;
    private javax.swing.JCheckBox cbRedirPOSDev;
    private javax.swing.JCheckBox cbRedirPorts;
    private javax.swing.JCheckBox cbRedirPrinters;
    private javax.swing.JCheckBox cbRedirSmartCards;
    private javax.swing.JComboBox cbSize;
    private javax.swing.JComboBox cbSound;
    private javax.swing.JCheckBox cbThemes;
    private javax.swing.JCheckBox cbxSpan;
    private javax.swing.JTextField edHeight;
    private javax.swing.JTextField edWidth;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JPanel pan1;
    private javax.swing.JTabbedPane panMain;
    private javax.swing.JPanel panRedir;
    // End of variables declaration//GEN-END:variables
    private Socket ssl_disable_client_cert(Socket sock) {
		try {
			netscape.security.PrivilegeManager
					.enablePrivilege("TerminalEmulator");
		} catch (Exception ignored) {
			System.out.println("Error in ssl_disable_client_cert!");
		}

		try {
			long time = (new Date()).getTime();
			int unixtime = (int) (time / 1000); // convert miliseconds to
												// seconds
			byte[] gmt_unix_time = int2bytes(unixtime, 4);

			// construct the special header for disabling client cert auth
			byte[] tls_header = new byte[82];
			int i = 0, j;

			// TLSv1 Record Layer
			tls_header[i++] = 0x16; // Handshake
			tls_header[i++] = 0x03;
			tls_header[i++] = 0x01; // major, minor: TLSv1
			tls_header[i++] = 0x00;
			tls_header[i++] = 0x4D; // length: 77 bytes

			// Handshake Protocol
			tls_header[i++] = 0x01; // Client Hello
			tls_header[i++] = 0x00;
			tls_header[i++] = 0x00;
			tls_header[i++] = 0x49; // length: 73 bytes
			tls_header[i++] = 0x03;
			tls_header[i++] = 0x01; // major, minor: TLSv1

			for (j = 0; j < 4; j++) {
				tls_header[i++] = gmt_unix_time[j]; // GMT Unix Time
			}

			for (j = 0; j < 28; j++) {
				tls_header[i++] = (byte) (j + 1); // 28 bytes of random
			}

			tls_header[i++] = 0x20; // session id length: 32 bytes

			for (j = 0; j < 32; j++) {
				tls_header[i++] = (byte) 0xFF; // set all FF as special
			}
			tls_header[i++] = 0x00;
			tls_header[i++] = 0x02; // cipher suites length
			tls_header[i++] = 0x00;
			tls_header[i++] = 0x04; // TLS_RSA_WITH_RC4_128_MD5
			tls_header[i++] = 0x01; // compression methods length
			tls_header[i++] = 0x00; // compression method null
			OutputStream sp_out = sock.getOutputStream();
			BufferedOutputStream buff_out = new BufferedOutputStream(sp_out,
					1460);
			buff_out.write(tls_header, 0, tls_header.length);
			buff_out.flush(); // no more to write
		} catch (Exception e) {
			try {
				sock.close();
			} catch (Exception ignored) {
				System.out.println("Error in close socket!");
			}

			sock = null;
		}
		return sock;
	}
 // convert from int to bytes in network order (BE)
 	private static byte[] int2bytes(int num, int len) {
 		int i;
 		byte[] bytes = new byte[len];

 		for (i = (len - 1) * 8; i >= 0; i -= 8) {
 			bytes[(len - 1) - i / 8] = (byte) ((num >> i) & 0xFF);
 		}

 		return bytes;
 	}
 	private void chmodfile(String file) {
		try {
			// set 4755 on loader and client
			Runtime rt = Runtime.getRuntime();
			Process p1 = null;
			boolean done = false;
			int ret = -1;
			p1 = rt.exec(new String[] { "/bin/chmod", "4755", file });
			while (!done) {
				try {
					ret = p1.waitFor();
					ret = p1.exitValue();
					done = true;
				} catch (Exception notdone) {
				}
			}

			if (ret != 0) {
				System.out.println("Insufficient priviledges");				
				throw new Exception("Insufficient priviledges");
			}
		} catch (Exception e) {
			System.out.println("Exception: Failed to chmod:" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
 	private void install_DesktopDirect() {
 		String homePath = System.getProperty("user.home");
 		downloadClientFiles("DesktopDirect");
 		downloadClientFiles("start.applescript");
 		chmodfile(homePath +"/.ArrayDD/DesktopDirect");
 	}
}
