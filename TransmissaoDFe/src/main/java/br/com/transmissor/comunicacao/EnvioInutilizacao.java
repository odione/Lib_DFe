package br.com.transmissor.comunicacao;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.xml.sax.SAXException;

import br.com.dfe.schema.TInutNFe;
import br.com.dfe.schema.TInutNFe.InfInut;
import br.com.dfe.schema.TProcInutNFe;
import br.com.dfe.schema.TRetInutNFe;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.AssinaturaDocumento;
import br.com.transmissor.utils.UtilsWebService;
import br.com.transmissor.utils.UtilsXML;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.com.utils.StringUtils;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao.NfeInutilizacaoStub;
import br.inf.portalfiscal.www.nfe.wsdl.nfeinutilizacao2.NfeInutilizacao2Stub;

public class EnvioInutilizacao extends ServicoWeb {

	private TRetInutNFe retInutNFe;
	
	public void inutiliza(String ano,String cnpj,String modelo,String serie,String nfIni, String nfFim, String justificativa) throws NoSuchAlgorithmException, 
			InvalidAlgorithmParameterException, SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException{
		preparaVariaveis();
		setRetInutNFe(null);
		if (getConfiguracao() != null) {
			log.info("Iniciando Inutilizacao! Ano: "+ano+" cnpj: "+cnpj+" modelo: "+modelo+" serie: "+serie+" nfIni: "+nfIni+" nfFim: "+nfFim+" just: "+justificativa);
			alteraConfigEnvio(modelo);
			carregaURL();
			verificaWSEstado();
			UtilsWebService.preparaAmbiente(getWs_url(),getConfiguracao());
			
			String strAno = StringUtils.rightStr(ano, 2);
			
			String ID = "ID".concat(String.valueOf(getConfiguracao().getUf().getCodigoUF())).
				concat(strAno).
				concat(cnpj).
				concat(modelo).
				concat(StringUtils.rightStr("000"+serie, 3)).
				concat(StringUtils.rightStr("000000000"+nfIni, 9)).
				concat(StringUtils.rightStr("000000000"+nfFim, 9));
			
			nfIni = String.valueOf(Integer.valueOf(nfIni));
			nfFim = String.valueOf(Integer.valueOf(nfFim));
			
			TInutNFe inut = new TInutNFe();
			inut.setVersao(getConfiguracao().getVersaoDF().getVersao());
			inut.setInfInut(new InfInut());
			inut.getInfInut().setId(ID);
			inut.getInfInut().setTpAmb(String.valueOf(getConfiguracao().getAmbiente().getAmbiente()));
			inut.getInfInut().setXServ("INUTILIZAR");
			inut.getInfInut().setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
			inut.getInfInut().setAno(strAno);
			inut.getInfInut().setCNPJ(cnpj);
			inut.getInfInut().setMod(modelo);
			inut.getInfInut().setSerie(serie);
			inut.getInfInut().setNNFIni(nfIni);
			inut.getInfInut().setNNFFin(nfFim);
			inut.getInfInut().setXJust(justificativa);
			
			String strXML = new LeitorXML().criaStrXML(inut, false);
			log.info("Envio Inut: XML: "+strXML);
			
			strXML = new AssinaturaDocumento(getConfiguracao().getCertificado(), getConfiguracao().getPrivateKey()).assinaInutilizada(strXML);
			
			setRetInutNFe(new LeitorXML().toObj(comunicaWS(strXML), TRetInutNFe.class));
			setProcessou(true);
			salvarInut(inut);
			log.info("Envio Inut Finalizado! Ret: "+retInutNFe.getInfInut().getCStat());
		} else {
			setMsgErro("Configuração Nula!");
			log.error(getMsgErro());
		}
	}
	
	@Override
	public String comunicaWS(String strXML) throws XMLStreamException, AxisFault, RemoteException {
		OMElement elemento = UtilsXML.toOMElement(strXML);
		
		if (isWSDL2(getConfiguracao().getUf())) {
			NfeInutilizacao2Stub.NfeCabecMsg cabec = new NfeInutilizacao2Stub.NfeCabecMsg();
			cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
			cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
			
			NfeInutilizacao2Stub.NfeCabecMsgE cabecE = new NfeInutilizacao2Stub.NfeCabecMsgE();
			cabecE.setNfeCabecMsg(cabec);
			
			NfeInutilizacao2Stub.NfeDadosMsg dados = new NfeInutilizacao2Stub.NfeDadosMsg();
			dados.setExtraElement(elemento);
			
			NfeInutilizacao2Stub stub = new NfeInutilizacao2Stub(getWs_url());
			return stub.nfeInutilizacaoNF2(dados, cabecE).getExtraElement().toString();
		} else {
			NfeInutilizacaoStub.NfeDadosMsg dados = new NfeInutilizacaoStub.NfeDadosMsg();
			dados.setExtraElement(elemento);
			
			NfeInutilizacaoStub.NfeCabecMsg cabec = new NfeInutilizacaoStub.NfeCabecMsg();
			cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
			cabec.setVersaoDados(getConfiguracao().getVersaoDF().getVersao());
			
			NfeInutilizacaoStub.NfeCabecMsgE cabecE = new NfeInutilizacaoStub.NfeCabecMsgE();
			cabecE.setNfeCabecMsg(cabec);
			
			NfeInutilizacaoStub stub = new NfeInutilizacaoStub(getWs_url());
			return stub.nfeInutilizacaoNF(dados, cabecE).getExtraElement().toString();
		}
	}

	@Override
	public void carregaURL() {
		setWs_url(UtilsWebService.getInutilizacaoURL(getConfiguracao().getUf(), getConfiguracao().getAmbiente(), 
			getConfiguracao().getTipoEmissao(), getConfiguracao().getModelo_df()));
	}
	
	public void salvarInut(TInutNFe inutNfe){
		if ((isProcessou()) && (retInutNFe != null)){
			if (retInutNFe.getInfInut().getId() == null) {
				retInutNFe.getInfInut().setId(inutNfe.getInfInut().getId());
			}
			TProcInutNFe procInut = new TProcInutNFe();
			procInut.setInutNFe(inutNfe);
			procInut.setRetInutNFe(retInutNFe);
			procInut.setVersao(getConfiguracao().getVersaoDF().getVersao());
			
			UtilsXML.salvarProcInutilizacao(procInut);
		}
	}
	
	private void alteraConfigEnvio(String modelo){
		getConfiguracao().setModelo_df(ModeloDF.valorDe(modelo));
	}

	public TRetInutNFe getRetInutNFe() {
		return retInutNFe;
	}
	public void setRetInutNFe(TRetInutNFe retInutNFe) {
		this.retInutNFe = retInutNFe;
	}
}