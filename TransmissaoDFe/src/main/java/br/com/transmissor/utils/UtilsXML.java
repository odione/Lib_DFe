package br.com.transmissor.utils;

import java.io.File;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;

import br.com.dfe.schema.TNFe;
import br.com.dfe.schema.TNfeProc;
import br.com.dfe.schema.TProcEvento;
import br.com.dfe.schema.TProcInutNFe;
import br.com.dfe.schema.TRetEvento;
import br.com.dfe.schema.TUfEmi;
import br.com.dfe.util.LeitorXML;
import br.com.transmissor.utils.enumarator.Ambiente;
import br.com.transmissor.utils.enumarator.Estado;
import br.com.transmissor.utils.enumarator.ModeloDF;
import br.com.transmissor.utils.enumarator.StatProc;
import br.com.transmissor.utils.enumarator.TipoEvento;
import br.com.utils.StringUtils;;

public class UtilsXML {
	
	public static final String sufixProcNfe = "-procNFe.xml";
	public static final String sufixProcEventoCCe = "_110110-procEventoNFe.xml";
	public static final String sufixProcEventoCanc = "_110111-procEventoNFe.xml";
	private static final String sufixInut = "-procInutNFe.xml";
	private static final String sufixErro = "-erroNFe.xml";
	
	protected static final Logger log = LogManager.getLogger(UtilsXML.class.getSimpleName());
	
	private static void gravarXML(String strXML, String pathFile){
		strXML = addXMLEncoding(strXML);
		File arq = new File(pathFile);
		FileRW.criaDiretorio(arq.getParent()+"/");
		arq.delete();
		FileRW.salvaXMLStr(strXML, pathFile);
	}
	
	/**
	 * salva TNfeProc no diretorio de autorizadas
	 * @param procNfe
	 */
	public static void gravarAutorizada(TNfeProc procNfe) {
		String pathFile = getPathAutorizada(procNfe.getNFe().getInfNFe().getEmit().getCNPJ(),
			ModeloDF.valorDe(procNfe.getNFe().getInfNFe().getIde().getMod()));
		
		pathFile += procNfe.getProtNFe().getInfProt().getChNFe()+sufixProcNfe;
		
		String strXML = new LeitorXML().criaStrXML(procNfe, false);
		gravarXML(strXML, pathFile);
	}
	
	/**
	 * Salva TNfeProc quando ocorrer rejeiçao, no diretorio de Erros
	 * @param procNfe
	 */
	public static void gravarXMLErro(TNfeProc procNfe) {
		String pathFile = getPathErro(procNfe.getNFe().getInfNFe().getEmit().getCNPJ(),
			ModeloDF.valorDe(procNfe.getNFe().getInfNFe().getIde().getMod()));
		
		pathFile += procNfe.getProtNFe().getInfProt().getChNFe()+sufixErro;
		
		String strXML = new LeitorXML().criaStrXML(procNfe, false);
		gravarXML(strXML, pathFile);
	}
	
	/**
	 * Salva TProcEvento CCe no diretorio de Carta de Correçao
	 * @param procEvento
	 */
	public static void gravarEventoCCe(br.com.dfe.schema.eventoCCe.TProcEvento procEvento) {
		if (StatProc.isEventoAut(procEvento.getRetEvento().getInfEvento().getCStat())) {
			String seq = StringUtils.rightStr("00"+procEvento.getRetEvento().getInfEvento().getNSeqEvento(), 2);
			String chave = procEvento.getRetEvento().getInfEvento().getChNFe();
			String pathFile = getPathXMLEventoCCe(chave, getCNPJFromChave(chave), getModeloFromChave(chave), seq);
			
			gravarXML(new LeitorXML().criaStrXML(procEvento, false), pathFile);
		}
	}
	
	/**
	 * Salva TProcEvento Cancelamento no diretorio de Cancelamento
	 * @param procEvento
	 */
	public static void gravarEventoCanc(br.com.dfe.schema.eventoCanc.TProcEvento procEvento) {
		if (StatProc.isEventoAut(procEvento.getRetEvento().getInfEvento().getCStat())) {
			String chave = procEvento.getRetEvento().getInfEvento().getChNFe();
			String pathFile = getPathXMLEventoCancelamento(chave, getCNPJFromChave(chave), getModeloFromChave(chave));
			
			gravarXML(new LeitorXML().criaStrXML(procEvento, false), pathFile);			
		}
	}
	
	/**
	 * Retorna o caminho completo do XML Autorizado
	 * @param chaveNF
	 * @param cnpj
	 * @param modelo
	 */
	public static String getPathXMLAutorizada(String chaveNF, String cnpj, String modelo){
		return getPathAutorizada(StringUtils.onlyNumber(cnpj),ModeloDF.valorDe(modelo))+chaveNF+sufixProcNfe;
	}
	
	/**
	 * 
	 * @param chave
	 * @param cnpj
	 * @param modelo
	 * @param seqEvento
	 * @return caminho do xml do evento CCe
	 */
	public static String getPathXMLEventoCCe(String chave, String cnpj, String modelo, String seqEvento) {
		return getPathEventoCCe(cnpj, ModeloDF.valorDe(modelo))+chave+"_"+seqEvento+sufixProcEventoCCe;
	}
	
	/**
	 * 
	 * @param chave
	 * @param cnpj
	 * @param modelo
	 * @return caminho do xml do evento Cancelamento
	 */
	public static String getPathXMLEventoCancelamento(String chave, String cnpj, String modelo) {
		return getPathEventoCanc(cnpj, ModeloDF.valorDe(modelo))+chave+sufixProcEventoCanc;
	}
	
	/**
	 * @param pathFile
	 * @return TNfeProc carregada de um arquivo XML
	 */
	public static TNfeProc carregaProcNFeFromFile (String pathFile){
		return new LeitorXML().toObj(FileRW.lerArquivo(pathFile), TNfeProc.class);
	}
	
	/**
	 * Salva TNfeProc no diretorio conforme o Retorno (Autorizada, Denegada, Rejeicao)
	 * @param procNfe
	 */
	public static void salvarProcNfe(TNfeProc procNfe) {
		if ((procNfe != null) && (procNfe.getProtNFe() != null)) {
			String strStat = procNfe.getProtNFe().getInfProt().getCStat();
			if ((StatProc.isAutorizada(strStat)) || (StatProc.isDenegada(strStat))) {
				UtilsXML.gravarAutorizada(procNfe);
			} else {
				UtilsXML.gravarXMLErro(procNfe);
			}
		}
	}
	
	/**
	 * Salva TProcEvento no diretorio correspondente ao tipo do evento (Canc, CCe)
	 * @param procEvento
	 */
	public static void salvarProcEvento(TProcEvento procEvento) {
		if ((procEvento != null) && (procEvento.getRetEvento() != null)) {
			if (StatProc.isEventoAut(procEvento.getRetEvento().getInfEvento().getCStat())) {
				if (procEvento.getRetEvento().getInfEvento().getTpEvento().equals(TipoEvento.CANCELAMENTO.getTipo())) {
					String pathFile = getPathEventoCanc(procEvento.getEvento().getInfEvento().getCNPJ(),
						ModeloDF.valorDe(getModeloFromChave(procEvento.getRetEvento().getInfEvento().getChNFe())));
					pathFile += procEvento.getRetEvento().getInfEvento().getChNFe()+sufixProcEventoCanc;
					
					gravarXML(new LeitorXML().criaStrXML(procEvento, false), pathFile);
				} else {
					String pathFile = getPathEventoCCe(procEvento.getEvento().getInfEvento().getCNPJ(),
						ModeloDF.valorDe(getModeloFromChave(procEvento.getRetEvento().getInfEvento().getChNFe())));
					String seq = StringUtils.rightStr("00"+procEvento.getRetEvento().getInfEvento().getNSeqEvento(), 2);
					pathFile += procEvento.getRetEvento().getInfEvento().getChNFe()+"_"+seq+sufixProcEventoCCe;
					
					gravarXML(new LeitorXML().criaStrXML(procEvento, false), pathFile);
				}
			} 
		}
	}
	
	public static void salvarProcInutilizacao(TProcInutNFe procInut){
		if ((procInut != null) && (procInut.getRetInutNFe() != null)) {
			if (StatProc.isInutilizada(procInut.getRetInutNFe().getInfInut().getCStat())) {
				String pathFile = getPathInutilizada(procInut.getInutNFe().getInfInut().getCNPJ(),
					ModeloDF.valorDe(procInut.getInutNFe().getInfInut().getMod()));
				pathFile += StringUtils.rightStr(procInut.getInutNFe().getInfInut().getId(), 41)+sufixInut;
				
				gravarXML(new LeitorXML().criaStrXML(procInut, false), pathFile);
			}
		}
	}
	
	/**
	 * Gera String do qrCode! Nfe deve ter a tag signature!
	 * @param nfe
	 * @param idCSC
	 * @param CSC
	 * @return String qrCode
	 */
	public static String getQrCodeStr(TNFe nfe, String idCSC, String CSC){
		String dest = "";
		if (nfe.getInfNFe().getDest() != null) {
			if (nfe.getInfNFe().getDest().getCPF() != null) {
				dest = nfe.getInfNFe().getDest().getCPF();				
			} else {
				dest = nfe.getInfNFe().getDest().getCNPJ();
			}
		}
		
		StringBuilder qrCode = new StringBuilder();
		try {
			String digVal = findDigestValueFromString(new LeitorXML().criaStrXML(nfe, false));
			if (digVal.equals("")) {
				return "";
			}
			
			qrCode.append("chNFe=").append(StringUtils.rightStr(nfe.getInfNFe().getId(), 44));
			
			qrCode.append("&nVersao=").append("100");
			qrCode.append("&tpAmb=").append(nfe.getInfNFe().getIde().getTpAmb());
			if (!dest.equals("")) {
				qrCode.append("&cDest=").append(dest);
			}
			
			String dhEmiHex = new String(Hex.encodeHex(nfe.getInfNFe().getIde().getDhEmi().getBytes()));
			String digValHex = new String(Hex.encodeHex(digVal.getBytes()));
			if (nfe.getInfNFe().getEmit().getEnderEmit().getUF() == TUfEmi.PR) {
				dhEmiHex = dhEmiHex.toLowerCase();
				digValHex = digValHex.toLowerCase();
			}
			
			qrCode.append("&dhEmi=").append(dhEmiHex);
			qrCode.append("&vNF=").append(nfe.getInfNFe().getTotal().getICMSTot().getVNF());
			qrCode.append("&vICMS=").append(nfe.getInfNFe().getTotal().getICMSTot().getVICMS());
			qrCode.append("&digVal=").append(digValHex);
			qrCode.append("&cIdToken=").append(idCSC);
			
			String hashQRCode = getHashSHA1(qrCode.toString()+CSC).toUpperCase();
			qrCode.append("&cHashQRCode=").append(hashQRCode);
		} catch (Exception e) {
			e.printStackTrace();
			log.catching(e);
		}
		
		return qrCode.toString();
	}
	
	public static String getUrlQRCode(TNFe nfe, String idCSC, String CSC) {
		if ((nfe != null) && (nfe.getInfNFe().getIde().getMod().equals(String.valueOf(ModeloDF.MODELO_NFCE.getModelo())))) {
			String url = UtilsWebService.getUrlQRCode(Estado.valorDe(Integer.valueOf(nfe.getInfNFe().getIde().getCUF())), 
				Ambiente.valorDe(nfe.getInfNFe().getIde().getTpAmb()));
			return "<![CDATA["+url.concat("?").concat(getQrCodeStr(nfe,idCSC,CSC)+"]]>");
		}
		return "";
	}
	
	public static OMElement toOMElement(String xml) {
		OMFactory factory = OMAbstractFactory.getOMFactory();
		OMElement element = factory.getMetaFactory().createOMBuilder(factory, StAXParserConfiguration.NON_COALESCING , 
			new InputSource(new StringReader(xml))).getDocumentElement();
		
		log.debug("XML transformado: "+element.toString());
		return element;
	}
	
	public static String getHashSHA1(String value){
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA-1");
			return new String(Hex.encodeHex(mDigest.digest(value.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        return "";
	}
	
	public static String findDigestValueFromString(String strXML) {
		if (strXML.contains("<DigestValue>")) {
			int inicio = strXML.indexOf("<DigestValue>")+13;
			int fim = strXML.indexOf("</DigestValue>");
			return strXML.substring(inicio, fim);
		}
		return "";
	}
	
	public static String getModeloFromChave(String chave){
		return chave.substring(20, 22);
	}
	
	public static String getCNPJFromChave(String chave) {
		return chave.substring(6, 20);
	}
	
	public static String getDirApp(){
		return System.getProperty("user.dir");
	}
	
	public static String getPathAutorizada(String cnpj, ModeloDF modeloDF) {
		return getDirApp() + "/Autorizada/" + StringUtils.onlyNumber(cnpj) + "/"+ String.valueOf(modeloDF.getModelo())+"/";
	}

	public static String getPathInutilizada(String cnpj, ModeloDF modeloDF) {
		return getDirApp() + "/Inutilizada/" + StringUtils.onlyNumber(cnpj) + "/"+ String.valueOf(modeloDF.getModelo())+"/";
	}

	public static String getPathEventoCanc(String cnpj, ModeloDF modeloDF) {
		return getDirApp()+ "/Evento/" + StringUtils.onlyNumber(cnpj)+"/"+String.valueOf(modeloDF.getModelo())+"/Cancelada/";
	}

	public static String getPathEventoCCe(String cnpj, ModeloDF modeloDF) {
		return getDirApp() + "/Evento/" + StringUtils.onlyNumber(cnpj) + "/"+ String.valueOf(modeloDF.getModelo())+"/CCe/";
	}

	public static String getPathErro(String cnpj, ModeloDF modeloDF) {
		return getDirApp()+ "/Erro/" + StringUtils.onlyNumber(cnpj) + "/"+String.valueOf(modeloDF.getModelo())+"/";
	}

	public static String getPathXSD() {
		return getDirApp()+"/XSD/";
	}
	
	public static X509Certificate getCertificadoFromAlias(String alias) {
		try {
			KeyStore ks = getKS();
			
			Enumeration<String> apelidos = ks.aliases();
			
			while (apelidos.hasMoreElements()) {
				if (apelidos.nextElement().equals(alias)) {
					return (X509Certificate)ks.getCertificate(alias);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PrivateKey getPrivateKeyFromAlias(String alias, String senha){
		try {
			KeyStore ks = getKS();
			Enumeration<String> apelidos = ks.aliases();
			
			while (apelidos.hasMoreElements()) {
				if (apelidos.nextElement().equals(alias)) {
					return (PrivateKey)ks.getKey(alias, senha.toCharArray());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static KeyStore getKS() throws Exception {
		KeyStore ks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
		ks.load(null,null);
		return ks;
	}
	
	public static String addXMLEncoding(String strXML) {
		if (!strXML.startsWith("<?xml version=")) {
			strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".concat(strXML);
		}
		return strXML;
	}
	
	public static boolean fileExist(String pathFile){
		return new File(pathFile).exists();
	}
	
	public static boolean isEventoCCe(TRetEvento retEvento) {
		return retEvento.getInfEvento().getTpEvento().equals(TipoEvento.CCE.getTipo());
	}
	
	public static boolean isEventoCancelamento(TRetEvento retEvento) {
		return retEvento.getInfEvento().getTpEvento().equals(TipoEvento.CANCELAMENTO.getTipo());
	}
}