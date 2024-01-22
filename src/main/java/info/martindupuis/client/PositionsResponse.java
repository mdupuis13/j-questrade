package info.martindupuis.client;

import info.martindupuis.Position;

import java.util.List;

record PositionsResponse(List<Position> positions,
                         int userId) {
}