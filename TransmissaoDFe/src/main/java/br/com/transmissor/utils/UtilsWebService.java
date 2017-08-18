package br.com.transmissor.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.transmissor.ConfigDFe;
import br.com.transmissor.SocketFactoryDinamico;
import br.com.transmissor.comunicacao.BuildCacerts;
import br.com.transmissor.utils.enumarator.Ambiente;
import br.com.transmissor.utils.enumarator.Estado;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.com.transmissor.utils.enumarator.TipoEmissao;
import br.com.transmissor.utils.enumarator.URL_ConsultaNFC;
import br.com.transmissor.utils.enumarator.WS_URL_EnvioNF;
import br.com.transmissor.utils.enumarator.WS_URL_Evento;
import br.com.transmissor.utils.enumarator.WS_URL_Inutilizacao;
import br.com.transmissor.utils.enumarator.WS_URL_RetEnvioNF;
import br.com.transmissor.utils.enumarator.WS_URL_SituacaoNF;
import br.com.transmissor.utils.enumarator.WS_URL_StatusServico;

public class UtilsWebService {
	
	private static final Logger log = LogManager.getLogger(UtilsWebService.class.getSimpleName());
	
	public static void preparaAmbiente(String ws_url, ConfigDFe configuracao) throws MalformedURLException{
		log.info("Preparando ambiente...");
		FileRW.criaDiretorio(System.getProperty("user.dir")+"/Resources");
		
		BuildCacerts.geraCacert(new URL(ws_url),System.getProperty("user.dir")+"/Resources/NFeCacerts");
		
		SocketFactoryDinamico socketFactoryDinamico = new SocketFactoryDinamico(configuracao.getCertificado(), 
			configuracao.getPrivateKey(),configuracao.getUf());
		socketFactoryDinamico.setFileCacerts(System.getProperty("user.dir")+"/Resources/NFeCacerts");
		
		Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);
		Protocol.registerProtocol("https", protocol);
		log.info("Ambiente Preparado com Sucesso!");
	}
	
	public static String getStatusServicoURL(Estado uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		for (WS_URL_StatusServico ws_url : WS_URL_StatusServico.values()) {
			if ((ws_url.getAmbiente() == ambiente) && (ws_url.getUf().contains(uf.getUF())) && 
					(ws_url.getTipoEmissao() == tipoEmissao) && (ws_url.getModelo() == modelo)) {
				return ws_url.getUrl();
			}
		}
		return "";
	}
	
	public static String getConsultaSitNFURL(Estado uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		for (WS_URL_SituacaoNF ws_url : WS_URL_SituacaoNF.values()) {
			if ((ws_url.getAmbiente() == ambiente) && (ws_url.getUf().contains(uf.getUF())) && 
				(ws_url.getTipoEmissao() == tipoEmissao) && (ws_url.getModelo() == modelo)) {
				return ws_url.getUrl();
			}
		}
		return "";
	}
	
	public static String getEnvioNFURL(Estado uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		for (WS_URL_EnvioNF ws_url : WS_URL_EnvioNF.values()) {
			if ((ws_url.getAmbiente() == ambiente) && (ws_url.getUf().contains(uf.getUF())) 
				&& (ws_url.getTipoEmissao() == tipoEmissao) && (ws_url.getModelo() == modelo)) {
				return ws_url.getUrl();
			}
		}
		return "";
	}
	
	public static String getEnvioEventoURL(Estado uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		for (WS_URL_Evento ws_url : WS_URL_Evento.values()) {
			if ((ws_url.getAmbiente() == ambiente) && (ws_url.getUf().contains(uf.getUF())) 
				&& (ws_url.getTipoEmissao() == tipoEmissao) && (ws_url.getModelo() == modelo)) {
				return ws_url.getUrl();
			}
		}
		return "";
	}
	
	public static String getInutilizacaoURL(Estado uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		for (WS_URL_Inutilizacao ws_url : WS_URL_Inutilizacao.values()) {
			if ((ws_url.getAmbiente() == ambiente) && (ws_url.getUf().contains(uf.getUF())) 
				&& (ws_url.getTipoEmissao() == tipoEmissao) && (ws_url.getModelo() == modelo)) {
				return ws_url.getUrl();
			}
		}
		return "";
	}
	
	public static String getUrlQRCode(Estado uf, Ambiente ambiente) {
		for (URL_ConsultaNFC url : URL_ConsultaNFC.values()) {
			if ((url.getAmbiente() == ambiente) && (url.getUf().contains(uf.getUF()))) {
				return url.getUrl();
			}
		}
		return "";
	}
	
	public static String getRetEnvioNFURL(Estado uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		for (WS_URL_RetEnvioNF ws_url : WS_URL_RetEnvioNF.values()) {
			if ((ws_url.getAmbiente() == ambiente) && (ws_url.getUf().contains(uf.getUF())) 
				&& (ws_url.getTipoEmissao() == tipoEmissao) && (ws_url.getModelo() == modelo)) {
				return ws_url.getUrl();
			}
		}
		return "";
	}
}