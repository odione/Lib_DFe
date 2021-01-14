package br.com.dfe;

import br.com.dfe.api.AssinaDocumento;
import br.com.dfe.api.TipoEmissao;
import br.com.dfe.certificado.Certificado;
import br.com.dfe.certificado.CertificadoHelper;
import br.com.dfe.impl.AssinaXML;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.util.XMLUtils;
import br.com.dfe.utils.ConverterUtils;
import org.apache.axiom.om.OMElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class TransmissorTest {

    private Configuracao configuracao;

    @BeforeEach
    void setup() throws Exception {
        if (this.configuracao == null) {
            this.configuracao = getConfiguracao();
        }
    }

    @Test
    public void statusServidor() throws Exception {
        TransmissorDFe transmissorDFe = new TransmissorDFe(configuracao);
        TRetConsStatServ retConsStatServ = transmissorDFe.statusServico(2, "65", TipoEmissao.NORMAL);
        assertEquals(retConsStatServ.getCStat(), "107");

        retConsStatServ = transmissorDFe.statusServico(1, "65", TipoEmissao.NORMAL);
        assertEquals(retConsStatServ.getCStat(), "107");

        retConsStatServ = transmissorDFe.statusServico(1, "55", TipoEmissao.NORMAL);
        assertEquals(retConsStatServ.getCStat(), "107");

        retConsStatServ = transmissorDFe.statusServico(2, "55", TipoEmissao.NORMAL);
        assertEquals(retConsStatServ.getCStat(), "107");
    }

    @Test
    @RepeatedTest(10)
    public void envioNFe() throws Exception {
        TransmissorDFe transmissorDFe = new TransmissorDFe(configuracao);
        String xmlTNFe = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe35191230590972000144550010000000801006494160\"><ide><cUF>35</cUF><cNF>00649416</cNF><natOp>VENDA</natOp><mod>55</mod><serie>1</serie><nNF>80</nNF><dhEmi>2019-12-05T11:44:00-03:00</dhEmi><dhSaiEnt>2019-12-05T11:44:00-03:00</dhSaiEnt><tpNF>1</tpNF><idDest>1</idDest><cMunFG>3551603</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>0</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.85</verProc></ide><emit><CNPJ>30590972000144</CNPJ><xNome>LUI COMERCIO DE GAS LTDA</xNome><xFant>LUI COMERCIO DE GAS LTDA</xFant><enderEmit><xLgr>RUA ANTONIO NOVAES</xLgr><nro>3238</nro><xBairro>CENTRO</xBairro><cMun>3551603</cMun><xMun>SERRA NEGRA</xMun><UF>SP</UF><CEP>13930000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderEmit><IE>662054651111</IE><CRT>1</CRT></emit><dest><CPF>21778749801</CPF><xNome>NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xNome><enderDest><xLgr>RUA SEBASTIAO RODRIGUES DE MORAIS</xLgr><nro>313</nro><xBairro>RES.VILLAGE</xBairro><cMun>3551603</cMun><xMun>SERRA NEGRA</xMun><UF>SP</UF><CEP>13930000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderDest><indIEDest>9</indIEDest></dest><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN>SEM GTIN</cEAN><xProd>GLP EM BOTIJAO P13</xProd><NCM>27111910</NCM><CEST>0601100</CEST><EXTIPI>00</EXTIPI><CFOP>5656</CFOP><uCom>UN</uCom><qCom>2.0000</qCom><vUnCom>1.0000000000</vUnCom><vProd>2.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>KG</uTrib><qTrib>26.0000</qTrib><vUnTrib>0.0769230769</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP>GLP</descANP><pGLP>90.0000</pGLP><pGNn>8.0000</pGNn><pGNi>2.0000</pGNi><vPart>1.00</vPart><UFCons>SP</UFCons></comb></prod><imposto><vTotTrib>0.44</vTotTrib><ICMS><ICMSSN500><orig>0</orig><CSOSN>500</CSOSN></ICMSSN500></ICMS><IPI><cEnq>999</cEnq><IPINT><CST>53</CST></IPINT></IPI><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>2.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>2.00</vNF><vTotTrib>0.44</vTotTrib></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>99</tPag><vPag>2.00</vPag></detPag></pag><infAdic><infCpl>ONU 1075 GAS LIQUEFEITO DE PETROLEO. CLASSE 2.1 RISCO SUB. 23. DECLARAMOS QUE OPRODUTO ESTA ADEQUADAMENTE ACONDICIONADO PARA SUPORTAR OS RISCOS NORMAIS DECARREGAMENTO, DESCARREGAMENTO, TRANSBORDO E TRANSPORTE. DECRETO 96.044/88,ART. 22, II. ISENTO ICMS CONF. ART. 2 , INSISO VII, ANEXO 2, DECRETO 3017/89.;Valor aprox. dos Tributos Federal: 0,44 Estadual: 0,00 Municipal: 0,00 Fonte: IBPT</infCpl></infAdic><infRespTec><CNPJ>13628140000150</CNPJ><xContato>HUDSON BORGES NESI</xContato><email>hudson@brazilsistem.com.br</email><fone>4836290077</fone></infRespTec></infNFe></NFe>";
        TRetEnviNFe tRetEnviNFe = transmissorDFe.enviaNf(xmlTNFe, a -> {});
        assertTrue(tRetEnviNFe.getCStat().matches("656|228"));
    }

    @Test
    public void nfceOffline() throws Exception {
        TransmissorDFe transmissorDFe = new TransmissorDFe(configuracao);
        String xmlTNFe = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe35191230590972000144650010000000451007509148\"><ide><cUF>35</cUF><cNF>00750914</cNF><natOp>VENDA</natOp><mod>65</mod><serie>1</serie><nNF>45</nNF><dhEmi>2019-12-05T11:48:00-03:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>3551603</cMunFG><tpImp>4</tpImp><tpEmis>9</tpEmis><cDV>8</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.85</verProc></ide><emit><CNPJ>30590972000144</CNPJ><xNome>LUI COMERCIO DE GAS LTDA</xNome><xFant>LUI COMERCIO DE GAS LTDA</xFant><enderEmit><xLgr>RUA ANTONIO NOVAES</xLgr><nro>3238</nro><xBairro>CENTRO</xBairro><cMun>3551603</cMun><xMun>SERRA NEGRA</xMun><UF>SP</UF><CEP>13930000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderEmit><IE>662054651111</IE><CRT>1</CRT></emit><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN>SEM GTIN</cEAN><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>27111910</NCM><CEST>0601100</CEST><EXTIPI>00</EXTIPI><CFOP>5656</CFOP><uCom>UN</uCom><qCom>4.0000</qCom><vUnCom>1.0000000000</vUnCom><vProd>4.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>KG</uTrib><qTrib>52.0000</qTrib><vUnTrib>0.0769230769</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP>GLP</descANP><pGLP>90.0000</pGLP><pGNn>8.0000</pGNn><pGNi>2.0000</pGNi><vPart>1.00</vPart><UFCons>SP</UFCons></comb></prod><imposto><vTotTrib>0.88</vTotTrib><ICMS><ICMSSN500><orig>0</orig><CSOSN>500</CSOSN></ICMSSN500></ICMS><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>4.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>4.00</vNF><vTotTrib>0.88</vTotTrib></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><indPag>0</indPag><tPag>01</tPag><vPag>4.00</vPag></detPag></pag><infAdic><infCpl>Valor aprox. dos Tributos Federal: 0,88 Estadual: 0,00 Municipal: 0,00 Fonte: IBPT</infCpl></infAdic><infRespTec><CNPJ>13628140000150</CNPJ><xContato>HUDSON BORGES NESI</xContato><email>hudson@brazilsistem.com.br</email><fone>4836290077</fone></infRespTec></infNFe></NFe>";
        String retorno = transmissorDFe.geraXMLContingenciaOffline(xmlTNFe);
        assertTrue(retorno.contains("tpEmis>9</tpEmis"));
    }

    @Test
    public void envioNFCe() throws Exception {
        TransmissorDFe transmissorDFe = new TransmissorDFe(configuracao);
        String xmlTNFe = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe35191230590972000144650010000000451007509148\"><ide><cUF>35</cUF><cNF>00750914</cNF><natOp>VENDA</natOp><mod>65</mod><serie>1</serie><nNF>45</nNF><dhEmi>2019-12-05T11:48:00-03:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>3551603</cMunFG><tpImp>4</tpImp><tpEmis>1</tpEmis><cDV>8</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.85</verProc></ide><emit><CNPJ>30590972000144</CNPJ><xNome>LUI COMERCIO DE GAS LTDA</xNome><xFant>LUI COMERCIO DE GAS LTDA</xFant><enderEmit><xLgr>RUA ANTONIO NOVAES</xLgr><nro>3238</nro><xBairro>CENTRO</xBairro><cMun>3551603</cMun><xMun>SERRA NEGRA</xMun><UF>SP</UF><CEP>13930000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderEmit><IE>662054651111</IE><CRT>1</CRT></emit><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN>SEM GTIN</cEAN><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>27111910</NCM><CEST>0601100</CEST><EXTIPI>00</EXTIPI><CFOP>5656</CFOP><uCom>UN</uCom><qCom>4.0000</qCom><vUnCom>1.0000000000</vUnCom><vProd>4.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>KG</uTrib><qTrib>52.0000</qTrib><vUnTrib>0.0769230769</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP>GLP</descANP><pGLP>90.0000</pGLP><pGNn>8.0000</pGNn><pGNi>2.0000</pGNi><vPart>1.00</vPart><UFCons>SP</UFCons></comb></prod><imposto><vTotTrib>0.88</vTotTrib><ICMS><ICMSSN500><orig>0</orig><CSOSN>500</CSOSN></ICMSSN500></ICMS><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>4.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>4.00</vNF><vTotTrib>0.88</vTotTrib></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><indPag>0</indPag><tPag>01</tPag><vPag>4.00</vPag></detPag></pag><infAdic><infCpl>Valor aprox. dos Tributos Federal: 0,88 Estadual: 0,00 Municipal: 0,00 Fonte: IBPT</infCpl></infAdic><infRespTec><CNPJ>13628140000150</CNPJ><xContato>HUDSON BORGES NESI</xContato><email>hudson@brazilsistem.com.br</email><fone>4836290077</fone></infRespTec></infNFe></NFe>";

        TNFe tnFe = XMLUtils.toObj(xmlTNFe, TNFe.class);
        tnFe.getInfNFe().getIde().setDhEmi(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'-03:00'")));
        xmlTNFe = XMLUtils.criaStrXML(tnFe);

        TRetEnviNFe tRetEnviNFe = transmissorDFe.enviaNf(xmlTNFe, a -> {});
        assertEquals("100", tRetEnviNFe.getCStat());
    }

    @Test
    public void consultaNFe() throws Exception {
        TransmissorDFe transmissorDFe = new TransmissorDFe(configuracao);
        TRetConsSitNFe consSitNFe = transmissorDFe.consultaNf("35191230590972000144550010000000801006494160");
        assertEquals("100", consSitNFe.getCStat());
    }

    @Test
    public void consultaNFCe() throws Exception {
        TransmissorDFe transmissorDFe = new TransmissorDFe(configuracao);
        TRetConsSitNFe retConsSitNFe = transmissorDFe.consultaNf("35191230590972000144650010000000451007509148");
        assertEquals("100", retConsSitNFe.getCStat());
    }

    @Test
    public void assinaNFe() throws Exception {
        AssinaDocumento assinaDocumento = new AssinaXML(configuracao.getCertificado());
        String xmlTNFe = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe42191113628140000150550010000028491009605747\"><ide><cUF>42</cUF><cNF>00960574</cNF><natOp>VENDA</natOp><mod>55</mod><serie>1</serie><nNF>2849</nNF><dhEmi>2019-11-13T11:41:00-03:00</dhEmi><dhSaiEnt>2019-11-13T11:41:00-03:00</dhSaiEnt><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4202800</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>7</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>0</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.84</verProc></ide><emit><CNPJ>13628140000150</CNPJ><xNome>BRAZIL SISTEM SITEMAS DE INFORMAT. LTDA</xNome><xFant>BRAZIL SISTEM SITEMAS DE INFORMAT. LTDA</xFant><enderEmit><xLgr>RUA FELIPE SCHMIDT</xLgr><nro>894</nro><xBairro>CENTRO</xBairro><cMun>4202800</cMun><xMun>BRACO DO NORTE</xMun><UF>SC</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>4836290077</fone></enderEmit><IE>256497150</IE><CRT>3</CRT></emit><dest><CNPJ>13628140000150</CNPJ><xNome>NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xNome><enderDest><xLgr>RUA JOSE SPECK</xLgr><nro>00515</nro><xBairro>NOSSA SENHORA DE FATIMA</xBairro><cMun>4202800</cMun><xMun>BRACO DO NORTE</xMun><UF>SC</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderDest><indIEDest>1</indIEDest><IE>256497150</IE></dest><autXML><CNPJ>06538909000427</CNPJ></autXML><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN>SEM GTIN</cEAN><xProd>GLP EM BOTIJAO P13</xProd><NCM>27111910</NCM><CEST>0600800</CEST><EXTIPI>00</EXTIPI><CFOP>5656</CFOP><uCom>UN</uCom><qCom>1.0000</qCom><vUnCom>80.0000000000</vUnCom><vProd>80.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>KG</uTrib><qTrib>13.0000</qTrib><vUnTrib>6.1538461538</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP>GLP</descANP><pGLP>90.0000</pGLP><pGNn>8.0000</pGNn><pGNi>2.0000</pGNi><vPart>80.00</vPart><UFCons>SC</UFCons></comb></prod><imposto><vTotTrib>17.54</vTotTrib><ICMS><ICMSST><orig>0</orig><CST>60</CST><vBCSTRet>580.00</vBCSTRet><pST>0.0000</pST><vICMSSubstituto>0.00</vICMSSubstituto><vICMSSTRet>180.00</vICMSSTRet><vBCSTDest>0.00</vBCSTDest><vICMSSTDest>0.00</vICMSSTDest></ICMSST></ICMS><IPI><cEnq>999</cEnq><IPINT><CST>53</CST></IPINT></IPI><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>80.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>80.00</vNF><vTotTrib>17.54</vTotTrib></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>99</tPag><vPag>80.00</vPag></detPag></pag><infAdic><infCpl>PRODUTO ESTA ADEQUADAMENTE ACONDICIONADO PARA SUPORTAR OS RISCOS NORMAIS DE;CARREGAMENTO, DESCARREGAMENTO, TRANSBORDO E TRANSPORTE. DECRETO 96.044/88,;ART. 22, II. ISENTO ICMS CONF. ART. 2 , INSISO VII, ANEXO 2, DECRETO 3017/89.;ACOMPANHA BLOCO DE NOTAS MODELO 02 SERIE D1;DO 1 AO 100 .;</infCpl></infAdic><infRespTec><CNPJ>13628140000150</CNPJ><xContato>HUDSON BORGES NESI</xContato><email>hudson@brazilsistem.com.br</email><fone>4836290077</fone></infRespTec></infNFe></NFe>";
        String assinado = assinaDocumento.assinarTNFe(xmlTNFe);
        System.out.println("Assinado: "+assinado);
    }

    @Test
    public void assinaEvento() throws Exception {
        AssinaDocumento assinaDocumento = new AssinaXML(configuracao.getCertificado());
        String xmlEvento = "<envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\"><idLote>1</idLote><evento versao=\"1.00\"><infEvento Id=\"ID1101105020012777464800017155001000000002100450482301\"><cOrgao>50</cOrgao><tpAmb>2</tpAmb><CNPJ>27774648000171</CNPJ><chNFe>50200127774648000171550010000000021004504823</chNFe><dhEvento>2020-01-14T10:31:23-03:00</dhEvento><tpEvento>110110</tpEvento><nSeqEvento>1</nSeqEvento><verEvento>1.00</verEvento><detEvento versao=\"1.00\"><descEvento>Carta de Correcao</descEvento><xCorrecao>testandoooooooooooooooooooooooooooooooo</xCorrecao><xCondUso>A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.</xCondUso></detEvento></infEvento></evento></envEvento>";
        String assinado = assinaDocumento.assinarEvento(xmlEvento);

        System.out.println("Assinado: "+assinado);

        assertFalse(assinado.contains("&#xd;"));
        assertFalse(assinado.contains("&#13;"));

        OMElement element = ConverterUtils.toOMElement(assinado);
        System.out.println("element: "+element.toString());
    }

    public Configuracao getConfiguracao() throws Exception {
        CertificadoHelper certificadoHelper = new CertificadoHelper();
        certificadoHelper.loadPFX(Paths.get("/home/odione/dsv/apps/sender/certs/rs_23456789.pfx"),"23456789");
        Certificado certificado = certificadoHelper.getCertificados().get(0);
        certificado.loadPrivateKey("23456789");

        return Configuracao.builder()
            .ambiente(2)
            .uf("RS")
            .certificado(certificado)
            .idCSC("1")
            .CSC("6f53f3f2-4702-41bb-9b61-55a70618be72")
            .build();
    }

    @Test
    void removeCharFromXML() {
        String xml = "<envEvento xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"1.00\"><idLote>1</idLote><evento versao=\"1.00\"><infEvento Id=\"ID1101105020012777464800017155001000000002100450482301\"><cOrgao>50</cOrgao><tpAmb>2</tpAmb><CNPJ>27774648000171</CNPJ><chNFe>50200127774648000171550010000000021004504823</chNFe><dhEvento>2020-01-14T10:31:23-03:00</dhEvento><tpEvento>110110</tpEvento><nSeqEvento>1</nSeqEvento><verEvento>1.00</verEvento><detEvento versao=\"1.00\"><descEvento>Carta de Correcao</descEvento><xCorrecao>testandoooooooooooooooooooooooooooooooo</xCorrecao><xCondUso>A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.</xCondUso></detEvento></infEvento><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#ID1101105020012777464800017155001000000002100450482301\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>H1hfMrDCRxwdt1e7PFPPxS8L2Zs=</DigestValue></Reference></SignedInfo><SignatureValue>m+SUPBragPh0PjnIzTSVxmOWf/5IjepMDBkH0qJi5HMMWgPOwudxQEWxTQAnt391/EyPWSZ/gqOM&#xd;" +
            "1tJh0U5flLSi1b+qrjar7F33sJ/GYQ8gtmL5cDxRJoDPhbaRGlrcOguodO2Q3p3pVQXQzCGdY/ID&#xd;" +
            "6BXxrY/1siEBePB4LUq5wCKfhhuZUNNxJr7qU9sLnyTL7wj0Zd2STDFVjw1tHX08KovUAT6PtXw3&#xd;" +
            "aFVAIT5LGvES6Mdoak/o101UgE/WDHkvFwamBOMUGTSCG/Sz//is/nb7lCkQfV51Npt6YoL5FUzN&#xd;" +
            "Rn4fJtW4Qkip+b5DdNZw6rAimlhFoo1F6j7dhQ==</SignatureValue><KeyInfo><X509Data><X509Certificate>MIIH4DCCBcigAwIBAgIILDIZEglkP3IwDQYJKoZIhvcNAQELBQAwgYkxCzAJBgNVBAYTAkJSMRMw&#xd;" +
            "EQYDVQQKEwpJQ1AtQnJhc2lsMTQwMgYDVQQLEytBdXRvcmlkYWRlIENlcnRpZmljYWRvcmEgUmFp&#xd;" +
            "eiBCcmFzaWxlaXJhIHYyMRIwEAYDVQQLEwlBQyBTT0xVVEkxGzAZBgNVBAMTEkFDIFNPTFVUSSBN&#xd;" +
            "dWx0aXBsYTAeFw0yMDAxMDYxNDQ3MjFaFw0yMDEyMTMxNzUzMDBaMIH7MQswCQYDVQQGEwJCUjET&#xd;" +
            "MBEGA1UEChMKSUNQLUJyYXNpbDE0MDIGA1UECxMrQXV0b3JpZGFkZSBDZXJ0aWZpY2Fkb3JhIFJh&#xd;" +
            "aXogQnJhc2lsZWlyYSB2MjESMBAGA1UECxMJQUMgU09MVVRJMRswGQYDVQQLExJBQyBTT0xVVEkg&#xd;" +
            "TXVsdGlwbGExFzAVBgNVBAsTDjIyNDI4MDI2MDAwMTc4MRowGAYDVQQLExFDZXJ0aWZpY2FkbyBQ&#xd;" +
            "SiBBMTE7MDkGA1UEAxMyQ0FSSUVMSSBEIEFWSUxBIE1JUkFOREEgMDE2NDc1MzgxODE6Mjc3NzQ2&#xd;" +
            "NDgwMDAxNzEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDUt8fY9YOLq/4puRDF+tww&#xd;" +
            "PpRSrfpEfyEUZ18pD/3npcH9rG0dEiEW4h+4X+weM5F9KQzp8JsZMj9ZSuaHTldRhOE0a5S+j9Td&#xd;" +
            "RzcES716Gyr8PBA8n5neuj/PNX8+wXGdCEP3Qh4+ixcbgZ30o5YLupiuvcr/3LF8f+UvLpN3IKr+&#xd;" +
            "2ERtfGWd/U+fFgdCD6DWYdLr4AWcbHGOKr9MELYAYJ5+meeYkEBWkVyy2rSY2+XxwAXkvs3U+wC1&#xd;" +
            "2Mk7gGnrqyJFEePM0sqxkbtRYFxffvdtyc78WeHnzp0PVNiU8dJnwDbmAH9d0f5Hs4tMkcXqnrro&#xd;" +
            "IghPJMF5xEDtF+VRAgMBAAGjggLWMIIC0jBUBggrBgEFBQcBAQRIMEYwRAYIKwYBBQUHMAKGOGh0&#xd;" +
            "dHA6Ly9jY2QuYWNzb2x1dGkuY29tLmJyL2xjci9hYy1zb2x1dGktbXVsdGlwbGEtdjEucDdiMB0G&#xd;" +
            "A1UdDgQWBBTet5z0rkhoP7DpqHEeqF3MB4jrajAJBgNVHRMEAjAAMB8GA1UdIwQYMBaAFDWuMRT2&#xd;" +
            "XtJ6T1j+NKgaZ5cKxJsHMF4GA1UdIARXMFUwUwYGYEwBAgEmMEkwRwYIKwYBBQUHAgEWO2h0dHBz&#xd;" +
            "Oi8vY2NkLmFjc29sdXRpLmNvbS5ici9kb2NzL2RwYy1hYy1zb2x1dGktbXVsdGlwbGEucGRmMIHe&#xd;" +
            "BgNVHR8EgdYwgdMwPqA8oDqGOGh0dHA6Ly9jY2QuYWNzb2x1dGkuY29tLmJyL2xjci9hYy1zb2x1&#xd;" +
            "dGktbXVsdGlwbGEtdjEuY3JsMD+gPaA7hjlodHRwOi8vY2NkMi5hY3NvbHV0aS5jb20uYnIvbGNy&#xd;" +
            "L2FjLXNvbHV0aS1tdWx0aXBsYS12MS5jcmwwUKBOoEyGSmh0dHA6Ly9yZXBvc2l0b3Jpby5pY3Bi&#xd;" +
            "cmFzaWwuZ292LmJyL2xjci9BQ1NPTFVUSS9hYy1zb2x1dGktbXVsdGlwbGEtdjEuY3JsMA4GA1Ud&#xd;" +
            "DwEB/wQEAwIF4DAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwgb4GA1UdEQSBtjCBs4Ef&#xd;" +
            "ZGlnaXRhaXNjZXJ0aWZpY2Fjb2VzQGdtYWlsLmNvbaAiBgVgTAEDAqAZExdDQVJJRUxJIEQgQVZJ&#xd;" +
            "TEEgTUlSQU5EQaAZBgVgTAEDA6AQEw4yNzc3NDY0ODAwMDE3MaA4BgVgTAEDBKAvEy0xNzAzMTk5&#xd;" +
            "MzAxNjQ3NTM4MTgxMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDCgFwYFYEwBAwegDhMMMDAwMDAw&#xd;" +
            "MDAwMDAwMA0GCSqGSIb3DQEBCwUAA4ICAQB1AwvutSVZmcRO33GmmFQSbqw+AXYlEjvbl+WM984O&#xd;" +
            "WQ7MApfia9LBW3cDKrU49PP1dAdKHq5E/Vl81sFOVw6Ii9udU3ToFwa8ZFVVbBO7pMy7i2dRyG3g&#xd;" +
            "KS8LL1Vti0DsZx46TIzTVxnUuIRN2x4HuSkaZtFd0KrUk1lYwE+/osRLCrhSWwppKb8C6NtNJKnp&#xd;" +
            "hYfjHe7FHUntuqPM1UgknPKVP/T2EMyC8m+B5WCH7OG461+9jDzzmMn5uY3LlHUSYesPoqlSZyCu&#xd;" +
            "t/jj/6ZXeKqn1Jq+o/k3SKY2zMTvinM/9WNutb3vIY76Q5YnmkjxF/yPdBNoU/M0BMHilNf9bcDX&#xd;" +
            "tEDod70pEUG3kzETTtJ3OIUx3D77OYYkuR1NUc4tDJom2eUJMXv3cVMCZop0JKiRHCz1iotGQKYt&#xd;" +
            "3beCNEfJqWLHOW72WajXpz7YcPfYCYjETkPocn+IVaLvjZZLzbPtkITchTtZAB5MNcRdOSYbb4Ms&#xd;" +
            "lcgYWwmbXcbXT5iH8phRju2nQHVq08u7cFNcO9Vzf4KoeXULo8J73ejC19RZ8YIDcsgdHRW2E1kt&#xd;" +
            "cPOnRZ5xWTcJLaa0VFi83dZTIfFzTYG8x5S8FLqL5kyyj/8c/Ie4E/rS0ibLA+hKyhKkpmZoHebA&#xd;" +
            "gpUCwz1B2XoV23yqAm+Pd81Lk9BCvdhlDQ==</X509Certificate></X509Data></KeyInfo></Signature></evento></envEvento>";

        System.out.println("XML:          |"+xml);
        String xmlAlterado = xml.replaceAll("&#xd;", "");
        System.out.println("XML Alterado: |"+xmlAlterado);
        Assertions.assertNotEquals(xmlAlterado, xml);
    }
}
