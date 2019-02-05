package cs.scrs.service.bean;

import cs.scrs.miner.models.Filechain;
import cs.scrs.service.connection.ConnectionServiceImpl;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.request.AsyncRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BeansManager {
	@Autowired
	private ConnectionServiceImpl connectionService;
	@Autowired
	private IPServiceImpl iPService;
	@Autowired
	private AsyncRequest asyncRequest;
	@Autowired
	private Filechain filechain;
	@Autowired
	private RestTemplate restTemplate;
	
//	//This line will guarantee the BeansManager class will be injected last
//	@Autowired
//	private Set<IInjectable> injectables = new HashSet<IInjectable>();
//	 
//	//This method will make sure all the injectable classes will get the BeansManager in its steady state,
//	//where it's class members are ready to be set
//	@PostConstruct
//	private void inject() {
//	   for (IInjectable injectableItem : injectables) {
//	       injectableItem.inject(this);
//	   }
//	}
	
	/**
	 * @return the connectionService
	 */
	public ConnectionServiceImpl getConnectionService() {
	
		return connectionService;
	}
	
	/**
	 * @param connectionService the connectionService to set
	 */
	public void setConnectionService(ConnectionServiceImpl connectionService) {
	
		this.connectionService = connectionService;
	}
	
	/**
	 * @return the iPService
	 */
	public IPServiceImpl getiPService() {
	
		return iPService;
	}
	
	/**
	 * @param iPService the iPService to set
	 */
	public void setiPService(IPServiceImpl iPService) {
	
		this.iPService = iPService;
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
	 * @return the filechain
	 */
	public Filechain getFilechain() {
	
		return filechain;
	}
	
	/**
	 * @param filechain the filechain to set
	 */
	public void setFilechain(Filechain filechain) {
	
		this.filechain = filechain;
	}

	public RestTemplate getRestTemplate() {

		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {

		this.restTemplate = restTemplate;
	}
	
	
	
	
	
	
	
	
}
