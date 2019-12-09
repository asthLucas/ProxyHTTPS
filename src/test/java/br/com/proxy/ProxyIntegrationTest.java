package br.com.proxy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FilterInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import br.com.proxy.utils.KeyStoreUtils;
import br.com.proxy.utils.SSLContextUtils;
import io.undertow.Undertow;
import io.undertow.protocols.ssl.SNISSLContext;

public class ProxyIntegrationTest {
	
	@Test
	public void testBootstrapServer_shouldCreateUndertowServer() throws Exception
	{
		KeyStoreUtils.initializeKeyStore();
		Map<String, SNISSLContext> domainContext = new HashMap<String, SNISSLContext>();
		domainContext.put("localhost", SSLContextUtils.createSNISSLContext("localhost", "identity", "truststore"));

		Undertow server = Bootstrap.bootstrapServer(8080, "localhost", domainContext);
		
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
		KeyStoreUtils.initializeKeyStore();
		Map<String, SNISSLContext> domainContext = new HashMap<String, SNISSLContext>();
		Undertow proxy = Bootstrap.bootstrapProxy(8080, "localhost", Arrays.asList("http://localhost:8081"), domainContext);
		Undertow server = Bootstrap.bootstrapServer(8081, "localhost", domainContext);
		
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