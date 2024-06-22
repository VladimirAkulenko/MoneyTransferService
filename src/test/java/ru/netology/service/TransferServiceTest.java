package ru.netology.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.exception.ErrorTransferOrConfirmException;
import ru.netology.exception.InvalidDataException;
import ru.netology.model.Amount;
import ru.netology.model.ConfirmOperation;
import ru.netology.model.OperationResponse;
import ru.netology.model.Transfer;
import ru.netology.repository.TransferRepository;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceTest {
    Transfer transaction;
    ConfirmOperation operation;
    TransferService service;
    @Mock
    TransferRepository repository;
    public static final String OPERATION_ID = "1";
    private static final String CODE = "0000";

    @BeforeEach
    public void setUp() {
        service = new TransferService(repository);
        transaction = new Transfer("1111111111111111", "01/25",
                "123", "2222222222222222", new Amount(2000, "EUR"));
        operation = new ConfirmOperation(OPERATION_ID, CODE);
    }


    @ParameterizedTest
    @MethodSource("argumentsTransferError")
    public void transferErrorValidationTest(Transfer transaction) {
        Assertions.assertThrows(InvalidDataException.class, () -> service.transfer(transaction));
    }

    @Test
    public void confirmOperationTest() {
        when(repository.confirmOperation("1")).thenReturn(transaction);
        Assertions.assertEquals(new OperationResponse("1"), service.confirmOperation(operation));
    }

    @ParameterizedTest
    @MethodSource("argumentsConfirmOperationError")
    public void confirmOperationErrorValidationTest(ConfirmOperation operation) {
        Assertions.assertThrows(InvalidDataException.class, () -> service.confirmOperation(operation));
    }

    @Test
    public void confirmOperationNullTransaction() {
        when(repository.confirmOperation("1")).thenReturn(null);
        Assertions.assertThrows(ErrorTransferOrConfirmException.class, () -> service.confirmOperation(operation));
    }

    public static Stream<Arguments> argumentsConfirmOperationError() {
        return Stream.of(
                Arguments.of(new ConfirmOperation(null, "4569")),
                Arguments.of(new ConfirmOperation("1", null))
        );
    }


    public static Stream<Arguments> argumentsTransferError() {
        return Stream.of(
                Arguments.of(new Transfer(null, "01/25",
                        "123", "2222222222222222", new Amount(2000, "EUR"))),
                Arguments.of(new Transfer("1111111111111111", null,
                        "123", "2222222222222222", new Amount(2000, "EUR"))),
                Arguments.of(new Transfer("1111111111111111", "01/25",
                        null, "2222222222222222", new Amount(2000, "EUR"))),
                Arguments.of(new Transfer("1111111111111111", "01/25",
                        "123", null, new Amount(2000, "EUR"))),
                Arguments.of(new Transfer("1111111111111111", "01/25",
                        "123", "2222222222222222", new Amount(2000, null))),
                Arguments.of(new Transfer("1111111111111111", "01/25",
                        "123", "2222222222222222", new Amount(0, "EUR")))
        );
    }
}
