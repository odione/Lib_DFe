package br.com.dfe.utils;

import org.apache.commons.lang3.StringUtils;

public class NFUtils {

	/**
	 * Coloca zeros a esquerda
	 * @param numero
	 * @return numero formatado
	 */
	public static String formatNumeroNF(String numero) {
		return StringUtils.leftPad(numero, 6, "0");
	}

	/**
	 * @param chave
	 * @return numero
	 */
	public static String getNumeroFromChave(String chave) {
		return chave.substring(28,34);
	}

	public static String getModeloFromChave(String chave) {
		return chave.substring(20,22);
	}

	/**
	 * @param chave
	 * @return serie
	 */
	public static String getSerieFromChave(String chave) {
		return chave.substring(22,25).replaceFirst("^0*", "");
	}
	public static String getTipoEmissaoFromChave(String chave) {
		return chave.substring(25,26);
	}
	public static String getChaveFromEvento(String xmlEvento) {
		int pos = xmlEvento.lastIndexOf("<chNFe>")+7;
		return xmlEvento.substring(pos, pos+44);
	}
}