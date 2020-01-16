package br.com.dfe.api;

import lombok.Getter;

@Getter
public class NaoAutorizadoException extends Exception {
	private static final long serialVersionUID = 1101797580255349854L;

	private String stat;
	private String msgWS;
	
	public NaoAutorizadoException(String stat, String msg) {
		super(String.format("Stat: %s Erro: %s",stat,msg));
		this.stat = stat;
		this.msgWS = msg;
	}
}