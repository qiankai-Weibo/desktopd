package com.desktopdirect.tcs;

import java.io.*;
import java.net.*;
import java.util.*;

public class FCP_KeepAlive extends Thread {

private FatClientProxy owner;
private long delay;
private volatile boolean keepalive;

public volatile boolean need_keepalive;

public FCP_KeepAlive(FatClientProxy my_owner, long my_delay, boolean need_keepalive)
{
	this.owner = my_owner;
	this.delay = my_delay;
	this.keepalive = true;
	this.need_keepalive = need_keepalive;
}

public void run()
{
	while (this.keepalive) {
		this.need_keepalive = true;
		if (this.need_keepalive) {
			try {
				this.owner.keepalive();
			} catch (Exception ignored) {
			}
		}
		
		try {
			this.sleep(this.delay);
		} catch (Exception ignored) {
		}
	}
}

public synchronized void terminate()
{
	if (this.keepalive) {
		this.keepalive = false;
	}
}

public void destroy()
{
	this.terminate();
}

} //end class FCP_KeepAlive
