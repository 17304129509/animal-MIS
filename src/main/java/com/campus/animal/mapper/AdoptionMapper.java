package com.campus.animal.mapper;

import com.campus.animal.entity.Adoption;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdoptionMapper {
    Optional<Adoption> findById(int id);
    Optional<Adoption> lockById(int id);
    int insert(Adoption row);
    int update(Adoption row);

    List<Adoption> findPage(@Param("status") String status, @Param("applicantId") Integer applicantId, @Param("offset") long offset, @Param("limit") int limit);
    long countFiltered(@Param("status") String status, @Param("applicantId") Integer applicantId);
    long count();
}
