package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepositoryCustom {
    Optional<Todo> findByIdWithUserFromQueryDsl(Long todoId);

    Page<TodoSearchResponse> search(Pageable pageable,
                                    String keyword,
                                    String managerName,
                                    LocalDateTime StartDate,
                                    LocalDateTime endDate);
}
