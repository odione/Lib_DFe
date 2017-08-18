package br.com.transmissor.utils.enumarator;

public enum TipoEmissao {
	
	NORMAL(1),
	CONTINGENCIA_SVCAN(6),
	CONTINGENCIA_SVRS(7),
	CONTINGENCIA_OFFLINE(9);
	
	private int tipo;
	
	private TipoEmissao(int tipo){
		setTipo(tipo);
	}
	
	public static TipoEmissao valorDe(int tipo){
		for (TipoEmissao tipoEmissao : TipoEmissao.values()) {
			if (tipoEmissao.getTipo() == tipo) {
				return tipoEmissao;
			}
		}
		return NORMAL;
	}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}