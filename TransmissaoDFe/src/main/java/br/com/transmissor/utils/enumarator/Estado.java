package br.com.transmissor.utils.enumarator;

public enum Estado {
	
	RONDONIA("RO",11),
	ACRE("AC",12),
	AMAZONAS("AM",13),
	RORAIMA("RR",14),
	PARA("PA",15),
	AMAPA("AP",16),
	TOCANTINS("TO",17),
	MARANHAO("MA",21),
	PIAUI("PI",22),
	CEARA("CE",23),
	RIO_GRANDE_DO_NORTE("RN",24),
	PARAIBA("PB",25),
	PERNAMBUCO("PE",26),
	ALAGOAS("AL",27),
	SERGIPE("SE",28),
	BAHIA("BA",29),
	MINAS_GERAIS("MG",31),
	ESPIRITO_SANTO("ES",32),
	RIO_DE_JANEIRO("RJ",33),
	SAO_PAULO("SP",35),
	PARANA("PR",41),
	SANTA_CATARINA("SC",42),
	RIO_GRANDE_DO_SUL("RS",43),
	MATO_GROSSO_DO_SUL("MS",50),
	MATO_GROSSO("MT",51),
	GOIAS("GO",52),
	DISTRITO_FEDERAL("DF",53);
	
	private String UF;
	private int codigoUF;
	
	Estado(String Uf,int codigoUF) {
		this.UF = Uf;
		this.codigoUF = codigoUF;
	}
	
	public static Estado valorDe(String ufSigla) {
		for (Estado est : Estado.values()) {
			if (est.getUF().equals(ufSigla)) {
				return est;
			}
		}
		return null;
	}
	
	public static Estado valorDe(int ufCod) {
		for (Estado est : Estado.values()) {
			if (est.getCodigoUF() == ufCod) {
				return est;
			}
		}
		return null;
	}

	public String getUF() {
		return UF;
	}
	public void setUF(String uF) {
		UF = uF;
	}
	public int getCodigoUF() {
		return codigoUF;
	}
	public void setCodigoUF(int codigoUF) {
		this.codigoUF = codigoUF;
	}
}
