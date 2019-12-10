package br.com.proxy.utils;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

import io.undertow.protocols.ssl.SNIContextMatcher;
import io.undertow.protocols.ssl.SNIContextMatcher.Builder;
import io.undertow.protocols.ssl.SNISSLContext;

public class SSLContextUtils {

    public static SNISSLContext createSNISSLContext(String host, String keyStorePath, String trustStorePath, SSLContext defaultContext) throws Exception
    {
    	SSLContext domainContext = createSSLContext(keyStorePath, trustStorePath);
    	
    	SNIContextMatcher matcher = createSNIContextMatcher(host, domainContext, defaultContext);
    	return new SNISSLContext(matcher);
    }
    
    public static SSLContext createSSLContext(String keyStorePath, String trustStorePath) throws Exception 
    {
    	KeyStore keyStore = KeyStoreUtils.createKeyStore(keyStorePath);
    	KeyStore trustStore = KeyStoreUtils.createKeyStore(trustStorePath);
    	
    	SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    	sslContext.init(KeyStoreUtils.createKeyManagers(keyStore), KeyStoreUtils.createTrustManagers(trustStore), new SecureRandom());
    	
    	return sslContext;
    }
    
    private static SNIContextMatcher createSNIContextMatcher(String host, SSLContext sslContext, SSLContext defaultContext)
    {
    	Builder builder = new SNIContextMatcher.Builder();
    	builder.setDefaultContext(defaultContext);
    	
    	if(defaultContext == null)
        	builder.setDefaultContext(sslContext);
    	
		return builder.addMatch(host, sslContext).build();
    }
}
