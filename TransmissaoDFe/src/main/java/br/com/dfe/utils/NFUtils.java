package br.com.dfe.utils;

import br.com.utils.StringUtils;

public class NFUtils {

	public static String formatNumeroNF(String numero) {
		return StringUtils.rightStr("000000"+numero, 6);
	}
	public static String getNumeroFromChave(String chave) {
		return chave.substring(28,34);
	}
	public static String getModeloFromChave(String chave) {
		return chave.substring(20,22);
	}
	public static String getSerieFromChave(String chave) {
		return chave.substring(22,25).replaceFirst("^0*", "");
	}
	public static String getChaveFromEvento(String xmlEvento) {
		int pos = xmlEvento.lastIndexOf("<chNFe>")+7;
		return xmlEvento.substring(pos, pos+44);
	}
}