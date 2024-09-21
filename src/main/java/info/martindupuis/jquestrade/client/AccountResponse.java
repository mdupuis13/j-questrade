package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.QuestradeAccount;

import java.util.Set;

record AccountResponse(
        Set<QuestradeAccount> accounts,
        int userId) {
}
