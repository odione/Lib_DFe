package br.com.dfe.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.dfe.MainTest;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.api.TransmissorDFe;
import br.com.dfe.configuracao.DadosResposta;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.utils.ConfiguraTeste;

public class TransmissorDFeTest extends MainTest {

	@Autowired
	private TransmissorDFe transmissor;
	
	@Autowired
	private ConfiguraTeste configura;
	
	@Before
	public void setup() throws Exception {
		configura.configuraDadosEmissor();
		configura.configuraConexaoSegura();
	}
	
	@Test
	public void statusServidor() throws Exception {
		DadosResposta<TRetConsStatServ> ret = transmissor.statusServico(1, "55", TipoEmissao.NORMAL);
		assertThat(ret).isNotNull();
		
		assertThat(ret.getRetorno().getCStat()).isEqualTo("107");
	}
}