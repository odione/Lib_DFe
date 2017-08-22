package br.com.transmissor.configuracao;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ResourceUtils;

@SpringBootConfiguration
public class Configuracao {
	
	private String PATH_CERT_TEST;
	private final String PASSWORD_CERT_TEST = "1234";
	private KeyStore ks;

	@Bean
	@Primary
	public DadosEmissor getEmissor() {
		iniciaKS();
		return new DadosEmissor("SC", "42", "55", "4.00", 2, getCertificate(), getPrivateKey());
	}
	
	private X509Certificate getCertificate() {
		try {
			return (X509Certificate) ks.getCertificate(ks.aliases().nextElement());
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) ks.getKey(ks.aliases().nextElement(), PASSWORD_CERT_TEST.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void iniciaKS() {
		try {
			PATH_CERT_TEST = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"certs/cert_A1.pfx").getPath();
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(new FileInputStream(PATH_CERT_TEST), PASSWORD_CERT_TEST.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}