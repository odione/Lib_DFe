package br.com.transmissor.utils.enumarator;

public enum VersaoDF {
	VERSAO_200("2.00"),
	VERSAO_310("3.10"),
	VERSAO_400("4.00");
	
	private String versao;
	
	VersaoDF(String versao){
		this.setVersao(versao);
	}

	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
}