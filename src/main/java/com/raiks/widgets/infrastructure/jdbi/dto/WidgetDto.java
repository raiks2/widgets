package com.raiks.widgets.infrastructure.jdbi.dto;

import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;

import org.jdbi.v3.core.mapper.Nested;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

/**
 * In case of a complex application, DTO would be used quite extensively
 * In this project they are used to prevent polluting the domain objects
 * with low-level annotations specific to JDBI that would create an
 * unwanted dependency on low-level stuff
 */
public final class WidgetDto {
    private Point bottomLeftCorner;
    private int width;
    private int height;
    private int zIndex;
    private String guid;

    public WidgetDto(
        @Nested("p") Point bottomLeftCorner,
        int width,
        int height,
        @ColumnName("zindex") int zIndex,
        String guid
    ) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        this.guid = guid;
    }

    public static WidgetDto of(Widget widget) {
        return new WidgetDto(
            widget.getBottomLeftCorner(),
            widget.getWidth(),
            widget.getHeight(),
            widget.getZIndex(),
            widget.getGuid()
        );
    }

    public Widget toWidget() {
        return new Widget(
            this.bottomLeftCorner,
            this.width,
            this.height,
            this.zIndex,
            this.guid
        );
    }
}
