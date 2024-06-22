package ru.netology.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfirmOperation {
    private String operationId;
    private String code;
}
