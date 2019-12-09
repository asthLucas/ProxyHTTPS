package br.com.proxy.utils;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import io.undertow.protocols.ssl.SNIContextMatcher;
import io.undertow.protocols.ssl.SNISSLContext;

public class SSLContextUtils {

    public static SNISSLContext createSNISSLContext(String host, String keyStorePath, String trustStorePath) throws Exception
    {
    	SSLContext domainContext = createSSLContext(keyStorePath, trustStorePath);
    	
    	SNIContextMatcher matcher = createSNIContextMatcher(host, domainContext);
    	return new SNISSLContext(matcher);
    }
    
    public static SSLContext createSSLContext(String keyStorePath, String trustStorePath) throws Exception 
    {
    	KeyStore keyStore = KeyStoreUtils.createKeyStore(keyStorePath);
    	KeyStore trustStore = KeyStoreUtils.createKeyStore(trustStorePath);
    	SSLContextBuilder sslContextbuilder = SSLContextBuilder.create();
    	sslContextbuilder.loadKeyMaterial(keyStore, "changeit".toCharArray());
    	sslContextbuilder.loadTrustMaterial(trustStore, ((TrustStrategy) (X509Certificate[] chain, String authType) -> true));
    	sslContextbuilder.setProtocol("TLSv1.2");
    	
    	SSLContext sslContext = sslContextbuilder.build();
    	sslContext.init(KeyStoreUtils.createKeyManagers(keyStore), KeyStoreUtils.createTrustManagers(keyStore), new SecureRandom());
    	
    	return sslContext;
    }
    
    private static SNIContextMatcher createSNIContextMatcher(String host, SSLContext sslContext)
    {
    	SNIContextMatcher sniContextMatcher = new SNIContextMatcher
													.Builder()
													.setDefaultContext(sslContext)
													.addMatch(host, sslContext)
													.build();
    	
    	return sniContextMatcher;
    }
}
