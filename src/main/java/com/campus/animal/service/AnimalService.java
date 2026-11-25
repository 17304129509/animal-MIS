package com.campus.animal.service;

import com.campus.animal.common.BusinessException;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.AuthContext;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.AnimalDtos;
import com.campus.animal.entity.Animal;
import com.campus.animal.entity.User;
import com.campus.animal.mapper.AnimalMapper;
import com.campus.animal.util.Enums;
import com.campus.animal.util.PageHelper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnimalService {
    private final AnimalMapper animalMapper;
    private final UserService userService;

    public Map<String, Object> create(AnimalDtos.AnimalRequest req) {
        RoleCheck.requireLogin();
        Enums.checkAnimalType(req.getType());
        Animal animal = new Animal();
        this.copyRequest(animal, req);
        animal.setStatus("健康");
        animal.setIsApproved(RoleCheck.hasRole("admin"));
        animal.setCreateUser(AuthContext.getUserId());
        animalMapper.insert(animal);
        return this.toMap(animal);
    }

    public PageResult<Map<String, Object>> list(String type, String status, String location, Boolean approvedOnly, int page, int pageSize) {
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(animalMapper.findPage(type, status, location, !RoleCheck.hasRole("admin") || !Boolean.FALSE.equals(approvedOnly), paging.offset(), paging.pageSize()),
                animalMapper.countFiltered(type, status, location, !RoleCheck.hasRole("admin") || !Boolean.FALSE.equals(approvedOnly)), this::toMap);
    }

    public Map<String, Object> getById(Integer id) {
        Animal animal = this.animalMapper.findById(id).orElseThrow(() -> BusinessException.notFound("档案不存在"));
        if (!Boolean.TRUE.equals(animal.getIsApproved()) && !RoleCheck.hasRole("admin")
                && !animal.getCreateUser().equals(AuthContext.getUserId())) {
            throw BusinessException.notFound("档案不存在");
        }
        return this.toMap(animal);
    }

    @Transactional
    public Map<String, Object> update(Integer id, AnimalDtos.AnimalRequest req) {
        RoleCheck.requireRole("volunteer", "admin");
        Animal animal = this.animalMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("档案不存在"));
        this.copyRequest(animal, req);
        this.animalMapper.update(animal);
        return this.toMap(animal);
    }

    @Transactional
    public Map<String, Object> updateStatus(Integer id, AnimalDtos.StatusRequest req) {
        RoleCheck.requireRole("volunteer", "admin");
        Enums.checkAnimalStatus(req.getStatus());
        Animal animal = this.animalMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("档案不存在"));
        animal.setStatus(req.getStatus());
        this.animalMapper.update(animal);
        return this.toMap(animal);
    }

    public void delete(Integer id) {
        RoleCheck.requireRole("admin");
        if (!this.animalMapper.existsById(id)) {
            throw BusinessException.notFound("档案不存在");
        }
        this.animalMapper.deleteById(id);
    }

    @Transactional
    public Map<String, Object> approve(Integer id, AnimalDtos.ApproveRequest req) {
        RoleCheck.requireRole("admin");
        Animal animal = this.animalMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("档案不存在"));
        animal.setIsApproved(req.getApproved() != null ? req.getApproved() : true);
        this.animalMapper.update(animal);
        return this.toMap(animal);
    }

    private void copyRequest(Animal animal, AnimalDtos.AnimalRequest req) {
        Enums.checkAnimalType(req.getType());
        animal.setAnimalName(req.getAnimalName());
        animal.setType(req.getType());
        animal.setColor(req.getColor());
        animal.setLocation(req.getLocation());
        if (req.getStatus() != null) {
            Enums.checkAnimalStatus(req.getStatus());
            animal.setStatus(req.getStatus());
        }
        animal.setPhotoUrl(req.getPhotoUrl());
        animal.setRemark(req.getRemark());
    }

    private Map<String, Object> toMap(Animal a) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("animalId", a.getAnimalId());
        m.put("animalName", a.getAnimalName());
        m.put("type", a.getType());
        m.put("color", a.getColor());
        m.put("location", a.getLocation());
        m.put("status", a.getStatus());
        m.put("photoUrl", a.getPhotoUrl());
        m.put("remark", a.getRemark());
        m.put("isApproved", a.getIsApproved());
        m.put("createUser", a.getCreateUser());
        m.put("createTime", a.getCreateTime());
        User creator = this.userService.findById(a.getCreateUser());
        if (creator != null) {
            m.put("creatorNickname", creator.getNickname());
        }
        return m;
    }

    public AnimalService(AnimalMapper animalMapper, UserService userService) {
        this.animalMapper = animalMapper;
        this.userService = userService;
    }
}
