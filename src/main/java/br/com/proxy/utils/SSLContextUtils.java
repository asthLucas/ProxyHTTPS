package br.com.proxy.utils;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

public class SSLContextUtils {

    public static SSLContext createSSLContext(String keyStorePath, String trustStorePath) throws Exception 
    {
    	KeyStore keyStore = KeyStoreUtils.createKeyStore(keyStorePath);
    	KeyStore trustStore = KeyStoreUtils.createKeyStore(trustStorePath);
    	
    	SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    	sslContext.init(KeyStoreUtils.createKeyManagers(keyStore), KeyStoreUtils.createTrustManagers(trustStore), new SecureRandom());
    	
    	return sslContext;
    }
}
