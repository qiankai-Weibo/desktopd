package com.desktopdirect.tcs;

public class FCP_WinRedirEntrySPX {

	protected int remote_port;
	protected int orig_port;
	protected int orig_ip;

public FCP_WinRedirEntrySPX(int remote_port, int orig_port, int orig_ip)
{
	this.remote_port = remote_port;
	this.orig_port = orig_port;
	this.orig_ip = orig_ip;
}

public byte[] encode()
{
	byte[] data = new byte[8];
	byte[] temp;
	//[action:2 bytes][orig_port:2 bytes][orig_ip:4 bytes]
	data[0] = 0;
	data[1] = FatClientProxySPX.WINREDIR_NEW_CONN;
	temp = FatClientProxy.int2bytes(this.orig_port, 2);
	data[2] = temp[0];
	data[3] = temp[1];
	temp = FatClientProxy.int2bytes(this.orig_ip, 4);
	data[4] = temp[0];
	data[5] = temp[1];
	data[6] = temp[2];
	data[7] = temp[3];

	return data;
}

public boolean equals(int remote_port)
{
	if (remote_port == this.remote_port) {
		return true;
	}
	return false;
}

public boolean equals(FCP_WinRedirEntrySPX entry)
{
	if (entry.remote_port == this.remote_port) {
		return true;
	}
	return false;
}

public boolean greaterThan(FCP_WinRedirEntrySPX entry)
{
	if (this.remote_port > entry.remote_port) {
		return true;
	}
	return false;
}

} //end class FCP_WinRedirEntry
