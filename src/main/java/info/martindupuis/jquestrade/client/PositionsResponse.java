package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Position;

import java.util.Set;

record PositionsResponse(
        Set<Position> positions,
        int userId) {
}