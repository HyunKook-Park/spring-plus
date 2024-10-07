package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    public final JPAQueryFactory q;

    @Override
    public Optional<Todo> findByIdWithUserFromQueryDsl(Long todoId) {
//        qTodo todo = QTodo.todo;
//        QUser user = QUser.user;
        Todo result = q
                .select(todo)
                .from()
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
//    private BooleanExpression todoIdEq(Long todoId) {
//        return todoId != null ? todo.id.eq(todoId) : null;
//}
}
