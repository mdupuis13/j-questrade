package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.QuestradeActivity;

import java.util.Set;

record AccountActivityResponse(
        Set<QuestradeActivity> activities,
        int userId) {
}
