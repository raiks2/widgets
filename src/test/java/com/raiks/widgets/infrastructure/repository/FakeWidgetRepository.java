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

public final class FakeWidgetRepository implements IWidgetRepository {
    private Map<String, Widget> widgets = new HashMap<>();

    public void createWidget(Widget widget) {
        widgets.put(widget.getGuid(), widget);
    }

    public Widget fetchWidget(String widgetGuid) {
        if (!widgets.containsKey(widgetGuid)) {
            throw new NoSuchElementException();
        }
        return widgets.get(widgetGuid);
    }

    public void updateWidget(Widget updatedWidget) {
        if (!widgets.containsKey(updatedWidget.getGuid())) {
            throw new NoSuchElementException();
        }
        Widget widget = widgets.get(updatedWidget.getGuid());
        widget = widget.setBottomLeftCorner(updatedWidget.getBottomLeftCorner())
            .setWidth(updatedWidget.getWidth())
            .setHeight(updatedWidget.getHeight())
            .setZIndex(updatedWidget.getZIndex());
        widgets.put(updatedWidget.getGuid(), widget);
    }

    public void deleteWidget(String widgetGuid) {
        if (!widgets.containsKey(widgetGuid)) {
            throw new NoSuchElementException();
        }
        widgets.remove(widgetGuid);
    }

    public List<Widget> fetchAllWidgets() {
        return sortWidgetsByZIndex(widgets.values());
    }

    private List<Widget> sortWidgetsByZIndex(Collection<Widget> widgets) {
        return widgets.stream()
            .sorted(Comparator.comparingInt(Widget::getZIndex))
            .collect(Collectors.toList());
    }
}
