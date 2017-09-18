package br.com.dfe.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.dfe.MainTest;
import br.com.dfe.api.TransmissorDFe;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.cce.TRetEnvEvento;
import br.com.dfe.util.ConfiguraTeste;

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
		TRetConsStatServ ret = transmissor.statusServico();
		assertThat(ret).isNotNull();
		
		assertThat(ret.getCStat()).isEqualTo("107");
	}
	
	@Test
	public void consultaNF() throws Exception {
		TRetConsSitNFe ret = transmissor.consultarNF("41170910709825000133550010000007171001565563");
		assertThat(ret).isNotNull();
		
		assertThat(ret.getCStat()).isEqualTo("100");
	}
	
	@Test
	public void enviaNF() throws Exception {
		String xml = Files.readAllLines(Paths.get("/home/odione/df_documentos/nfe/xml", "/nfe.xml")).get(0);
		TRetEnviNFe ret = transmissor.enviarNF(xml);
		assertThat(ret).isNotNull();
		
		assertThat(ret.getCStat()).isEqualTo("228");
		assertThat(ret.getProtNFe()).isNull();
	}
	
	@Test
	public void enviaCCe() throws Exception {
		String xml = Files.readAllLines(Paths.get("/home/odione/df_documentos/nfe/xml", "/cce.xml")).get(0);
		TRetEnvEvento ret = transmissor.enviarCCe(xml);
		
		assertThat(ret).isNotNull();
		assertThat(ret.getCStat()).isEqualTo("128");
		assertThat(ret.getRetEvento()).isNotNull();
		assertThat(ret.getRetEvento().get(0).getInfEvento().getCStat()).isNotNull();
	}
	
	@Test
	public void enviaCancelamento() throws Exception {
		String xml = Files.readAllLines(Paths.get("/home/odione/df_documentos/nfe/xml", "/canc.xml")).get(0);
		br.com.dfe.schema.canc.TRetEnvEvento ret = transmissor.cancelarNF(xml);
		
		assertThat(ret).isNotNull();
		assertThat(ret.getCStat()).isEqualTo("128");
		assertThat(ret.getRetEvento()).isNotNull();
		assertThat(ret.getRetEvento().get(0).getInfEvento().getCStat()).isNotNull();
	}
	
	@Test
	public void enviaInut() throws Exception {
		String xml = Files.readAllLines(Paths.get("/home/odione/df_documentos/nfe/xml", "/inut.xml")).get(0);
		TRetInutNFe ret = transmissor.inutilizar(xml);
		
		assertThat(ret).isNotNull();
		assertThat(ret.getInfInut()).isNotNull();
		assertThat(ret.getInfInut().getCStat()).isNotNull();
	}
}