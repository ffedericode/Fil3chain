package cs.scrs.miner;


import cs.scrs.config.network.Network;
import cs.scrs.miner.models.Filechain;
import cs.scrs.service.mining.IMiningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@ComponentScan("cs.scrs")
@EnableAsync
public class Fil3Chain implements CommandLineRunner {

	@Autowired
	Filechain filechain;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	IMiningService ms;
	@Autowired
	Network networkProperties;


	// avvia applicazione SpringBoot con il thread run
	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(Fil3Chain.class, args);
		//
		// Stampa dei Bean
		// String[] beanNames = ctx.getBeanDefinitionNames();
		// Arrays.sort(beanNames);
		// for (String beanName : beanNames) {
		// System.out.println(beanName);
		// }

	}

	// Applicazione reale
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Chiave Privata Personale: "+ms.getPrivateKey());
		System.out.println("Chiave Pubblica: "+ms.getPublicKey());
		filechain.initializeFilechain();
		filechain.update();
		/*
		 * Example of post Request with messageConverter
		 */
		/*String uriList="http://"+networkProperties.getEntrypoint().getIp();
		uriList+=":"+networkProperties.getEntrypoint().getPort();
		uriList+=networkProperties.getEntrypoint().getBaseUri();
		uriList+=networkProperties.getActions().getConnect();
		System.out.println("Request uri: "+uriList);
		IP myIp = new IP("10.192.0.10:8080");
		System.out.println("My ip: "+ myIp);
		
		// Make the HTTP GET request, marshaling the response to a IP[] object
		ResponseEntity<IP[]> ips = restTemplate.postForEntity(uriList, myIp.toString(), IP[].class);
		IP[] result = ips.getBody();
		System.out.println("IpList length "+result.length);
		for (IP string : result) {
			System.out.println("Ip found "+ string);
		}*/


	
	}

}
