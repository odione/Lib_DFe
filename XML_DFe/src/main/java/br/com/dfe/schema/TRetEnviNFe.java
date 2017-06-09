package br.com.dfe.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRetEnviNFe", propOrder = {
    "tpAmb",
    "verAplic",
    "cStat",
    "xMotivo",
    "cuf",
    "dhRecbto",
    "infRec",
    "protNFe"
})
public class TRetEnviNFe {

    @XmlElement(required = true)
    protected String tpAmb;
    @XmlElement(required = true)
    protected String verAplic;
    @XmlElement(required = true)
    protected String cStat;
    @XmlElement(required = true)
    protected String xMotivo;
    @XmlElement(name = "cUF", required = true)
    protected String cuf;
    @XmlElement(required = true)
    protected String dhRecbto;
    protected TRetEnviNFe.InfRec infRec;
    protected TProtNFe protNFe;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    
    public boolean isAutorizada() {
    	return "100|150|110|301|302|303".contains(protNFe != null ? protNFe.getInfProt().getCStat() : cStat);
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
    public String getCUF() {
        return cuf;
    }
    public void setCUF(String value) {
        this.cuf = value;
    }
    public String getDhRecbto() {
        return dhRecbto;
    }
    public void setDhRecbto(String value) {
        this.dhRecbto = value;
    }
    public TRetEnviNFe.InfRec getInfRec() {
        return infRec;
    }
    public void setInfRec(TRetEnviNFe.InfRec value) {
        this.infRec = value;
    }
    public TProtNFe getProtNFe() {
        return protNFe;
    }
    public void setProtNFe(TProtNFe value) {
        this.protNFe = value;
    }
    public String getVersao() {
        return versao;
    }
    public void setVersao(String value) {
        this.versao = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "nRec",
        "tMed"
    })
    public static class InfRec {

        @XmlElement(required = true)
        protected String nRec;
        @XmlElement(required = true)
        protected String tMed;

        public String getNRec() {
            return nRec;
        }
        public void setNRec(String value) {
            this.nRec = value;
        }
        public String getTMed() {
            return tMed;
        }
        public void setTMed(String value) {
            this.tMed = value;
        }
    }
}