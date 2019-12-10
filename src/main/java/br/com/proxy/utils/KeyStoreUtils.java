package br.com.proxy.utils;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class KeyStoreUtils {

	public static KeyStore createKeyStore(String keyStoreName)
	{
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("JKS");			
			keyStore.load(KeyStoreUtils.class.getClassLoader().getResourceAsStream(keyStoreName.concat(".jks")), "changeit".toCharArray());
			return keyStore;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
				
		if(keyStore == null)
			System.out.println("[ERROR] Failed to initialize store ".concat(keyStoreName));
		
		return keyStore;
	}
	
	public static KeyManager[] createKeyManagers(KeyStore keyStore)
	{
    	KeyManagerFactory keyManagerFactory = null;
    	
    	try {
			keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
			keyManagerFactory.init(keyStore, "changeit".toCharArray());
			return keyManagerFactory.getKeyManagers();
		} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
			e.printStackTrace();
		}
		
		if(keyManagerFactory == null)
			System.out.println("[ERROR] Failed to initialize KeyManagers");
		
		return null;
	}

	public static TrustManager[] createTrustManagers(KeyStore keyStore)
	{
		TrustManagerFactory trustManagerFactory = null;
    	try {
    		trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
			trustManagerFactory.init(keyStore);
			return trustManagerFactory.getTrustManagers();
		} catch (KeyStoreException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	    	
		if(trustManagerFactory == null)
			System.out.println("[ERROR] Failed to initialize TrustManagers");

		return null;
	}
}
