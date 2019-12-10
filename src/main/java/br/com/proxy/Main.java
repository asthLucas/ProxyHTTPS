package br.com.proxy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.proxy.utils.SSLContextUtils;
import io.undertow.protocols.ssl.SNISSLContext;

public class Main {

	public static void main(String[] args) throws Exception {
    	Map<String, SNISSLContext> domainContextMap = new HashMap<String, SNISSLContext>();
		domainContextMap.put("test", SSLContextUtils.createSNISSLContext("localhost", "identity", "truststore", null));
		domainContextMap.put("domain1.test", SSLContextUtils.createSNISSLContext("domain1.localhost", "identity1", "truststore1", domainContextMap.get("localhost")));
		domainContextMap.put("domain2.test", SSLContextUtils.createSNISSLContext("domain2.localhost", "identity2", "truststore2", domainContextMap.get("domain1.localhost")));
		domainContextMap.put("proxy.test", SSLContextUtils.createSNISSLContext("proxy.localhost", "identity3", "truststore3", null));

		Bootstrap.bootstrapServer(8081, "test", domainContextMap);

		List<String> targets = Arrays.asList("domain1.test","domain2.test");
		Bootstrap.bootstrapProxy(8080, "proxy.test", targets, domainContextMap);
	}
}