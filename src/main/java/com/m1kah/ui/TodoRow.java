/*
Copyright (c) 2014 Mika Hämäläinen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.m1kah.ui;

import com.m1kah.model.Todo;
import com.m1kah.model.TodoStore;
import com.vaadin.data.Property;
import com.vaadin.ui.*;

public class TodoRow extends HorizontalLayout {
    private final TodoStore todoStore;
    private final Todo todo;
    private CheckBox checkBox;
    private TextField todoField;
    private Button removeButton;

    public TodoRow(TodoStore todoStore, final Todo todo) {
        this.todoStore = todoStore;
        this.todo = todo;
        initTodoField();
        initCheckBox();
        initRemoveButton();
        initLayout();
        initListeners();
    }

    private void initTodoField() {
        todoField = new TextField();
        todoField.setValue(todo.getText());
        todoField.setReadOnly(true);
        if (todo.isCompleted()) {
            todoField.addStyleName("completed");
        }
    }

    private void initCheckBox() {
        checkBox = new CheckBox();
        checkBox.addStyleName("toggle");
        checkBox.setImmediate(true);
        checkBox.setValue(todo.isCompleted());

    }

    private void initRemoveButton() {
        removeButton = new Button("X");
        removeButton.setDisableOnClick(true);
    }

    private void initLayout() {
        addComponent(checkBox);
        addComponent(todoField);
        addComponent(removeButton);

        setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
        setComponentAlignment(todoField, Alignment.MIDDLE_LEFT);
        setComponentAlignment(removeButton, Alignment.MIDDLE_RIGHT);

        setExpandRatio(todoField, 1);

        addStyleName("todo-row");
        setWidth(100.0f, Unit.PERCENTAGE);
    }

    private void initListeners() {
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (checkBox.getValue()) {
                    todoField.addStyleName("completed");
                } else {
                    todoField.removeStyleName("completed");
                }
                todoStore.setCompleted(todo, !todo.isCompleted());
            }
        });

        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                todoStore.removeTodo(todo);
            }
        });
    }

    public Todo getTodo() {
        return todo;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
