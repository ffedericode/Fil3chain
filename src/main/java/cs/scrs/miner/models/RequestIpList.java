package cs.scrs.miner.models;

import java.util.Iterator;
import java.util.List;

public class RequestIpList {
	/**
	 * 
	 */
	public RequestIpList() {
		super();
	}

	private List<String> ips;

	/**
	 * @param ips
	 */
	public RequestIpList(List<String> ips) {
		super();
		this.ips = ips;
	}

	
	/**
	 * @return the ips
	 */
	public List<String> getIps() {
	
		return ips;
	}

	
	/**
	 * @param ips the ips to set
	 */
	public void setIps(List<String> ips) {
	
		this.ips = ips;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ip;
		String toString = "RequestIpList [ips=";
		Iterator<String> resourceIterator = ips.iterator();
		while (resourceIterator.hasNext()) {
			ip = resourceIterator.next();
			toString+= ip.toString()+"\n";
		} 
		toString += "]";
		return toString;
	}
	
	
	
}
