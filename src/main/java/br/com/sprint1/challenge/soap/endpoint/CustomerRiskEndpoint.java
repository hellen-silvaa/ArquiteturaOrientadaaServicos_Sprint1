package br.com.sprint1.challenge.soap.endpoint;

import br.com.sprint1.challenge.config.SoapWebServiceConfig;
import br.com.sprint1.challenge.dto.ChurnDtos.ChurnPredictionResponse;
import br.com.sprint1.challenge.service.ChurnService;
import br.com.sprint1.challenge.soap.model.CustomerRiskRequest;
import br.com.sprint1.challenge.soap.model.CustomerRiskResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CustomerRiskEndpoint {

    private final ChurnService churnService;

    public CustomerRiskEndpoint(ChurnService churnService) {
        this.churnService = churnService;
    }

    @PayloadRoot(namespace = SoapWebServiceConfig.NAMESPACE_URI, localPart = "CustomerRiskRequest")
    @ResponsePayload
    public CustomerRiskResponse getCustomerRisk(@RequestPayload CustomerRiskRequest request) {
        ChurnPredictionResponse prediction = churnService.getPrediction(request.getCustomerId());

        CustomerRiskResponse response = new CustomerRiskResponse();
        response.setCustomerId(prediction.customerId());
        response.setCustomerName(prediction.customerName());
        response.setScore(prediction.score());
        response.setRiskLevel(prediction.riskLevel());
        response.setSummary(buildSummary(prediction));
        return response;
    }

    private String buildSummary(ChurnPredictionResponse prediction) {
        if (prediction.reasons() == null || prediction.reasons().isEmpty()) {
            return "Sem sinais adicionais de risco.";
        }
        return String.join("; ", prediction.reasons());
    }
}

