package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.schema.TConsReciNFe;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.ws.RetornoAutorizacaoWS;
import lombok.val;

@Service("retornoAutorizacao")
public class RetornoAutorizacaoService implements Servico {
	
	@Autowired
	private XMLConverter mapper;
	
	@Override
	public MetodoWS getMetodo() {
		return new RetornoAutorizacaoWS();
	}

	@Override
	public String getDados(DadosRequisicao dadosArquivo) throws Exception {
		TRetEnviNFe retEnviNF = mapper.toObj(dadosArquivo.getRawFile(), TRetEnviNFe.class);
		val consultaRecibo = new TConsReciNFe();
		consultaRecibo.setVersao(retEnviNF.getVersao());
		consultaRecibo.setNRec(retEnviNF.getInfRec().getNRec());
		consultaRecibo.setTpAmb(retEnviNF.getTpAmb());
		
		return mapper.toString(consultaRecibo, false);
	}

	@Override
	public DadosRequisicao createDados(String raw) throws Exception {
		return DadosRequisicao.builder()
				.rawFile(raw)
				.build();
	}
}
