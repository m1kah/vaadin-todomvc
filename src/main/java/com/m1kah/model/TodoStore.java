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

package com.m1kah.model;

import java.util.*;

public class TodoStore {
    private List<Todo> todos = new ArrayList<>();
    private Collection<TodoStoreChangeListener> listeners = new HashSet<>();

    public void addTodo(Todo todo) {
        todos.add(todo);
        notifyListeners();
    }

    public void removeTodo(Todo todo) {
        todos.remove(todo);
        notifyListeners();
    }

    public void setCompleted(Todo todo, boolean completed) {
        todo.setCompleted(completed);
        notifyListeners();
    }

    public List<Todo> getTodos() {
        return Collections.unmodifiableList(todos);
    }

    private void notifyListeners() {
        for (TodoStoreChangeListener listener : listeners) {
            listener.todosChanged(this);
        }
    }

    public void addTodoStoreChangeListener(TodoStoreChangeListener listener) {
        listeners.add(listener);
    }

    public void clearCompleted() {
        Iterator<Todo> i = todos.iterator();
        while (i.hasNext()) {
            Todo todo = i.next();
            if (todo.isCompleted()) {
                i.remove();
            }
        }
        notifyListeners();
    }
}
