package br.com.transmissor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.transmissor.configuracao.DadosEmissor;

public class DadosEmissorTest extends MainTest {

	@Autowired
	private DadosEmissor dadosEmissor;
	
	@Test
	public void verificaDadosIsNotNull() {
		assertThat(dadosEmissor).isNotNull();
	}
	
	@Test
	public void certificadoIsNotNull() {
		assertThat(dadosEmissor.getCertificado()).isNotNull();
	}
}