package com.raiks.widgets.core.application.service;

import java.util.List;

public interface Bundle<T> {
    BundleSpecification getBundleSpecification();
    List<T> getElements();
}
