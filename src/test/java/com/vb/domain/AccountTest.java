package com.vb.domain;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountTest {

    @Test
    public void testAdjustByWorksProperly() {
        Account a = new Account(10);

        a.adjustBy(-5);

        assertEquals(5, a.getBalance(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdjustByWorksFailDueMinusBalance() {
        Account a = new Account(10);

        a.adjustBy(-11);
    }

    @Test
    public void testFailsWhenTwoThreadsAdjust() throws InterruptedException {
        ExecutorService ex = Executors.newFixedThreadPool(2);
        Account a = new Account(10);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicBoolean exceptionThrown = new AtomicBoolean(false);

        ex.submit(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                a.adjustBy(-6);
            } catch (IllegalArgumentException e) {
                exceptionThrown.set(true);
            }
        });
        ex.submit(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                a.adjustBy(-5);
            } catch (IllegalArgumentException e) {
                exceptionThrown.set(true);
            }
        });

        countDownLatch.countDown();
        ex.awaitTermination(1, TimeUnit.SECONDS);
        ex.shutdown();

        assertTrue(exceptionThrown.get());
    }

    @Test
    public void testRollBackTransferOnFail() {
        final Account a = new Account(10);
        final Account b = new Account(10);

        a.transferTo(b, 5);

        assertEquals(5, a.getBalance(), 0);
        assertEquals(15, b.getBalance(), 0);

        try {
            a.transferTo(b, 20);
        } catch (final IllegalArgumentException e) {
            System.out.println("failed to transfer money");
        }

        assertEquals(5, a.getBalance(), 0);
        assertEquals(15, b.getBalance(), 0);
    }

    @Test
    public void testTransferIsWithoutDeadLock() throws InterruptedException {
        ExecutorService ex = Executors.newFixedThreadPool(200);
        final Account a = new Account(10000);
        final Account b = new Account(10000);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 100; i++) {
            ex.submit(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a.transferTo(b, 10);
            });
            ex.submit(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                b.transferTo(a, 1);

            });
        }

        countDownLatch.countDown();
        ex.awaitTermination(1, TimeUnit.SECONDS);
        ex.shutdown();

        assertEquals(9100, a.getBalance(), 0);
        assertEquals(10900, b.getBalance(), 0);
    }

}