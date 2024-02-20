package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Activity;

import java.util.List;

record AccountActivityResponse(
        List<Activity> activities,
        int userId) {
}
