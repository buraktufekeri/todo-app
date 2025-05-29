package com.todo.repository;

import com.todo.model.RoleDocument;
import com.todo.model.enums.RoleEnum;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CouchbaseRepository<RoleDocument, String> {

    RoleDocument findByRole(RoleEnum roleEnum);
}
