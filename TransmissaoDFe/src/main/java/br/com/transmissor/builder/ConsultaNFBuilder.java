package br.com.transmissor.builder;

import br.com.dfe.schema.situacao.TConsSitNFe;
import br.com.transmissor.api.DadosBuilder;
import br.com.transmissor.configuracao.DadosEmissor;

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