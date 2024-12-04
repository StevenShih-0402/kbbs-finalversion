package com.management.kbbs.exception;
// 借書數量超過上限的錯誤回報

public class BorrowLimitException extends RuntimeException {
    public BorrowLimitException(String message) {
        super(message);
    }
}