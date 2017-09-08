package br.com.dfe.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CertificadoUtils {

	private KeyStore ks;
	private String password;
	
	public boolean loadRepositorioWindows() {
		try {
			ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			ks.load(null, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean loadPFX(String pathFile, String password) {
		try {
			return loadPFX(new FileInputStream(pathFile), password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean loadPFX(InputStream in, String password) {
		this.password = password;
		try {
			ks = KeyStore.getInstance("PKCS12");
			ks.load(in, password.toCharArray());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public X509Certificate getCertificado(String alias) throws Exception {
		return (X509Certificate) ks.getCertificate(alias);
	}
	
	public PrivateKey getPrivateKey(String alias, String password) throws Exception {
		return (PrivateKey) ks.getKey(alias, password.toCharArray());
	}
	
	public X509Certificate getCertificado() throws Exception {
		return (X509Certificate) ks.getCertificate(ks.aliases().nextElement());
	}
	
	public PrivateKey getPrivateKey() throws Exception {
		return (PrivateKey) ks.getKey(ks.aliases().nextElement(), password.toCharArray());
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public KeyStore getKeyStore() {
		return ks;
	}
}