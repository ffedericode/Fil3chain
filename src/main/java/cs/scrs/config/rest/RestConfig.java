package cs.scrs.config.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Basic properties for miner keys
 * @author ivan18
 */
@Configuration
@ConfigurationProperties(
		value="rest",
		locations = "classpath:configurations/rest.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false, 
		ignoreUnknownFields = false
		)
public class RestConfig {
	@NestedConfigurationProperty
	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public String toString() {
		return "RestConfig [connection=" + connection + "]";
	}
	
	

}
