package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Account;

import java.util.Set;

record AccountResponse(
        Set<Account> accounts,
        int userId) {
}
