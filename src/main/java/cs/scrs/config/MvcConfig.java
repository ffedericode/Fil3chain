
package cs.scrs.config;

import cs.scrs.config.ui.AUiConfig;
import cs.scrs.config.ui.Resource;
import cs.scrs.config.ui.UiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Iterator;


/**
 * Questa classe si occupa della configurazione di springMVC
 * in particolare, vengono settati i percorsi per le risorse statiche ed 
 * il componente che si occupa del reperimento delle viste html
 * 
 * @author ivan18
 */

@Configuration
@EnableTransactionManagement
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	UiConfig uiCfg;

	/**
	 * Aggiunta di un view controller che al path / associa la view index
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}
	
	/**
	 * Aggiunta delle Risorse a seconda dell'ambiente utilizzato
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Resource resource;
		AUiConfig config = uiCfg.getConfigFromEnvironment(uiCfg.getEnvironment());

		Iterator<Resource> resourceIterator = config.getResources().iterator();
		//Per ogni risorsa presente nell ambiente considerato
		while (resourceIterator.hasNext()) {
			//Prossimo elemento risorsa da considerare
			resource = resourceIterator.next();
			//Viene aggiunto il pattern che corrisponde al nome risorsa con il relativo pattern di locazione
			registry
			.addResourceHandler(resource.getPattern())
			.addResourceLocations(resource.getLocation());
		} 
	}
	
	/**
	 * Creazione del Bean adibito alla risoluzione delle view a seconda dell'ambiente utilizzato
	 * @return InternalResourceViewResolver
	 * @throws Exception
	 */
	@Bean
	public InternalResourceViewResolver viewResolver() throws Exception {
		InternalResourceViewResolver resolver;
		System.out.println("Environment found: "+uiCfg.getEnvironment());
		System.out.println("Environment To String: "+ uiCfg.getConfigFromEnvironment(uiCfg.getEnvironment()).toString());

		//Configurazione attuale
		AUiConfig config = uiCfg.getConfigFromEnvironment(uiCfg.getEnvironment());

		resolver = new InternalResourceViewResolver();
		resolver.setPrefix(config.getPrefix());
		resolver.setSuffix(config.getSuffix());
		return resolver;
	}


}