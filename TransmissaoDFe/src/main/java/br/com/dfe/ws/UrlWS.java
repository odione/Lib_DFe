package br.com.dfe.ws;

import java.io.Serializable;

import lombok.Data;

@Data
public class UrlWS implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String uf;
	private String homologacao;
	private String producao;
}