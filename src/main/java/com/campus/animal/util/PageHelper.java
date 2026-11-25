package com.campus.animal.util;

import com.campus.animal.common.PageResult;
import java.util.List;
import java.util.function.Function;

public record PageHelper(int page, int pageSize) {
    public static PageHelper of(int page, int pageSize) {
        return of(page, pageSize, 100);
    }

    public static PageHelper of(int page, int pageSize, int maxSize) {
        return new PageHelper(Math.max(page, 1), Math.min(Math.max(pageSize, 1), maxSize));
    }

    public long offset() {
        return (page - 1L) * pageSize;
    }

    public <T, R> PageResult<R> result(List<T> rows, long count, Function<T, R> mapper) {
        return new PageResult<>(rows.stream().map(mapper).toList(), count, page, pageSize);
    }
}
