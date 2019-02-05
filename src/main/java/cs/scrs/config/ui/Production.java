package cs.scrs.config.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Iterator;
import java.util.List;

/**
 * Basic properties for profile production of Ui module
 * @author ivan18
 */
@ConfigurationProperties(
		value = "ui.production",
		locations = "classpath:configurations/ui.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false, 
		ignoreUnknownFields = false
		)
public class Production extends AUiConfig{
	//Stringa raprpesentante il path delle risorse associate all'ambiente
	private String prefix;
	//Stringa rappresentate l'estensione dei file view
	private String suffix;
	//Array contentente una lista di risorse
	@NestedConfigurationProperty
	private List<Resource> resources;

	@Override
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	@Override
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}


	@Override
	public String toString() {
		Resource resource;
		String toString= "Production [prefix=" + prefix + ", suffix=" + suffix + ", resources=[\n"; 
		Iterator<Resource> resourceIterator = resources.iterator();
		while (resourceIterator.hasNext()) {
			resource = resourceIterator.next();
			toString+= resource.toString()+"\n";
		} 
		toString += "]";
		return toString;
	}
}

