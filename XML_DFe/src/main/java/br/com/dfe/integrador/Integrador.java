package br.com.dfe.integrador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Base64;

@XmlRootElement(name = "Integrador")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Integrador {

    @XmlElement(name = "Identificador")
    protected Identificador identificador;

    @XmlElement(name = "Componente")
    private Componente componente;

    @XmlElement(name = "IntegradorResposta")
    private IntegradorResposta integradorResposta;

    @XmlElement(name = "Resposta")
    private Resposta resposta;

    public void addDadosXML(String soapEnvelope) {
        String envelopeBase64 = Base64.getEncoder().encodeToString(soapEnvelope.getBytes());
        addParametro("dados", envelopeBase64);
    }

    public void addParametro(String nome, String valor) {
        if (this.componente.getMetodo().getParametros().getParametros() == null) {
            this.componente.getMetodo().getParametros().setParametros(new ArrayList<>());
        }
        this.componente.getMetodo().getParametros().getParametros().add(Parametro.builder()
            .nome(nome)
            .valor(valor)
            .build()
        );
    }
}
