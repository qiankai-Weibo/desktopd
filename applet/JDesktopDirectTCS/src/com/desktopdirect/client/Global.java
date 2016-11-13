package com.desktopdirect.client;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import java.io.*;



/**
 * 
 * @author lrapport
 * The Global class manages all data that is shared between the activities:
 *   - Virtual portal accounts
 */  
public class Global {
	class MacCmdAction implements PrivilegedAction {
        private String m_Cmd;

        public MacCmdAction(String prmCmd) {
                m_Cmd = prmCmd;
        }

        public Object run() {
        	String address="";
      	  try {
//               ProcessBuilder pb = new ProcessBuilder(m_Cmd);
//               Process p = pb.start();
      		     Process p = Runtime.getRuntime().exec(m_Cmd);
                 BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                 String line;
                 while((line=br.readLine())!=null){
                     int index=line.indexOf("Hardware UUID: ");
                     if(index!=-1){
                         address=line.substring(index+15);
                         break;
                     }
                 }
                 br.close();
                 m_DevID = address.trim();
            } catch (IOException ex) {
                 System.out.println("Get device id exception: "+ex.getMessage());
                 return null;
            }
      	  return null;
        }
    }

  // Platform type indicator
  static final int    PLATFORM_UNKNOWN = 0;
  static final int    PLATFORM_SPX     = 1;
  static final int    PLATFORM_AG      = 2;
  
  public static final int LOG_LEVEL_DEBUG = 0;
  public static final int LOG_LEVEL_INFO  = 1;
  public static final int LOG_LEVEL_ERROR = 2;
  
  public static final String LOG_PROTOCOL = "DesktopDirectTCS";
  

  private int      m_LogLevel;
  private boolean  m_LogEnabled;
  protected static String   m_DevID="859949DB-3D1F-59B2-A892-D1309A6EDDF1";
  Global() {
    m_LogLevel = LOG_LEVEL_DEBUG;
    m_LogEnabled = true;
  }
  
  public void writeLog(int prmLogLevel, String prmMsg) {
    if (prmLogLevel >= m_LogLevel) {
        System.out.println(prmMsg);
    }
  }
  
  public boolean getLogEnabled() {
    return m_LogEnabled;
  }
  
  public void setLogEnabled(boolean prmEnabled) {
    m_LogEnabled = prmEnabled;
  }
  
  
  public int getLogLevel() {
    return m_LogLevel;
  }
  
  public void setLogLevel(int prmLevel) {
    m_LogLevel = prmLevel;
  }
  
  public void clearLogData() {
      
  }
  
  public static String getDeviceID() {
	  Global g = new Global();
	  MacCmdAction la = g.new MacCmdAction("/usr/sbin/system_profiler SPHardwareDataType");
	  AccessController.doPrivileged(la);
      System.out.println("deviceid is:"+m_DevID);
      return m_DevID;
  }
  

 }
  
