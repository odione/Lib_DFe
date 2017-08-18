package br.com.transmissor.utils.enumarator;

public enum TipoEvento {
	
	CANCELAMENTO("110111"),
	CCE("110110");
	
	private String tipo;
	
	private TipoEvento(String tipo) {
		setTipo(tipo);
	}
	
	public static TipoEvento valorDe(String tipo) {
		for (TipoEvento evento : TipoEvento.values()) {
			if (evento.getTipo().equals(tipo)) {
				return evento;
			}
		}
		return CANCELAMENTO;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}