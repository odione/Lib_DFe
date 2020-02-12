package br.com.dfe.utils;

import br.com.dfe.api.NaoAutorizadoException;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.cce.TRetEnvEvento;

public class NFUtils {

	public static final String STAT_DENEGADA = "110|301|302|303";
	public static final String STAT_AUTORIZADA = "100|150|124|"+STAT_DENEGADA;
	public static final String STAT_INUTILIZADA = "102";
	public static final String STAT_EVENTO = "135|136";
	public static final String STAT_CANCELADA = "101|151|155|"+STAT_EVENTO;
	public static final String STAT_EPEC = "136|124";

	public static String getModeloFromChave(String chave) {
		return chave.substring(20,22);
	}

	public static String getTipoEmissaoFromChave(String chave) {
		return chave.substring(25,26);
	}
	public static String getChaveFromEvento(String xmlEvento) {
		int pos = xmlEvento.lastIndexOf("<chNFe>")+7;
		return xmlEvento.substring(pos, pos+44);
	}
	public static String getModeloFromInutilizacao(String xmlInut) {
		int pos = xmlInut.lastIndexOf("<mod>")+5;
		return xmlInut.substring(pos, pos+2);
	}

	public static void validaRetEnvNFe(TRetEnviNFe retorno) throws NaoAutorizadoException {
		String stat = retorno.getProtNFe() != null ? retorno.getProtNFe().getInfProt().getCStat() : retorno.getCStat();

		if (!STAT_AUTORIZADA.contains(stat)) {
			String msg = retorno.getProtNFe() == null ? retorno.getXMotivo() : retorno.getProtNFe().getInfProt().getXMotivo();
			throw new NaoAutorizadoException(stat, msg);
		}
	}

	public static void validaRetEnvEvento(TRetEnvEvento retorno) throws NaoAutorizadoException {
		String stat = retorno.getRetEvento().isEmpty() ? retorno.getCStat() : retorno.getRetEvento().get(0).getInfEvento().getCStat();

		if (!STAT_EVENTO.contains(stat)) {
			String msg = retorno.getRetEvento().isEmpty() ? retorno.getXMotivo() : retorno.getRetEvento().get(0).getInfEvento().getXMotivo();
			throw new NaoAutorizadoException(stat, msg);
		}
	}

	public static void validaRetEnvEvento(br.com.dfe.schema.canc.TRetEnvEvento retorno) throws NaoAutorizadoException {
		String stat = retorno.getRetEvento().isEmpty() ? retorno.getCStat() : retorno.getRetEvento().get(0).getInfEvento().getCStat();

		if (!STAT_CANCELADA.contains(stat)) {
			String msg = retorno.getRetEvento().isEmpty() ? retorno.getXMotivo() : retorno.getRetEvento().get(0).getInfEvento().getXMotivo();
			throw new NaoAutorizadoException(stat, msg);
		}
	}

	public static void validaRetInutNFe(TRetInutNFe retorno) throws NaoAutorizadoException {
		String stat = retorno.getInfInut().getCStat();
		if (!STAT_INUTILIZADA.contains(stat)) {
			throw new NaoAutorizadoException(stat, retorno.getInfInut().getXMotivo());
		}
	}

	public static void validaRetConsSitNFe(TRetConsSitNFe retorno) throws NaoAutorizadoException {
		String stat = retorno.getCStat();

		if ((!STAT_AUTORIZADA.contains(stat)) && (!STAT_DENEGADA.contains(stat)) && (!STAT_CANCELADA.contains(stat)))  {
			throw new NaoAutorizadoException(stat, retorno.getXMotivo());
		}
	}

	public static void validaRetEnvEvento(br.com.dfe.schema.generico.TRetEnvEvento retorno) throws NaoAutorizadoException {
		String stat = retorno.getRetEvento().isEmpty() ? retorno.getCStat() : retorno.getRetEvento().get(0).getInfEvento().getCStat();

		if (!STAT_EPEC.contains(stat)) {
			String msg = retorno.getRetEvento().isEmpty() ? retorno.getXMotivo() : retorno.getRetEvento().get(0).getInfEvento().getXMotivo();
			throw new NaoAutorizadoException(stat, msg);
		}
	}
}