package br.com.transmissor.comunicacao;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.xml.sax.SAXException;

import br.com.dfe.schema.eventoCCe.TEnvEvento;
import br.com.dfe.schema.eventoCCe.TEvento;
import br.com.dfe.schema.eventoCCe.TEvento.InfEvento;
import br.com.dfe.schema.eventoCCe.TEvento.InfEvento.DetEvento;
import br.com.dfe.schema.eventoCCe.TProcEvento;
import br.com.dfe.schema.eventoCCe.TRetEnvEvento;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.AssinaturaDocumento;
import br.com.transmissor.utils.UtilsWebService;
import br.com.transmissor.utils.UtilsXML;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.com.transmissor.utils.enumarator.TipoEvento;
import br.com.transmissor.utils.enumarator.VersaoEvento;
import br.com.utils.StringUtils;
import br.inf.portalfiscal.www.nfe.wsdl.recepcaoevento.RecepcaoEventoStub;

public class EnvioCCe extends ServicoWeb {
	
	private TRetEnvEvento retEnvEvento;
	
	public void enviaCCe(String chaveNF, String cnpj, int sequencial, String correcao) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			SAXException, IOException, ParserConfigurationException, MarshalException, XMLSignatureException, TransformerException, XMLStreamException {
		preparaVariaveis();
		setRetEnvEvento(null);
		if (getConfiguracao() != null) {
				log.info("Iniciando Envio CCe! chave: "+chaveNF+" cnpj: "+cnpj+" seq: "+sequencial+" correcao: "+correcao);
				alteraConfigEnvio(UtilsXML.getModeloFromChave(chaveNF));
				carregaURL();
				verificaWSEstado();
				UtilsWebService.preparaAmbiente(getWs_url(),getConfiguracao());
				
				correcao = Normalizer.normalize(correcao, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
				
				DetEvento detEvento = new DetEvento();
				detEvento.setDescEvento("Carta de Correcao");
				detEvento.setXCorrecao(correcao);
				detEvento.setXCondUso("A Carta de Correcao e disciplinada pelo paragrafo 1o-A do art. 7o do Convenio S/N, de 15 de dezembro de 1970 e pode ser"
						+ " utilizada para regularizacao de erro ocorrido na emissao de documento fiscal, desde que o erro nao esteja relacionado com: "
						+ "I - as variaveis que determinam o valor do imposto tais como: base de calculo, aliquota, diferenca de preco, quantidade, valor da operacao ou da prestacao; "
						+ "II - a correcao de dados cadastrais que implique mudanca do remetente ou do destinatario; III - a data de emissao ou de saida.");
				detEvento.setVersao(VersaoEvento.VERSAO_100.getVersao());
				
				LocalDateTime hoje = LocalDateTime.now();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
				String seq = StringUtils.rightStr("00"+String.valueOf(sequencial), 2);
				
				TEvento evento = new TEvento();
				evento.setVersao(VersaoEvento.VERSAO_100.getVersao());
				evento.setInfEvento(new InfEvento());
				evento.getInfEvento().setId("ID"+TipoEvento.CCE.getTipo()+chaveNF+seq);
				evento.getInfEvento().setCOrgao(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
				evento.getInfEvento().setTpAmb(String.valueOf(getConfiguracao().getAmbiente().getAmbiente()));
				evento.getInfEvento().setCNPJ(cnpj);
				evento.getInfEvento().setChNFe(chaveNF);
				evento.getInfEvento().setDhEvento(hoje.format(dtf)+ZonedDateTime.now().getOffset().getId());
				evento.getInfEvento().setTpEvento(TipoEvento.CCE.getTipo());
				evento.getInfEvento().setNSeqEvento(String.valueOf(sequencial));
				evento.getInfEvento().setVerEvento(VersaoEvento.VERSAO_100.getVersao());
				evento.getInfEvento().setDetEvento(detEvento);
				
				TEnvEvento envEvento = new TEnvEvento();
				envEvento.setIdLote("1");
				envEvento.setVersao(VersaoEvento.VERSAO_100.getVersao());
				envEvento.getEvento().add(evento);
				
				String strXML = new LeitorXML().criaStrXML(envEvento, false);
				log.info("Envio CCe: XML: "+strXML);
				
				strXML = new AssinaturaDocumento(getConfiguracao().getCertificado(), getConfiguracao().getPrivateKey()).assinaEvento(strXML);
				
				setRetEnvEvento(new LeitorXML().toObj(comunicaWS(strXML), TRetEnvEvento.class));
				setProcessou(true);
				salvarEvento(evento);
				log.info("Envio CCe Finalizado! Ret: "+retEnvEvento.getCStat());
		} else {
			setMsgErro("Configuração Nula!");
			log.error(getMsgErro());
		}
	}
	
	public void salvarEvento(TEvento evento){
		if ((isProcessou()) && (retEnvEvento != null)) {
			if ((retEnvEvento.getRetEvento() != null) && (!retEnvEvento.getRetEvento().isEmpty())) {
				TProcEvento procEvento = new TProcEvento();
				procEvento.setEvento(evento);
				procEvento.setRetEvento(retEnvEvento.getRetEvento().get(0));
				procEvento.setVersao(VersaoEvento.VERSAO_100.getVersao());
				
				UtilsXML.gravarEventoCCe(procEvento);
			}
		}
	}

	@Override
	public String comunicaWS(String strXML) throws XMLStreamException, AxisFault, RemoteException {
		OMElement elemento = UtilsXML.toOMElement(strXML);
		
		RecepcaoEventoStub.NfeCabecMsg cabec = new RecepcaoEventoStub.NfeCabecMsg();
		cabec.setCUF(String.valueOf(getConfiguracao().getUf().getCodigoUF()));
		cabec.setVersaoDados(VersaoEvento.VERSAO_100.getVersao());
		
		RecepcaoEventoStub.NfeCabecMsgE cabecE = new RecepcaoEventoStub.NfeCabecMsgE();
		cabecE.setNfeCabecMsg(cabec);
		
		RecepcaoEventoStub.NfeDadosMsg dados = new RecepcaoEventoStub.NfeDadosMsg();
		dados.setExtraElement(elemento);
		
		RecepcaoEventoStub stub = new RecepcaoEventoStub(getWs_url());
		return stub.nfeRecepcaoEvento(dados, cabecE).getExtraElement().toString();
	}

	@Override
	public void carregaURL() {
		setWs_url(UtilsWebService.getEnvioEventoURL(getConfiguracao().getUf(), getConfiguracao().getAmbiente(), 
			getConfiguracao().getTipoEmissao(), getConfiguracao().getModelo_df()));
	}
	
	private void alteraConfigEnvio(String modelo){
		getConfiguracao().setModelo_df(ModeloDF.valorDe(modelo));
	}

	public TRetEnvEvento getRetEnvEvento() {
		return retEnvEvento;
	}
	public void setRetEnvEvento(TRetEnvEvento retEnvEvento) {
		this.retEnvEvento = retEnvEvento;
	}
}