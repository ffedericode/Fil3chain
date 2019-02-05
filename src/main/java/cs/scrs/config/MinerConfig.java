package cs.scrs.config;


import cs.scrs.config.rest.RestConfig;
import cs.scrs.miner.models.Filechain;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.mining.VerifyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 *MinerConfig instanzia i bean, ovvero gli oggetti, iniziali base per poter avviare il programma
 *l'ordine in cui vengono creati è importante, poiché sono dipendenti.
 */
@Configuration
public class MinerConfig {

	@Autowired
	RestConfig restProperties;


	// Creazione del bean per ottenere i servizi altrove

	@Bean
	public HttpComponentsClientHttpRequestFactory HttpRequestFactory() {

		System.out.println("1\n" + restProperties.getConnection());

		HttpComponentsClientHttpRequestFactory crf = new HttpComponentsClientHttpRequestFactory();
		crf.setConnectTimeout(restProperties.getConnection().getConnectTimeout());
		crf.setConnectionRequestTimeout(restProperties.getConnection().getRequestTimeout());
		crf.setReadTimeout(restProperties.getConnection().getReadTimeout());
		return crf;
	}

	@Bean
	public RestTemplate RestTemplate() {

		System.out.println("2");
		RestTemplate restTemplate = new RestTemplate(HttpRequestFactory());

		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.TEXT_PLAIN);

		MappingJackson2HttpMessageConverter mc = new MappingJackson2HttpMessageConverter();
		mc.setSupportedMediaTypes(mediaTypes);

		restTemplate.getMessageConverters().add(mc);
		return restTemplate;
	}

	@Bean
	public IPServiceImpl IPServiceImpl() {

		System.out.println("3");
		IPServiceImpl ipServiceImpl = new IPServiceImpl();
		ipServiceImpl.setIpList(Collections.synchronizedList(new ArrayList<>()));
		return ipServiceImpl;
	}

	@Bean
	public VerifyServiceImpl verifyServiceImpl() {

		System.out.println("6");
		VerifyServiceImpl verifyServiceImpl = new VerifyServiceImpl();
		return verifyServiceImpl;

	}

	@Bean
	public Filechain Filechain() {

		System.out.println("8");
		Filechain filechain = new Filechain();
		return filechain;
	}

}
