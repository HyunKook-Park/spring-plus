package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    public final JPAQueryFactory q;

    @Override
    public Optional<Todo> findByIdWithUserFromQueryDsl(Long todoId) {
        Todo result = q
                .select(todo)
                .from(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);

    }

    @Override
    public Page<TodoSearchResponse> search(Pageable pageable,
                                           String keyword,
                                           String managerName,
                                           LocalDateTime startDate,
                                           LocalDateTime endDate) {

        QManager manager = QManager.manager;

        long totalCount = Objects.requireNonNull(q
                .select(todo.count())
                .from(todo)
                .fetchOne());

        if (totalCount == 0) {
            new PageImpl<>(Collections.emptyList(), pageable, totalCount);
        }

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(todo.title.containsIgnoreCase(keyword));
        }

        if (managerName != null && !managerName.isEmpty()) {
            builder.and(manager.user.nickName.containsIgnoreCase(managerName));
        }

        if (startDate != null && endDate != null) {
            builder.and(todo.createdAt.between(startDate, endDate));
        } else if (startDate != null) {
            builder.and(todo.createdAt.gt(startDate));
        } else if (endDate != null) {
            builder.and(todo.createdAt.lt(endDate));
        }


        List<TodoSearchResponse> result = q
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.id,
                        todo.title,
                        manager.user.count(),
                        comment.count()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        manager.todo.eq(todo),
                        comment.todo.eq(todo),
                        builder
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, totalCount);
    }
}
