package br.com.transmissor.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.transmissor.MainTest;

public class StatusServidorServiceTest extends MainTest {
	
	@Autowired
	private StatusServidorService service;
	
	@Test
	public void consulta() throws Exception {
		Assertions.assertThat(service.getMetodo()).isNotNull();
	}
}