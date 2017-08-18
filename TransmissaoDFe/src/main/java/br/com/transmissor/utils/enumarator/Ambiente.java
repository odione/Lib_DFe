package br.com.transmissor.utils.enumarator;

public enum Ambiente {
	HOMOLOGACAO(2),
	PRODUCAO(1);
	
	private int ambiente;
	
	private Ambiente(int ambiente) {
		setAmbiente(ambiente);
	}

	public int getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(int ambiente) {
		this.ambiente = ambiente;
	}
	
	public static Ambiente valorDe(int ambiente) {
		if (ambiente == 1) {
			return PRODUCAO;
		} else {
			return HOMOLOGACAO;
		}
	}
	
	public static Ambiente valorDe(String ambiente) {
		return valorDe(Integer.valueOf(ambiente));
	}
}