package br.com.dfe.api;

import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;

public interface TransmissorDFe {

	TRetConsStatServ statusServico() throws Exception;
	
	/**
	 * @param chave
	 * @return TRetConsSitNFe
	 * @throws Exception
	 */
	TRetConsSitNFe consultarNF(String chave) throws Exception;
	
	/**
	 * @param xmlTNFe
	 * @return TRetEnviNFe
	 * @throws Exception
	 */
	TRetEnviNFe enviarNF(String xmlTNFe) throws Exception;
	
	/**
	 * @param xmlTEnvEvento
	 * @return TRetEnvEvento
	 * @throws Exception
	 */
	TRetEnvEvento cancelarNF(String xmlTEnvEvento) throws Exception;
	
	/**
	 * @param xmlTEnvEvento
	 * @return TRetEnvEvento
	 * @throws Exception
	 */
	br.com.dfe.schema.cce.TRetEnvEvento enviarCCe(String xmlTEnvEvento) throws Exception;
	
	/**
	 * @param xmlInutNFe
	 * @return TRetInutNFe
	 * @throws Exception
	 */
	TRetInutNFe inutilizar(String xmlInutNFe) throws Exception;
	
	/**
	 * @param envEvento
	 * @return br.com.dfe.schema.generico.TRetEnvEvento
	 * @throws Exception
	 */
	br.com.dfe.schema.generico.TRetEnvEvento enviarEPEC(String envEvento) throws Exception;
//	Servico getServico();
}