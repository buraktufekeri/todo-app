package com.todo.repository;

import com.todo.model.ActionDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends CouchbaseRepository<ActionDocument, String> {
}
