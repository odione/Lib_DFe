package br.com.dfe.schema.eventoCCe;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRetEnvEvento", namespace = "http://www.portalfiscal.inf.br/nfe", propOrder = {
    "idLote",
    "tpAmb",
    "verAplic",
    "cOrgao",
    "cStat",
    "xMotivo",
    "retEvento"
})
public class TRetEnvEvento {

    @XmlElement(required = true)
    protected String idLote;
    @XmlElement(required = true)
    protected String tpAmb;
    @XmlElement(required = true)
    protected String verAplic;
    @XmlElement(required = true)
    protected String cOrgao;
    @XmlElement(required = true)
    protected String cStat;
    @XmlElement(required = true)
    protected String xMotivo;
    protected List<TretEvento> retEvento;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    
    public boolean isAutorizado() {
    	if (!getRetEvento().isEmpty()) {
    		return "135|136".contains(getRetEvento().get(0).getInfEvento().getCStat());
    	}
    	return false;
    }

    public String getIdLote() {
        return idLote;
    }
    public void setIdLote(String value) {
        this.idLote = value;
    }
    public String getTpAmb() {
        return tpAmb;
    }
    public void setTpAmb(String value) {
        this.tpAmb = value;
    }
    public String getVerAplic() {
        return verAplic;
    }
    public void setVerAplic(String value) {
        this.verAplic = value;
    }
    public String getCOrgao() {
        return cOrgao;
    }
    public void setCOrgao(String value) {
        this.cOrgao = value;
    }
    public String getCStat() {
        return cStat;
    }
    public void setCStat(String value) {
        this.cStat = value;
    }
    public String getXMotivo() {
        return xMotivo;
    }
    public void setXMotivo(String value) {
        this.xMotivo = value;
    }
    public List<TretEvento> getRetEvento() {
        if (retEvento == null) {
            retEvento = new ArrayList<TretEvento>();
        }
        return this.retEvento;
    }
    public String getVersao() {
        return versao;
    }
    public void setVersao(String value) {
        this.versao = value;
    }
}