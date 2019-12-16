package com.raiks.widgets.infrastructure.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.raiks.widgets.core.application.service.IWidgetRepository;
import com.raiks.widgets.core.domain.Widget;
import com.raiks.widgets.infrastructure.jdbi.dto.WidgetDto;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

public final class SqlWidgetRepository implements IWidgetRepository {
    private Jdbi jdbi;

    public SqlWidgetRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void createWidget(Widget widget) {
        jdbi.useHandle(
            handle -> handle.createUpdate(
                "INSERT INTO widget(guid, x, y, width, height, zindex) VALUES (:guid, :x, :y, :width, :height, :zIndex)"
            ).bind("guid", widget.getGuid())
            .bind("x", widget.getBottomLeftCorner().getX())
            .bind("y", widget.getBottomLeftCorner().getY())
            .bind("width", widget.getWidth())
            .bind("height", widget.getHeight())
            .bind("zIndex", widget.getZIndex())
            .execute()
        );
    }

    @Override
    public Widget fetchWidget(String widgetGuid) {
        try {
            Widget widget = jdbi.withHandle(
                handle ->  {
                    return handle.createQuery(
                        "SELECT guid, x AS p_x, y AS p_y, width, height, zindex FROM widget WHERE guid = :widgetGuid"
                    ).bind("widgetGuid", widgetGuid)
                    .registerRowMapper(ConstructorMapper.factory(WidgetDto.class))
                    .mapTo(WidgetDto.class)
                    .one()
                    .toWidget();
                }
            );
            return widget;
        } catch (IllegalStateException e) {
            throw new NoSuchElementException("Widget with guid " + widgetGuid + " does not exist");
        }
    }

    @Override
    public void updateWidget(Widget updatedWidget) {
        jdbi.useHandle(
            handle -> {
                handle.createUpdate(
                    "UPDATE widget SET x = :x, y =:y, width = :width, height = :height, zindex = :zIndex WHERE guid = :widgetGuid"
                ).bind("x", updatedWidget.getBottomLeftCorner().getX())
                .bind("y", updatedWidget.getBottomLeftCorner().getY())
                .bind("width", updatedWidget.getWidth())
                .bind("height", updatedWidget.getHeight())
                .bind("widgetGuid", updatedWidget.getGuid())
                .bind("zIndex", updatedWidget.getZIndex())
                .execute();
           }
        );
    }

    @Override
    public void deleteWidget(String widgetGuid) {
        jdbi.useHandle(
            handle -> handle.createUpdate("DELETE FROM widget where guid = :widgetGuid").bind("widgetGuid", widgetGuid).execute()
        );
    }

    @Override
    public List<Widget> fetchAllWidgets() {
        return jdbi.withHandle(
            handle ->  {
                return handle.registerRowMapper(ConstructorMapper.factory(WidgetDto.class))
                    .createQuery("SELECT guid, x AS p_x, y AS p_y, width, height, zindex FROM widget")
                    .mapTo(WidgetDto.class)
                    .list()
                    .stream()
                    .map(WidgetDto::toWidget)
                    .collect(Collectors.toList());
            }
        );
    }
}
