package br.com.dfe.integrador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parametro {

    @XmlElement(name = "Nome")
    private String nome;

    @XmlElement(name = "Valor")
    private String valor;
}
