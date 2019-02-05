package cs.scrs.config.ui;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Resource property for the environments of Ui module
 * @author ivan18
 */
@ConfigurationProperties
public class Resource {
	private String pattern;
	private String location;

	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "Resource [pattern=" + pattern + ", location=" + location + "]";
	}
	
	
}
