package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.exception.ErrorTransferOrConfirmException;
import ru.netology.exception.InvalidDataException;
import ru.netology.logger.Logger;
import ru.netology.logger.LoggerImpl;
import ru.netology.model.ConfirmOperation;
import ru.netology.model.OperationResponse;
import ru.netology.model.Transfer;
import ru.netology.repository.TransferRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private static Logger logger;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
        logger = LoggerImpl.getInstance();
    }

    public OperationResponse transfer(Transfer transaction) {

        validation(transaction);

        final String transferId = Integer.toString(transferRepository.getOperationId());

        transferRepository.addTransfer(transferId, transaction);
        transferRepository.addCode(transferId, UUID.randomUUID().toString());

        logger.info(transaction.toString());

        return new OperationResponse(transferId);
    }

    public OperationResponse confirmOperation(ConfirmOperation operation) {
        validationConfirmOperation(operation);
        Transfer transaction;
        final String operationId = operation.getOperationId();

        if (transferRepository.checkCode(operationId)) {
            transaction = transferRepository.confirmOperation(operationId);
        } else {
            transaction = transferRepository.errorConfirmOperation(operationId);
        }

        if (transaction == null) {
            throw new ErrorTransferOrConfirmException("Ошибка подтверждения: транзакция с id  " + operationId
                    + " не был найден" + operation);
        } else {
            final String code = transferRepository.removeCode(operationId);
            logger.info("Перевод одобрен: " + operationId + " , " + code);

            logger.info("Статус транзакции изменен");
            logger.info(transaction.toString());
            return new OperationResponse(operationId);
        }
    }

    private void validation(Transfer transaction) {
        final String cardFromNumber = transaction.getCardFromNumber();
        final String cardToNumber = transaction.getCardToNumber();
        final String cardFromCVV = transaction.getCardFromCVV();
        final String cardFromValidTill = transaction.getCardFromValidTill();
        final Integer amount = transaction.getAmount().getValue();

        checkCard(cardFromNumber, cardToNumber);
        checkCVV(cardFromCVV);
        checkValidTill(cardFromValidTill);
        checkAmount(amount);
    }

    //Проверка номеров карт
    public void checkCard(String cardFromNumber, String cardToNumber) {
        if (cardFromNumber == null || cardFromNumber.isEmpty()) {
            throw new InvalidDataException("Номер карты отправителя обязателен");
        } else if (cardToNumber == null || cardToNumber.isEmpty()) {
            throw new InvalidDataException("Номер карты получателя обязателен");
        } else if (!cardFromNumber.matches("[0-9]{16}")) {
            throw new InvalidDataException("Номер вашей карты должен состоять из 16 символов");
        } else if (!cardToNumber.matches("[0-9]{16}")) {
            throw new InvalidDataException("Номер карты получателя должен состоять из 16 символов");
        }
    }

    // Проверка срока действия карты
    public void checkValidTill(String cardFromValidTill) {
        if (cardFromValidTill == null || cardFromValidTill.isEmpty()) {
            throw new InvalidDataException("Срок действия карты обязателен");
        }
        StringBuilder stringBuilder = new StringBuilder(cardFromValidTill);

        int month = Integer.parseInt(stringBuilder.substring(0, 2 ));
        int year = Integer.parseInt("20" + stringBuilder.substring(3, 5 ));

        if (month > 12) {
            throw new InvalidDataException("Текущий месяц не может быть больше 12");
        }
        if (LocalDate.now().getYear()>= year){
            if (LocalDate.now().getMonthValue() > month){
                throw new InvalidDataException("Срок действия карты истёк");
            }
        }
    }

    // Проверка кода CVV
    public void checkCVV(String cardFromCVV) {
        if (cardFromCVV == null) {
            throw new InvalidDataException("CVC / CVC2 номер карты отправителя обязателен");
        } else if (cardFromCVV.length() > 0 && !cardFromCVV.matches("[0-9]{3}")) {
            throw new InvalidDataException("CVC / CVC2 код должен состоять из 3-х символов");
        }
    }

    //Проверка суммы перевода
    public void checkAmount(Integer amount) {
        if (amount == null){
            throw new InvalidDataException("Необходимо указать сумму перевода");
        } else if (amount <= 0) {
            throw new InvalidDataException("Сумма перевода должна быть больше 0");
        }
    }

    private void validationConfirmOperation(ConfirmOperation operation) {
        if (operation.getCode() == null || operation.getCode().length() == 0) {
           throw new InvalidDataException("Неверный код");
        } else if (operation.getOperationId() == null || operation.getOperationId().length() == 0){
            throw new InvalidDataException("Неверный идентификатор операции");
        }
    }
}
