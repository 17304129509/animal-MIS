package com.campus.animal.mapper;

import com.campus.animal.entity.Animal;
import com.campus.animal.entity.FeedPoint;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    Map<String, Object> stats();
    List<Map<String, Object>> animals(Boolean approved);
    int updateAnimal(Animal animal);
    List<Map<String, Object>> rescues(String status);
    List<Map<String, Object>> adoptions(String status);
    List<Map<String, Object>> feedPoints();
    int updateFeedPoint(FeedPoint point);
    List<Map<String, Object>> users();
    int deleteUser(int id);
    List<Map<String, Object>> feedRecords();
    List<Map<String, Object>> volunteers();
}
