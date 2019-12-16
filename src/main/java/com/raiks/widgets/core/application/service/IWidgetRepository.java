package com.raiks.widgets.core.application.service;

import java.util.List;

import com.raiks.widgets.core.domain.Widget;

/**
 * This is a technology-agnostic repository manipulating Widgets (an aggregate root for Point)
 * It is stored alongside WidgetService to prevent high-level modules from depending on 
 * low-level ones (dependency inversion principle)
 */
public interface IWidgetRepository {
    public void createWidget(Widget widget);
    public Widget fetchWidget(String widgetGuid);
    public void updateWidget(Widget updatedWidget);
    public void deleteWidget(String widgetGuid);
    public List<Widget> fetchAllWidgets();
}
