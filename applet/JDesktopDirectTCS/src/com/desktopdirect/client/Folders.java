package com.desktopdirect.client;

import java.util.ArrayList;

public class Folders {
  public class Folder extends PortalItem {
	  
	  // The folder's name
	  private String         m_Name;
	  
	  // List of all portal items under the folder 
	  private ArrayList      m_Items;
	  private ArrayList		 m_Apps; 	
	  
	  Folder(String prmName, Folder prmParent) {
		  super();
		  m_Name = prmName;
		  m_Items = new ArrayList();
		  m_Folder = prmParent;
		  m_Apps = new ArrayList();
	  }
	  
	  public String getName() {
		  return m_Name;
	  }
	  
	  public void addItem(Object prmItem) {
		  m_Items.add(prmItem);
	  }
	  
	  public void addApp(Object prmItem) {
		  m_Apps.add(prmItem);
	  } 
	  
	  public ArrayList getItems() {
		  return m_Items;
	  }
	  
	  public ArrayList getApps() {
		  return m_Apps;
	  }
	  
	  public Folder getParent() {
		  return m_Folder;
	  }
  }
 
    

static public Folder BuildItemFolder(Folder prmRootFolder, String prmFolderStr, PortalItem prmItem) {
	  int i;
	  Folder res = prmRootFolder;
	  
	  if (prmFolderStr.length() == 0) {
		  prmRootFolder.addApp(prmItem);
		  return res;
	  }
	  
	  String[] lst = prmFolderStr.split("\\\\");
	  
	  Folder nf = null;
	  
	  for (i = 0; i < lst.length; i++) {
          int f;
		  ArrayList items = res.getItems(); 
		  for (f = 0; f < items.size(); f++) {
			  if (items.get(f) instanceof Folders.Folder) {
				  if (((Folder)items.get(f)).getName().equalsIgnoreCase(lst[i])) {
					  nf = (Folder)items.get(f);
					  break;
				  }
			  }
		  }  
		  
  	      if (nf == null) {
  	    	  Folders folders = new Folders();
  	    	  nf = folders.new Folder(lst[i], res);
	    	  res.addItem(nf);
	      } 
	    
	      res = nf;
	  }
	  
	  if (nf != null) {
	    nf.addApp(prmItem);
	  }
	  
	  return res;
  }
  
}
