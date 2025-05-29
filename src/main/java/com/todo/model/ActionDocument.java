package com.todo.model;

import com.todo.model.enums.ActionEnum;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Document
public class ActionDocument extends BaseDocument {

    @Field("action")
    private ActionEnum action;

    public ActionDocument() {
        super("action");
    }

    public ActionDocument(ActionEnum action) {
        this();
        this.action = action;
    }

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }
}
