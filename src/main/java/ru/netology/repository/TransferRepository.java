package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Transfer;
import ru.netology.model.TransactionState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TransferRepository {
    private final Map<String, Transfer> transferMap = new ConcurrentHashMap<>();
    private final AtomicInteger operationId = new AtomicInteger();

    private final Map<String, String> codes = new ConcurrentHashMap<>();

    public int getOperationId() {
        return operationId.incrementAndGet();
    }

    public void addTransfer(String id, Transfer transaction) {
        transferMap.put(id, transaction);
        transaction.setState(TransactionState.LOAD);
    }

    public Transfer confirmOperation(String id) {
        if (!transferMap.containsKey(id)) {
            return null;
        }
        Transfer transaction = transferMap.get(id);
        transaction.setState(TransactionState.OK);
        return transaction;
    }

    public Transfer errorConfirmOperation(String id) {
        if (!transferMap.containsKey(id)) {
            return null;
        }
        Transfer transaction = transferMap.get(id);
        transaction.setState(TransactionState.ERROR);
        return transaction;
    }

    public void addCode(String id, String code) {
        codes.put(id, code);
    }

    public String removeCode(String id) {
        return codes.remove(id);
    }

    public boolean checkCode(String id){
        return codes.containsKey(id);
    }
}
