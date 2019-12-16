package com.raiks.widgets.infrastructure.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.raiks.widgets.core.application.service.IWidgetRepository;
import com.raiks.widgets.core.domain.Point;
import com.raiks.widgets.core.domain.Widget;

public final class InMemoryWidgetRepository implements IWidgetRepository {
    private Map<String, Widget> widgets = new HashMap<>();

    @Override
    public void createWidget(Widget widget) {
        if (widget.getGuid() == null) {
            throw new IllegalArgumentException("Widget must have a guid in order to be added to the repository");
        }
        widgets.put(widget.getGuid(), widget);
    }

    @Override
    public Widget fetchWidget(String widgetGuid) {
        throwIfNoSuchWidget(widgetGuid);
        return widgets.get(widgetGuid);
    }

    @Override
    public void updateWidget(Widget updatedWidget) {
        throwIfNoSuchWidget(updatedWidget.getGuid());
        Widget widget = widgets.get(updatedWidget.getGuid());
        widget = widget.setBottomLeftCorner(updatedWidget.getBottomLeftCorner())
            .setWidth(updatedWidget.getWidth())
            .setHeight(updatedWidget.getHeight())
            .setZIndex(updatedWidget.getZIndex());
        widgets.put(updatedWidget.getGuid(), widget);
    }

    @Override
    public void deleteWidget(String widgetGuid) {
        throwIfNoSuchWidget(widgetGuid);
        widgets.remove(widgetGuid);
    }

    @Override
    public List<Widget> fetchAllWidgets() {
        return sortWidgetsByZIndex(widgets.values());
    }

    private void throwIfNoSuchWidget(String widgetGuid) {
        if (!widgets.containsKey(widgetGuid)) {
            throw new NoSuchElementException("Widget with id " + widgetGuid + " does not exist");
        }
    }

    private List<Widget> sortWidgetsByZIndex(Collection<Widget> widgets) {
        return widgets.stream()
            .sorted(Comparator.comparingInt(Widget::getZIndex))
            .collect(Collectors.toList());
    }
}
