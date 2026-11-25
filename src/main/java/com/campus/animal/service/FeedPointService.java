package com.campus.animal.service;

import com.campus.animal.common.BusinessException;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.AuthContext;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.FeedDtos;
import com.campus.animal.entity.FeedPoint;
import com.campus.animal.entity.FeedRecord;
import com.campus.animal.entity.User;
import com.campus.animal.mapper.FeedPointMapper;
import com.campus.animal.mapper.FeedRecordMapper;
import com.campus.animal.util.Enums;
import com.campus.animal.util.PageHelper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedPointService {
    private final FeedPointMapper feedPointMapper;
    private final FeedRecordMapper feedRecordMapper;
    private final UserService userService;

    public Map<String, Object> create(FeedDtos.FeedPointRequest req) {
        RoleCheck.requireRole("admin");
        FeedPoint point = new FeedPoint();
        point.setPointName(req.getPointName());
        point.setLocation(req.getLocation());
        if (req.getFoodStatus() != null) {
            Enums.checkFoodStatus(req.getFoodStatus());
            point.setFoodStatus(req.getFoodStatus());
        }
        feedPointMapper.insert(point);
        return this.toMap(point);
    }

    public PageResult<Map<String, Object>> list(String foodStatus, int page, int pageSize) {
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(feedPointMapper.findPage(foodStatus, paging.offset(), paging.pageSize()),
                feedPointMapper.countFiltered(foodStatus), this::toMap);
    }

    public Map<String, Object> getById(Integer id, int recordPage, int recordPageSize) {
        FeedPoint point = this.feedPointMapper.findById(id).orElseThrow(() -> BusinessException.notFound("点位不存在"));
        Map<String, Object> m = this.toMap(point);
        PageHelper paging = PageHelper.of(recordPage, recordPageSize, 20);
        var records = feedRecordMapper.findPage(null, id, paging.offset(), paging.pageSize());
        m.put("recentRecords", records.stream().map(this::recordToMap).toList());
        return m;
    }

    @Transactional
    public Map<String, Object> updateStatus(Integer id, FeedDtos.FoodStatusRequest req) {
        RoleCheck.requireRole("volunteer", "admin");
        Enums.checkFoodStatus(req.getFoodStatus());
        FeedPoint point = this.feedPointMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("点位不存在"));
        point.setFoodStatus(req.getFoodStatus());
        point.setLastUpdateUser(AuthContext.getUserId());
        point.setUpdateTime(LocalDateTime.now());
        this.feedPointMapper.update(point);
        return this.toMap(point);
    }

    public void delete(Integer id) {
        RoleCheck.requireRole("admin");
        this.feedPointMapper.deleteById(id);
    }

    private Map<String, Object> toMap(FeedPoint p) {
        User u;
        HashMap<String, Object> m = new HashMap<>();
        m.put("pointId", p.getPointId());
        m.put("pointName", p.getPointName());
        m.put("location", p.getLocation());
        m.put("foodStatus", p.getFoodStatus());
        m.put("lastUpdateUser", p.getLastUpdateUser());
        m.put("updateTime", p.getUpdateTime());
        if (p.getLastUpdateUser() != null && (u = this.userService.findById(p.getLastUpdateUser())) != null) {
            m.put("lastUpdateNickname", u.getNickname());
        }
        return m;
    }

    private Map<String, Object> recordToMap(FeedRecord r) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("recordId", r.getRecordId());
        m.put("userId", r.getUserId());
        m.put("pointId", r.getPointId());
        m.put("foodType", r.getFoodType());
        m.put("feedTime", r.getFeedTime());
        User u = this.userService.findById(r.getUserId());
        if (u != null) {
            m.put("nickname", u.getNickname());
        }
        return m;
    }

    public FeedPointService(FeedPointMapper feedPointMapper, FeedRecordMapper feedRecordMapper, UserService userService) {
        this.feedPointMapper = feedPointMapper;
        this.feedRecordMapper = feedRecordMapper;
        this.userService = userService;
    }
}
