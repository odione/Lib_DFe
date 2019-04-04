package br.com.dfe.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.dfe.MainTest;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.configuracao.DadosRequisicao;
import br.com.dfe.utils.ConfiguraTeste;

public class UrlServiceTest extends MainTest {
	
	@Autowired
	private URLService service;
	
	@Autowired
	private ConfiguraTeste configura;
	
	@Before
	public void setup() throws Exception {
		configura.configuraDadosEmissor();
	}
	
	@Test
	public void getUrlStatus() throws Exception {
		DadosRequisicao dados = DadosRequisicao.builder()
				.ambiente(2)
				.modelo("65")
				.tipoEmissao(TipoEmissao.NORMAL)
				.build();
		assertThat(service.getUrlStatusServico(dados)).isNotEmpty();
	}
}