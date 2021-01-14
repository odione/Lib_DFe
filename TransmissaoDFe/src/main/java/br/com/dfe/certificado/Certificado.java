package br.com.dfe.certificado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
public class Certificado {
    private String alias;
    private X509Certificate certificate;
    private PrivateKey privateKey;
    private KeyStore keyStore;

    @SneakyThrows
    public void loadPrivateKey(String password) {
        char[] pass = Objects.isNull(password) ? null : password.toCharArray();
        this.privateKey = (PrivateKey) keyStore.getKey(alias, pass);
    }
}
