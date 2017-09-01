package br.com.dfe.builder;

import br.com.dfe.api.DadosBuilder;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TConsStatServ;

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