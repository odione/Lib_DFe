package br.com.transmissor.comunicacao;

import java.rmi.RemoteException;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

import br.com.dfe.schema.TConsReciNFe;
import br.com.dfe.schema.TRetConsReciNFe;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.UtilsWebService;
import br.com.transmissor.utils.UtilsXML;
import br.inf.portalfiscal.www.nfe.wsdl.nferetautorizacao.NfeRetAutorizacaoStub;

public class RetEnvioNF extends ServicoWeb {
	
	private TRetConsReciNFe retConsRec;
	
	public RetEnvioNF() {
		super();
	}
	
	public void enviaConsultaRecibo(String recibo) {
		retConsRec = null;
		if (!recibo.trim().equalsIgnoreCase("")) {
			try {
				log.info("Iniciando Consulta de Recibo de Lote...");
				carregaURL();
				TConsReciNFe consRec = new TConsReciNFe();
				consRec.setVersao(getConfiguracao().getVersaoDF().getVersao());
				consRec.setNRec(recibo);
				consRec.setTpAmb(String.valueOf(getConfiguracao().getAmbiente().getAmbiente()));
				
				String strXML = new LeitorXML().criaStrXML(consRec, false);
				
				int loops = 0;
				while (retConsRec == null) {
					Thread.sleep(1500);
					loops++;
					log.debug("Tentativa numero: "+loops+" de Consulta Recibo...");
					retConsRec = new LeitorXML().toObj(comunicaWS(strXML), TRetConsReciNFe.class);
					if (loops < 5) {
						if ("105".contains(retConsRec.getCStat())) {
							retConsRec = null;
						}
					}
				}
				log.info("Consulta de Recebido Finalizada! Ret: "+retConsRec.getCStat());
			} catch (Exception e) {
				e.printStackTrace();
				log.catching(e);
			}
		}
	}

	@Override
	public String comunicaWS(String strXML) throws XMLStreamException, AxisFault, RemoteException {
		OMElement elemento = UtilsXML.toOMElement(strXML);
		
		NfeRetAutorizacaoStub.NfeCabecMsg cabec = new NfeRetAutorizacaoStub.NfeCabecMsg();
		cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
		cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
		
		NfeRetAutorizacaoStub.NfeCabecMsgE cabecE = new NfeRetAutorizacaoStub.NfeCabecMsgE();
		cabecE.setNfeCabecMsg(cabec);
		
		NfeRetAutorizacaoStub.NfeDadosMsg dados = new NfeRetAutorizacaoStub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		NfeRetAutorizacaoStub stub = new NfeRetAutorizacaoStub(getWs_url());
		return stub.nfeRetAutorizacaoLote(dados, cabecE).getExtraElement().toString();
	}

	@Override
	public void carregaURL() {
		setWs_url(UtilsWebService.getRetEnvioNFURL(getConfiguracao().getUf(), getConfiguracao().getAmbiente(), 
			getConfiguracao().getTipoEmissao(), getConfiguracao().getModelo_df()));
	}

	public TRetConsReciNFe getRetConsRec() {
		return retConsRec;
	}
	public void setRetConsRec(TRetConsReciNFe retConsRec) {
		this.retConsRec = retConsRec;
	}
}