package br.com.dfe.builder;

import br.com.dfe.api.DadosBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TConsSitNFe;

public class ConsultaNFBuilder implements DadosBuilder {
	
	private TConsSitNFe consulta;
	
	public ConsultaNFBuilder(DadosEmissor dadosEmissor) {
		consulta = new TConsSitNFe();
		consulta.setTpAmb(dadosEmissor.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setXServ("CONSULTAR");
	}
	
	public ConsultaNFBuilder comChave(String chave) {
		consulta.setChNFe(chave);
		return this;
	}

	@Override
	public Object build() {
		return consulta;
	}
}