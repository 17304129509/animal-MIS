package com.campus.animal.service;

import com.campus.animal.common.BusinessException;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.AuthContext;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.FeedDtos;
import com.campus.animal.entity.FeedRecord;
import com.campus.animal.entity.User;
import com.campus.animal.mapper.FeedPointMapper;
import com.campus.animal.mapper.FeedRecordMapper;
import com.campus.animal.util.PageHelper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedRecordService {
    private final FeedRecordMapper feedRecordMapper;
    private final FeedPointMapper feedPointMapper;
    private final UserService userService;

    @Transactional
    public Map<String, Object> create(FeedDtos.FeedRecordRequest req) {
        RoleCheck.requireLogin();
        if (!this.feedPointMapper.existsById(req.getPointId())) {
            throw BusinessException.notFound("投喂点位不存在");
        }
        if (req.getAmount() != null && req.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw BusinessException.badRequest("金额不能为负数");
        }
        Integer quantity = req.getQuantity() != null ? req.getQuantity() : 1;
        if (quantity < 1) {
            throw BusinessException.badRequest("数量至少为1");
        }
        if (req.getAmount() != null && req.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal total = req.getAmount().multiply(BigDecimal.valueOf(quantity));
            if (total.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
                throw BusinessException.badRequest("投喂金额超出范围");
            }
            int pointsToDeduct = total.intValue();
            this.userService.deductLovePoints(AuthContext.getUserId(), pointsToDeduct);
        }
        FeedRecord record = new FeedRecord();
        record.setUserId(AuthContext.getUserId());
        record.setPointId(req.getPointId());
        record.setFoodType(req.getFoodType());
        record.setAmount(req.getAmount());
        record.setQuantity(quantity);
        record.setRemark(req.getRemark());
        feedRecordMapper.insert(record);
        Map<String, Object> result = this.toMap(record);
        User u = this.userService.findById(AuthContext.getUserId());
        if (u != null) {
            result.put("lovePoints", u.getLovePoints());
        }
        return result;
    }

    public PageResult<Map<String, Object>> myRecords(int page, int pageSize) {
        RoleCheck.requireLogin();
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(feedRecordMapper.findPage(AuthContext.getUserId(), null, paging.offset(), paging.pageSize()),
                feedRecordMapper.countFiltered(AuthContext.getUserId(), null), this::toMap);
    }

    public PageResult<Map<String, Object>> byPoint(Integer pointId, int page, int pageSize) {
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(feedRecordMapper.findPage(null, pointId, paging.offset(), paging.pageSize()),
                feedRecordMapper.countFiltered(null, pointId), this::toMap);
    }

    public List<Map<String, Object>> stats() {
        return feedRecordMapper.countByUser();
    }

    private Map<String, Object> toMap(FeedRecord r) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("recordId", r.getRecordId());
        m.put("userId", r.getUserId());
        m.put("pointId", r.getPointId());
        m.put("foodType", r.getFoodType());
        m.put("amount", r.getAmount());
        m.put("quantity", r.getQuantity());
        m.put("remark", r.getRemark());
        m.put("feedTime", r.getFeedTime());
        User u = this.userService.findById(r.getUserId());
        if (u != null) {
            m.put("nickname", u.getNickname());
        }
        return m;
    }

    public FeedRecordService(FeedRecordMapper feedRecordMapper, FeedPointMapper feedPointMapper, UserService userService) {
        this.feedRecordMapper = feedRecordMapper;
        this.feedPointMapper = feedPointMapper;
        this.userService = userService;
    }
}
