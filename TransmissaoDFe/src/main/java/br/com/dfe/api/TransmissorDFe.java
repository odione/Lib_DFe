package br.com.dfe.api;

import br.com.dfe.configuracao.DadosResposta;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;

public interface TransmissorDFe {
	
	/**
	 * 
	 * @param ambiente
	 * @param modelo
	 * @param tipoEmissao
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<TRetConsStatServ> statusServico(int ambiente, String modelo, TipoEmissao tipoEmissao) throws Exception;
	
	/**
	 * @param chave
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<TRetConsSitNFe> consultarNF(String chave) throws Exception;
	
	/**
	 * @param xmlTNFe
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<TRetEnviNFe> enviarNF(String xmlTNFe) throws Exception;
	
	/**
	 * @param xmlTEnvEvento
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<TRetEnvEvento> cancelarNF(String xmlTEnvEvento) throws Exception;
	
	/**
	 * @param xmlTEnvEvento
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<br.com.dfe.schema.cce.TRetEnvEvento> enviarCCe(String xmlTEnvEvento) throws Exception;
	
	/**
	 * @param xmlInutNFe
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<TRetInutNFe> inutilizar(String xmlInutNFe) throws Exception;
	
	/**
	 * @param envEvento
	 * @return DadosResposta
	 * @throws Exception
	 */
	DadosResposta<br.com.dfe.schema.generico.TRetEnvEvento> enviarEPEC(String envEvento) throws Exception;
}