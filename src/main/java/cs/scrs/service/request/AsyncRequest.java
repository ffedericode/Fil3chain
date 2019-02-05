package cs.scrs.service.request;


import com.google.common.reflect.TypeToken;
import cs.scrs.config.network.Network;
import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.models.IP;
import cs.scrs.miner.models.Pairs;
import cs.scrs.miner.models.RequestIpList;
import cs.scrs.service.util.Conversions;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

//import cs.scrs.service.bean.IInjectable;



@Service("AsyncRequest")
public class AsyncRequest {


	private Integer timeoutSeconds;

	public final static Integer REQNUMBER = 6;
	
	private static final int TIMEOUT_MILLIS = 5000;

	@Autowired
	private Network networkProperties;
	/**
	 * 
	 */

	public AsyncRequest() {
		// loadConfiguration();
	}
	@PostConstruct
	public void init(){
		System.out.println("AsyncRequest init method called");
		this.setTimeoutSeconds(networkProperties.getTimeoutSeconds());
	}
	/**
	 * 
	 */
//	public void loadConfiguration() {
//
//		// Carica la configurazione TODO UTILIZZARE SPRING
//		Properties prop = new Properties();
//		InputStream in = Object.class.getResourceAsStream("/network.properties");
//		try {
//			prop.load(in);
//			// Imposta il timeout
//			this.setTimeoutSeconds(Integer.parseInt(prop.getProperty("timeoutSeconds", "3")));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	@Async
	public Future<Pairs<IP, Integer>> findMaxChainLevel(String uriMiner) {

		//
		// SimpleClientHttpRequestFactory rf = ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory());
		// rf.setReadTimeout(1000 * 5);
		// rf.setConnectTimeout(1000 * 5);
		// restTemplate.setRequestFactory(rf);

		String result = "";
		Integer level = -1;
		Integer counter = 0;
		while (counter <= REQNUMBER) {
			try {
				System.out.println("\nRichiesta ad :" + uriMiner);
//				result = restTemplate.getForObject("http://" + uriMiner + "/fil3chain/updateAtMaxLevel", String.class);
				result=doGet("http://" + uriMiner + "/fil3chain/updateAtMaxLevel");
				level = Integer.decode(result);
				System.out.println("Chain Level"+level);
				return new AsyncResult<>(new Pairs<>(new IP(uriMiner), level));
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("\nSono Morto: " + uriMiner + " Causa: " + e.getMessage());
				counter++;
			}

			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * @param uriMiner
	 * @return
	 */
	@Async
	public Future<String> pingUser(String uriMiner) {

		// SimpleClientHttpRequestFactory rf = ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory());
		// rf.setReadTimeout(1000 * 5);
		// rf.setConnectTimeout(1000 * 5);
		// restTemplate.setRequestFactory(rf);

		Integer counter = 0;
		while (counter <= REQNUMBER) {
			try {
				System.out.println("\nRichiesta ad :" + uriMiner);
//				String response = restTemplate.postForObject("http://" + uriMiner + "/user_ping", null, String.class);
				String response = doPost("http://" + uriMiner + "/user_ping", "");
				return new AsyncResult<>(response);
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("\nSono Morto: " + uriMiner + " Causa: " + e.getMessage());
				counter++;
			}

			// Aspetto prima della prossima richiesta
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	// @Async
	// public Future<List<Block>> sendBlockToMiners(Block block) throws InterruptedException {
	//
	// System.out.println(restTemplate.toString());
	//
	// HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
	// rf.setReadTimeout(1000 * 10);
	// rf.setConnectTimeout(1000 * 10);
	// rf.setConnectionRequestTimeout(1000 * 10);
	// restTemplate.setRequestFactory(rf);
	//
	// List<Block> blocks = new ArrayList<Block>();
	// String bool = Boolean.FALSE.toString();
	// Map<IP, Integer> map = new HashMap<IP, Integer>();
	// Map<IP, Integer> counter = Collections.synchronizedMap(map);
	// connectionService.firstConnectToEntryPoint();
	// synchronized (counter) {
	// for (IP ip : ipService.getIPList()) {
	// counter.put(ip, 0);
	// }
	//
	// System.out.println("dimensione lista hashmap " + counter.size());
	//
	// }
	//
	// while (counter.size() > 0) {
	//
	// for (IP ip : ipService.getIPList()) {
	// System.out.println("Invio blocco a: " + ip.getIp());
	// try {
	// // String response = HttpUtil.doPost("http://" + ip.getIp() + "/fil3chain/newBlock",
	// // JsonUtility.toJson(block));
	//
	// String response = restTemplate.postForObject("http://" + ip.getIp() + "/fil3chain/newBlock", block, String.class);
	// System.out.println("Ho inviato il blocco e mi è ritornato come risposta: " + response);
	// synchronized (counter) {
	//
	// // Se ho mandato il blocco rimuovo il miner
	// counter.remove(ip);
	//
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// Thread.sleep(1000);
	// System.out.println("Il miner " + ip.getIp() + " non è più connesso.");
	// System.out.println("Errore invio blocco: " + bool);
	// } finally {
	// synchronized (counter) {
	// // altrimenti aumenta il counter di uno
	//
	// counter.put(ip, counter.get(ip) + 1);
	// if (counter.get(ip) > REQNUMBER)
	// counter.remove(ip);
	//
	// }
	// }
	// }
	//
	// }
	//
	// // Annullo il blocco appena minato
	// block = null;
	//
	// return new AsyncResult<>(blocks);
	// }

	
	
	@Async
	public Future<Block> getAsyncBlockFromHash(IP designedMiner, String hash) {

//		HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
//		rf.setReadTimeout(1000 * 10);
//		rf.setConnectTimeout(1000 * 10);
//		rf.setConnectionRequestTimeout(1000 * 10);
//		restTemplate.setRequestFactory(rf);
		
		
		 Type type = new TypeToken<Block>() {}.getType();
		Block blockResponse = null;
		try {
//			blockResponse = restTemplate.getForObject("http://" + designedMiner.getIp() + "/fil3chain/getBlockByhash?hash=" + hash, Block.class);
			blockResponse=doGetJSON("http://"+ designedMiner+"/fil3chain/getBlockByhash?hash=" + hash, type);
		} catch (Exception e) {
			System.out.println("Errore ricezione Blocco");
		}
		return new AsyncResult<>(blockResponse);
	}

	// Metodo Sincrono per prendere blocco con un determinato Hash dal miner designato
	public Block getBlockFromHash(IP designedMiner, String hash) {

//		HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
//		rf.setReadTimeout(1000 * 10);
//		rf.setConnectTimeout(1000 * 10);
//		rf.setConnectionRequestTimeout(1000 * 10);
//		restTemplate.setRequestFactory(rf);
		
		Type type = new TypeToken<Block>() {}.getType();
		Block blockResponse = null;
		try {
//			blockResponse = restTemplate.getForObject("http:// /fil3chain/getBlockByhash?hash=" + hash, Block.class);
			blockResponse=doGetJSON("http://"+ designedMiner.getIp()+"/fil3chain/getBlockByhash?hash=" + hash, type);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore ricezione Blocco");
		}
		return blockResponse;
	}

	// Metodo Asincrono per prendere blocchi con un determinato ChainLevel dal miner designato
	@Async
	public Future<List<Block>> getAsyncBlocksFromChainLevel(Pairs<IP, Integer> designedMiner, Integer myChainLevel) {

//		HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
//		rf.setReadTimeout(1000 * 10);
//		rf.setConnectTimeout(1000 * 10);
//		rf.setConnectionRequestTimeout(1000 * 10);
//		restTemplate.setRequestFactory(rf);

		Type type = new TypeToken<List<Block>>() {}.getType();
		List<Block> blockResponse = new ArrayList<Block>();
		try {
//			blockResponse = restTemplate.getForObject("http://" + designedMiner.getValue1().getIp() + "/fil3chain/getBlockByChain?chainLevel=" + myChainLevel,blockResponse.getClass());
			blockResponse=doGetJSON("http://" + designedMiner.getValue1().getIp() + "/fil3chain/getBlockByChain?chainLevel=" + myChainLevel, type);
		} catch (Exception e) {
			System.out.println("Errore ricezione Blocchi");
		}
		return new AsyncResult<>(blockResponse);
	}

	// Metodo Sincrono per prendere blocchi con un determinato ChainLevel dal miner designato
	public List<Block> getBlocksFromChainLevel(Pairs<IP, Integer> designedMiner, Integer myChainLevel) {

//		HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
//		rf.setReadTimeout(1000 * 10);
//		rf.setConnectTimeout(1000 * 10);
//		rf.setConnectionRequestTimeout(1000 * 10);
//		restTemplate.setRequestFactory(rf);

		Type type=new TypeToken<List<Block>>(){}.getType();	
		List<Block> blockResponse = new ArrayList<Block>();
		try {
			blockResponse=doGetJSON("http://" + designedMiner.getValue1().getIp() + "/fil3chain/getBlockByChain?chainLevel=" + myChainLevel, type);
//			type = conversionsService.fromJson(body, type.getClass());
//			System.out.println("string " + type);
//			System.out.println("size " + type.size());
//			//Block block = (Block) blockResponse.get(0);
////			System.out.println("bloccoooooo" + block);
//			for (Block block : type) {
//				System.out.println(block);
//			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Errore ricezione Blocchi");
		}
		return blockResponse;
	}

	// Metodo per richiedere ad EntryPoint lista Ip dei Miner collegati
	public String getIpFromEntryPoint(String entryPointUrl, IP myIp) {

		System.out.println("getIpFromEntryPoint");

		String requestIp = "";
		RequestIpList requestIps = null;
		String postRequest = "{\"user_ip\":\"" + myIp.getIp() + ":8080\"}";
		try {
			requestIp = doPost(entryPointUrl, postRequest);
			// requestIp = this.doPost(entryPointUrl, postRequest);
			System.out.println("getIpFromEntryPoint get RequestIp " + requestIp);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Errore ricezione Ip da Entry Point");
		}
		System.out.println(requestIp);
		return requestIp;
	}



	public static String doGet(String url) throws Exception {
		
//		HttpPost request = new HttpPost(url);

		HttpGet request=new HttpGet(url);
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIMEOUT_MILLIS).setConnectTimeout(TIMEOUT_MILLIS).setConnectionRequestTimeout(TIMEOUT_MILLIS).build();

		request.setConfig(requestConfig);

		HttpClient client = HttpClientBuilder.create().build();

		HttpResponse response;
		response = client.execute(request);
		BufferedReader rd;
		rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

	/**
	 * esegue una richiesta get
	 * 
	 * @param url
	 *            url a cui inviare la richiesta
	 * @return risposta ricevuta
	 * @throws possibili
	 *             errori di comunicazione HTTP
	 */
	@SuppressWarnings("unchecked")
	public static <T> T doGetJSON(String url, Type t) throws IOException {

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(TIMEOUT_MILLIS)
			.setConnectTimeout(TIMEOUT_MILLIS)
			.setConnectionRequestTimeout(TIMEOUT_MILLIS)
			.build();

		request.setConfig(requestConfig);

		HttpResponse response;
		response = client.execute(request);
		BufferedReader rd;
		rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		return Conversions.fromJson(result.toString(), t);
	}

	/**
	 * esegue una richiesta post
	 * 
	 * @param url
	 *            url a cui inviare la richiesta
	 * @return risposta ricevuta
	 * @throws possibili
	 *             errori di comunicazione HTTP
	 */
	public static String doPost(String url, String raw_data) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
//		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		for (Pairs<?, ?> p : parameters) {
//			urlParameters.add(new BasicNameValuePair(p.getValue1().toString(), p.getValue2().toString()));
//		}
		post.setEntity(new StringEntity(raw_data));
		HttpResponse response = client.execute(post);
		BufferedReader rd;
		rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
	
	
	
	public int getTimeoutSeconds() {

		return timeoutSeconds;
	}

	public void setTimeoutSeconds(int timeoutSeconds) {

		this.timeoutSeconds = timeoutSeconds;
	}
	
	
	
	
	
	
	
	
	
	

}
