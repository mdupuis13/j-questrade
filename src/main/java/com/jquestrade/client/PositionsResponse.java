package com.jquestrade.client;

import com.jquestrade.Position;

import java.util.List;

record PositionsResponse(List<Position> positions,
                         int userId) {
}