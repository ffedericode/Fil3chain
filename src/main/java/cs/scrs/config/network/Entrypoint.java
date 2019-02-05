package cs.scrs.config.network;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
		prefix = "network.entrypoint",
		locations = "classpath:configurations/network.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false, 
		ignoreUnknownFields = false
		)
public class Entrypoint {
	private String ip;
	private String port;
	private String baseUri;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	@Override
	public String toString() {
		return "Entrypoint [ip=" + ip + ", port=" + port + ", baseUri=" + baseUri + "]";
	}
	
	
}
