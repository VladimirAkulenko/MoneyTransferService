package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.advice.ErrorResponse;
import ru.netology.model.Amount;
import ru.netology.model.ConfirmOperation;
import ru.netology.model.OperationResponse;
import ru.netology.model.Transfer;

import java.util.Objects;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContainerTest {
    @Autowired
    TestRestTemplate restTemplate;
    @Container
    private final GenericContainer<?> myApp = new GenericContainer<>("myapp:latest")
            .withExposedPorts(5500);

    private final ConfirmOperation operation = new ConfirmOperation("1", "0000");
    private final Transfer transaction = new Transfer("1111111111111111", "01/25",
            "123", "2222222222222222", new Amount(2000, "RUR"));


    @Test
    public void myAppTransferTest() {
        OperationResponse response = restTemplate.postForObject("http://localhost:" + myApp.getMappedPort(5500)
                + "/transfer", transaction, OperationResponse.class);
        Assertions.assertEquals(new OperationResponse("1"), response);
    }

    @Test
    public void myAppTransferInvalidDataTest() {
        Transfer transaction = new Transfer(null, null, null,
                null, new Amount(0, null));
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("http://localhost:"
                + myApp.getMappedPort(5500) + "/transfer", transaction, ErrorResponse.class);
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()).id(), 1);
    }

    @Test
    void appConfirmOperationTest() {
        restTemplate.postForObject("http://localhost:" + myApp.getMappedPort(5500)
                + "/transfer", transaction, OperationResponse.class);
        OperationResponse response = restTemplate.postForObject("http://localhost:"
                + myApp.getMappedPort(5500) + "/confirmOperation", operation, OperationResponse.class);
        Assertions.assertEquals(new OperationResponse("1"), response);
    }

}
