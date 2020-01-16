package br.com.dfe.utils;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import javax.naming.ldap.LdapName;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@NoArgsConstructor
public class CertificadoHelper {

	private KeyStore ks;

	@SneakyThrows
	public void loadRepositorioWindows()  {
		ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		ks.load(null, null);
	}

	@SneakyThrows
	public void loadPFX(Path pathFile, String password) {
		ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(pathFile.toFile()), password.toCharArray());
	}

	@SneakyThrows
	public List<X509Certificate> getCertificados() {
		return Collections.list(ks.aliases()).stream()
			.map(this::getCertificate)
			.collect(Collectors.toList());
	}

	@SneakyThrows
	public X509Certificate getCertificate(String alias) {
		return (X509Certificate) ks.getCertificate(alias);
	}

	@SneakyThrows
	public PrivateKey getPrivateKey(X509Certificate certificate) {
		String alias = getAlias(certificate);
		return (PrivateKey) ks.getKey(alias, null);
	}

	@SneakyThrows
	public PrivateKey getPrivateKey(@NonNull String alias, @NonNull String password) {
		return (PrivateKey) ks.getKey(alias, password.toCharArray());
	}

	@SneakyThrows
	public String getAlias(Certificate certificate) {
		return ks.getCertificateAlias(certificate);
	}

	@SneakyThrows
	public static String getCNFromx509(X509Certificate certificado) {
		LdapName name = new LdapName(certificado.getSubjectX500Principal().getName());
		return name.getRdns().stream()
			.filter(rdn -> rdn.getType().toUpperCase().equals("CN"))
			.findFirst()
			.map(r -> r.getValue().toString())
			.orElseThrow(() -> new RuntimeException("CN não encontrado no certificado"));
	}
}