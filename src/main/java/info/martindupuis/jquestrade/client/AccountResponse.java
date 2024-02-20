package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Account;

import java.util.List;

record AccountResponse(
        List<Account> accounts,
        int userId) {
}
