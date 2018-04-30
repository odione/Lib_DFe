package br.com.transmissor.comunicacao;

import java.rmi.RemoteException;

import javax.xml.stream.XMLStreamException;

import org.apache.axis2.AxisFault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.transmissor.ConfigDFe;
import br.com.transmissor.utils.enumarator.Estado;
import br.com.transmissor.utils.enumarator.ModeloDF;

public abstract class ServicoWeb {

	private boolean processou;
	private String msgErro;
	private String ws_url;
	private ConfigDFe configuracao;
	private boolean assincrono;
	
	protected static final Logger log = LogManager.getLogger(ServicoWeb.class.getSimpleName());
	
	public ServicoWeb() {
		processou = false;
		msgErro = "";
		ws_url = "";
	}
	
	public abstract String comunicaWS(String strXML) throws XMLStreamException, AxisFault, RemoteException;
	
	public abstract void carregaURL();
	
	public void preparaVariaveis(){
		setProcessou(false);
		setMsgErro("");
	}
	
	public boolean isWSDL2(Estado estado) {
		if (estado != null){
			if (getConfiguracao().getModelo_df() == ModeloDF.MODELO_NFE) {
				String uf_wsdl = "BA";
				return (!uf_wsdl.contains(estado.getUF()));
			}
		}
		return true;
	}
	
	public void verificaWSEstado() {
		if ((getWs_url() == null) || (getWs_url().equals(""))) {
			throw new IllegalArgumentException("Estado não possui WebService para este Serviço!");
		}
	}
	
	public void trataException(Exception e) {
		while (e != null) {
			log.error("Tratando erro: "+e.toString());
			e = (Exception) e.getCause();
		}
	}
	
	public boolean isProcessou() {
		return processou;
	}
	public void setProcessou(boolean processou) {
		this.processou = processou;
	}
	public String getMsgErro() {
		return msgErro;
	}
	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}
	public String getWs_url() {
		return ws_url;
	}
	public void setWs_url(String ws_url) {
		this.ws_url = ws_url;
	}
	public ConfigDFe getConfiguracao() {
		return configuracao;
	}
	public void setConfiguracao(ConfigDFe configuracao) {
		this.configuracao = configuracao;
		setAssincrono(("SP|BA".contains(this.configuracao.getUf().getUF()) && (this.configuracao.getModelo_df() == ModeloDF.MODELO_NFE)));
	}
	public boolean isAssincrono() {
		return assincrono;
	}
	public void setAssincrono(boolean assincrono) {
		this.assincrono = assincrono;
	}
}