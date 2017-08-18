package br.com.transmissor.utils.enumarator;

public enum VersaoEvento {
	
	VERSAO_100("1.00");
	
	private String versao;
	
	private VersaoEvento(String versao) {
		setVersao(versao);
	}

	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
}