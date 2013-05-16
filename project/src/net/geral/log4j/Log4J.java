package net.geral.log4j;

import org.apache.log4j.Logger;

@Deprecated
public class Log4J {
	@Deprecated
	public static void verbose(Object o) {
		Logger.getRootLogger().info(o);
	}

	@Deprecated
	public static void warning(Object o) {
		Logger.getRootLogger().warn(o);
	}

	@Deprecated
	public static void debug(Object o) {
		Logger.getRootLogger().debug(o);
	}

	@Deprecated
	public static void error(Object o) {
		Logger.getRootLogger().error(o);
	}

	@Deprecated
	public static void setLevel(LogLevel level) {
		Logger.getRootLogger().setLevel(level.log4j_level);
	}
}
