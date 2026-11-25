package com.campus.animal.service;

import com.campus.animal.common.BusinessException;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.AuthContext;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.RescueDtos;
import com.campus.animal.entity.RescueOrder;
import com.campus.animal.entity.User;
import com.campus.animal.mapper.RescueOrderMapper;
import com.campus.animal.util.Enums;
import com.campus.animal.util.PageHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RescueService {
    private final RescueOrderMapper rescueOrderMapper;
    private final UserService userService;

    public Map<String, Object> create(RescueDtos.RescueRequest req) {
        RoleCheck.requireLogin();
        RescueOrder order = new RescueOrder();
        order.setAnimalId(req.getAnimalId());
        order.setPublishUser(AuthContext.getUserId());
        order.setRescueContent(req.getRescueContent());
        order.setLocation(req.getLocation());
        order.setOrderStatus("待处理");
        rescueOrderMapper.insert(order);
        return this.toMap(order);
    }

    public PageResult<Map<String, Object>> list(String orderStatus, int page, int pageSize) {
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(rescueOrderMapper.findPage(orderStatus, null, paging.offset(), paging.pageSize()),
                rescueOrderMapper.countFiltered(orderStatus, null), this::toMap);
    }

    public Map<String, Object> getById(Integer id) {
        RescueOrder order = this.rescueOrderMapper.findById(id).orElseThrow(() -> BusinessException.notFound("工单不存在"));
        return this.toMap(order);
    }

    @Transactional
    public Map<String, Object> claim(Integer id) {
        RoleCheck.requireRole("volunteer", "admin");
        RescueOrder order = this.rescueOrderMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("工单不存在"));
        if (!"待处理".equals(order.getOrderStatus())) {
            throw BusinessException.badRequest("仅待处理工单可认领");
        }
        order.setDealUser(AuthContext.getUserId());
        order.setOrderStatus("救助中");
        this.rescueOrderMapper.update(order);
        return this.toMap(order);
    }

    @Transactional
    public Map<String, Object> updateStatus(Integer id, RescueDtos.StatusUpdateRequest req) {
        RoleCheck.requireRole("volunteer", "admin");
        Enums.checkOrderStatus(req.getOrderStatus());
        RescueOrder order = this.rescueOrderMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("工单不存在"));
        requireActive(order);
        if (!"救助中".equals(req.getOrderStatus()) && !"已完成".equals(req.getOrderStatus())) {
            throw BusinessException.badRequest("状态更新仅支持救助中或已完成");
        }
        if (order.getDealUser() == null) {
            order.setDealUser(AuthContext.getUserId());
        }
        order.setOrderStatus(req.getOrderStatus());
        if (req.getResult() != null) {
            order.setResult(req.getResult());
        }
        this.rescueOrderMapper.update(order);
        return this.toMap(order);
    }

    @Transactional
    public Map<String, Object> close(Integer id, RescueDtos.StatusUpdateRequest req) {
        RoleCheck.requireRole("admin");
        RescueOrder order = this.rescueOrderMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("工单不存在"));
        String status = req.getOrderStatus() != null ? req.getOrderStatus() : "已关闭";
        Enums.checkOrderStatus(status);
        if (!"已关闭".equals(status) && !"已完成".equals(status)) {
            throw BusinessException.badRequest("关闭操作仅支持已完成或已关闭");
        }
        if (!("已完成".equals(order.getOrderStatus()) && "已关闭".equals(status))) {
            requireActive(order);
        }
        order.setOrderStatus(status);
        if (req.getResult() != null) {
            order.setResult(req.getResult());
        }
        this.rescueOrderMapper.update(order);
        return this.toMap(order);
    }

    public PageResult<Map<String, Object>> myOrders(int page, int pageSize) {
        RoleCheck.requireLogin();
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(rescueOrderMapper.findPage(null, AuthContext.getUserId(), paging.offset(), paging.pageSize()),
                rescueOrderMapper.countFiltered(null, AuthContext.getUserId()), this::toMap);
    }

    @Transactional
    public void adminTransition(int id, String status, String result) {
        RoleCheck.requireRole("admin");
        RescueOrder order = rescueOrderMapper.lockById(id)
                .orElseThrow(() -> BusinessException.notFound("工单不存在"));
        requireActive(order);
        if ("已完成".equals(status) && "待处理".equals(order.getOrderStatus())) {
            throw BusinessException.badRequest("请先接单");
        }
        if ("已驳回".equals(status) && !"待处理".equals(order.getOrderStatus())) {
            throw BusinessException.badRequest("仅待处理工单可驳回");
        }
        if (result.length() > 200) {
            throw BusinessException.badRequest("救助结果不能超过200字");
        }
        order.setOrderStatus(status);
        order.setResult(result);
        rescueOrderMapper.update(order);
    }

    private void requireActive(RescueOrder order) {
        if (!java.util.Set.of("待处理", "救助中", "处理中").contains(order.getOrderStatus())) {
            throw BusinessException.badRequest("该工单已处理完毕");
        }
    }

    public List<Map<String, Object>> rescueRank(int limit) {
        return rescueOrderMapper.countCompletedByDealUser(Math.max(limit, 0));
    }

    private Map<String, Object> toMap(RescueOrder o) {
        User deal;
        HashMap<String, Object> m = new HashMap<>();
        m.put("rescueId", o.getRescueId());
        m.put("animalId", o.getAnimalId());
        m.put("publishUser", o.getPublishUser());
        m.put("rescueContent", o.getRescueContent());
        m.put("location", o.getLocation());
        m.put("orderStatus", "处理中".equals(o.getOrderStatus()) ? "救助中" : o.getOrderStatus());
        m.put("dealUser", o.getDealUser());
        m.put("result", o.getResult());
        m.put("createTime", o.getCreateTime());
        User pub = this.userService.findById(o.getPublishUser());
        if (pub != null) {
            m.put("publishNickname", pub.getNickname());
        }
        if (o.getDealUser() != null && (deal = this.userService.findById(o.getDealUser())) != null) {
            m.put("dealNickname", deal.getNickname());
        }
        return m;
    }

    public RescueService(RescueOrderMapper rescueOrderMapper, UserService userService) {
        this.rescueOrderMapper = rescueOrderMapper;
        this.userService = userService;
    }
}
