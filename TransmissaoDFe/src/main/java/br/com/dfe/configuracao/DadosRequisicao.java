package br.com.dfe.configuracao;

import java.util.function.Function;

import br.com.dfe.api.TipoEmissao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DadosRequisicao {
	
	private String modelo;
	private String versao;
	private int ambiente;
	private TipoEmissao tipoEmissao;
	private String rawFile;
	private Function<DadosRequisicao, String> url;
	
	public String getAmbienteStr() {
		return String.valueOf(ambiente);
	}
	public void setTipoEmissaoInt(int tipo) {
		this.tipoEmissao = TipoEmissao.getFromInt(tipo);
	}
}
