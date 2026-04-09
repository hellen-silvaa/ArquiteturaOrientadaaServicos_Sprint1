package br.com.sprint1.challenge.soap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CustomerRiskResponse", namespace = CustomerRiskRequest.NAMESPACE)
public class CustomerRiskResponse {

    @XmlElement(required = true, namespace = CustomerRiskRequest.NAMESPACE)
    private Long customerId;

    @XmlElement(required = true, namespace = CustomerRiskRequest.NAMESPACE)
    private String customerName;

    @XmlElement(required = true, namespace = CustomerRiskRequest.NAMESPACE)
    private int score;

    @XmlElement(required = true, namespace = CustomerRiskRequest.NAMESPACE)
    private String riskLevel;

    @XmlElement(required = true, namespace = CustomerRiskRequest.NAMESPACE)
    private String summary;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

