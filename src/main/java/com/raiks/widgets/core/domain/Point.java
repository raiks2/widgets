package com.raiks.widgets.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable class representing a Point on the Cartesian plain
 * It's a value object - all instances with equal x and y are the same
 */
public final class Point {
    private final int x;
    private final int y;

    @JsonCreator
    public Point(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point setX(int newX) {
        return new Point(newX, y);
    }

    public Point setY(int newY) {
        return new Point(x, newY);
    }
}
