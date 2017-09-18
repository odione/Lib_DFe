package br.com.dfe.api;

import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.canc.TRetEnvEvento;

public interface PosTransmissao {
	
	public static final String STAT_DENEGADA = "110|301|302|303";
	public static final String STAT_AUTORIZADA = "100|150|"+STAT_DENEGADA;
	public static final String STAT_INUTILIZADA = "102";
	public static final String STAT_EVENTO = "135|136";
	public static final String STAT_CANCELADA = "101|151|155";

	void validaRetEnvNFe(TRetEnviNFe retorno) throws NaoAutorizadoException;
	void validaRetEnvEvento(TRetEnvEvento retorno) throws NaoAutorizadoException;
	void validaRetEnvEvento(br.com.dfe.schema.cce.TRetEnvEvento retorno) throws NaoAutorizadoException;
	void validaRetInutNFe(TRetInutNFe retorno) throws NaoAutorizadoException;
	void validaRetConsSitNFe(TRetConsSitNFe retorno) throws NaoAutorizadoException;
}