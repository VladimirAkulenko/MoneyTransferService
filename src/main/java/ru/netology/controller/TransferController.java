package ru.netology.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.netology.exception.InvalidDataException;
import ru.netology.model.ConfirmOperation;
import ru.netology.model.OperationResponse;
import ru.netology.model.Transfer;
import ru.netology.service.TransferService;

@RestController
@CrossOrigin()
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public OperationResponse transfer(@RequestBody Transfer transaction) {
        return transferService.transfer(transaction);
    }

    @PostMapping("/confirmOperation")
    public OperationResponse confirmOperation(@RequestBody ConfirmOperation operation) {
        return transferService.confirmOperation(operation);
    }
}