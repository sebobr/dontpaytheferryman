package splitter;

import java.io.IOException;

public interface KeyValueAcceptor<K, V> {
	public void write(K k, V v) throws IOException, InterruptedException;
	public void close() throws IOException, InterruptedException;
}