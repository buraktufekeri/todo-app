package com.todo.service;

import com.todo.model.ActionDocument;

import java.util.List;

public interface ActionService {

    void saveActions();

    List<ActionDocument> getActions();
}
