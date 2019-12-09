package br.com.proxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.proxy.utils.KeyStoreUtils;
import br.com.proxy.utils.SSLContextUtils;
import io.undertow.protocols.ssl.SNISSLContext;

public class Main {

	public static void main(String[] args) throws Exception {
    	KeyStoreUtils.initializeKeyStore();

    	Map<String, SNISSLContext> domainContextMap = new HashMap<String, SNISSLContext>();
		domainContextMap.put("default", SSLContextUtils.createSNISSLContext("default", "identity", "truststore"));
		domainContextMap.put("domain1.localhost", SSLContextUtils.createSNISSLContext("domain1.localhost", "identity", "truststore"));
		domainContextMap.put("domain2.localhost", SSLContextUtils.createSNISSLContext("domain2.localhost", "identity", "truststore"));

		Bootstrap.bootstrapServer(8081, "localhost", domainContextMap);

		List<String> targets = Arrays.asList("https://domain1.localhost:8081","https://domain2.localhost:8081");
		Bootstrap.bootstrapProxy(8080, "localhost", targets, domainContextMap);
	}
}