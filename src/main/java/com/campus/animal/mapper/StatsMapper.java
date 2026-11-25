package com.campus.animal.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StatsMapper {
    @Select("""
            SELECT DATE(feed_time) AS day, COUNT(*) AS count
            FROM feed_record WHERE feed_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
            GROUP BY DATE(feed_time) ORDER BY day
            """)
    List<Map<String, Object>> weeklyFeeding();

    @Select("SELECT food_status AS name, COUNT(*) AS value FROM feed_point GROUP BY food_status")
    List<Map<String, Object>> foodStatus();
}
