package br.com.dfe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Configuracao {
    private String uf;
    private int ambiente;
    private X509Certificate certificado;
    private PrivateKey privateKey;
    private String pathCacerts;
    private String idCSC;
    private String CSC;

    public boolean isAsync() {
        return "BA|SP".contains(uf.toUpperCase());
    }
}
