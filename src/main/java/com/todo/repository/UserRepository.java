package com.todo.repository;

import com.todo.model.UserDocument;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CouchbaseRepository<UserDocument, String> {

    UserDocument findByUsername(String username);
}
