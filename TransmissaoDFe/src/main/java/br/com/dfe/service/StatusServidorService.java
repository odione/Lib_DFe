package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.schema.TConsStatServ;
import br.com.dfe.ws.StatusServidorWS;

@Service("statusService")
public class StatusServidorService implements Servico {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	
	@Override
	public MetodoWS getMetodo() {
		return new StatusServidorWS();
	}

	@Override
	public String getDados(DadosRequisicao dados) throws Exception {
		TConsStatServ consulta = new TConsStatServ();
		consulta.setCUF(dadosEmissor.getUfCodigo());
		consulta.setTpAmb(dados.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setXServ("STATUS");
		return xmlConverter.toString(consulta, false);
	}

	@Override
	public DadosRequisicao createDados(String xml) {
		return new DadosRequisicao();
	}
}