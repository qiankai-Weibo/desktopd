package com.desktopdirect.client;

import org.w3c.dom.*;


public class Apps {
  public class App extends PortalItem {
	
	// The name identifying the application
	protected String         m_Name;
	
	// The application's description
	protected String         m_Desc;
	
	// The application's location (vary based on the type of the application)
	protected String         m_Location;
	
	// The application's window height and width
	protected int            m_Height;
	protected int            m_Width;
	
	App() {
      super();
	  m_Name = "";
	  m_Desc = "";
	  m_Height = 640;
	  m_Width = 480;
	}
	
	public String getName() {
		return m_Name;
	}
	
	public String getDesc() {
		return m_Desc;
	}
	
	public int getHeight() {
		return m_Height;
	}
	
	public int getWidth() {
		return m_Width;
	}
	
	public String getLocation() {
		return m_Location;
	}
	
	
  }
  
  public class TSApp extends App {
	  // Unique ID of the TS app
	  private String m_ID;
	  
	  // App's working directory
	  private String m_WorkDir;
	  
	  // The App icon updated tag
	  private String m_IconUpdatedTag;
	  
	  TSApp(Element prmNode, Folders.Folder prmRootFolder) {
		m_ID = "";
		if (prmNode.hasChildNodes()) {
	      m_ID = prmNode.getChildNodes().item(0).getNodeValue();
		}
	    m_Name = prmNode.getAttribute(ARTInterface.ATTR_APP_NAME);; //Html.fromHtml(prmNode.getAttribute(ARTInterface.ATTR_APP_NAME)).toString();
	    m_Desc = prmNode.getAttribute(ARTInterface.ATTR_APP_DESC);; //Html.fromHtml(prmNode.getAttribute(ARTInterface.ATTR_APP_DESC)).toString();
	    m_Location = prmNode.getAttribute(ARTInterface.ATTR_APP_LOC);
	    m_WorkDir = prmNode.getAttribute(ARTInterface.ATTR_APP_DIR);
	    m_Height = Integer.parseInt(prmNode.getAttribute(ARTInterface.ATTR_APP_HEIGHT));
	    m_Width = Integer.parseInt(prmNode.getAttribute(ARTInterface.ATTR_APP_WIDTH));
	    
	    m_IconUpdatedTag = prmNode.getAttribute(ARTInterface.ATTR_APP_ICON_UPDATED);
	    
	    m_Folder = Folders.BuildItemFolder(prmRootFolder, prmNode.getAttribute(ARTInterface.ATTR_APP_FOLDER), this);
	  }
	  
	  public String getID() {
		  return m_ID;
	  }
	  
	  public String getWorkDir() {
		  return m_WorkDir;
	  }
	  
	  public String getIconUpdatedTag() {
		  return m_IconUpdatedTag;
	  }
	  
  }
}
