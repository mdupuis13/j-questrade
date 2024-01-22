package info.martindupuis.client;

import info.martindupuis.Account;

import java.util.List;

record AccountResponse(
        List<Account> accounts,
        int userId) {
}
