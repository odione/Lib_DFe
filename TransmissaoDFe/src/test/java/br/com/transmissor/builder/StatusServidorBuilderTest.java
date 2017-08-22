package br.com.transmissor.builder;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.transmissor.MainTest;
import br.com.transmissor.api.DadosBuilder;

public class StatusServidorBuilderTest extends MainTest {
	
	@Autowired
	private DadosBuilder builder;

	@Test
	public void verificaBuilder() {
		Assertions.assertThat(builder).isNotNull();
	}
}