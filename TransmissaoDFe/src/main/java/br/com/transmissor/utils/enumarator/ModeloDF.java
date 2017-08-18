package br.com.transmissor.utils.enumarator;

public enum ModeloDF {
	MODELO_NFE(55),
	MODELO_NFCE(65);
	
	private int modelo;
	
	private ModeloDF(int modelo) {
		this.setModelo(modelo);
	}

	public int getModelo() {
		return modelo;
	}
	public void setModelo(int modelo) {
		this.modelo = modelo;
	}
	
	public static ModeloDF valorDe(String modelo){
		int iModelo = Integer.valueOf(modelo);
		if (MODELO_NFCE.getModelo() == iModelo) {
			return MODELO_NFCE;
		}
		return MODELO_NFE;
	}
}