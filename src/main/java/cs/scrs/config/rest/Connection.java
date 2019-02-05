package cs.scrs.config.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Basic properties for Connection Rest
 * @author ivan18
 */
@Configuration
@ConfigurationProperties(
		value="rest.connection",
		locations = "classpath:configurations/rest.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false, 
		ignoreUnknownFields = false
		)
public class Connection {
	private int requestTimeout;
	private int connectTimeout;
	private int readTimeout;
	public int getRequestTimeout() {
		return requestTimeout;
	}
	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	@Override
	public String toString() {
		return "Connection [requestTimeout=" + requestTimeout + ", connectTimeout=" + connectTimeout + ", readTimeout="
				+ readTimeout + "]";
	}
	
	
	

}
