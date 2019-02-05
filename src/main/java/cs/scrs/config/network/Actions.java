package cs.scrs.config.network;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
		prefix = "network.actions",
		locations = "classpath:configurations/network.properties",
		exceptionIfInvalid = true,
		ignoreInvalidFields = false,
		ignoreUnknownFields = false
)
public class Actions {
	private String connect;
	private String disconnect;
	private String keepAlive;
	private String sendTransaction;
	private String getTransaction;

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	public String getDisconnect() {
		return disconnect;
	}

	public void setDisconnect(String disconnect) {
		this.disconnect = disconnect;
	}

	public String getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(String keepAlive) {
		this.keepAlive = keepAlive;
	}

	public String getSendTransaction() {
		return sendTransaction;
	}

	public void setSendTransaction(String sendTransaction) {
		this.sendTransaction = sendTransaction;
	}

	public String getGetTransaction() {
		return getTransaction;
	}

	public void setGetTransaction(String getTransaction) {
		this.getTransaction = getTransaction;
	}

	@Override
	public String toString() {
		return "Actions{" +
				"connect='" + connect + '\'' +
				", disconnect='" + disconnect + '\'' +
				", keepAlive='" + keepAlive + '\'' +
				", sendTransaction='" + sendTransaction + '\'' +
				", getTransaction='" + getTransaction + '\'' +
				'}';
	}
}
