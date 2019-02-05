package cs.scrs.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



/**
 * Basic properties for miner keys
 * 
 * Permette di caricare le chiavi da file esterni all avvio del programma
 */
@Configuration
@ConfigurationProperties(value = "key", locations = "classpath:configurations/keys.properties", exceptionIfInvalid = true, ignoreInvalidFields = false, ignoreUnknownFields = false)
public class KeysConfig {

	private String publicKey;
	private String privateKey;


	public String getPublicKey() {

		return publicKey;
	}

	public void setPublicKey(String publicKey) {

		this.publicKey = publicKey;
	}

	public String getPrivateKey() {

		return privateKey;
	}

	public void setPrivateKey(String privateKey) {

		this.privateKey = privateKey;
	}

	@Override
	public String toString() {

		return "KeysConfigProperties [publicKey=" + publicKey + ", privateKey=" + privateKey + "]";
	}

}
