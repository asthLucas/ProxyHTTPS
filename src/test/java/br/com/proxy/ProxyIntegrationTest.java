package br.com.proxy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FilterInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.undertow.Undertow;

public class ProxyIntegrationTest {
	
	@Test
	public void testBootstrapServer_shouldCreateUndertowServer() throws Exception
	{
		Undertow server = Bootstrap.bootstrapServer(8080, "localhost");
		
		URL url = new URL("http://localhost:8080");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		con.connect();
		assertEquals(con.getResponseCode(), 200);
		
		server.stop();
	}
	
	@Test
	public void testBootstrapProxy_shouldCreateUndertowProxy() throws Exception
	{
		Undertow proxy = Bootstrap.bootstrapProxy(8080, "localhost", Arrays.asList("http://localhost:8081"));
		Undertow server = Bootstrap.bootstrapServer(8081, "localhost");
		
		URL url = new URL("http://localhost:8080");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		con.connect();
		String responseBody = new String(((FilterInputStream) con.getContent()).readAllBytes());

		assertEquals(con.getResponseCode(), 200);
		assertEquals(responseBody, "Hello World");
		
		server.stop();
		proxy.stop();
	}
}