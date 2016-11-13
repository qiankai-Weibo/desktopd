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

public class wintcsmng extends tcsmng {

    	public final static String JNIDLL = "DDJNI.dll";
	public final static String ClientDLL = "DDDllClient.dll";
  public final static String Clientvl3DLL = "vl3ntlm.dll";   
  public final static String ClientProxyDLL = "DDProxy.dll";
	public final static String File_Separator = System.getProperty( "file.separator" );

	public static boolean isJNIDLLLoaded = false;
	public static boolean isPathOK = false;
	private String SysRoot  = null;
	private String targetPath = null;
        private File JNIdllFile = null;
        private File ClientdllFile = null;
        private File ClientdllVl3File = null;
        private File ClientdllProxyFile = null;
      native public int loadLib(String prmDLL);
      native public void init(String prmSPX, int prmPort, String prmSPCookie, String prmSessID, String prmModID, String prmCfg,String prmFmLst, String prmDtLst, int prmCommPort,int hideDesks);
      native public void initTCS();
      native public void finish();
      native public String getDBMngParam(String prmParam);
      native public void beforeConnect(int prmHostIndex);
      native public void powerupDesktop(int prmHostIndex);
      native public void launchDesktop(
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
      native public int getHostsCount();
      native public String getHostHost(int prmHostIndex);
      native public String getHostDesc(int prmHostIndex);
      native public int getHostTTL(int prmHostIndex);
      native public int getHostState(int prmHostIndex);


      native public String getCurAppName();
      native public String getCurAppIconURL();
      native public void launchCurApp();
      native public void popCurFolder();
      native public void setCurFolder(int prmIndex);
      native public void setHosts(ARTInterface.HostResults prmHosts);
      native public void setCurSubFolder(int prmIndex);
      native public int getHasParentFolder();
      native public int  getSubFoldersCount();
      native public String getCurFolder();
      native public String getCurSubFolder();
      native public int getAppsCount();
      native public void logout();
      native public void setCurApp(int prmIndex);
      native public ARTInterface getARTInterface();

    public wintcsmng() {
      //init the path variables
      targetPath = System.getProperty( "java.io.tmpdir" );
    }
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
   public boolean writeDDJNIlib(){
     byte[] array = new byte[8192];



     try {
       InputStream inputStream = wintcsmng.class.getResource(JNIDLL).openStream();

       JNIdllFile = new File(targetPath+File_Separator+JNIDLL);
       System.out.println("INFO: begin to write DD JNI DLL");
       FileOutputStream outputStream = new FileOutputStream(JNIdllFile);
       for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
         outputStream.write(array, 0, i);
       }
       outputStream.close();
       System.out.println("INFO: DD JNI DLL writing done");
       inputStream.close();
       return true;
     } catch (Exception e) {
       System.err.println("INFO: DD JNI DLL writing failed ("+e.getMessage()+")");
     }
     return false;
   }

    public boolean writeDDVl3Client(){
     byte[] array = new byte[8192];



     try {
       InputStream inputStream = wintcsmng.class.getResource(Clientvl3DLL).openStream();

       ClientdllVl3File = new File(targetPath+File_Separator+Clientvl3DLL); //tongjj
       System.out.println("INFO: begin to write DD v13");
       FileOutputStream outputStream = new FileOutputStream(ClientdllVl3File);
       for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
         outputStream.write(array, 0, i);
       }
       outputStream.close();
       System.out.println("INFO: DD v13 DLL writing done");
       inputStream.close();
       return true;
     } catch (Exception e) {
       System.err.println("INFO: DD v13 DLL writing failed ("+e.getMessage()+")");
     }
     return false;
   }

    public boolean writeDDClientProxy(){
     byte[] array = new byte[8192];



     try {
       InputStream inputStream = wintcsmng.class.getResource(ClientProxyDLL).openStream();

       ClientdllProxyFile = new File(targetPath+File_Separator+ClientProxyDLL);
       System.out.println("INFO: begin to write DD proxy DLL");
       FileOutputStream outputStream = new FileOutputStream(ClientdllProxyFile);
       for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
         outputStream.write(array, 0, i);
       }
       outputStream.close();
       System.out.println("INFO: DD proxy DLL writing done");
       inputStream.close();
       return true;
     } catch (Exception e) {
       System.err.println("INFO: DD proxy DLL writing failed ("+e.getMessage()+")");
     }
     return false;
   }

   public boolean writeDDClient(){
     byte[] array = new byte[8192];


     try {
       InputStream inputStream = wintcsmng.class.getResource(ClientDLL).openStream();

       ClientdllFile = new File(targetPath+File_Separator+ClientDLL);
       System.out.println("INFO: begin to write DD Client DLL");
       FileOutputStream outputStream = new FileOutputStream(ClientdllFile);
       for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
         outputStream.write(array, 0, i);
       }
       outputStream.close();
       System.out.println("INFO: DD Client DLL writing done");
       inputStream.close();
       return true;
     } catch (Exception e) {
       System.err.println("ERROR: DD Client DLL writing failed ("+e.getMessage()+")");
     }
     return false;
   }
 
   public boolean loadJNILib(){
     int r;

     System.out.println("INFO: Loading DD JNI DLL from " + targetPath);
     try {
       System.load(targetPath+File_Separator+JNIDLL);
       //System.load("C:\\DDJNI.dll");
       isJNIDLLLoaded = true;
       System.out.println("INFO: DD JNI DLL is loaded!");

       System.out.println("INFO: "+targetPath+ClientDLL);
       r = loadLib(targetPath+ClientDLL);
       //r = loadLib("C:\\DDDllclient.dll");
       System.out.println("INFO: Load client result "+r);

       return true;
     }
       catch(java.lang.UnsatisfiedLinkError e){
         System.err.println("ERROR: fail to load the DD JNI DLL from " + targetPath+" ("+e.getMessage()+")");
         e.printStackTrace();
       }
       catch(Exception e){
         e.printStackTrace();
       }


     return false;
   }

   public boolean isReady() {

      if ((writeDDClient() == false) ||
          (writeDDJNIlib() == false) ||
          (writeDDVl3Client() == false) ||
          (writeDDClientProxy() == false)) {
        // Failed to download JNI or client DLL cannot proceed
        System.out.println("Failed to download JNI or client DLL cannot proceed");
        return false;
      }

      // Load JNI
      if (loadJNILib() == false) {
        // Failed to load JNI cannot proceed
        System.out.println("Failed to loadJNILib");
        return false;
      }
      
      return true;
   }
}
