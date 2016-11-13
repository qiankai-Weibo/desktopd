package com.desktopdirect.tcs;

public class FCP_WinRedirEntryAG {

	protected int remote_port;
	protected int orig_port;
	protected int orig_ip;

public FCP_WinRedirEntryAG(int remote_port, int orig_port, int orig_ip)
{
	this.remote_port = remote_port;
	this.orig_port = orig_port;
	this.orig_ip = orig_ip;
}

public int get_ip() {
	return orig_ip;
}

public int get_port() {
	return orig_port;
}

public int get_id() {
	return remote_port;
}

public boolean equals(int remote_port)
{
	if (remote_port == this.remote_port) {
		return true;
	}
	return false;
}

public boolean equals(FCP_WinRedirEntryAG entry)
{
	if (entry.remote_port == this.remote_port) {
		return true;
	}
	return false;
}

public boolean greaterThan(FCP_WinRedirEntryAG entry)
{
	if (this.remote_port > entry.remote_port) {
		return true;
	}
	return false;
}

} //end class FCP_WinRedirEntry
