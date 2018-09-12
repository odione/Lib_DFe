package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.builder.StatusServidorBuilder;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;

@Service("statusService")
public class StatusServidorService implements Servico {

	@Autowired
	@Qualifier("statusWS")
	private MetodoWS metodoWS;
	
	@Autowired private StatusServidorBuilder builder;
	@Autowired private XMLConverter xmlConverter;
	
	@Override
	public MetodoWS getMetodo() {
		return metodoWS;
	}

	@Override
	public String getDados() throws Exception {
		return xmlConverter.toString(builder.build(), false);
	}
}