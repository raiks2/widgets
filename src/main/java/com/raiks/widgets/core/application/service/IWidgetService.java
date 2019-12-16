package com.raiks.widgets.core.application.service;

import java.util.List;

import com.raiks.widgets.core.domain.Widget;

/**
 * This is an application service encapsulating a set of application use cases
 * In theory, it can be invoked by any driver (command line, mobile GUI etc.)
 */
public interface IWidgetService {
    public static final int INITIAL_WIDGET_ZINDEX = 0;

    public Widget createWidget(Widget widget);
    public Widget fetchWidget(String widgetGuid);
    public List<Widget> fetchAllWidgets();
    public void updateWidget(Widget updatedWidget);
    public void deleteWidget(String widgetGuid);
}
