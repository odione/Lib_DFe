package br.com.transmissor.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLErrorHandler implements ErrorHandler {

	private List<String> listaComErrosDeValidacao;  
     
    public XMLErrorHandler() {
    	this.listaComErrosDeValidacao = new ArrayList<String>();
    }     
    
    public void error(SAXParseException exception) throws SAXException {  
        if (isError(exception)) {  
            listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));  
        }  
    }  
  
    public void fatalError(SAXParseException exception) throws SAXException {  
        listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));  
    }  
  
    public void warning(SAXParseException exception) throws SAXException {  
        listaComErrosDeValidacao.add(tratamentoRetorno(exception.getMessage()));  
    }  
	  
    private String tratamentoRetorno(String message) {  
        message = message.replaceAll("cvc-type.3.1.3:", "");  
        message = message.replaceAll("cvc-complex-type.2.4.a:", "");  
        message = message.replaceAll("cvc-complex-type.2.4.b:", "");  
        message = message.replaceAll("The value", "O valor");  
        message = message.replaceAll("of element", "do campo");  
        message = message.replaceAll("is not valid", "não é valido");  
        message = message.replaceAll("Invalid content was found starting with element", "Encontrado o campo");  
        message = message.replaceAll("One of", "Campo(s)");  
        message = message.replaceAll("is expected", "é obrigatorio");  
        message = message.replaceAll("\\{", "");  
        message = message.replaceAll("\\}", "");  
        message = message.replaceAll("\"", "");  
        message = message.replaceAll("http://www.portalfiscal.inf.br/nfe:", "");
        // altera nome dos campos  
        message = message.replaceAll("cUF", "Código da UF do emitente");  
        message = message.replaceAll("natOp", "Descrição do CFOP");  
        message = message.replaceAll("IndPag", "Forma de Pagamento");  
        message = message.replaceAll("nNF", "Número da Nota Fiscal");  
        message = message.replaceAll("dEmi", "Data de Emissão da Nota Fiscal Eletrônica");  
        message = message.replaceAll("dSaiEnt", "Data da Nota Fiscal");  
        message = message.replaceAll("tpNF", "Tipo da Operação");  
        message = message.replaceAll("cMunFG", "Código do Municipio do emitente");  
        message = message.replaceAll("tpImp", "Formato de impressão do DANFE");  
        message = message.replaceAll("tpEmis", "Tipo de Emissão da Nota Fiscal Eletrônica");  
        message = message.replaceAll("finNFe", "Finalidade da emissão da Nota Fiscal Eletrônica");  
        message = message.replaceAll("xNome", "Razão Social");  
        message = message.replaceAll("xFant", "Nome Fantasia");  
        message = message.replaceAll("xLgr", "Logradouro");  
        message = message.replaceAll("nro", "Número");  
        message = message.replaceAll("xCpl", "Complemento");  
        message = message.replaceAll("xBairro", "Bairro");  
        message = message.replaceAll("cMun", "Código do Município");  
        message = message.replaceAll("xMun", "Nome do Município");  
        message = message.replaceAll("cPais", "Código do País");  
        message = message.replaceAll("xPais", "Nome do País");  
        message = message.replaceAll("IE", "Inscrição Estadual");  
        message = message.replaceAll("CRT", "Código de Regime Tributário");  
        message = message.replaceAll("nItem", "Número de Itens");  
        message = message.replaceAll("cProd", "Código do Produto");  
        message = message.replaceAll("xProd", "Descrição do Produto");  
        message = message.replaceAll("NCM", "Classificação Fiscal");  
        message = message.replaceAll("uCom", "Unidade");  
        message = message.replaceAll("qCom", "Quantidade");  
        message = message.replaceAll("vUnCom", "Valor Unitário");  
        message = message.replaceAll("vProd", "Valor Total dos Produtos");  
        message = message.replaceAll("uTrib", "Unidade");  
        message = message.replaceAll("qTrib", "Quantidade");  
        message = message.replaceAll("vUnTrib", "Valor Unitário");  
        message = message.replaceAll("pCredSN", "I.C.M.S.");  
        message = message.replaceAll("vCredICMSSN", "Valor de Crédito do I.C.M.S.");  
        message = message.replaceAll("vNF", "Valor Total da Nota");
        return message.trim();  
    }  
  
    public List<String> getListaComErrosDeValidacao() {  
        return listaComErrosDeValidacao;  
    }  
  
    private boolean isError(SAXParseException exception) {
    	return ! (exception.getMessage().startsWith("cvc-pattern-valid") ||  
            exception.getMessage().startsWith("cvc-maxLength-valid") ||  
            exception.getMessage().startsWith("cvc-datatype"));
    }
}