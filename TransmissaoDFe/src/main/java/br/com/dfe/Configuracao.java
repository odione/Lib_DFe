package br.com.dfe;

import br.com.dfe.certificado.Certificado;
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
    private Certificado certificado;
    private String idCSC;
    private String CSC;

    public boolean isAsync() {
        return "BA|SP".contains(uf.toUpperCase());
    }

    public boolean usaIntegrador() {
        return "CE".equalsIgnoreCase(uf);
    }
}
