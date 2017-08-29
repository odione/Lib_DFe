package br.com.transmissor.api;

public class NaoAutorizadoException extends Exception {
	private static final long serialVersionUID = 1101797580255349854L;

	public NaoAutorizadoException(String stat, String msg) {
		super(String.format("Solicitação não Aceita! Stat: %s Erro: %s",stat,msg));
	}
}