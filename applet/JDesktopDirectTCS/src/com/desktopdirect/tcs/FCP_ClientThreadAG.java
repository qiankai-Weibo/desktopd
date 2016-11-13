package com.desktopdirect.tcs;

import java.io.*;
import java.net.*;
import java.util.*;

public class FCP_ClientThreadAG extends Thread {

protected Socket sock;
protected InputStream in;
protected OutputStream out;

protected FCP_AcceptThreadAG owner;
protected volatile boolean ready;
protected boolean being_closed;
protected boolean terminated;
protected boolean connection_ready = false;
protected byte[] data;

public int remote_port;

public FCP_ClientThreadAG() { } //do nothing

public FCP_ClientThreadAG(FCP_AcceptThreadAG my_owner, Socket client_sock, int prmRemotePort)
{
	try {
		this.ready = false;
		this.being_closed = false;
		this.terminated = false;
		this.owner = my_owner;
		this.sock = client_sock;
		this.in = this.sock.getInputStream();
		this.out = this.sock.getOutputStream();
		this.remote_port = prmRemotePort;
		this.connection_ready = (this.owner.owner.m_AddConnResultIn.read() == 1);
	} catch (Exception e) {
		this.remote_port = -1;
		this.owner.print_message("Couldn't start a client: "+e.getMessage()+"\n", FatClientProxy.INFO_FAILURE);
	}
	this.data = new byte[FatClientProxy.MAX_LOCAL_MSS];
	if (this.data == null) {
		this.remote_port = -1;
		this.owner.print_message("Couldn't start a client: Insufficient Memory\n", FatClientProxy.INFO_FAILURE);
	}
	if (this.remote_port == -1) {
		try {
			this.sock.close();
		} catch (Exception ignored) {
		}
	}
	//the calling function should check this.remote_port before calling start()
}

public void mark_ready(boolean ready)
{
	this.ready = ready;
}

public void run()
{
	int len = 0;
	int iterations = 0;

	//wait until the backend connection is ready
	while (!this.ready) {
		try { this.sleep((long)100); }
		catch (Exception ignored) { }
	}
	//read and forward
	while (len >= 0) {
		//don't starve other threads
		iterations++;
		if (iterations > 10) {
			this.yield();
			iterations = 0;
		}
		
		try {
			len = this.in.read(this.data, 0, this.data.length);
		} catch (Exception e) {
			this.owner.print_message("Client Read: "+e.getMessage()+"\n", FatClientProxy.INFO_WARNING);
			break;
		}
		if ((len > 0)&&(connection_ready)) {
			this.owner.send_to_server(this.remote_port, len, this.data);
			//this.owner.print_message("Sending: "+len+" bytes\n", FatClientProxy.INFO_NOTICE);
		}
	}
	this.terminate();
}

public void send(int len, byte[] b)
{
	try {
		if (len > 0) {
			this.out.write(b, 0, len);
			this.out.flush();
			//this.owner.print_message("Receiving: "+len+" bytes\n", FatClientProxy.INFO_NOTICE);
		}
	} catch (Exception e) {
		this.owner.print_message("Client Send: "+e.getMessage()+"\n", FatClientProxy.INFO_WARNING);
		this.terminate();
	}
}

public void close()
{
	this.being_closed = true;
	this.terminate();
}

public synchronized void terminate()
{
	if (!this.terminated) {
		try {
			this.sock.close();
		} catch (Exception ignored) {
		}
		this.owner.remove(this);
		if (!this.being_closed) {
			this.owner.close_server(this.remote_port);
		}
		this.terminated = true;
	} else {
		this.owner.print_message("Tried to close client again!\n", FatClientProxy.INFO_WARNING);
	}
}

public boolean equals(FCP_ClientThreadAG client)
{
	return (client != null && this.remote_port == client.remote_port);
}

public void destroy()
{
	this.terminate();
}

} //end class FCP_ClientThread
