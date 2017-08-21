package br.com.transmissor.utils.enumarator;

public enum WS_URL_StatusServico {
	
	//55 - NF-e
	SVRS_HOMOLOGACAO("https://nfe-homologacao.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","AC|AL|AP|DF|PB|RJ|RN|RO|RR|SC|SE|TO|ES",
		Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVRS_PRODUCAO("https://nfe.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","AC|AL|AP|DF|PB|RJ|RN|RO|RR|SC|SE|TO|ES",
		Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	AM_HOMOLOGACAO("https://homnfe.sefaz.am.gov.br/services2/services/NfeStatusServico2","AM",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	AM_PRODUCAO("https://nfe.sefaz.am.gov.br/services2/services/NfeStatusServico2","AM",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	BA_HOMOLOGACAO("https://hnfe.sefaz.ba.gov.br/webservices/NfeStatusServico/NfeStatusServico.asmx","BA",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	BA_PRODUCAO("https://nfe.sefaz.ba.gov.br/webservices/NfeStatusServico/NfeStatusServico.asmx","BA",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	CE_HOMOLOGACAO("https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeStatusServico2?wsdl","CE",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	CE_PRODUCAO("https://nfe.sefaz.ce.gov.br/nfe2/services/NfeStatusServico2?wsdl","CE",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	GO_HOMOLOGACAO("https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2?wsdl","GO",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	GO_PRODUCAO("https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2?wsdl","GO",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MG_HOMOLOGACAO("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeStatusServico2","MG",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MG_PRODUCAO("https://nfe.fazenda.mg.gov.br/nfe2/services/NfeStatus2","MG",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MS_HOMOLOGACAO("https://homologacao.nfe.ms.gov.br/homologacao/services2/NfeStatusServico2","MS",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MS_PRODUCAO("https://nfe.fazenda.ms.gov.br/producao/services2/NfeStatusServico2","MS",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MT_HOMOLOGACAO("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2?wsdl","MT",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	MT_PRODUCAO("https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2?wsdl","MT",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PE_HOMOLOGACAO("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2","PE",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PE_PRODUCAO("https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2","PE",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PR_HOMOLOGACAO("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeStatusServico3","PR",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	PR_PRODUCAO("https://nfe.fazenda.pr.gov.br/nfe/NFeStatusServico3","PR",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	RS_HOMOLOGACAO("https://nfe-homologacao.sefazrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","RS",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	RS_PRODUCAO("https://nfe.sefazrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","RS",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SP_HOMOLOGACAO("https://homologacao.nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx","SP",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SP_PRODUCAO("https://nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx","SP",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVAN_HOMOLOGACAO("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx","MA|PA|PI",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVAN_PRODUCAO("https://www.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx","MA|PA|PI",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFE),
	SVC_AN_HOMOLOGACAO("https://hom.sefazvirtual.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx","AC|AL|AP|DF|ES|MG|PB|RJ|RN|RO|RR|RS|SC|SE|SP|TO",
		Ambiente.HOMOLOGACAO,TipoEmissao.CONTINGENCIA_SVCAN,ModeloDF.MODELO_NFE),
	SVC_AN_PRODUCAO("https://hom.svc.fazenda.gov.br/NfeStatusServico2/NfeStatusServico2.asmx","AC|AL|AP|DF|ES|MG|PB|RJ|RN|RO|RR|RS|SC|SE|SP|TO",
		Ambiente.PRODUCAO,TipoEmissao.CONTINGENCIA_SVCAN,ModeloDF.MODELO_NFE),
	SVC_RS_HOMOLOGACAO("https://nfe-homologacao.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","AM|BA|CE|GO|MA|MS|MT|PA|PE|PI|PR",
		Ambiente.HOMOLOGACAO,TipoEmissao.CONTINGENCIA_SVRS,ModeloDF.MODELO_NFE),
	SVC_RS_PRODUCAO("https://nfe.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","AM|BA|CE|GO|MA|MS|MT|PA|PE|PI|PR",
		Ambiente.PRODUCAO,TipoEmissao.CONTINGENCIA_SVRS,ModeloDF.MODELO_NFE),
	
	//65 - NFC-e
	SVRS_HOMOLOGACAO_NFC("https://nfce-homologacao.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx",
		"AC|AP|DF|PB|RJ|RN|RO|RR|SE|TO|BA|MA|PA|PI",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	SVRS_PRODUCAO_NFC("https://nfce.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx",
		"AC|AP|DF|PB|RJ|RN|RO|RR|SE|TO|BA|MA|PA|PI",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	AM_HOMOLOGACAO_NFC("https://homnfce.sefaz.am.gov.br/nfce-services/services/NfeStatusServico2","AM",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	AM_PRODUCAO_NFC("https://nfce.sefaz.am.gov.br/nfce-services/services/NfeStatusServico2","AM",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	MT_HOMOLOGACAO_NFC("https://homologacao.sefaz.mt.gov.br/nfcews/services/NfeStatusServico2","MT",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	MT_PRODUCAO_NFC("https://nfce.sefaz.mt.gov.br/nfcews/services/NfeStatusServico2","MT",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	PR_HOMOLOGACAO_NFC("https://homologacao.nfce.fazenda.pr.gov.br/nfce/NFeStatusServico3","PR",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	PR_PRODUCAO_NFC("https://nfce.fazenda.pr.gov.br/nfce/NFeStatusServico3","PR",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	RS_HOMOLOGACAO_NFC("https://nfce-homologacao.sefazrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","RS",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	RS_PRODUCAO_NFC("https://nfce.sefazrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx","RS",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	SP_HOMOLOGACAO_NFC("https://homologacao.nfce.fazenda.sp.gov.br/ws/nfestatusservico2.asmx","SP",Ambiente.HOMOLOGACAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE),
	SP_PRODUCAO_NFC("https://nfce.fazenda.sp.gov.br/ws/nfestatusservico2.asmx","SP",Ambiente.PRODUCAO,TipoEmissao.NORMAL,ModeloDF.MODELO_NFCE);

	private String url;
	private String uf;
	private Ambiente ambiente;
	private TipoEmissao tipoEmissao;
	private ModeloDF modelo;
	
	private WS_URL_StatusServico(String url, String uf, Ambiente ambiente, TipoEmissao tipoEmissao, ModeloDF modelo) {
		this.setUrl(url);
		this.setUf(uf);
		this.setAmbiente(ambiente);
		this.setTipoEmissao(tipoEmissao);
		this.setModelo(modelo);
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
	public TipoEmissao getTipoEmissao() {
		return tipoEmissao;
	}
	public void setTipoEmissao(TipoEmissao tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}
	public ModeloDF getModelo() {
		return modelo;
	}
	public void setModelo(ModeloDF modelo) {
		this.modelo = modelo;
	}
}