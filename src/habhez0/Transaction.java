package habhez0;

/**
 * @author: Habib Hezarehee (habhez-0)
 * @email: habhez-0@student.ltu.se
 */

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private LocalDateTime date;
    private Double amount;
    private Double newBalance;
    private String transactionType;

    public Transaction(LocalDateTime date, Double amount, Double newBalance) {
        this.date = date;
        this.amount = amount;
        this.newBalance = newBalance;
        if (amount > 0) {
            this.transactionType = "Deposit";
        } else {
            this.transactionType = "Withdrawal";
        }
    }

    public Transaction(String date, Double amount, Double newBalance) {
        this.date = LocalDateTime.parse(date);
        this.amount = amount;
        this.newBalance = newBalance;
        if (amount > 0) {
            this.transactionType = "Deposit";
        } else {
            this.transactionType = "Withdrawal";
        }
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction transaction)) return false;

        if (!getDate().equals(transaction.getDate())) return false;
        if (!getAmount().equals(transaction.getAmount())) return false;
        if (!getNewBalance().equals(transaction.getNewBalance())) return false;
        return transactionType.equals(transaction.transactionType);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", newBalance=" + newBalance +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
