package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.schema.TConsSitNFe;
import br.com.dfe.utils.NFUtils;
import br.com.dfe.ws.ConsultaNFWS;

@Service("consultaNFService")
public class ConsultaNFService implements Servico {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private XMLConverter xmlConverter;
	
	@Override
	public MetodoWS getMetodo() {
		return new ConsultaNFWS();
	}

	@Override
	public String getDados(DadosRequisicao dadosArquivo) throws Exception {
		TConsSitNFe consulta = new TConsSitNFe();
		consulta.setTpAmb(dadosArquivo.getAmbienteStr());
		consulta.setVersao(dadosEmissor.getVersao());
		consulta.setChNFe(dadosArquivo.getRawFile());
		consulta.setXServ("CONSULTAR");
		return xmlConverter.toString(consulta, false);
	}
	
	@Override
	public DadosRequisicao createDados(String chave) {
		return DadosRequisicao.builder()
				.modelo(NFUtils.getModeloFromChave(chave))
				.ambiente(dadosEmissor.getAmbiente())
				.rawFile(chave)
				.tipoEmissao(TipoEmissao.getFromStr(NFUtils.getTipoEmissaoFromChave(chave)))
				.build();
	}
}