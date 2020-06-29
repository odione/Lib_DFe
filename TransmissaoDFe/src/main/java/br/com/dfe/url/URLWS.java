package br.com.dfe.url;

import br.com.dfe.api.TipoEmissao;
import br.com.dfe.url.Operacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class URLWS {
    private String producao;
    private String homologacao;
    private TipoEmissao tipoEmissao;
    private String modelo;
    private String ufs;
    private Operacao operacao;
}
