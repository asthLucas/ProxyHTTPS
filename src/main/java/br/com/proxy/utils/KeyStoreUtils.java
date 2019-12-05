package br.com.proxy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class KeyStoreUtils {
	
	public static KeyStore createKeyStore()
	{
		KeyStore keyStore = null;
		//Utils.execute("src/main/resources/initializeKeyStore.sh", true);
		try {
			keyStore = KeyStore.getInstance("JKS");
			keyStore.load(KeyStoreUtils.class.getResourceAsStream("src/main/resources/keystore.jks"), "changeit".toCharArray());

			Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(new File("src/main/resources/localhost.pem")));
			keyStore.setCertificateEntry("cert-localhost", certificate);
			return keyStore;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}
				
		if(keyStore == null)
			System.out.println("[ERROR] Failed to initialize KeyStore");
		
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
