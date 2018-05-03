package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.schema.TConsReciNFe;
import br.com.dfe.schema.TRetEnviNFe;
import lombok.val;

@Service("retornoAutorizacao")
public class RetornoAutorizacaoService implements Servico {
	
	@Autowired
	private XMLConverter mapper;
	
	@Autowired
	@Qualifier("retornoAutorizacaoWS")
	private MetodoWS metodoWS;
	
	private TRetEnviNFe retEnviNF;

	@Override
	public MetodoWS getMetodo() {
		return metodoWS;
	}

	@Override
	public String getDados() throws Exception {
		val dados = new TConsReciNFe();
		dados.setVersao(retEnviNF.getVersao());
		dados.setNRec(retEnviNF.getInfRec().getNRec());
		dados.setTpAmb(retEnviNF.getTpAmb());
		
		return mapper.toString(dados, false);
	}
	
	public RetornoAutorizacaoService setRetEnviNF(TRetEnviNFe retorno) {
		this.retEnviNF = retorno;
		return this;
	}
}
