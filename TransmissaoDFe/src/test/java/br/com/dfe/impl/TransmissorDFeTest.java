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
	public void enviaNFC() throws Exception {
		String xml = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe41170910709825000133650010000011371004858240\"><ide><cUF>41</cUF><cNF>00485824</cNF><natOp>VENDA</natOp><mod>65</mod><serie>1</serie><nNF>1137</nNF><dhEmi>2017-10-02T09:17:00-03:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4106902</cMunFG><tpImp>4</tpImp><tpEmis>1</tpEmis><cDV>0</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.68</verProc></ide><emit><CNPJ>10709825000133</CNPJ><xNome>BRAZIL SISTEM SISTEMAS DE INF LTDA</xNome><xFant>BRAZIL SISTEM SISTEMAS DE INF LTDA</xFant><enderEmit><xLgr>RUA FELIPE SCHMIDT</xLgr><nro>894</nro><xBairro>INSS</xBairro><cMun>4106902</cMun><xMun>CURITIBA</xMun><UF>PR</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>4836290049</fone></enderEmit><IE>9047404115</IE><CRT>3</CRT></emit><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN/><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>27111910</NCM><CEST>0600800</CEST><indEscala>S</indEscala><EXTIPI>00</EXTIPI><CFOP>5656</CFOP><uCom>KG</uCom><qCom>1.0000</qCom><vUnCom>45.0000000000</vUnCom><vProd>45.00</vProd><cEANTrib/><uTrib>KG</uTrib><qTrib>1.0000</qTrib><vUnTrib>45.0000000000</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP>GLP</descANP><pGNn>0.0800</pGNn><pGNi>0.9200</pGNi><vPart>1.00</vPart><UFCons>PR</UFCons></comb></prod><imposto><ICMS><ICMS60><orig>0</orig><CST>60</CST></ICMS60></ICMS><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCPUFDest>0.00</vFCPUFDest><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>45.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>45.00</vNF></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>90</tPag><vPag>45.00</vPag></detPag></pag><infAdic><infCpl>MD5 PAF-ECF: 4CA4CD4FC0087B30F9146498646ACE34</infCpl></infAdic></infNFe></NFe>";
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