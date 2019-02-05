package cs.scrs.config.network;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Basic properties for Network
 * @author ivan18
 */
@Configuration
@ConfigurationProperties(
		prefix = "network",
		locations = "classpath:configurations/network.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false, 
		ignoreUnknownFields = false
		)
public class Network {
	private int timeoutSeconds;
	private Entrypoint entrypoint;
	private Pooldispatcher pooldispatcher;
	private Actions actions;
	
	
	
	
	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}
	public void setTimeoutSeconds(int timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}
	public Entrypoint getEntrypoint() {
		return entrypoint;
	}
	public void setEntrypoint(Entrypoint entrypoint) {
		this.entrypoint = entrypoint;
	}
	public Pooldispatcher getPooldispatcher() {
		return pooldispatcher;
	}
	public void setPooldispatcher(Pooldispatcher pooldispatcher) {
		this.pooldispatcher = pooldispatcher;
	}
	public Actions getActions() {
		return actions;
	}
	public void setActions(Actions actions) {
		this.actions = actions;
	}
	@Override
	public String toString() {
		return "NetworkConfig [timeoutSeconds=" + timeoutSeconds + ", entrypoint=" + entrypoint + ", pooldispatcher="
				+ pooldispatcher + ", actions=" + actions + "]";
	}
	
	

}
