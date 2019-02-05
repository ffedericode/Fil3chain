package cs.scrs.config.network;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
		prefix = "network.pooldispatcher",
		locations = "classpath:configurations/network.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false, 
		ignoreUnknownFields = false
		)
public class Pooldispatcher {
	private String baseUri;
	//private String timeoutSeconds;
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
//	public String getTimeoutSeconds() {
//		return timeoutSeconds;
//	}
//	public void setTimeoutSeconds(String timeoutSeconds) {
//		this.timeoutSeconds = timeoutSeconds;
//	}
	@Override
	public String toString() {
		return "Pooldispatcher [baseUri=" + baseUri + "]";
	}
	
}
