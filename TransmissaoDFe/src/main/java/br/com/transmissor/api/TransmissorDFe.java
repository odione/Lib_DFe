package br.com.transmissor.api;

import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;

public interface TransmissorDFe {

	TRetConsStatServ statusServico() throws Exception;
	
	TRetConsSitNFe consultarNF(String chave) throws Exception;
	
	TRetEnviNFe enviarNF(String xmlTNFe) throws Exception;
	
	TRetEnvEvento cancelarNF(String xmlTEnvEvento) throws Exception;
	
	br.com.dfe.schema.cce.TRetEnvEvento enviarCCe(String xmlTEnvEvento) throws Exception;
	
	TRetInutNFe inutilizar(String xmlInutNFe) throws Exception;
}