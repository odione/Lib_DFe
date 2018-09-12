package br.com.dfe.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.schema.TConsStatServ;
import br.com.dfe.api.DadosBuilder;

@Component("statusBuilder")
public class StatusServidorBuilder implements DadosBuilder<TConsStatServ> {
	
	private TConsStatServ consulta;
	
	@Autowired
	private DadosEmissor dadosEmissor;
	
	public void buildConsulta() {
		consulta = new TConsStatServ();
		consulta.setCUF(dadosEmissor.getUfCodigo());
		consulta.setTpAmb(dadosEmissor.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setXServ("STATUS");
	}

	@Override
	public TConsStatServ build() {
		buildConsulta();
		return consulta;
	}
}