package com.todo.service.impl;

import com.todo.model.ActionDocument;
import com.todo.model.enums.ActionEnum;
import com.todo.repository.ActionRepository;
import com.todo.service.ActionService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;

    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public void saveActions() {
        if (actionRepository.count() == 0) {
            List<ActionDocument> actionDocuments = Arrays.stream(ActionEnum.values())
                    .map(ActionDocument::new)
                    .toList();

            actionRepository.saveAll(actionDocuments);
        }
    }

    @Override
    public List<ActionDocument> getActions() {
        return actionRepository.findAll();
    }
}
