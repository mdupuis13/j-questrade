package com.jquestrade.client;

import com.jquestrade.Account;

import java.util.List;

record AccountResponse(
        List<Account> accounts,
        int userId) {
}
