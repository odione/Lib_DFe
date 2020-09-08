package br.com.dfe;

import br.com.dfe.utils.NFUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NFUtilsTest {

    @Test
    public void getModeloFromInut() {
        String xml = "<inutNFe xmlns=\"http://www.portalfiscal.inf.br/nfe\" versao=\"4.00\"><infInut Id=\"ID42191362814000015055001000000001000000005\"><tpAmb>2</tpAmb><xServ>INUTILIZAR</xServ><cUF>42</cUF><ano>19</ano><CNPJ>13628140000150</CNPJ><mod>55</mod><serie>1</serie><nNFIni>1</nNFIni><nNFFin>5</nNFFin><xJust>testandooo inutt</xJust></infInut></inutNFe>";
        String modelo = NFUtils.getModeloFromInutilizacao(xml);
        assertEquals("55", modelo);
    }

    @Test
    public void getChaveFromNFe() {
        String xml = "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe versao=\"4.00\" Id=\"NFe42191113628140000150550010000028491009605747\"><ide><cUF>42</cUF><cNF>00960574</cNF><natOp>VENDA</natOp><mod>55</mod><serie>1</serie><nNF>2849</nNF><dhEmi>2019-11-13T11:41:00-03:00</dhEmi><dhSaiEnt>2019-11-13T11:41:00-03:00</dhSaiEnt><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4202800</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>7</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>0</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.7.0.84</verProc></ide><emit><CNPJ>13628140000150</CNPJ><xNome>BRAZIL SISTEM SITEMAS DE INFORMAT. LTDA</xNome><xFant>BRAZIL SISTEM SITEMAS DE INFORMAT. LTDA</xFant><enderEmit><xLgr>RUA FELIPE SCHMIDT</xLgr><nro>894</nro><xBairro>CENTRO</xBairro><cMun>4202800</cMun><xMun>BRACO DO NORTE</xMun><UF>SC</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>4836290077</fone></enderEmit><IE>256497150</IE><CRT>3</CRT></emit><dest><CNPJ>13628140000150</CNPJ><xNome>NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xNome><enderDest><xLgr>RUA JOSE SPECK</xLgr><nro>00515</nro><xBairro>NOSSA SENHORA DE FATIMA</xBairro><cMun>4202800</cMun><xMun>BRACO DO NORTE</xMun><UF>SC</UF><CEP>88750000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais></enderDest><indIEDest>1</indIEDest><IE>256497150</IE></dest><autXML><CNPJ>06538909000427</CNPJ></autXML><det nItem=\"1\"><prod><cProd>000013</cProd><cEAN>SEM GTIN</cEAN><xProd>GLP EM BOTIJAO P13</xProd><NCM>27111910</NCM><CEST>0600800</CEST><EXTIPI>00</EXTIPI><CFOP>5656</CFOP><uCom>UN</uCom><qCom>1.0000</qCom><vUnCom>80.0000000000</vUnCom><vProd>80.00</vProd><cEANTrib>SEM GTIN</cEANTrib><uTrib>KG</uTrib><qTrib>13.0000</qTrib><vUnTrib>6.1538461538</vUnTrib><indTot>1</indTot><comb><cProdANP>210203001</cProdANP><descANP>GLP</descANP><pGLP>90.0000</pGLP><pGNn>8.0000</pGNn><pGNi>2.0000</pGNi><vPart>80.00</vPart><UFCons>SC</UFCons></comb></prod><imposto><vTotTrib>17.54</vTotTrib><ICMS><ICMSST><orig>0</orig><CST>60</CST><vBCSTRet>580.00</vBCSTRet><pST>0.0000</pST><vICMSSubstituto>0.00</vICMSSubstituto><vICMSSTRet>180.00</vICMSSTRet><vBCSTDest>0.00</vBCSTDest><vICMSSTDest>0.00</vICMSSTDest></ICMSST></ICMS><IPI><cEnq>999</cEnq><IPINT><CST>53</CST></IPINT></IPI><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>80.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>80.00</vNF><vTotTrib>17.54</vTotTrib></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>99</tPag><vPag>80.00</vPag></detPag></pag><infAdic><infCpl>PRODUTO ESTA ADEQUADAMENTE ACONDICIONADO PARA SUPORTAR OS RISCOS NORMAIS DE;CARREGAMENTO, DESCARREGAMENTO, TRANSBORDO E TRANSPORTE. DECRETO 96.044/88,;ART. 22, II. ISENTO ICMS CONF. ART. 2 , INSISO VII, ANEXO 2, DECRETO 3017/89.;ACOMPANHA BLOCO DE NOTAS MODELO 02 SERIE D1;DO 1 AO 100 .;</infCpl></infAdic><infRespTec><CNPJ>13628140000150</CNPJ><xContato>HUDSON BORGES NESI</xContato><email>hudson@brazilsistem.com.br</email><fone>4836290077</fone></infRespTec></infNFe></NFe>";
        String chave = NFUtils.getChaveFromNFe(xml);
        assertEquals("42191113628140000150550010000028491009605747", chave);
    }

    @Test
    public void getSerieFromChave() {
        String chave = "42191113628140000150550010000028491009605747";
        String serie = NFUtils.getSerieFromChave(chave);
        assertEquals("1", serie);
    }

    @Test
    public void getNumeroFromChave() {
        String chave = "42191113628140000150550010000028491009605747";
        String numero = NFUtils.getNumeroFromChave(chave);
        assertEquals("002849", numero);
    }
}
