package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TodoRepositoryCustom {
    Optional<Todo> findByIdWithUserFromQueryDsl(Long todoId);

}
