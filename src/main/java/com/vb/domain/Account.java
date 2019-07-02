package com.vb.domain;

import org.multiverse.api.StmUtils;
import org.multiverse.api.callables.TxnCallable;
import org.multiverse.api.references.TxnInteger;

public class Account {

    private final TxnInteger balance;

    Account(final int balance) {

        this.balance = StmUtils.newTxnInteger(balance);
    }

    public Integer getBalance() {
        return balance.atomicGet();
    }

    public void adjustBy(final int amount) {
        StmUtils.atomic(() -> {
            balance.increment(amount);
            if (balance.get() < 0) {
                throw new IllegalArgumentException("Not enough money");
            }
        });
    }

    public void transferTo(final Account other, final int amount) {
        StmUtils.atomic(() -> {
            adjustBy(-amount);
            other.adjustBy(amount);
        });
    }

    @Override
    public String toString() {
        return StmUtils.atomic((TxnCallable<String>) txn -> "Balance: " + balance.get(txn));
    }
}
