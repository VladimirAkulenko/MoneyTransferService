package ru.netology.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
public class Transfer {
    private final String cardFromCVV;
    private final String cardFromNumber;
    private final String cardFromValidTill;
    private final String cardToNumber;
    private final Amount amount;

    private final Date date;

    private final double commission;
    @Setter
    private TransactionState state;

    public Transfer(String cardFromNumber, String cardFromValidTill, String cardFromCVV, String cardToNumber, Amount amount) {
        this.cardFromCVV = cardFromCVV;
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
        date = new Date();
        commission = amount.getValue()/100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer that = (Transfer) o;
        return Double.compare(that.commission, commission) == 0
                && Objects.equals(cardFromCVV, that.cardFromCVV)
                && Objects.equals(cardFromNumber, that.cardFromNumber)
                && Objects.equals(cardFromValidTill, that.cardFromValidTill)
                && Objects.equals(cardToNumber, that.cardToNumber)
                && Objects.equals(amount, that.amount)
                && Objects.equals(date, that.date)
                && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardFromCVV, cardFromNumber, cardFromValidTill, cardToNumber, amount, date, commission, state);
    }

    public String toString(){
        return "Новый перевод: " + "Номер карты отправителя = " + cardFromNumber
                + " , Номер карты получателя = " + cardToNumber + " , Сумма = "
                + amount + " , " + "коммиссия: " + commission + " , " + "статус: " + state;
    }
}
