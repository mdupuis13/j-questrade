package info.martindupuis.client;

import info.martindupuis.Activity;

import java.util.List;

record AccountActivityResponse(
        List<Activity> activities,
        int userId) {
}
