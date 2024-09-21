package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.QuestradePosition;

import java.util.Set;

record PositionsResponse(
        Set<QuestradePosition> positions,
        int userId) {
}