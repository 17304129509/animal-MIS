package com.campus.animal.mapper;

import com.campus.animal.entity.FeedRecord;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedRecordMapper {
    Optional<FeedRecord> findById(int id);
    int insert(FeedRecord row);

    List<FeedRecord> findPage(@Param("userId") Integer userId, @Param("pointId") Integer pointId, @Param("offset") long offset, @Param("limit") int limit);
    long countFiltered(@Param("userId") Integer userId, @Param("pointId") Integer pointId);
    long count();
    List<Map<String, Object>> countByUser();
}
