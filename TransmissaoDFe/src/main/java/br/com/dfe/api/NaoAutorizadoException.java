package br.com.dfe.api;

import br.com.dfe.ws.RetornoWS;

public class NaoAutorizadoException extends Exception {
	private static final long serialVersionUID = 1101797580255349854L;

	private RetornoWS retornoWS;
	
	public NaoAutorizadoException(String stat, String msg) {
		super(String.format("Solicitação não Aceita! Stat: %s Erro: %s",stat,msg));
		retornoWS = new RetornoWS(stat, msg);
	}
	
	public RetornoWS getRetornoWS() {
		return this.retornoWS;
	}
}