package br.com.transmissor.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.transmissor.MainTest;
import br.com.transmissor.util.ConfiguraTeste;

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
		assertThat(service.getUrlStatusServico()).isNotEmpty();
	}
}