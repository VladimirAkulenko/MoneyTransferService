package ru.netology.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.model.Amount;
import ru.netology.model.TransactionState;
import ru.netology.model.Transfer;


@SpringBootTest
public class TransferRepositoryTest {
    TransferRepository repository;
    Transfer transfer;
    public static final String OPERATION_ID = "1";

    @BeforeEach
    public void setUp() {
        repository = new TransferRepository();
        transfer = new Transfer("1111111111111111", "01/25",
                "123", "2222222222222222", new Amount(2000, "RUR"));
    }

    @Test
    public void confirmOperationTest() {
        repository.addTransfer(OPERATION_ID, transfer);
        Assertions.assertEquals(transfer, repository.confirmOperation("1"));
        Assertions.assertEquals(TransactionState.OK, transfer.getState());
    }

    @Test
    public void confirmOperationTestNull() {
        Assertions.assertNull(repository.confirmOperation("1"));
    }

    @Test
    public void errorConfirmOperationTest() {
        repository.addTransfer(OPERATION_ID, transfer);
        Assertions.assertEquals(transfer, repository.errorConfirmOperation("1"));
        Assertions.assertEquals(TransactionState.ERROR, transfer.getState());
    }

    @Test
    public void errorConfirmOperationTestNull() {
        Assertions.assertNull(repository.confirmOperation("1"));
    }
}
