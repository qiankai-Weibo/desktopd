/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.desktopdirect.client;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 *
 * @author chenyue
 */
public class CfgManager {
	
	// DD 3.5+ XML configuration attributes
	protected static final String XML_SET_RDP_AGENT = "rdpagent";
	protected static final String XML_SET_RDP_PROXY = "rdpagentproxy";
	protected static final String XML_SET_CITRIX_CLIENT = "citrixclient";
	protected static final String XML_SET_CITRIX_PROXY = "citrixclientproxy";
	protected static final String XML_SET_CUSTOM_DEST = "customdest";
	protected static final String XML_SET_POWERMNG = "powermng";
	protected static final String XML_SET_SCREEN_SIZE = "screensize";
	protected static final String XML_SET_COLOR_DEPTH = "colordepth";
	protected static final String XML_SET_BITMAP_CACHE = "bitmapcache";
	protected static final String XML_SET_DESKTOP_WALLPAPER = "desktopwall";
	protected static final String XML_SET_FULL_WIN_DRAG = "fullwindowdrag";
	protected static final String XML_SET_MENU_ANIM = "menuanim";
	protected static final String XML_SET_THEMES = "themes";
	protected static final String XML_SET_SSO = "sso";
	protected static final String XML_SET_DOMAIN = "domain";
	protected static final String XML_SET_IDLE_ALERT = "idlealert";
	protected static final String XML_SET_LIFETIME_ALERT = "lifetimealert";
	protected static final String XML_SET_CONSOLE = "console";
	

    //list<private String >  m_Plugins;//do we need this on iphone??????

    private String m_TSAgentURL;//do we need this on iphone??????
    private String m_TSAgentVersion;//do we need this on iphone??????
    private String m_Domain;
    private String m_AgentProxy;
    private String m_DefHost;
    private String m_ARPServerWithPort;
    private String m_ARPServerAdd;
    private String m_InstID;

    private int m_ARPServerPort;
    private int m_DefHeight;
    private int m_DefWidth;
    private int m_DefSize;
    private int m_DefColorDepth;

    private boolean m_SSO;

    private boolean m_RedirDrives;
    private boolean m_RedirPrinters;
    private boolean m_RedirPorts;
    private boolean m_RedirSmartCards;
    private boolean m_RedirClipboard;
    private boolean m_RedirPOS;
    private boolean m_AllowRedirConfig;
    private boolean m_FilesStoredLocally;
    private boolean m_ARPEnabled;
    private boolean m_AllowCustomDest;
    private boolean m_StartupOptions;
    private boolean m_AutoStart;
    private boolean m_AutoClose;
    private boolean m_HideOnConnect;
    private boolean m_AllowConnectToConsole;
    private boolean m_EnableDefaultGUI;
    private boolean m_EnablePowerMng;
    private boolean m_DisableBitmapCache;
    private boolean m_DisableWallpaper;
    private boolean m_DisableFullWindowDrag;
    private boolean m_DisableMenuAnim;
    private boolean m_DisableTheme;

    private int     m_IdleAlert;
    private int     m_LifetimeAlert;

    private boolean m_AutoLaunchSingleDesktop;
    private boolean m_AutoLogoutSingleDesktop;
    private int     m_RDPPort;

    private String m_SPX;
    
    private String m_OrigXML = "";

    public CfgManager(String prmCfgStr){
    	init();
    	parseConfig(prmCfgStr);
    }
    
    public CfgManager(){
    	init();
    }
    
    
    private void init() {

	m_TSAgentURL = null;
        m_TSAgentVersion = null;
        m_Domain = null;
	m_AgentProxy = null;
	m_DefHost = null;
	m_ARPServerWithPort = "127.0.0.1:9090";
	m_ARPServerAdd = "127.0.0.1";
	m_InstID = "default";

	m_DefHeight = -1;
    m_DefWidth = -1;
        //m_DefSize = SIZE_FULL;
        //m_DefColorDepth = COLOR_8;

	m_SSO = false;
        m_RedirDrives = false;
        m_RedirPrinters = false;
        m_RedirPorts = false;
        m_RedirSmartCards = false;
        m_RedirClipboard = false;
        m_RedirPOS = false;
        m_AllowRedirConfig = false;

        m_FilesStoredLocally = false;
        m_ARPEnabled = false;
        m_AllowCustomDest = false;

        m_StartupOptions = false;
        m_AutoStart = false;
        m_AutoClose = false;
        m_HideOnConnect = false;
        m_AllowConnectToConsole = false;
        m_EnableDefaultGUI = false;
        m_EnablePowerMng = false;

        m_DisableBitmapCache = true;
        m_DisableWallpaper = true;
        m_DisableFullWindowDrag = true;
        m_DisableMenuAnim = true;
        m_DisableTheme = true;

        m_IdleAlert = 0;
        m_LifetimeAlert = 0;

        m_AutoLaunchSingleDesktop = false;
        m_AutoLogoutSingleDesktop = false;
        m_RDPPort = 3389;

	    m_ARPServerPort = 9090;
        m_SPX = "";
    }

    public boolean parseConfig(String prmCfgStr){
      String cfg;
      int i;
      System.out.println("enter parseConfig string");
      System.out.println(prmCfgStr);
      if (prmCfgStr.charAt(0) == '*') {
        cfg = prmCfgStr.substring(1);
      } else {
        cfg = UnMaskCfg(prmCfgStr);
      }
      System.out.println(prmCfgStr);

      //1 AgentInfo (ignored)
	
      i = cfg.indexOf(";");
      if (i > -1) {
        cfg = cfg.substring(i+1);
      }

      //2 default SSO domain
      i = cfg.indexOf(";");
      if (i > -1) {
        if (i > 0) {
          m_Domain = cfg.substring(0, i);
        }
        cfg = cfg.substring(i+1);
      }

      //3 sso enable\disable
      i = cfg.indexOf(";");
      if (i > -1) {
        m_SSO = cfg.substring(0, i).equals("1");
        cfg = cfg.substring(i+1);
      }

     //4 m_DefSize, rdp window size, 0-4
     i = cfg.indexOf(";");
     if (i > -1) {
        try {
          m_DefSize = new Integer(cfg.substring(0, i)).intValue();
        } catch (Exception e) {
          m_DefSize = 0;
        }
        cfg = cfg.substring(i+1);
     }

     //5 m_DefWidth, rdp window width
     i = cfg.indexOf(";");
     if (i > -1) {
        try {
          m_DefWidth = new Integer(cfg.substring(0, i)).intValue();
        } catch (Exception e) {
          m_DefWidth = 0;
        }
        cfg = cfg.substring(i+1);
     }

     //6 m_DefHeight, rdp window height
     i = cfg.indexOf(";");
     if (i > -1) {
        try {
          m_DefHeight = new Integer(cfg.substring(0, i)).intValue();
        } catch (Exception e) {
          m_DefHeight = 0;
        }
        cfg = cfg.substring(i+1);
     }

     //7 m_DefColorDepth
     i = cfg.indexOf(";");
      if (i > -1) {
        try {
          m_DefColorDepth = new Integer(cfg.substring(0, i)).intValue();
        } catch (Exception e) {
          m_DefColorDepth = 0;
        }
        cfg = cfg.substring(i+1);
     }

     //8
     i = cfg.indexOf(";");
     if (i > -1) {
        m_AllowRedirConfig = cfg.charAt(0) == '1';
        m_RedirDrives = cfg.charAt(1) == '1';
        m_RedirPrinters = cfg.charAt(2) == '1';
        m_RedirPorts = cfg.charAt(3) == '1';
        m_RedirSmartCards = cfg.charAt(4) == '1';
        m_RedirClipboard = cfg.charAt(5) == '1';
        if ((cfg.charAt(6) == '1') || (cfg.charAt(6) == '0')) {
          m_RedirPOS = cfg.charAt(6) == '1';
        }
        cfg = cfg.substring(i+1);
     }
   
     //9 plugins
     //not apply for mac os
     i = cfg.indexOf(";");
     if (i > -1) {
         cfg = cfg.substring(i+1);
     }
	
     //10 m_AgentProxy
     //not apply for MacOS
     i = cfg.indexOf(";");
      if (i > -1) {
        if (i > 0) {
          m_AgentProxy = cfg.substring(0, i);
        }
        cfg = cfg.substring(i+1);
     }

     //11 default host (deprcated)
     i = cfg.indexOf(";");
     if (i > -1) {
        if (i > 0) {
          m_DefHost = cfg.substring(0, i);
        }
        cfg = cfg.substring(i+1);
     }

     //12 enable art server
     i = cfg.indexOf(";");
     if (i > -1) {
        m_ARPEnabled = cfg.charAt(0) == '1';
        m_AllowCustomDest = cfg.charAt(1) == '1';
        cfg = cfg.substring(i+1);
     }

     
     //13 art server address, previous use "arp", I keep it same as delphi code
     i = cfg.indexOf(";");
     if (i > -1) {
        if (m_ARPEnabled){
        	SetARPServerWithPort(cfg.substring(0, i));
            /*m_ARPServerWithPort = cfg.substring(0, i);
            System.out.println(m_ARPServerWithPort);
            m_ARPServerAdd = m_ARPServerWithPort.substring(0, m_ARPServerWithPort.indexOf(':'));
            System.out.println(m_ARPServerAdd);
            m_ARPServerPort = Integer.parseInt(m_ARPServerWithPort.substring(m_ARPServerWithPort.indexOf(':')+1, i));
            System.out.println(m_ARPServerPort);*/

        }
        cfg = cfg.substring(i+1);
     }

     //14 m_StartupOption (deprecated)
     i = cfg.indexOf(";");
      if (i > -1) {

        m_StartupOptions = cfg.charAt(0) == '1';
        m_AutoStart = cfg.charAt(1) == '1';
        m_AutoClose = cfg.charAt(2) == '1';
        m_HideOnConnect = cfg.charAt(3) == '1';
      
        cfg = cfg.substring(i+1);
     }

     //15 m_AllowConnectToConsole
     i = cfg.indexOf(";");
     if (i > -1) {
        m_AllowConnectToConsole = cfg.charAt(0) == '1';
        cfg = cfg.substring(i+1);
     }

     //16 m_EnableDefaultGUI		m_EnablePowerMng
     i = cfg.indexOf(";");
     if (i > -1) {
        m_EnableDefaultGUI = cfg.charAt(0) == '1';
        m_EnablePowerMng = cfg.charAt(1) == '1';
        cfg = cfg.substring(i+1);
     }

     //17 the preformace tab page
     //the default values are true, it means that they are disabled by default
     i = cfg.indexOf(";");
     if (i > -1) {
          m_DisableBitmapCache = cfg.charAt(0) == '1';
          m_DisableWallpaper = cfg.charAt(1) == '1';
          m_DisableFullWindowDrag = cfg.charAt(2) == '1';
          m_DisableMenuAnim = cfg.charAt(3) == '1';
          m_DisableTheme = cfg.charAt(4) == '1';
          
          cfg = cfg.substring(i+1);
     }

     //18 Idle timeout alert
     i = cfg.indexOf(";");
     if (i > -1) {
         try {
           m_IdleAlert = Integer.parseInt(cfg.substring(0, i));
         } catch (Exception e) {

         }
         cfg = cfg.substring(i+1);
     }
       
     //19 Lifetime timeout alert
     i = cfg.indexOf(";");
     if (i > -1) {
         try {
           m_LifetimeAlert = Integer.parseInt(cfg.substring(0, i));
         } catch (Exception e) {

         }
         cfg = cfg.substring(i+1);
     }

	

     //20 ART Instance
     i = cfg.indexOf(";");
     if (i > -1) {
         if (cfg.substring(0, i).length() > 0) {
           m_InstID = cfg.substring(0, i);
         }
         cfg = cfg.substring(i+1);
     }

     //21 auto-start options
     i = cfg.indexOf(";");
     if (i > -1) {
         m_AutoLaunchSingleDesktop = cfg.charAt(0) == '1';
          m_AutoLogoutSingleDesktop = cfg.charAt(1) == '1';

          cfg = cfg.substring(i+1);
     }

     // 22 Default RDP port
     i = cfg.indexOf(";");
     if (i > -1) {
         try {
           m_RDPPort = Integer.parseInt(cfg.substring(0, i));
         } catch (Exception e) {
           m_RDPPort = 3389;
         }
         cfg = cfg.substring(i+1);
     }

      
        
      return true;
    }
    
    
    public boolean parseXMLConfig(Element prmDoc) {
    	
    	NodeList nodes;
    	String st;
    	int i;
    	System.out.println("enter parseXMLConfig element");
        nodes = prmDoc.getElementsByTagName(XML_SET_CUSTOM_DEST);
        if (nodes.getLength() > 0) {
        	m_AllowCustomDest = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_POWERMNG);
        if (nodes.getLength() > 0) {
        	m_EnablePowerMng = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_SCREEN_SIZE);
        if (nodes.getLength() > 0) {
        	st = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0));
        	i = st.indexOf("X");
        	if (i > -1) {
        	  String wst, hst;
        	  wst = st.substring(0,i);
        	  hst = st.substring(i+1);
        	  try {
        	    m_DefWidth = Integer.parseInt(wst);
        	    m_DefHeight = Integer.parseInt(hst);
        	    System.out.println("resolution width is "+wst +",height is "+hst);
        	  } catch (Exception e) {
        		  m_DefWidth = 0;
        		  m_DefHeight = 0;
        	  }
        	  
        	  if ((m_DefWidth == 0) && (m_DefHeight == 0)) {
        		  // full size
        		  m_DefSize = 0; 
        	  } else {
        		  // Custom
        		  m_DefSize = 4;
        	  }
        	}
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_COLOR_DEPTH);
        if (nodes.getLength() > 0) {
        	try {
        		i = Integer.parseInt(ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)));
       	    } catch (Exception e) {
        		i = 0;
        	}
       	    
       	    switch (i) {
       	     case 8:
       	    	 m_DefColorDepth = 1;
       	    	 break;
       	    	 
       	     case 16:
       	    	 m_DefColorDepth = 2;
      	    	 break;
      	    	 
       	     case 24:
       	    	 m_DefColorDepth = 3;
      	    	 break;
                 
             case 32:
                 m_DefColorDepth = 4;
                 break;
      	    	 
       	     default:
       	    	 m_DefColorDepth = 0;
       	    }
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_BITMAP_CACHE);
        if (nodes.getLength() > 0) {
        	m_DisableBitmapCache = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("0");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_DESKTOP_WALLPAPER);
        if (nodes.getLength() > 0) {
        	m_DisableWallpaper = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_FULL_WIN_DRAG);
        if (nodes.getLength() > 0) {
        	m_DisableFullWindowDrag = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_MENU_ANIM);
        if (nodes.getLength() > 0) {
        	m_DisableMenuAnim = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_THEMES);
        if (nodes.getLength() > 0) {
        	m_DisableTheme = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_SSO);
        if (nodes.getLength() > 0) {
        	m_SSO = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        	System.out.println("CfgManager m_SSO is "+m_SSO);
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_DOMAIN);
        if (nodes.getLength() > 0) {
        	m_Domain = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0));
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_IDLE_ALERT);
        if (nodes.getLength() > 0) {
        	try {
        	  m_IdleAlert = Integer.parseInt(ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)));
        	} catch (Exception e) {
        	}
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_LIFETIME_ALERT);
        if (nodes.getLength() > 0) {
        	try {
        	  m_LifetimeAlert = Integer.parseInt(ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)));
        	} catch (Exception e) {
        	}
        }
        
        nodes = prmDoc.getElementsByTagName(XML_SET_CONSOLE);
        if (nodes.getLength() > 0) {
        	m_AllowConnectToConsole = ARTInterface.getCharacterDataFromElement((Element)nodes.item(0)).equals("1");
        }
        
    	return true;
    }
    
    public boolean parseXMLConfig(String prmDoc) {
    	
    	System.out.println("enter parseXMLConfig string");
    	try {
    		
    		ByteArrayInputStream is = new ByteArrayInputStream(prmDoc.getBytes());
    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    		DocumentBuilder db = dbf.newDocumentBuilder();
    		Document doc = db.parse(is);
    		
    		NodeList nodes = doc.getElementsByTagName(ARTInterface.ART_SETTINGS);
        	if (nodes.getLength() > 0) {
        		return parseXMLConfig((Element) nodes.item(0));
        	} 
        } catch (Exception e) {
        	
        }
    	
    	return false;
    }
    
    private String MaskCfg(String prmCfgStr){
        return "";
    }
    private String UnMaskCfg(String prmCfgStr){

        String res = "";
        char ch;
        int i;

        for(i=0; i<prmCfgStr.length(); i++){
            switch(prmCfgStr.charAt(i)){
                case '-':
                case '.':
                case '[':
                case ']':
                    res = res+prmCfgStr.charAt(i);
                    break;
                default:
                    ch = (char)(prmCfgStr.charAt(i)-1);
                    res = res+ch;
                    break;
            }

        }


        return res;
    }
    private void ExtractAgentInfo(String prmInfo){

    }

    private int getIntValue(String prmCfgStr, int prmLength){
        return 0;

    }


    public String GetTSAgentURL(){
        return "";
    }
    public String GetTSAgentVersion(){

        return "";
    }
    public void SetDomain(String  prmDomain){

    }
    public String GetDomain(){
        return m_Domain;
    }
    public void SetSSO(boolean prmSSO){


    }
    public boolean GetSSO(){
        return m_SSO;
    }
    public void SetDefHeight(int prmHeight){
        m_DefHeight = prmHeight;
    }
    public int GetDefHeight(){
        return m_DefHeight;
    }
    public void SetDefWidth(int prmWidth){
        m_DefWidth = prmWidth;
    }
    public int GetDefWidth(){
        return m_DefWidth;
    }
    public void SetDefSize(int prmSize){
        m_DefSize = prmSize;
    }
    public int GetDefSize(){
        return m_DefSize;
    }
    public void SetDefColorDepth(int prmDepth){
        m_DefColorDepth = prmDepth;
    }
    public int GetDefColorDepth(){
        return m_DefColorDepth;
    }
    public void SetRedir(boolean prmDrives, boolean prmPrinters, boolean prmPorts, boolean prmSmartCards, boolean prmClipboard){
    	m_RedirPrinters= prmPrinters;
    	m_RedirDrives = prmDrives;
    	m_RedirPorts=prmPorts;
    	m_RedirClipboard=prmClipboard;
    	m_RedirSmartCards=prmSmartCards;
    }
    public boolean GetRedirDrives(){
        return m_RedirDrives;
    }
    public boolean GetRedirPrinters(){
    	//System.out.println("CfgManager GetRedirPrinters is "+m_RedirPrinters);
        return m_RedirPrinters;
    }
    public boolean GetRedirPorts(){
        return m_RedirPorts;
    }
    public boolean GetRedirSmartCards(){
        return m_RedirSmartCards;
    }
    public boolean GetRedirClipboard(){
        return m_RedirClipboard;
    }
    public boolean GetRedirPOS() {
        return m_RedirPOS;
    }
    public void SetAllowRedirConfig(boolean prmAllow){

    }
    public boolean GetAllowRedirConfig(){
        return m_AllowRedirConfig;
    }

    public int GetPluginsCount(){

        return 0;
    }
    public String GetPluginName(int prmIndex){
        return "";
    }
    public boolean IsPluginReq(int prmIndex){
        return false;
    }
    public String  GetAgentProxy(){
        return m_AgentProxy;
    }
    public boolean FilesStoredLocally(){
        return false;
    }
    public String  GetDefHost(){
        return "";
    }
    public boolean ARPEnabled(){
        return m_ARPEnabled;
    }
    public boolean AllowCustomDest(){
        return m_AllowCustomDest;
    }

    public String  GetARPServerWithPort(){
        return m_ARPServerWithPort;
    }
    public String  GetARPServerAdd(){
        return m_ARPServerAdd;
    }
    public void SetARPServerAdd(String  prmNewARPadd){
        m_ARPServerAdd = prmNewARPadd;
    }
    public int GetARPServerPort(){
        return m_ARPServerPort;
    }
    public void SetARPServerPort(int prmNewARPPort){
        m_ARPServerPort = prmNewARPPort;
    }

    public void SetARPServerWithPort(String  prmNewARPaddWithPort){
        m_ARPServerWithPort = prmNewARPaddWithPort;
        m_ARPServerAdd = m_ARPServerWithPort.substring(0, m_ARPServerWithPort.indexOf(':'));
        m_ARPServerPort = Integer.parseInt(m_ARPServerWithPort.substring(m_ARPServerWithPort.indexOf(':')+1));
    }

    public boolean GetStartupoptions(){
        return false;
    }
    public boolean GetAutoStart(){
        return false;
    }
    public boolean GetAutoClose(){
        return false;
    }
    public boolean GetHideOnConnect(){
        return false;
    }
    public boolean GetAllowConnectToConsole(){
        return m_AllowConnectToConsole;
    }
    public boolean GetEnableDefaultGUI()//use default GUI
    {
        return m_EnableDefaultGUI;
    }
    public boolean GetEnablePowerMng()//wake up
    {
        return m_EnablePowerMng;
    }
    public boolean GetBitmapCache(){
        return m_DisableBitmapCache;
    }
    public boolean GetWallpaper(){
        return m_DisableWallpaper;
    }
    public boolean GetFullWindowDrag(){
        return m_DisableFullWindowDrag;
    }
    public boolean GetMenuAnim(){
        return m_DisableMenuAnim;
    }
    public boolean GetTheme (){
        return m_DisableTheme;
    }

    public String  GetInstID(){
        return m_InstID;
    }
    
    public void SetInstID(String prmInstID) {
    	m_InstID = prmInstID;
    }

    public int GetRDPPort() {
        return m_RDPPort;
    }
    
    public void SetRDPPort(int prmPort) {
    	m_RDPPort = prmPort;
    }

    public void setSPX(String prmSPX) {
        m_SPX = prmSPX;
    }

    public String getSPX() {
        return m_SPX;
    }

    public void setOrigXML(Document prmDoc) {
    	try 
        { 
           DOMSource domSource = new DOMSource(prmDoc); 
           StringWriter writer = new StringWriter(); 
           StreamResult result = new StreamResult(writer); 
           TransformerFactory tf = TransformerFactory.newInstance(); 
           Transformer transformer = tf.newTransformer(); 
           transformer.transform(domSource, result); 
           writer.flush();
           m_OrigXML = writer.toString();
        } 
        catch(TransformerException ex) 
        { 
           ex.printStackTrace(); 
        }
    }
    
    public String getOrigXML() {
    	return m_OrigXML;
    }

}
