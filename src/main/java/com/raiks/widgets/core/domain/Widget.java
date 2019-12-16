package com.raiks.widgets.core.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable class representing a square Widget on the Cartesian plain
 * Is an entity (identity is important). As such, is uniquely identified by guid
 */
public final class Widget {
    private final Point bottomLeftCorner;
    private final int width;
    private final int height;
    private final Integer zIndex;
    private final String guid;

    @JsonCreator
    public Widget(
        @JsonProperty("bottomLeftCorner") Point bottomLeftCorner,
        @JsonProperty("width") int width,
        @JsonProperty("height") int height,
        @JsonProperty("zIndex") Integer zIndex,
        @JsonProperty("guid") String guid
    ) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.guid = guid;
    }

    public Widget(
        Point bottomLeftCorner,
        int width,
        int height,
        Integer zIndex
    ) {
        this(bottomLeftCorner, width, height, zIndex, null);
    }

    public Widget(Point bottomLeftCorner, int width, int height) {
        this(bottomLeftCorner, width, height, null, null);
    }

    public Point getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public Widget setBottomLeftCorner(Point newBottomLeftCorner) {
        return new Widget(newBottomLeftCorner, width, height, zIndex, guid);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Widget setWidth(int newWidth) {
        return new Widget(bottomLeftCorner, newWidth, height, zIndex, guid);
    }

    public Widget setHeight(int newHeight) {
        return new Widget(bottomLeftCorner, width, newHeight, zIndex, guid);
    }

    @JsonProperty("zIndex")
    public Integer getZIndex() {
        return zIndex;
    }

    public Widget setZIndex(Integer newZIndex) {
        return new Widget(bottomLeftCorner, width, height, newZIndex, guid);
    }

    public String getGuid() {
        return guid;
    }

    public Widget setGuid(String newGuid) {
        return new Widget(bottomLeftCorner, width, height, zIndex, newGuid);
    }

    @Override
    public String toString() {
        return String.format(
            "{ bottomLeftCorner: { x: %d, y: %d }, width: %d, height: %d, zIndex: %d }",
            getBottomLeftCorner().getX(),
            getBottomLeftCorner().getY(),
            getWidth(),
            getHeight(),
            getZIndex()
        );
    }
}
