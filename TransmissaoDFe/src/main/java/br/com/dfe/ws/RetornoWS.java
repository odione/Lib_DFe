package br.com.dfe.ws;

import java.io.Serializable;

import lombok.Data;

@Data
public class RetornoWS implements Serializable {
	private static final long serialVersionUID = 4000779961065466175L;

	private String stat;
	private String msg;

	public RetornoWS(String stat, String msg) {
		super();
		this.stat = stat;
		this.msg = msg;
	}
}