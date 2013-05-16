package net.geral.stream;

import java.io.IOException;
import java.io.Writer;

//its purpose is to do nothing
public class NullWriter extends Writer {
	@Override
	public void close() throws IOException {}

	@Override
	public void flush() throws IOException {}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {}
}
