package com.raiks.widgets.core.application.service;

import com.raiks.widgets.core.domain.Widget;

/**
 * This is a repository adding paging functionality to IWidgetRepository
 */
public interface IPagingWidgetRepository extends IWidgetRepository {
    Bundle<Widget> fetchManyWidgets(BundleSpecification bundleSpecification);
}
