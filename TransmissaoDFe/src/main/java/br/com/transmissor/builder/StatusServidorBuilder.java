package br.com.transmissor.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dfe.schema.TConsStatServ;
import br.com.transmissor.api.DadosBuilder;
import br.com.transmissor.api.XMLConverter;
import br.com.transmissor.configuracao.DadosEmissor;

@Component("statusBuilder")
public class StatusServidorBuilder implements DadosBuilder {
	
	private TConsStatServ consulta;
	
	@Autowired
	private DadosEmissor dadosEmissor;
	
	@Autowired
	private XMLConverter converter;
	
	public StatusServidorBuilder() {
		consulta = new TConsStatServ();
		consulta.setCUF(dadosEmissor.getUfCodigo());
		consulta.setTpAmb(dadosEmissor.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setXServ("STATUS");
	}

	@Override
	public String build() {
		return converter.toString(consulta, true);
	}
}