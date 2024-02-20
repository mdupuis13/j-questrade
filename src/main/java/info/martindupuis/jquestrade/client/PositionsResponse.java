package info.martindupuis.jquestrade.client;

import info.martindupuis.jquestrade.Position;

import java.util.List;

record PositionsResponse(List<Position> positions,
                         int userId) {
}