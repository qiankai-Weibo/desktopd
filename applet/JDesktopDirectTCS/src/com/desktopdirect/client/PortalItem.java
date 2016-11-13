package com.desktopdirect.client;


public class PortalItem {
	
  // The folder the item belongs to
  protected Folders.Folder m_Folder;
  
  
  PortalItem() {
	  m_Folder = null;
  }
  
  public Folders.Folder getFolder() {
	  return m_Folder;
  }
  
  public void setFolder(Folders.Folder prmFolder) {
	  m_Folder = prmFolder;
  }

}
