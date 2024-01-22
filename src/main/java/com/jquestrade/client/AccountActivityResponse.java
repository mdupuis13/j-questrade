package com.jquestrade.client;

import com.jquestrade.Activity;

import java.util.List;

record AccountActivityResponse(
        List<Activity> activities,
        int userId) {
}
