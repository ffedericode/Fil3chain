package cs.scrs.config.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Basic properties for Ui module
 * @author ivan18
 */

@Configuration
@ConfigurationProperties(
		prefix = "ui",
		locations = "classpath:configurations/ui.properties",
		exceptionIfInvalid = true
		)
public class UiConfig implements IUiConfig{
	//Stringa rappresentante l'ambiente da utilizzare
	private String environment;
		
	@NestedConfigurationProperty
	private Development development;

	@NestedConfigurationProperty
	private Production production;


	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public Development getDevelopment() {
		return development;
	}
	public void setDevelopment(Development development) {
		this.development = development;
	}
	/*
	public Production getProduction() {
		return production;
	}
	public void setProduction(Production production) {
		this.production = production;
	}
	*/
	@Override
	public AUiConfig getConfigFromEnvironment(String environment) {
		if(this.environment.equals("development")){
			return this.development;	
			
		}else if(this.environment.equals("production")){
			return this.production;

			
		}else{
			return null;
		}
	}
}