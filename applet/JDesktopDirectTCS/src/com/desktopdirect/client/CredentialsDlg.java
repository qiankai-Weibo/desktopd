package com.desktopdirect.client;

import java.awt.*;

import javax.swing.*;

import com.borland.jbcl.layout.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>Title: Java Vesion Citrix TCS</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ArrayNetworks, Inc.</p>
 *
 * @author Zhifeng Xia  Peng Huaping
 * @version 1.5
 */
public class CredentialsDlg extends JDialog implements ActionListener, KeyListener{
	private static final long serialVersionUID = -858964222166180378L;
	XYLayout MyXyLayout = new XYLayout();
	JLabel Username_Label = new JLabel();
	JLabel Password_Label = new JLabel();
	
	JButton OK_Button = new JButton();
	JButton Cancel_Button = new JButton();
	JTextField Username_Txt = new JTextField();
	
	JPasswordField Pwd_Txt = new JPasswordField();

	public static final int Result_OK = 1;
	public static final int Result_Cancel = 0;
	int m_result = Result_Cancel;
	
	//multilingual support
	private Locale currentLocale;
	
	//multilingual support
	
	public CredentialsDlg(Frame owner, String title, boolean modal
			) {
		super(owner, title, modal);
		
		
		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jbInit();
			pack();
			centerScreen();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public CredentialsDlg() {
		this(new Frame(), "VDI Authentication", false);
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(MyXyLayout);
		Username_Label.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
		Username_Label.setText("Username:");
		Password_Label.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
		Password_Label.setText("Password:");
		OK_Button.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
		OK_Button.setSelected(true);
		OK_Button.setText("OK");
		OK_Button.setDefaultCapable(true);
		OK_Button.addActionListener(this);
	
		Cancel_Button.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
		Cancel_Button.setText("Cancel");
		Cancel_Button.addActionListener(this);
		Username_Txt.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
		Username_Txt.setText("");
		Username_Txt.addKeyListener(this);
		
		this.setModal(true);
		this.setResizable(false);
		this.setTitle("");
		Pwd_Txt.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
		Pwd_Txt.setText("");		
		Pwd_Txt.addKeyListener(this);	
		MyXyLayout.setHeight(200);
		this.getContentPane().add(Username_Label,
				new XYConstraints(13, 24, -1, -1));
		
		this.getContentPane().add(Password_Label,
				new XYConstraints(13, 74, -1, -1));
		
		this.getContentPane().add(OK_Button,
				new XYConstraints(30, 109 + 40, 65, -1));
		
		this.getContentPane().add(Cancel_Button,
				new XYConstraints(144, 109 + 40, -1, -1));
		
		this.getContentPane().add(Username_Txt,
				new XYConstraints(75, 20, 168, -1));
		
		this.getContentPane().add(Pwd_Txt,
				new XYConstraints(75, 70, 168, -1));
		
		MyXyLayout.setWidth(260);
	}	
	
	// centers the dialog within the screen [1.1]
	//  (put that in the Frame/Dialog class)
	public void centerScreen() {
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
		setResizable(false);
		requestFocus();
	}

	public int showDialog() {
		setVisible(true);
		return m_result;
	}

	public String getUserName() {
		return Username_Txt.getText().trim();
	}

	public String getPasswd() {
		return String.valueOf(Pwd_Txt.getPassword()).trim();
	}

	public void OKButton_EnterKey() {		
		m_result = Result_OK;
		dispose();
	}

	public void CancelButton_EscKey() {				
		m_result = Result_Cancel;
		dispose();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OK")) {
			OKButton_EnterKey();
		} else if (e.getActionCommand().equals("Cancel")) {
			CancelButton_EscKey();
		}		
	}
	
	public void keyTyped(KeyEvent e) { }	
	public void keyReleased(KeyEvent e) { }
	public void keyPressed(KeyEvent e) { 		
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
    		OKButton_EnterKey();
    	} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
    		CancelButton_EscKey();
    	}
	}
	
}


