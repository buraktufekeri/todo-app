package com.todo.repository;

import com.todo.model.TodoListDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoListRepository extends CouchbaseRepository<TodoListDocument, String> {

    TodoListDocument findByTitle(String title);

    List<TodoListDocument> findAllByUserId(String userId);
}
