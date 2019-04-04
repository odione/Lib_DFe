package br.com.dfe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.XMLConverter;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.schema.TInutNFe;
import br.com.dfe.ws.InutilizacaoWS;

@Service("inutilizacao")
public class InutilizacaoService implements Servico {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private AssinaDocumento assinador;
	@Autowired private XMLConverter xmlConverter;
	
	@Override
	public MetodoWS getMetodo() {
		return new InutilizacaoWS();
	}

	@Override
	public String getDados(DadosRequisicao dadosArquivo) throws Exception {
		return assinador.assinarInutilizada(dadosArquivo.getRawFile(), dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
	}

	@Override
	public DadosRequisicao createDados(String raw) throws Exception {
		TInutNFe inutNFe = xmlConverter.toObj(raw, TInutNFe.class);
		return DadosRequisicao.builder()
				.rawFile(raw)
				.versao(inutNFe.getVersao())
				.modelo(inutNFe.getInfInut().getMod())
				.ambiente(Integer.parseInt(inutNFe.getInfInut().getTpAmb()))
				.tipoEmissao(dadosEmissor.getTipoEmissao())
				.build();
	}
}