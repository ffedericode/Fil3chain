package cs.scrs.service.ip;


import cs.scrs.miner.models.IP;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;





/**
 * Classe che si occupa di gestire gli accessi alla lista IP
 *
 */

@Service
public class IPServiceImpl {

	private List<IP> ipList;

	
	/**
	 * 
	 */
	public IPServiceImpl() {

	}


	/**
	 * @return
	 */
	public List<IP> getIPList() {

		synchronized (ipList) {
			return new ArrayList<>(ipList);
		}
	}

	/**
	 * @param ips
	 */
	public void setAllIp(List<IP> ips) {

		synchronized (ipList) {
			ipList.clear();
			ipList.addAll(ips);
		}

	}

	/**
	 * @param ip
	 * @return
	 */
	public int indexOf(String ip) {

		synchronized (ipList) {
			for (int i = 0; i < ipList.size(); i++)
				if (ipList.get(i).getIp().equals(ip))
					return i;
			return -1;
		}
	}

	/**
	 * @param ip
	 */
	public void addIP(IP ip) {

		synchronized (ipList) {
			if (ipList.indexOf(ip.getIp()) < 0)
				ipList.add(ip);
		}
	}

	/**
	 * @param ip
	 */
	public void removeIP(IP ip) {

		synchronized (ipList) {
			int index = indexOf(ip.getIp());
			if (index >= 0)
				ipList.remove(index);
		}
	}

	
	/**
	 * @return the ipList
	 */
	private List<IP> getIpList() {
	
		return ipList;
	}

	
	/**
	 * @param ipList the ipList to set
	 */
	public void setIpList(List<IP> ipList) {
	
		this.ipList = ipList;
	}
	
}
