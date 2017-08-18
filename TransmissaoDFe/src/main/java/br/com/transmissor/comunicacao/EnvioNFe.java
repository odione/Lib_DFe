package br.com.transmissor.comunicacao;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Iterator;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMElement;
import org.xml.sax.SAXException;

import br.com.dfe.schema.TEnviNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNfeProc;
import br.com.dfe.schema.TRetEnviNFe;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.AssinaturaDocumento;
import br.com.transmissor.utils.UtilsWebService;
import br.com.transmissor.utils.UtilsXML;
import br.com.transmissor.utils.ValidacaoXML;
import br.com.transmissor.utils.enumarator.Ambiente;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.com.transmissor.utils.enumarator.TipoEmissao;
import br.inf.portalfiscal.www.nfe.wsdl.nfeautorizacao.NfeAutorizacaoStub;

public class EnvioNFe extends ServicoWeb {

	private TRetEnviNFe retEnviNFe;
	
	public EnvioNFe() {
		super();
	}
	
	public void enviarNF(String strXML) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, 
			SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException  {
		preparaVariaveis();
		setRetEnviNFe(null);
		if (getConfiguracao() != null) {
			log.info("Iniciando NF...");
			TNFe nfe = new LeitorXML().toObj(strXML, TNFe.class);
			alterConfigEnvio(nfe);
			carregaURL();
			verificaWSEstado();
			
			UtilsWebService.preparaAmbiente(getWs_url(),getConfiguracao());
			
			TEnviNFe envNFe = new TEnviNFe();
			envNFe.setIdLote("1");
			envNFe.setIndSinc("1");
			envNFe.setVersao(getConfiguracao().getVersaoDF().getVersao());
			envNFe.getNFe().add(nfe);
			
			strXML = new LeitorXML().criaStrXML(envNFe, false);
			
			strXML = new AssinaturaDocumento(getConfiguracao().getCertificado(), getConfiguracao().getPrivateKey()).assinaEnvNFe(strXML);
			
			strXML = insereUrlQRCode(strXML);
			
			log.info("Envio NF! XML: "+strXML);
			
			if (ValidacaoXML.validaXMLEnvio(strXML)) {
				setRetEnviNFe(new LeitorXML().toObj(comunicaWS(strXML), TRetEnviNFe.class));
				
				if (isAssincrono()) {
					consultaRetEnvio();
				}
				
				setProcessou(true);
				salvarDF(new LeitorXML().toObj(strXML, TEnviNFe.class).getNFe().get(0));
				log.info("Envio NF Finalizado! Ret: "+retEnviNFe.getCStat());					
			} else {
				log.debug(ValidacaoXML.errosValidacao);
				setMsgErro(ValidacaoXML.errosValidacao);
			}
		} else {
			setMsgErro("Configuração Nula!");
			log.error(getMsgErro());
		}
	}
	
	@Override
	public String comunicaWS(String strXML) throws XMLStreamException, RemoteException {
		OMElement elemento = UtilsXML.toOMElement(strXML);
		
		Iterator<?> children = elemento.getChildrenWithLocalName("NFe");  
		while (children.hasNext()) {  
		    OMElement omElement = (OMElement) children.next();  
		    if (omElement != null && "NFe".equals(omElement.getLocalName())) {  
		        omElement.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe", null);  
		    }  
		}
		
		NfeAutorizacaoStub.NfeCabecMsg cabec = new NfeAutorizacaoStub.NfeCabecMsg();
		cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
		cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
		
		NfeAutorizacaoStub.NfeCabecMsgE cabecE = new NfeAutorizacaoStub.NfeCabecMsgE();
		cabecE.setNfeCabecMsg(cabec);
		
		NfeAutorizacaoStub.NfeDadosMsg dados = new NfeAutorizacaoStub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeAutorizacaoStub stub = new NfeAutorizacaoStub(getWs_url());
		return stub.nfeAutorizacaoLote(dados, cabecE).getExtraElement().toString();
	}

	@Override
	public void carregaURL() {
		setWs_url(UtilsWebService.getEnvioNFURL(getConfiguracao().getUf(), getConfiguracao().getAmbiente(), 
			getConfiguracao().getTipoEmissao(),getConfiguracao().getModelo_df()));
	}
	
	public void salvarDF(TNFe nfe){
		if (isProcessou() && (retEnviNFe != null)){
			log.debug(new LeitorXML().criaStrXML(retEnviNFe, false));
			if (retEnviNFe.getProtNFe() != null) {
				TNfeProc nfeProc = new TNfeProc();
				nfeProc.setNFe(nfe);
				nfeProc.setProtNFe(retEnviNFe.getProtNFe());
				nfeProc.setVersao(retEnviNFe.getProtNFe().getVersao());
				
				UtilsXML.salvarProcNfe(nfeProc);
			}
		}
	}
	
	public void alterConfigEnvio(TNFe nfe) {
		if (nfe != null) {
			getConfiguracao().setAmbiente(Ambiente.valorDe(Integer.valueOf(nfe.getInfNFe().getIde().getTpAmb())));
			getConfiguracao().setModelo_df(ModeloDF.valorDe(nfe.getInfNFe().getIde().getMod()));
			getConfiguracao().setTipoEmissao(TipoEmissao.valorDe(Integer.valueOf(nfe.getInfNFe().getIde().getTpEmis())));
		}
	}
	
	private String insereUrlQRCode(String strXML){
		if (getConfiguracao().getModelo_df() == ModeloDF.MODELO_NFCE) {
			LocalDate dataVigencia = LocalDate.of(2015, 12, 2);
			if ((getConfiguracao().getAmbiente() == Ambiente.HOMOLOGACAO) || 
				((getConfiguracao().getAmbiente() == Ambiente.PRODUCAO) && (LocalDate.now().isAfter(dataVigencia)))) {
				log.info("Pegando URL QR Code...");
				TEnviNFe envNFe = new LeitorXML().toObj(strXML, TEnviNFe.class);
				String urlQRCode = UtilsXML.getUrlQRCode(envNFe.getNFe().get(0),getConfiguracao().getIdCSC(),getConfiguracao().getCSC());
				String tag = "<infNFeSupl><qrCode>"+urlQRCode+"</qrCode></infNFeSupl>";
				StringBuilder sb = new StringBuilder(strXML);
				sb.insert(sb.lastIndexOf("</infNFe>")+9, tag);
				strXML = sb.toString();
				log.info("Pegou URL QR Code: "+urlQRCode);				
			}
		}
		return strXML;
	}
	
	private void consultaRetEnvio(){
		if ((retEnviNFe != null) && (retEnviNFe.getCStat().equals("103"))) {
			RetEnvioNF retEnvio = new RetEnvioNF();
			retEnvio.preparaVariaveis();
			retEnvio.setConfiguracao(getConfiguracao());
			retEnvio.enviaConsultaRecibo(retEnviNFe.getInfRec().getNRec());
			
			if (retEnvio.getRetConsRec() != null) {
				getRetEnviNFe().setCStat(retEnvio.getRetConsRec().getCStat());
				getRetEnviNFe().setXMotivo(retEnvio.getRetConsRec().getXMotivo());
				if ((retEnvio.getRetConsRec().getProtNFe() != null) && (!retEnvio.getRetConsRec().getProtNFe().isEmpty())) {
					getRetEnviNFe().setProtNFe(retEnvio.getRetConsRec().getProtNFe().get(0));
				}
				log.info("Consulta de Recibo Finalizada! Ret: "+retEnvio.getRetConsRec().getCStat());
			} else {
				log.error("Consulta de Recibo com Resposta Nula!");
			}
		}
	}
	
	public TRetEnviNFe getRetEnviNFe() {
		return retEnviNFe;
	}
	public void setRetEnviNFe(TRetEnviNFe retEnviNFe) {
		this.retEnviNFe = retEnviNFe;
	}
}