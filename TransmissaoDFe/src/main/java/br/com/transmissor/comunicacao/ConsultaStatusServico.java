package br.com.transmissor.comunicacao;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

import br.com.dfe.schema.TConsStatServ;
import br.com.dfe.schema.TRetConsStatServ;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.UtilsWebService;
import br.com.transmissor.utils.UtilsXML;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub.NfeCabecMsg;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub.NfeCabecMsgE;
import br.inf.portalfiscal.www.nfe.wsdl.nfestatusservico2.NfeStatusServico2Stub.NfeDadosMsg;

public class ConsultaStatusServico extends ServicoWeb {
	
	private TRetConsStatServ retConsStatServ;
	
	public ConsultaStatusServico() {
		super();
	}

	public void consultar() throws MalformedURLException, AxisFault, RemoteException, XMLStreamException {
		preparaVariaveis();
		setRetConsStatServ(null);
		if (getConfiguracao() != null) {
			carregaURL();
			verificaWSEstado();
			UtilsWebService.preparaAmbiente(getWs_url(),getConfiguracao());
			log.info("Iniciando status servico...");
			
			TConsStatServ consultaXML = new TConsStatServ();
			consultaXML.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
			consultaXML.setTpAmb(String.valueOf(getConfiguracao().getAmbiente().getAmbiente()));
			consultaXML.setVersao(getConfiguracao().getVersaoDF().getVersao());
			consultaXML.setXServ("STATUS");
			
			String strXML = new LeitorXML().criaStrXML(consultaXML, false);
			log.info("status servico: XML: "+strXML);
			
			retConsStatServ = new LeitorXML().toObj(comunicaWS(strXML), TRetConsStatServ.class);
			setProcessou(true);
			log.info("status servico finalizado! Ret: "+retConsStatServ.getCStat());
		} else {
			setMsgErro("Configuração Nula!");
			log.error(getMsgErro());
		}
	}
	
	@Override
	public String comunicaWS(String strXML) throws XMLStreamException, AxisFault, RemoteException {
		OMElement elemento = UtilsXML.toOMElement(strXML);
		
		NfeStatusServico2Stub.NfeDadosMsg dados = new NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeStatusServico2Stub.NfeCabecMsg cabec = new NfeCabecMsg();
		cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
		cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
		
		NfeStatusServico2Stub.NfeCabecMsgE cabecE = new NfeCabecMsgE();
		cabecE.setNfeCabecMsg(cabec);
		
		NfeStatusServico2Stub stub = new NfeStatusServico2Stub(getWs_url());
		return stub.nfeStatusServicoNF2(dados, cabecE).getExtraElement().toString();			
	}
	
	@Override
	public void carregaURL() {
		setWs_url(UtilsWebService.getStatusServicoURL(getConfiguracao().getUf(), getConfiguracao().getAmbiente(), 
			getConfiguracao().getTipoEmissao(),getConfiguracao().getModelo_df()));
	}
	
	public TRetConsStatServ getRetConsStatServ() {
		return retConsStatServ;
	}
	public void setRetConsStatServ(TRetConsStatServ retConsStatServ) {
		this.retConsStatServ = retConsStatServ;
	}
}