package net.geral.log4j;

import org.apache.log4j.Level;

@Deprecated
public enum LogLevel {
	WARNING(Level.WARN),
	VERBOSE(Level.INFO),
	DEBUG(Level.DEBUG);

	public final Level	log4j_level;

	private LogLevel(Level l) {
		log4j_level = l;
	}
}
