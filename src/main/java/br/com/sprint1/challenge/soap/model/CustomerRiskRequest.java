package br.com.sprint1.challenge.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CustomerRiskRequest", namespace = CustomerRiskRequest.NAMESPACE)
public class CustomerRiskRequest {

    static final String NAMESPACE = "http://challenge.sprint1.com.br/soap/customer-risk";

    @XmlElement(required = true, namespace = NAMESPACE)
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}

