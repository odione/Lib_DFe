package br.com.dfe.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;

import lombok.Getter;
import lombok.Setter;

public class CertificadoUtils {

	@Getter private KeyStore ks;
	@Setter private String password;
	
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
	
	/**
	 * 
	 * @param pathFile
	 * @param password
	 * @return true se conseguiu carregar KeyStore
	 */
	public boolean loadPFX(String pathFile, String password) {
		try {
			return loadPFX(new FileInputStream(pathFile), password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param inputStream do arquivo PFX
	 * @param password
	 * @return true se conseguiu carregar KeyStore
	 */
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
	
	public String getCNFromx509(X509Certificate certificado) {
		try {
			LdapName name = new LdapName(certificado.getSubjectX500Principal().getName());
			
			return name.getRdns().stream()
				.filter(rdn -> rdn.getType().toUpperCase().equals("CN"))
				.findFirst()
				.get().getValue().toString();
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
		return "";
	}
}