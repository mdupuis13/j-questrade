package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Activity;

import java.util.Set;

record AccountActivityResponse(
        Set<Activity> activities,
        int userId) {
}
