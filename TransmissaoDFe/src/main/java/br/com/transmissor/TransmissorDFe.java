package br.com.transmissor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.axis2.AxisFault;
import org.xml.sax.SAXException;

import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.schema.eventoCanc.TRetEnvEvento;
import br.com.transmissor.comunicacao.ConsultaSitNF;
import br.com.transmissor.comunicacao.ConsultaStatusServico;
import br.com.transmissor.comunicacao.EnvioCCe;
import br.com.transmissor.comunicacao.EnvioCancelamento;
import br.com.transmissor.comunicacao.EnvioInutilizacao;
import br.com.transmissor.comunicacao.EnvioNFe;

public class TransmissorDFe {
	
	private ConfigDFe configuracao;
	private ConsultaStatusServico statusServico;
	private ConsultaSitNF situacaoNF;
	private EnvioNFe envioNF;
	private EnvioCancelamento envioCancelamento;
	private EnvioCCe envioCCe;
	private EnvioInutilizacao envioInutilizacao;
	
	public TransmissorDFe() {
		super();
		setConfiguracao(new ConfigDFe());
		setStatusServico(new ConsultaStatusServico());
		setSituacaoNF(new ConsultaSitNF());
		setEnvioNF(new EnvioNFe());
		setEnvioCancelamento(new EnvioCancelamento());
		setEnvioCCe(new EnvioCCe());
		setEnvioInutilizacao(new EnvioInutilizacao());
	}
	
	public TRetConsStatServ consultaStatusServico() throws AxisFault, MalformedURLException, RemoteException, XMLStreamException{
		statusServico.setConfiguracao(getConfiguracao());
		statusServico.consultar();
		return statusServico.getRetConsStatServ();
	}
	
	public TRetConsSitNFe consultaSitNF(String chaveNF) throws AxisFault, MalformedURLException, RemoteException, XMLStreamException{
		situacaoNF.setConfiguracao(getConfiguracao());
		situacaoNF.consultaNF(chaveNF);
		return situacaoNF.getRetConsSitNFe();
	}
	
	public TRetEnviNFe enviaNF(String strXML) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException {
		envioNF.setConfiguracao(getConfiguracao());
		envioNF.enviarNF(strXML);
		return envioNF.getRetEnviNFe();
	}
	
	public TRetEnvEvento enviaCancelamento(String chaveNF,String cnpj,String protAut,String justificativa) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException{
		envioCancelamento.setConfiguracao(getConfiguracao());
		envioCancelamento.enviaCancelamento(chaveNF, cnpj, protAut, justificativa);
		return envioCancelamento.getRetEnvEvento();
	}
	
	public br.com.dfe.schema.eventoCCe.TRetEnvEvento enviaCCe(String chaveNF,String cnpj,int sequencial,String correcao) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException{
		envioCCe.setConfiguracao(getConfiguracao());
		envioCCe.enviaCCe(chaveNF, cnpj, sequencial, correcao);
		return envioCCe.getRetEnvEvento();
	}
	
	public TRetInutNFe enviaInutilizacao(String ano,String cnpj,String modelo,String serie,String nfIni, String nfFim,String justificativa) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException{
		envioInutilizacao.setConfiguracao(getConfiguracao());
		envioInutilizacao.inutiliza(ano, cnpj, modelo, serie, nfIni, nfFim, justificativa);
		return envioInutilizacao.getRetInutNFe();
	}
	
	public ConfigDFe getConfiguracao() {
		return configuracao;
	}
	public void setConfiguracao(ConfigDFe configuracao) {
		this.configuracao = configuracao;
	}
	public ConsultaStatusServico getStatusServico() {
		return statusServico;
	}
	public void setStatusServico(ConsultaStatusServico statusServico) {
		this.statusServico = statusServico;
	}
	public ConsultaSitNF getSituacaoNF() {
		return situacaoNF;
	}
	public void setSituacaoNF(ConsultaSitNF situacaoNF) {
		this.situacaoNF = situacaoNF;
	}
	public EnvioNFe getEnvioNF() {
		return envioNF;
	}
	public void setEnvioNF(EnvioNFe envioNF) {
		this.envioNF = envioNF;
	}
	public EnvioCancelamento getEnvioCancelamento() {
		return envioCancelamento;
	}
	public void setEnvioCancelamento(EnvioCancelamento envioCancelamento) {
		this.envioCancelamento = envioCancelamento;
	}
	public EnvioCCe getEnvioCCe() {
		return envioCCe;
	}
	public void setEnvioCCe(EnvioCCe envioCCe) {
		this.envioCCe = envioCCe;
	}
	public EnvioInutilizacao getEnvioInutilizacao() {
		return envioInutilizacao;
	}
	public void setEnvioInutilizacao(EnvioInutilizacao envioInutilizacao) {
		this.envioInutilizacao = envioInutilizacao;
	}
}