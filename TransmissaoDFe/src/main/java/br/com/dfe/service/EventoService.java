package br.com.dfe.service;

import static br.com.dfe.utils.NFUtils.getChaveFromEvento;
import static br.com.dfe.utils.NFUtils.getModeloFromChave;
import static br.com.dfe.utils.NFUtils.getTipoEmissaoFromChave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.MetodoWS;
import br.com.dfe.api.Servico;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.configuracao.DadosEmissor;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.ws.EventoWS;

@Service("evento")
public class EventoService implements Servico {
	
	@Autowired private DadosEmissor dadosEmissor;
	@Autowired private AssinaDocumento assinador;

	@Override
	public MetodoWS getMetodo() {
		return new EventoWS();
	}

	@Override
	public String getDados(DadosRequisicao dadosArquivo) throws Exception {
		return assinador.assinarEvento(dadosArquivo.getRawFile(), dadosEmissor.getCertificado(), dadosEmissor.getPrivateKey());
	}

	@Override
	public DadosRequisicao createDados(String raw) throws Exception {
		String chave = getChaveFromEvento(raw);
		return DadosRequisicao.builder()
				.ambiente(dadosEmissor.getAmbiente())
				.modelo(getModeloFromChave(chave))
				.tipoEmissao(TipoEmissao.getFromStr(getTipoEmissaoFromChave(chave)))
				.rawFile(raw)
				.build();
	}
}