package br.com.transmissor.utils.enumarator;

public enum URL_ConsultaNFC {
	
	//UF que usam NFC-e: AC AM AP RO RR TO PA DF MT PB MA PI RN SE BA PR RS SP RJ
	
	AC_HOMOLOGACAO("http://hml.sefaznet.ac.gov.br/nfce","AC",Ambiente.HOMOLOGACAO),
	AC_PRODUCAO("http://www.sefaznet.ac.gov.br/nfe","AC",Ambiente.PRODUCAO),
	AM_HOMOLOGACAO("http://homnfce.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp","AM",Ambiente.HOMOLOGACAO),
	AM_PRODUCAO("http://sistemas.sefaz.am.gov.br/nfceweb/consultarNFCe.jsp","AM",Ambiente.PRODUCAO),
	AP_HOMOLOGACAO("http://www.sefaz.ap.gov.br/sate/seg/SEGf_AcessarFuncao.jsp?cdFuncao=FIS_1261","AP",Ambiente.HOMOLOGACAO),
	AP_PRODUCAO("http://www.sefaz.ap.gov.br/sate/seg/SEGf_AcessarFuncao.jsp?cdFuncao=FIS_1261","AP",Ambiente.PRODUCAO),
	DF_HOMOLOGACAO("http://dec.fazenda.df.gov.br/ConsultarNFCe.aspx","DF",Ambiente.HOMOLOGACAO),
	DF_PRODUCAO("http://dec.fazenda.df.gov.br/ConsultarNFCe.aspx","DF",Ambiente.PRODUCAO),
	PB_HOMOLOGACAO("http://www6.receita.pb.gov.br/atf/seg/SEGf_AcessarFuncao.jsp?cdFuncao=FIS_1410","PB",Ambiente.HOMOLOGACAO),
	PB_PRODUCAO("https://www5.receita.pb.gov.br/atf/seg/SEGf_AcessarFuncao.jsp?cdFuncao=FIS_1410","PB",Ambiente.PRODUCAO),
	RJ_HOMOLOGACAO("http://www4.fazenda.rj.gov.br/consultaNFCe/QRCode","RJ",Ambiente.HOMOLOGACAO),
	RJ_PRODUCAO("http://www4.fazenda.rj.gov.br/consultaNFCe/QRCode","RJ",Ambiente.PRODUCAO),
	RN_HOMOLOGACAO("http://nfce.set.rn.gov.br/consultarNFCe.aspx","RN",Ambiente.HOMOLOGACAO),
	RN_PRODUCAO("http://nfce.set.rn.gov.br/consultarNFCe.aspx","RN",Ambiente.PRODUCAO),
	RO_HOMOLOGACAO("http://www.nfce.sefin.ro.gov.br/consultanfce/consulta.jsp","RO",Ambiente.HOMOLOGACAO),
	RO_PRODUCAO("http://www.nfce.sefin.ro.gov.br/consultanfce/consulta.jsp","RO",Ambiente.PRODUCAO),
	RR_HOMOLOGACAO("http://200.174.88.103:8080/nfce/servlet/qrcode","RR",Ambiente.HOMOLOGACAO),
	RR_PRODUCAO("https://www.sefaz.rr.gov.br/nfce/servlet/qrcode","RR",Ambiente.PRODUCAO),
	SE_HOMOLOGACAO("http://www.hom.nfe.se.gov.br/portal/consultarNFCe.jsp","SE",Ambiente.HOMOLOGACAO),
	SE_PRODUCAO("http://www.nfce.se.gov.br/portal/consultarNFCe.jsp","SE",Ambiente.PRODUCAO),
	TO_HOMOLOGACAO("http://www.sefaz.to.gov.br/nfce-portal/portal/principal.jsp","TO",Ambiente.HOMOLOGACAO),
	TO_PRODUCAO("http://www.sefaz.to.gov.br/nfce-portal/portal/principal.jsp","TO",Ambiente.PRODUCAO),
	BA_HOMOLOGACAO("http://hnfe.sefaz.ba.gov.br/servicos/nfce/modulos/geral/NFCEC_consulta_chave_acesso.aspx","BA",Ambiente.HOMOLOGACAO),
	BA_PRODUCAO("http://nfe.sefaz.ba.gov.br/servicos/nfce/modulos/geral/NFCEC_consulta_chave_acesso.aspx","BA",Ambiente.PRODUCAO),
	MA_HOMOLOGACAO("http://www.hom.nfce.sefaz.ma.gov.br/portal/consultarNFCe.jsp","MA",Ambiente.HOMOLOGACAO),
	MA_PRODUCAO("http://www.nfce.sefaz.ma.gov.br/portal/consultarNFCe.jsp","MA",Ambiente.PRODUCAO),
	PA_HOMOLOGACAO("https://appnfc.sefa.pa.gov.br/portal-homologacao/view/consultas/nfce/nfceForm.seam","PA",Ambiente.HOMOLOGACAO),
	PA_PRODUCAO("https://appnfc.sefa.pa.gov.br/portal/view/consultas/nfce/nfceForm.seam","PA",Ambiente.PRODUCAO),
	PI_HOMOLOGACAO("http://webas.sefaz.pi.gov.br/nfceweb-homologacao/consultarNFCe.jsf","PI",Ambiente.HOMOLOGACAO),
	PI_PRODUCAO("http://webas.sefaz.pi.gov.br/nfceweb/consultarNFCe.jsf","PI",Ambiente.PRODUCAO),
	MT_HOMOLOGACAO("http://homologacao.sefaz.mt.gov.br/nfce/consultanfce","MT",Ambiente.HOMOLOGACAO),
	MT_PRODUCAO("http://www.sefaz.mt.gov.br/nfce/consultanfce","MT",Ambiente.PRODUCAO),
	PR_HOMOLOGACAO("http://www.dfeportal.fazenda.pr.gov.br/dfe-portal/rest/servico/consultaNFCe","PR",Ambiente.HOMOLOGACAO),
	PR_PRODUCAO("http://www.dfeportal.fazenda.pr.gov.br/dfe-portal/rest/servico/consultaNFCe","PR",Ambiente.PRODUCAO),
	RS_HOMOLOGACAO("https://www.sefaz.rs.gov.br/NFCE/NFCE-COM.aspx","RS",Ambiente.HOMOLOGACAO),
	RS_PRODUCAO("https://www.sefaz.rs.gov.br/NFCE/NFCE-COM.aspx","RS",Ambiente.PRODUCAO),
	SP_HOMOLOGACAO("https://www.homologacao.nfce.fazenda.sp.gov.br/NFCeConsultaPublica/Paginas/ConsultaQRCode.aspx","SP",Ambiente.HOMOLOGACAO),
	SP_PRODUCAO("https://www.nfce.fazenda.sp.gov.br/NFCeConsultaPublica/Paginas/ConsultaQRCode.aspx","SP",Ambiente.PRODUCAO);
	
	private String url;
	private String uf;
	private Ambiente ambiente;
	
	private URL_ConsultaNFC(String url, String uf, Ambiente ambiente) {
		setUrl(url);
		setUf(uf);
		setAmbiente(ambiente);
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public Ambiente getAmbiente() {
		return ambiente;
	}
	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}
}