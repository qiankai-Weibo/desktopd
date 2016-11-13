package com.desktopdirect.tcs;

import java.io.*;
import java.net.*;
import java.util.*;

public class FCP_AcceptThreadAG extends Thread {

protected FatClientProxy owner;
protected ServerSocket server_sock;
protected Hashtable clients;
protected boolean terminated;
protected FCP_WinRedirEntryAG winredir_entry;

public int local_ip;
public int listen_port;
//public int remote_port;

public FCP_AcceptThreadAG() { } //do nothing

public FCP_AcceptThreadAG(FatClientProxy my_owner, String ip, ServerSocket sock, int port)
{
	try {
                
		this.owner = my_owner;
                //this.remote_port = prmRemotePort;
		this.local_ip = my_owner.bytes2int(InetAddress.getByName(ip).getAddress(), 0, 4);
		this.listen_port = 0;
		this.terminated = false;
		this.clients = new Hashtable(5, (float)0.75);
                this.winredir_entry = null;
			if (sock == null) {
				this.server_sock = new ServerSocket(0, 5,
						InetAddress.getByName(ip));
				this.listen_port = server_sock.getLocalPort();
			} else {
				this.server_sock = sock;
				this.listen_port = port;
			}
	} catch (Exception e) {
		this.listen_port = -1;
                //this.remote_port = -1;
	}
	if (this.listen_port == -1) {
		try {
			this.server_sock.close();
		} catch (Exception ignored) {
		}
	}
	//the calling function should check this.listen_port before calling start()
}

public void run()
{
	FCP_ClientThreadAG client;
	Socket client_sock;
        
        int count = 0;
	
	while (true) {
		try {
			print_message("Waiting for connection on "+this.listen_port+"\n", FatClientProxy.INFO_NOTICE);
			client_sock = this.server_sock.accept();
			client_sock.setTcpNoDelay(false);  //turn off Nagle's for localhost sockets
			client_sock.setSoLinger(false, 1); //immediate close for localhost sockets
			print_message("Got a connection on "+this.listen_port+"\n", FatClientProxy.INFO_NOTICE);
                        
                        int c = owner.nextAvailPort();
                        owner.m_Conns.put(new Integer(c), this);
                        count++;
                        
                        
                        owner.winredir_connect(winredir_entry.get_ip(), winredir_entry.get_port(), c);
                        
			client = new FCP_ClientThreadAG(this, client_sock, c);
			//client.setPriority(MAX_PRIORITY);
            client.mark_ready(true);
			if (client != null && client.remote_port > 0) {
				this.clients.put(new Integer(client.remote_port), client);
				//this.owner.client_connect(this.local_ip, this.listen_port, client.remote_port);
				client.start();
				print_message("Started a connection on "+this.listen_port+"\n", FatClientProxy.INFO_NOTICE);
			} else {
				print_message("Unable to accept a connection!\n", FatClientProxy.INFO_ERROR);
			}
		} catch (Exception e) {
			print_message("Stopped listening on port "+this.listen_port+": "
					+e.getMessage()+"\n", FatClientProxy.INFO_FAILURE);
			break;
		}
                
                if (count >32) {
                    break;
                }
	}
	this.terminate();
}

public synchronized void send_to_server(int remote_port, int len, byte[] b)
{
	//this.owner.send(this.local_ip, owner.INTERNAL_PORT, remote_port, len, b);
	this.owner.send(winredir_entry.get_ip(), winredir_entry.get_port(), remote_port, len, b);
}

public synchronized void setEntry(FCP_WinRedirEntryAG prmEntry) {
    winredir_entry = prmEntry;
}

public void mark_client_ready(int remote_port, boolean ready)
{
	FCP_ClientThreadAG client;

	client = (FCP_ClientThreadAG)this.clients.get(new Integer(remote_port));
	if (client != null) {
		client.mark_ready(ready);
	} else {
		print_message("Accept: Got ready for invalid client\n", FatClientProxy.INFO_WARNING);
	}
}


public FCP_ClientThreadAG get_client(int remote_port) {
	FCP_ClientThreadAG client;

	client = (FCP_ClientThreadAG)this.clients.get(new Integer(remote_port));
	if (client != null) {
		return client;
	} else {
		print_message("Accept: Got data for invalid client\n", FatClientProxy.INFO_WARNING);
	}
	
	return null;
}

public void send_to_client(int remote_port, int len, byte[] b)
{
	FCP_ClientThreadAG client;

	client = (FCP_ClientThreadAG)this.clients.get(new Integer(remote_port));
	if (client != null) {
		client.send(len, b);
	} else {
		print_message("Accept: Got data for invalid client\n", FatClientProxy.INFO_WARNING);
	}
}

public void close_client(int remote_port)
{
	FCP_ClientThreadAG client;

	client = (FCP_ClientThreadAG)this.clients.get(new Integer(remote_port));
	if (client != null) {
		client.close();
	} //otherwise this client was already closed
}

public synchronized void close_server(int remote_port)
{
	this.owner.close_server(this.local_ip, owner.INTERNAL_PORT, remote_port);
}

public void print_message(String msg, byte importance)
{
	this.owner.print_message(msg, importance);
}

public synchronized void remove(FCP_ClientThreadAG client)
{
	try {
		this.clients.remove(new Integer(client.remote_port));
	} catch (Exception e) {
		print_message("In Accept.remove: "+e.getMessage()+"\n", FatClientProxy.INFO_WARNING);
	}
}

public synchronized void terminate()
{
	FCP_ClientThreadAG client;
	if (!this.terminated) {
		try {
			this.server_sock.close();
		} catch (Exception ignored) {
		}
		try {
			Enumeration e;
			for (e = this.clients.keys(); e.hasMoreElements(); ) {
				client = (FCP_ClientThreadAG)this.clients.get(e.nextElement());
				client.terminate(); //calls remove()
			}
		} catch (Exception ignored) {
		}
		this.owner.removeAG(this);
		this.terminated = true;
	} else {
		print_message("Trying to close accept again!\n", FatClientProxy.INFO_WARNING);
	}
}

public boolean equals(FCP_AcceptThreadAG accept)
{
	return (accept != null &&
	        this.local_ip == accept.local_ip &&
	        this.listen_port == accept.listen_port);
}

public void destroy()
{
	this.terminate();
}

public int getListenPort() {
  return this.listen_port;
}

} //end class FCP_AcceptThread
