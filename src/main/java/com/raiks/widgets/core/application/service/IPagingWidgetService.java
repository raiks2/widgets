package com.raiks.widgets.core.application.service;

import java.util.List;

import com.raiks.widgets.core.domain.Widget;

public interface IPagingWidgetService extends IWidgetService {
    Bundle<Widget> fetchManyWidgets(BundleSpecification bundleSpecification);
}
