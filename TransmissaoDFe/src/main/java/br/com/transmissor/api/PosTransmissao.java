package br.com.transmissor.api;

import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;

public interface PosTransmissao {

	void validaRetEnvNFe(TRetEnviNFe retorno) throws NaoAutorizadoException;
	void validaRetEnvEvento(TRetEnvEvento retorno) throws NaoAutorizadoException;
	void validaRetEnvEvento(br.com.dfe.schema.cce.TRetEnvEvento retorno) throws NaoAutorizadoException;
	void validaRetInutNFe(TRetInutNFe retorno) throws NaoAutorizadoException;
}