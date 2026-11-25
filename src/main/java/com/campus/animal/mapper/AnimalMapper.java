package com.campus.animal.mapper;

import com.campus.animal.entity.Animal;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnimalMapper {
    Optional<Animal> findById(int id);
    Optional<Animal> lockById(int id);
    int insert(Animal row);
    int update(Animal row);
    int deleteById(int id);
    boolean existsById(int id);

    List<Animal> findPage(@Param("type") String type, @Param("status") String status, @Param("location") String location, @Param("approvedOnly") boolean approvedOnly, @Param("offset") long offset, @Param("limit") int limit);
    long countFiltered(@Param("type") String type, @Param("status") String status, @Param("location") String location, @Param("approvedOnly") boolean approvedOnly);
    long count();
    long countByStatus(String value);
}
