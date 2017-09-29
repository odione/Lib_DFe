package br.com.dfe.api;

public enum TipoEmissao {

	NORMAL, EPEC, CONTINGENCIA_SV_AN, CONTINGENCIA_SV_RS, CONTINGENCIA_OFFLINE;
	
	public static TipoEmissao getFromInt(int tipo) {
		switch (tipo) {
			case 4:	return EPEC;
			case 6:	return CONTINGENCIA_SV_AN;
			case 7: return CONTINGENCIA_SV_RS;
			case 9: return CONTINGENCIA_OFFLINE;
		default:
			return NORMAL;
		}
	}
	
	public static boolean isContingenciaOnLine(TipoEmissao tipo) {
		return (tipo.equals(CONTINGENCIA_SV_AN) || tipo.equals(CONTINGENCIA_SV_RS));
	}
}