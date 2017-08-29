package br.com.transmissor.util;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.dfe.schema.TNFe;
import br.com.transmissor.MainTest;
import br.com.transmissor.api.XMLConverter;

public class XmlConverterTest extends MainTest {

	@Autowired
	private XMLConverter converter;
	
	@Test
	public void convertNfe() throws Exception {
		String xml = Files.readAllLines(Paths.get("/home/odione/df_documentos/nfe/xml", "/nfe.xml")).get(0);
		
//		xml = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe42170813628140000150550010000006581005478498\"><ide><cUF>42</cUF><cNF>00547849</cNF><natOp>VENDA</natOp><mod>55</mod><serie>1</serie><nNF>658</nNF><dhEmi>2017-08-07T15:19:00-03:00</dhEmi><dhSaiEnt>2017-08-07T15:19:00-03:00</dhSaiEnt><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4202800</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>8</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>0</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.67</verProc></ide><emit><CNPJ>13628140000150</CNPJ><xNome>BRAZIL SISTEM SISTEMAS DE INF LTDA</xNome><xFant>BRAZIL SISTEM SISTEMAS DE INF LTDA</xFant><enderEmit><xLgr>RUA FELIPE SCHMIDT</xLgr><nro>894</nro><xCpl>BURRO</xCpl><xBairro>INSS</xBairro><cMun>4202800</cMun><xMun>BRACO DO NORTE</xMun><UF>SC</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>4836290049</fone></enderEmit><IE>256497150</IE><CRT>3</CRT></emit><dest><CNPJ>13628140000150</CNPJ><xNome>NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xNome><enderDest><xLgr>RUA JOSE SPECK</xLgr><nro>00515</nro><xBairro>NOSSA SENHORA DE FATIMA</xBairro><cMun>4202800</cMun><xMun>BRACO DO NORTE</xMun><UF>SC</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderDest><indIEDest>1</indIEDest><IE>256497150</IE></dest><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN/><xProd>GLP EM BOTIJAO P13</xProd><NCM>27111910</NCM><CEST>0600800</CEST><indEscala>S</indEscala><CFOP>5656</CFOP><uCom>UN</uCom><qCom>1.0000</qCom><vUnCom>80.0000000000</vUnCom><vProd>80.00</vProd><cEANTrib/><uTrib>UN</uTrib><qTrib>1.0000</qTrib><vUnTrib>80.0000000000</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP/><UFCons>SC</UFCons></comb></prod><imposto><ICMS><ICMS60><orig>0</orig><CST>60</CST></ICMS60></ICMS><IPI><cEnq>999</cEnq><IPINT><CST>53</CST></IPINT></IPI><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>90.00</vBC><vICMS>22.50</vICMS><vICMSDeson>0.00</vICMSDeson><vFCPUFDest>0.00</vFCPUFDest><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>80.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>1.80</vPIS><vCOFINS>2.70</vCOFINS><vOutro>0.00</vOutro><vNF>80.00</vNF></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag></pag><infAdic><infCpl>MD5 PAF-ECF: 4CA4CD4FC0087B30F9146498646ACE34;PRODUTO ESTA ADEQUADAMENTE ACONDICIONADO PARA SUPORTAR OS RISCOS NORMAIS DE;CARREGAMENTO, DESCARREGAMENTO, TRANSBORDO E TRANSPORTE. DECRETO 96.044/88,;ART. 22, II. ISENTO ICMS CONF. ART. 2 , INSISO VII, ANEXO 2, DECRETO 3017/89.;ACOMPANHA BLOCO DE NOTAS MODELO 02 SERIE D1;DO 1 AO 100 .;</infCpl></infAdic></infNFe></NFe>";
		
		System.out.println(xml+"\nQtd: "+xml.length());
		
		TNFe nfe = converter.toObj(xml, TNFe.class);
		assertThat(nfe).isNotNull();
		assertThat(nfe.getInfNFe().getId()).isNotEmpty();
	}
}