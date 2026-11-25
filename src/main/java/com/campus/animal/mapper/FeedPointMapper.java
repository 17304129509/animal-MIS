package com.campus.animal.mapper;

import com.campus.animal.entity.FeedPoint;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FeedPointMapper {
    Optional<FeedPoint> findById(int id);
    Optional<FeedPoint> lockById(int id);
    int insert(FeedPoint row);
    int update(FeedPoint row);
    int deleteById(int id);
    boolean existsById(int id);

    List<FeedPoint> findPage(@Param("foodStatus") String foodStatus, @Param("offset") long offset, @Param("limit") int limit);
    long countFiltered(@Param("foodStatus") String foodStatus);
    long count();
    long countByFoodStatus(String value);
}
