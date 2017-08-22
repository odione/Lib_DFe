package br.com.transmissor.api;

import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.cce.TRetEnvEvento;

public interface PosTransmissao {

	void validaRetEnvNFe(TRetEnviNFe retorno) throws Exception;
	void validaRetEnvEvento(TRetEnvEvento retorno) throws Exception;
	void validaRetEnvEvento(br.com.dfe.schema.canc.TRetEnvEvento retorno) throws Exception;
	void validaRetInutNFe(TRetInutNFe retorno) throws Exception;
}