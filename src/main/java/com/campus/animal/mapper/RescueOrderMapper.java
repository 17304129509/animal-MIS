package com.campus.animal.mapper;

import com.campus.animal.entity.RescueOrder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RescueOrderMapper {
    Optional<RescueOrder> findById(int id);
    Optional<RescueOrder> lockById(int id);
    int insert(RescueOrder row);
    int update(RescueOrder row);

    List<RescueOrder> findPage(@Param("orderStatus") String orderStatus, @Param("publishUser") Integer publishUser, @Param("offset") long offset, @Param("limit") int limit);
    long countFiltered(@Param("orderStatus") String orderStatus, @Param("publishUser") Integer publishUser);
    long count();
    long countByOrderStatus(String value);
    List<Map<String, Object>> countCompletedByDealUser(int limit);
}
