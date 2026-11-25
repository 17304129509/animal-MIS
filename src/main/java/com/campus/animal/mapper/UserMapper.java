package com.campus.animal.mapper;

import com.campus.animal.entity.User;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    Optional<User> findById(int id);
    Optional<User> lockById(int id);
    int insert(User row);

    List<User> findPage(@Param("role") String role, @Param("offset") long offset, @Param("limit") int limit);
    long countFiltered(@Param("role") String role);
    long count();
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    int updateProfile(User user);
    int updatePassword(@Param("id") int id, @Param("password") String password);
    int upgradePassword(@Param("id") int id, @Param("old") String old, @Param("password") String password);
    int updateRole(@Param("id") int id, @Param("role") String role);
    int updatePoints(@Param("id") int id, @Param("points") int points);
}
