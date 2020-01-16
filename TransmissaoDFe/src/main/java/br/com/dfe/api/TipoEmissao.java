package br.com.dfe.api;

import lombok.NonNull;

public enum TipoEmissao {

	NORMAL, EPEC, CONTINGENCIA_SV_AN, CONTINGENCIA_SV_RS, CONTINGENCIA_OFFLINE;
	
	public static TipoEmissao of(int tipo) {
		switch (tipo) {
			case 4:	return EPEC;
			case 6:	return CONTINGENCIA_SV_AN;
			case 7: return CONTINGENCIA_SV_RS;
			case 9: return CONTINGENCIA_OFFLINE;
		default:
			return NORMAL;
		}
	}
	
	public static TipoEmissao of(String tipoStr) {
		return of(Integer.parseInt(tipoStr));
	}
	
	public static boolean isContingenciaOnLine(@NonNull TipoEmissao tipo) {
		return (tipo.equals(CONTINGENCIA_SV_AN) || tipo.equals(CONTINGENCIA_SV_RS));
	}

	public static boolean isContingenciaOffline(@NonNull String tipoEmissao) {
		return tipoEmissao.equals("9");
	}
}