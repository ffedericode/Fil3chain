package cs.scrs.service.connection;


import com.google.common.reflect.TypeToken;
import cs.scrs.config.network.Network;
import cs.scrs.miner.models.IP;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.request.AsyncRequest;
import cs.scrs.service.util.Conversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


@Service("ConnectionServiceImpl")
public class ConnectionServiceImpl {

	private String ipEntryPoint;
	private String portEntryPoint;
	private String entryPointBaseUri;
	private String poolDispatcherBaseUri;
	private String actionConnect;
	private String actionDisconnect;
	private String actionKeepAlive;

	private IP ip;
	private static final String IP_REGEX = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	@Autowired
	private AsyncRequest asyncRequest;
	@Autowired
	private IPServiceImpl ipService;
	@Autowired
	private Conversions conversionsService;
	@Autowired
	private Network networkProperties;


	public ConnectionServiceImpl() {

	}
	@PostConstruct
	public void init(){
		// impostazioni valori delle entità di rete
		this.selectIp();
		this.setIpEntryPoint(networkProperties.getEntrypoint().getIp());
		this.setPortEntryPoint(networkProperties.getEntrypoint().getPort());
		this.setEntryPointBaseUri(networkProperties.getEntrypoint().getBaseUri());
		this.setPoolDispatcherBaseUri(networkProperties.getPooldispatcher().getBaseUri());
		this.setActionConnect(networkProperties.getActions().getConnect());
		this.setActionDisconnect(networkProperties.getActions().getDisconnect());
		this.setActionKeepAlive(networkProperties.getActions().getKeepAlive());
	}

	//@PostConstruct
	//	private void initalizeConnectionServiceImpl() {
	//		this.selectIp();
	//		this.loadNetworkConfig();
	//		this.firstConnectToEntryPoint();
	//	}
	/**
	 * Permette di selezionare l'IP da utilizzare per la sessione corrente tramite un dialog.
	 *
	 * @return
	 */
	public void selectIp() {

		ArrayList<String> ips = getAllIpAddresses();
		if (ips == null || ips.size()<1) {
			System.err.println("Non sei connesso a nessuna rete. oppure non sei connesso alla VPN");

		}else{

			//			String input = (String) JOptionPane.showInputDialog(null, "Scegli il tuo indirizzo IP", "Lista IP", JOptionPane.QUESTION_MESSAGE, null, // Use
			//					// default
			//					// icon
			//					ips.toArray(), // Array of choices
			//					ips.get(0)); // Initial choice
			if(ips.size()>0)
				for (Iterator<String> iterator = ips.iterator(); iterator.hasNext();) {
					String string = (String) iterator.next();
					if(string.startsWith("10.192."))
						this.ip= new IP(string);
				}
		}
	}

	/**
	 * @return
	 */
	public ArrayList<String> getAllIpAddresses() {

		ArrayList<String> ips = new ArrayList<>();

		try {
			Enumeration<?> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<?> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					if (i.getHostAddress().matches(IP_REGEX)) {
						ips.add(i.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		}

		return ips;
	}

	/*
	 * public void loadMinerConfiguration() { // Carica la configurazione Properties prop = new Properties(); InputStream in = Miner.class.getResourceAsStream("/miner.properties"); try { prop.load(in); // Imposta il timeout blockChain.setnBlockUpdate(Integer.parseInt(prop.getProperty( "nBlockUpdate", "10"))); } catch (IOException e) { e.printStackTrace(); } }


	public void loadNetworkConfig() {

		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/network.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// impostazioni valori delle entità di rete
		this.setIpEntryPoint(prop.getProperty("ipEntryPoint"));
		this.setPortEntryPoint(prop.getProperty("portEntryPoint"));
		this.setEntryPointBaseUri(prop.getProperty("entryPointBaseUri"));
		this.setPoolDispatcherBaseUri(prop.getProperty("poolDispatcherBaseUri"));
		this.setActionConnect(prop.getProperty("actionConnect"));
		this.setActionDisconnect(prop.getProperty("actionDisconnect"));
		this.setActionKeepAlive(prop.getProperty("actionKeepAlive"));

		// inizializzazone
		//filechain.initializeFilechain();

	}
	 */

	/**
	 * @return @throws SocketException
	 */
	@SuppressWarnings("unchecked")
	public boolean firstConnectToEntryPoint() {

		String url = "http://" + this.getIpEntryPoint() + ":" + this.getPortEntryPoint() + this.getEntryPointBaseUri() + this.getActionConnect();
		String result = "";
		Integer counter = 0;
		System.out.println("asynk "+ asyncRequest.toString()+"<---");
		while (counter <= AsyncRequest.REQNUMBER) {
			try {
				System.out.println("URL: " + url);
				System.out.println("Il mio IP: " + ip);
				//				result = HttpUtil.doPost(url, "{\"user_ip\":\"" + this.getIp() + ":8080\"}");
				result = asyncRequest.getIpFromEntryPoint(url, ip);
				Type type = new TypeToken<ArrayList<String>>() {
				}.getType();
				List<String> ips = conversionsService.fromJson(result, type);
				ArrayList<IP> iplist = new ArrayList<>();
				if (ips != null && ips.size() != 0) {
					for (String ip : ips) {
						iplist.add(new IP(ip));
					}
				}

				ipService.setAllIp((List<IP>) iplist.clone());

				System.out.println("Numero di IP ottenuti: " + ipService.getIPList().size());
				ipService.getIPList().forEach(ip -> System.out.println(ip));

				return Boolean.TRUE;
			} catch (Exception ex) {
				System.err.println("Errore durante la richiesta di IP\n" + ex);
				counter++;
			}

			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * @return the ipEntryPoint
	 */
	public String getIpEntryPoint() {

		return ipEntryPoint;
	}

	/**
	 * @param ipEntryPoint
	 *            the ipEntryPoint to set
	 */
	public void setIpEntryPoint(String ipEntryPoint) {

		this.ipEntryPoint = ipEntryPoint;
	}

	/**
	 * @return the portEntryPoint
	 */
	public String getPortEntryPoint() {

		return portEntryPoint;
	}

	/**
	 * @param portEntryPoint
	 *            the portEntryPoint to set
	 */
	public void setPortEntryPoint(String portEntryPoint) {

		this.portEntryPoint = portEntryPoint;
	}

	/**
	 * @return the entryPointBaseUri
	 */
	public String getEntryPointBaseUri() {

		return entryPointBaseUri;
	}

	/**
	 * @param entryPointBaseUri
	 *            the entryPointBaseUri to set
	 */
	public void setEntryPointBaseUri(String entryPointBaseUri) {

		this.entryPointBaseUri = entryPointBaseUri;
	}

	/**
	 * @return the poolDispatcherBaseUri
	 */
	public String getPoolDispatcherBaseUri() {

		return poolDispatcherBaseUri;
	}

	/**
	 * @param poolDispatcherBaseUri
	 *            the poolDispatcherBaseUri to set
	 */
	public void setPoolDispatcherBaseUri(String poolDispatcherBaseUri) {

		this.poolDispatcherBaseUri = poolDispatcherBaseUri;
	}

	/**
	 * @return the actionConnect
	 */
	public String getActionConnect() {

		return actionConnect;
	}

	/**
	 * @param actionConnect
	 *            the actionConnect to set
	 */
	public void setActionConnect(String actionConnect) {

		this.actionConnect = actionConnect;
	}

	/**
	 * @return the actionDisconnect
	 */
	public String getActionDisconnect() {

		return actionDisconnect;
	}

	/**
	 * @param actionDisconnect
	 *            the actionDisconnect to set
	 */
	public void setActionDisconnect(String actionDisconnect) {

		this.actionDisconnect = actionDisconnect;
	}

	/**
	 * @return the actionKeepAlive
	 */
	public String getActionKeepAlive() {

		return actionKeepAlive;
	}

	/**
	 * @param actionKeepAlive
	 *            the actionKeepAlive to set
	 */
	public void setActionKeepAlive(String actionKeepAlive) {

		this.actionKeepAlive = actionKeepAlive;
	}


	/**
	 * @return the ip
	 */
	public IP getIp() {

		return ip;
	}


	/**
	 * @param ip the ip to set
	 */
	public void setIp(IP ip) {

		this.ip = ip;
	}


	/**
	 * @return the asyncRequest
	 */
	public AsyncRequest getAsyncRequest() {

		return asyncRequest;
	}


	/**
	 * @param asyncRequest the asyncRequest to set
	 */
	public void setAsyncRequest(AsyncRequest asyncRequest) {

		this.asyncRequest = asyncRequest;
	}


	/**
	 * @return the ipService
	 */
	public IPServiceImpl getIpService() {

		return ipService;
	}


	/**
	 * @param ipService the ipService to set
	 */
	public void setIpService(IPServiceImpl ipService) {

		this.ipService = ipService;
	}


	/**
	 * @return the conversionsService
	 */
	public Conversions getConversionsService() {

		return conversionsService;
	}


	/**
	 * @param conversionsService the conversionsService to set
	 */
	public void setConversionsService(Conversions conversionsService) {

		this.conversionsService = conversionsService;
	}

}
