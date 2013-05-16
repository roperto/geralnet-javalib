package net.geral.configuration;

public class ConfigurationException extends Exception {
	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Exception cause) {
		super(message, cause);
	}

	public ConfigurationException(Exception e) {
		super(e);
	}

	private static final long	serialVersionUID	= 1L;
}
