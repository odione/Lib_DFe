package br.com.transmissor.comunicacao;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

import br.com.dfe.schema.TConsSitNFe;
import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNfeProc;
import br.com.dfe.schema.TProcEvento;
import br.com.dfe.schema.TRetConsSitNFe;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.AssinaturaDocumento;
import br.com.transmissor.utils.UtilsWebService;
import br.com.transmissor.utils.UtilsXML;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsulta.NfeConsultaStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeconsulta2.NfeConsulta2Stub;

public class ConsultaSitNF extends ServicoWeb {
	
	private TRetConsSitNFe retConsSitNFe;
	private TNFe nfe;
	
	public ConsultaSitNF(){
		super();
	}
	
	public void consultaNF(String chaveNF) throws MalformedURLException, AxisFault, RemoteException, XMLStreamException{
		preparaVariaveis();
		setRetConsSitNFe(null);
		if (getConfiguracao() != null){
			log.info("Iniciando ConsultaNF! Chave: "+chaveNF);
			alteraConfigEnvio(UtilsXML.getModeloFromChave(chaveNF));
			carregaURL();
			verificaWSEstado();
			UtilsWebService.preparaAmbiente(getWs_url(),getConfiguracao());
			
			TConsSitNFe consultaNF = new TConsSitNFe();
			consultaNF.setTpAmb(String.valueOf(getConfiguracao().getAmbiente().getAmbiente()));
			consultaNF.setVersao(getConfiguracao().getVersaoDF().getVersao());
			consultaNF.setXServ("CONSULTAR");
			consultaNF.setChNFe(chaveNF);

			String strXML = new LeitorXML().criaStrXML(consultaNF, false);
			log.info("ConsultaNF! XML: "+strXML);				
			retConsSitNFe = new LeitorXML().toObj(comunicaWS(strXML), TRetConsSitNFe.class);
			setProcessou(true);
			attXML();
			log.info("ConsultaNF Terminou! Ret: "+retConsSitNFe.getCStat());
		} else {
			setMsgErro("Configuração Nula!");
			log.warn(getMsgErro());
		}
	}

	@Override
	public String comunicaWS(String strXML) throws XMLStreamException, AxisFault, RemoteException {
		strXML = AssinaturaDocumento.addXmlEncoding(strXML);
		OMElement elemento = UtilsXML.toOMElement(strXML);
		
		if (isWSDL2(getConfiguracao().getUf())) {
			NfeConsulta2Stub.NfeDadosMsg dados = new NfeConsulta2Stub.NfeDadosMsg();
			dados.setExtraElement(elemento);
			
			NfeConsulta2Stub.NfeCabecMsg cabec = new NfeConsulta2Stub.NfeCabecMsg();
			cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
			cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
			
			NfeConsulta2Stub.NfeCabecMsgE cabecE = new NfeConsulta2Stub.NfeCabecMsgE();
			cabecE.setNfeCabecMsg(cabec);
			
			NfeConsulta2Stub stub = new NfeConsulta2Stub(getWs_url());
			return stub.nfeConsultaNF2(dados, cabecE).getExtraElement().toString();
		} else {
			NfeConsultaStub.NfeDadosMsg dados = new NfeConsultaStub.NfeDadosMsg();
			dados.setExtraElement(elemento);
			
			NfeConsultaStub.NfeCabecMsg cabec = new NfeConsultaStub.NfeCabecMsg();
			cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
			cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
			
			NfeConsultaStub.NfeCabecMsgE cabecE = new NfeConsultaStub.NfeCabecMsgE();
			cabecE.setNfeCabecMsg(cabec);
			
			NfeConsultaStub stub = new NfeConsultaStub(getWs_url());
			return stub.nfeConsultaNF(dados, cabecE).getExtraElement().toString();
		}
	}
	
	@Override
	public void carregaURL() {
		setWs_url(UtilsWebService.getConsultaSitNFURL(getConfiguracao().getUf(), getConfiguracao().getAmbiente(), 
			getConfiguracao().getTipoEmissao(),getConfiguracao().getModelo_df()));
	}
	
	public void attXML(){
		if (getConfiguracao().isAtualizaXMLConsulta()) {
			if ((nfe != null) && (retConsSitNFe.getProtNFe() != null)) {
				if (!UtilsXML.fileExist(UtilsXML.getPathXMLAutorizada(retConsSitNFe.getChNFe(),
					UtilsXML.getCNPJFromChave(retConsSitNFe.getChNFe()),
					UtilsXML.getModeloFromChave(retConsSitNFe.getChNFe())))) {
					
					TNfeProc procNfe = new TNfeProc();
					procNfe.setNFe(nfe);
					procNfe.setProtNFe(retConsSitNFe.getProtNFe());
					procNfe.setVersao(retConsSitNFe.getVersao());
					UtilsXML.salvarProcNfe(procNfe);
				}
			}
			
			for (TProcEvento procEvento : retConsSitNFe.getProcEventoNFe()) {
				String pathEvento = "";
				if (UtilsXML.isEventoCCe(procEvento.getRetEvento())) {
					pathEvento = UtilsXML.getPathEventoCCe(UtilsXML.getCNPJFromChave(procEvento.getRetEvento().getInfEvento().getChNFe()), 
						ModeloDF.valorDe(UtilsXML.getModeloFromChave(procEvento.getRetEvento().getInfEvento().getChNFe())));
				} else if (UtilsXML.isEventoCancelamento(procEvento.getRetEvento())){
					pathEvento = UtilsXML.getPathEventoCanc(UtilsXML.getCNPJFromChave(procEvento.getRetEvento().getInfEvento().getChNFe()), 
						ModeloDF.valorDe(UtilsXML.getModeloFromChave(procEvento.getRetEvento().getInfEvento().getChNFe())));
				}
				if (!UtilsXML.fileExist(pathEvento)) {
					UtilsXML.salvarProcEvento(procEvento);					
				}
			}
		}
	}
	
	private void alteraConfigEnvio(String modelo){
		getConfiguracao().setModelo_df(ModeloDF.valorDe(modelo));
	}

	public TRetConsSitNFe getRetConsSitNFe() {
		return retConsSitNFe;
	}
	public void setRetConsSitNFe(TRetConsSitNFe retConsSitNFe) {
		this.retConsSitNFe = retConsSitNFe;
	}
	public TNFe getNfe() {
		return nfe;
	}
	public void setNfe(TNFe nfe) {
		this.nfe = nfe;
	}
}