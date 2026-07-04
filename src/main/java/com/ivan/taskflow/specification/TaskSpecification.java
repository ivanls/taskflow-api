package com.ivan.taskflow.specification;

import com.ivan.taskflow.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasCompleted(Boolean completed) {
        return (root, query, cb) ->
                completed == null ? null :
                        cb.equal(root.get("completed"), completed);
    }

    public static Specification<Task> hasTitle(String title) {
        return (root, query, cb) ->
                (title == null || title.isBlank()) ? null :
                        cb.like(cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%");
    }
}
