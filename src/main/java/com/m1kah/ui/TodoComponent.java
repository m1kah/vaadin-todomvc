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
import com.m1kah.model.TodoStoreChangeListener;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

public class TodoComponent extends VerticalLayout {
    private TextField inputField;
    private VerticalLayout todosLayout;
    private Label itemsLeftLabel;
    private OptionGroup filterOptionGroup;
    private Button clearCompletedButton;
    private CheckBox toggleAllButton;
    private TodoStore todoStore = new TodoStore();
    private TodoFilter filter = TodoFilter.All;
    private int itemsLeft = 0;

    public TodoComponent() {
        setStyleName("todo-component");
        initInputRow();
        initTodosLayout();
        initFooter();
        initListeners();
    }

    private void initInputRow() {
        toggleAllButton = new CheckBox();
        toggleAllButton.setId("toggle-all");

        inputField = new TextField();
        inputField.setInputPrompt("What needs to be done?");

        Panel panel = new Panel(inputField);
        panel.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object o, Object o1) {
                addNewTodo();
            }
        });

        HorizontalLayout inputFieldLayout = new HorizontalLayout(toggleAllButton, panel);
        inputFieldLayout.setComponentAlignment(toggleAllButton, Alignment.MIDDLE_LEFT);
        inputFieldLayout.setId("new-todo");
        addComponent(inputFieldLayout);
    }

    private void addNewTodo() {
        String userInput = inputField.getValue();
        if (userInput != null && userInput.trim() != "") {
            todoStore.addTodo(new Todo(userInput.trim()));
            inputField.setValue("");
        }
    }

    private void initTodosLayout() {
        todosLayout = new VerticalLayout();
        addComponent(todosLayout);
    }

    private void initFooter() {
        itemsLeftLabel = new Label("0 items left");
        filterOptionGroup = new OptionGroup();
        filterOptionGroup.setId("filter-radio");
        for (TodoFilter todoFilter : TodoFilter.values()) {
            filterOptionGroup.addItem(todoFilter);
        }
        filterOptionGroup.setValue(filter);
        clearCompletedButton = new Button("Clear completed");

        HorizontalLayout footerLayout = new HorizontalLayout(itemsLeftLabel, filterOptionGroup, clearCompletedButton);
        footerLayout.setWidth(100.0f, Unit.PERCENTAGE);
        footerLayout.setComponentAlignment(itemsLeftLabel, Alignment.MIDDLE_LEFT);
        footerLayout.setComponentAlignment(filterOptionGroup, Alignment.MIDDLE_CENTER);
        footerLayout.setComponentAlignment(clearCompletedButton, Alignment.MIDDLE_RIGHT);
        footerLayout.setExpandRatio(itemsLeftLabel, 0.25f);
        footerLayout.setExpandRatio(filterOptionGroup, 0.5f);
        footerLayout.setExpandRatio(clearCompletedButton, 0.25f);
        addComponent(footerLayout);
    }

    private void initListeners() {
        todoStore.addTodoStoreChangeListener(new TodoStoreChangeListener() {
            @Override
            public void todosChanged(TodoStore todoStore) {
                refreshTodoList();
            }
        });

        clearCompletedButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                todoStore.clearCompleted();
            }
        });

        filterOptionGroup.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Object value = valueChangeEvent.getProperty().getValue();
                filter = (TodoFilter) value;
                refreshTodoList();
            }
        });
        toggleAllButton.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (isAllCompleted()) {
                    deselectAll();
                } else {
                    selectAll();
                }
            }
        });
    }

    private void selectAll() {
        updateAllCompleteFields(true);
    }

    private void deselectAll() {
        updateAllCompleteFields(false);
    }

    private void updateAllCompleteFields(boolean completed) {
        for (Component component : todosLayout) {
            if (component instanceof TodoRow) {
                TodoRow todoRow = (TodoRow) component;
                todoRow.getTodo().setCompleted(completed);
            }
        }
        refreshTodoList();
    }

    private boolean isAllCompleted() {
        for (Component component : todosLayout) {
            if (component instanceof TodoRow) {
                TodoRow todoRow = (TodoRow) component;
                if (!todoRow.getTodo().isCompleted()) {
                    return false;
                }
            }
        }

        return true;
    }

    private void refreshTodoList() {
        todosLayout.removeAllComponents();
        itemsLeft = 0;
        for (Todo todo : todoStore.getTodos()) {
            if (filter.filter(todo)) {
                todosLayout.addComponent(new TodoRow(todoStore, todo));
                if (!todo.isCompleted()) {
                    itemsLeft++;
                }
            }
        }
        updateItemsLeftLabel();
    }

    private void updateItemsLeftLabel() {
        itemsLeftLabel.setValue(String.format("%d items left", itemsLeft));
    }
}
