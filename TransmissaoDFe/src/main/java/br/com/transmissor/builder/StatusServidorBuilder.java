package br.com.transmissor.builder;

import br.com.dfe.schema.TConsStatServ;
import br.com.transmissor.api.DadosBuilder;
import br.com.transmissor.configuracao.DadosEmissor;

public class StatusServidorBuilder implements DadosBuilder {
	
	private TConsStatServ consulta;
	
	public StatusServidorBuilder(DadosEmissor dadosEmissor) {
		consulta = new TConsStatServ();
		consulta.setCUF(dadosEmissor.getUfCodigo());
		consulta.setTpAmb(dadosEmissor.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setXServ("STATUS");
	}

	@Override
	public Object build() {
		return consulta;
	}
}