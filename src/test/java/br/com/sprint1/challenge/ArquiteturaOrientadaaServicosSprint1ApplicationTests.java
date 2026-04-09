package br.com.sprint1.challenge;

import br.com.sprint1.challenge.entity.Customer;
import br.com.sprint1.challenge.entity.Dealership;
import br.com.sprint1.challenge.entity.ServiceRecord;
import br.com.sprint1.challenge.entity.Vehicle;
import br.com.sprint1.challenge.repository.CustomerRepository;
import br.com.sprint1.challenge.repository.DealershipRepository;
import br.com.sprint1.challenge.repository.ServiceRecordRepository;
import br.com.sprint1.challenge.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArquiteturaOrientadaaServicosSprint1ApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DealershipRepository dealershipRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldExposeOpenApiDocs() {
        ResponseEntity<String> response = restTemplate.getForEntity("/v3/api-docs", String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains("\"openapi\""));
    }

    @Test
    void shouldExposeCustomerRiskWsdl() {
        ResponseEntity<String> response = restTemplate.getForEntity("/ws/customerRisk.wsdl", String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains("CustomerRiskPort"));
        Assertions.assertTrue(response.getBody().contains("CustomerRiskRequest"));
    }

    @Test
    void shouldReturnSoapCustomerRiskResponse() throws Exception {
        Long customerIdForSoap = createCustomerForSoapScenario();
        String soapRequest = """
                <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"
                                  xmlns:cus=\"http://challenge.sprint1.com.br/soap/customer-risk\">
                  <soapenv:Header/>
                  <soapenv:Body>
                    <cus:CustomerRiskRequest>
                      <cus:customerId>%d</cus:customerId>
                    </cus:CustomerRiskRequest>
                  </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(customerIdForSoap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> request = new HttpEntity<>(soapRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/ws", request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        var document = factory.newDocumentBuilder()
                .parse(new ByteArrayInputStream(response.getBody().getBytes(StandardCharsets.UTF_8)));

        XPath xPath = XPathFactory.newInstance().newXPath();
        String customerId = (String) xPath.evaluate("//*[local-name()='customerId']/text()", document, XPathConstants.STRING);
        String customerName = (String) xPath.evaluate("//*[local-name()='customerName']/text()", document, XPathConstants.STRING);
        String riskLevel = (String) xPath.evaluate("//*[local-name()='riskLevel']/text()", document, XPathConstants.STRING);

        Assertions.assertEquals(String.valueOf(customerIdForSoap), customerId);
        Assertions.assertFalse(customerName.isBlank());
        Assertions.assertFalse(riskLevel.isBlank());
    }

    private Long createCustomerForSoapScenario() {
        long unique = System.nanoTime();

        Dealership dealership = dealershipRepository.save(new Dealership(
                null,
                "Ford Test",
                "Sao Paulo",
                "Sudeste"
        ));

        Customer customer = customerRepository.save(new Customer(
                null,
                "Cliente SOAP Teste",
                "soap-" + unique + "@email.com",
                "11999990000",
                "Sao Paulo",
                "SP",
                dealership.getId()
        ));

        Vehicle vehicle = vehicleRepository.save(new Vehicle(
                null,
                "VIN" + unique,
                "T-Cross",
                "SUV",
                2022,
                45000,
                customer.getId(),
                dealership.getId(),
                LocalDate.now().plusMonths(3),
                "WARN"
        ));

        serviceRecordRepository.save(new ServiceRecord(
                null,
                vehicle.getId(),
                dealership.getId(),
                "Revisao",
                LocalDate.now().minusDays(220),
                new BigDecimal("500.00")
        ));

        return customer.getId();
    }
}

