package com.m1kah.ui;

import com.m1kah.model.Todo;

/**
 * Copyright (c) 2013 Mika Hämäläinen
 */
public enum TodoFilter {
    All, Active, Completed;

    public boolean filter(Todo todo) {
        switch (this) {
            case Active:
                return !todo.isCompleted();
            case Completed:
                return todo.isCompleted();
            case All:
            default:
                return true;
        }
    }
}
