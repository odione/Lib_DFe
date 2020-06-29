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
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@NoArgsConstructor
public class CertificadoHelper {

	private KeyStore ks;

	@SneakyThrows
	public void loadRepositorioWindows()  {
		ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		ks.load(null, null);

		Collections.list(ks.aliases()).forEach(alias -> log.info("Load repositorio windows | Alias: "+alias));
	}

	@SneakyThrows
	public void loadPFX(Path pathFile, String password) {
		ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(pathFile.toFile()), password.toCharArray());

		Collections.list(ks.aliases()).forEach(alias -> log.info("Load PFX | Alias: "+alias));
	}

    @SneakyThrows
    public Map<String, X509Certificate> getCertificados() {
        return Collections.list(ks.aliases()).stream()
			.distinct()
            .collect(Collectors.toMap(alias -> alias, alias -> getCertificate(alias)));
    }

	@SneakyThrows
	public X509Certificate getCertificate(String alias) {
		return (X509Certificate) ks.getCertificate(alias);
	}

	@SneakyThrows
	public PrivateKey getPrivateKey(X509Certificate certificate) {
        Map<String, X509Certificate> certificados = getCertificados();

        for (String alias : certificados.keySet()) {
        	if (certificados.get(alias).getSerialNumber().compareTo(certificate.getSerialNumber()) == 0) {
        		return getPrivateKey(alias, null);
			}
		}

        throw new RuntimeException("Problema ao extrair a chave privada");
	}

	@SneakyThrows
	public PrivateKey getPrivateKey(@NonNull String alias, String password) {
		log.info("Tentando pegar private key | alias: "+alias);
		return (PrivateKey) ks.getKey(alias, (password != null) ? password.toCharArray() : null);
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