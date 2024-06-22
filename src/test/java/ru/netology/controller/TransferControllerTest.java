package ru.netology.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.model.Amount;
import ru.netology.model.ConfirmOperation;
import ru.netology.model.OperationResponse;
import ru.netology.model.Transfer;
import ru.netology.service.TransferService;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferControllerTest {
    Transfer transaction;
    ConfirmOperation operation;
    @Mock
    TransferService service;

    TransferController controller;

    @BeforeEach
    public void setUp() {
        controller = new TransferController(service);
        transaction = new Transfer("1111111111111111", "01/25",
                "123", "2222222222222222", new Amount(2000, "RUR"));
        operation = new ConfirmOperation("1", "0000");
    }

    @Test
    public void transferTest() {
        when(service.transfer(transaction)).thenReturn(new OperationResponse("1"));
        Assertions.assertEquals(new OperationResponse("1"), controller.transfer(transaction));
    }

    @Test
    public void confirmOperationTest() {
        when(service.confirmOperation(operation)).thenReturn(new OperationResponse("1"));
        Assertions.assertEquals(new OperationResponse("1"), controller.confirmOperation(operation));
    }
}
