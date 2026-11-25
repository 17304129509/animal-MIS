package com.campus.animal.service;

import static com.campus.animal.common.BusinessException.badRequest;
import static com.campus.animal.common.BusinessException.notFound;

import com.campus.animal.config.AuthContext;
import com.campus.animal.dto.AdminDtos;
import com.campus.animal.entity.Animal;
import com.campus.animal.entity.FeedPoint;
import com.campus.animal.entity.RescueOrder;
import com.campus.animal.mapper.AdminMapper;
import com.campus.animal.mapper.AnimalMapper;
import com.campus.animal.mapper.FeedPointMapper;
import com.campus.animal.mapper.RescueOrderMapper;
import com.campus.animal.util.Enums;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final AnimalMapper animalMapper;
    private final RescueOrderMapper rescueOrderMapper;
    private final FeedPointMapper feedPointMapper;

    public List<Map<String, Object>> animals(String filter) {
        Boolean approved = switch (filter) {
            case "pending" -> Boolean.FALSE;
            case "approved" -> Boolean.TRUE;
            default -> null;
        };
        var rows = adminMapper.animals(approved);
        rows.forEach(row -> row.put("is_approved", row.remove("approval_number")));
        return rows;
    }

    @Transactional
    public int createAnimal(AdminDtos.AnimalRequest request) {
        Animal animal = new Animal();
        copyAnimal(request, animal);
        animal.setCreateUser(AuthContext.getUserId());
        animal.setIsApproved(true);
        animal.setStatus("在校园");
        animalMapper.insert(animal);
        return animal.getAnimalId();
    }

    @Transactional
    public void editAnimal(int id, AdminDtos.AnimalRequest request) {
        Animal animal = lockAnimal(id);
        copyAnimal(request, animal);
        adminMapper.updateAnimal(animal);
    }

    @Transactional
    public void decideAnimal(int id, boolean approve, String reason) {
        Animal animal = lockAnimal(id);
        animal.setIsApproved(approve);
        if (!approve) {
            String note = reason == null || reason.isBlank() ? "[已驳回]" : "[驳回原因] " + reason;
            String previous = animal.getRemark();
            animal.setRemark(previous == null || previous.isBlank() ? note : previous + "; " + note);
        }
        animalMapper.update(animal);
    }

    public List<Map<String, Object>> rescues(String filter) {
        String status = switch (filter) {
            case "pending" -> "待处理";
            case "processing" -> "处理中";
            case "completed" -> "已完成";
            default -> null;
        };
        var rows = adminMapper.rescues(status);
        rows.forEach(row -> {
            if ("救助中".equals(row.get("order_status"))) {
                row.put("order_status", "处理中");
            }
        });
        return rows;
    }

    public int createRescue(AdminDtos.RescueRequest request) {
        RescueOrder order = new RescueOrder();
        order.setAnimalId(request.animalId());
        order.setPublishUser(AuthContext.getUserId());
        order.setRescueContent(request.rescueContent());
        order.setLocation(request.location());
        order.setUrgency(valueOr(request.urgency(), "普通"));
        rescueOrderMapper.insert(order);
        return order.getRescueId();
    }

    public List<Map<String, Object>> adoptions(String filter) {
        return adminMapper.adoptions(switch (filter) {
            case "pending" -> "待审核";
            case "approved" -> "已通过";
            case "rejected" -> "已驳回";
            default -> null;
        });
    }

    public void createFeedPoint(AdminDtos.FeedPointRequest request) {
        FeedPoint point = new FeedPoint();
        copyPoint(request, point);
        feedPointMapper.insert(point);
    }

    @Transactional
    public void editFeedPoint(int id, AdminDtos.FeedPointRequest request) {
        FeedPoint point = feedPointMapper.lockById(id).orElseThrow(() -> notFound("点位不存在"));
        copyPoint(request, point);
        adminMapper.updateFeedPoint(point);
    }

    public void deleteUser(int id) {
        if (id == AuthContext.getUserId()) {
            throw badRequest("不能删除自己");
        }
        if (adminMapper.deleteUser(id) == 0) {
            throw notFound("用户不存在");
        }
    }

    private Animal lockAnimal(int id) {
        return animalMapper.lockById(id).orElseThrow(() -> notFound("档案不存在"));
    }

    private void copyAnimal(AdminDtos.AnimalRequest request, Animal animal) {
        animal.setAnimalName(request.animalName());
        animal.setType(request.type());
        animal.setColor(valueOr(request.color(), ""));
        animal.setGender(valueOr(request.gender(), ""));
        animal.setLocation(valueOr(request.location(), ""));
        animal.setHealth(valueOr(request.health(), "健康"));
        animal.setRemark(valueOr(request.remark(), ""));
    }

    private void copyPoint(AdminDtos.FeedPointRequest request, FeedPoint point) {
        String status = valueOr(request.foodStatus(), "充足");
        Enums.checkFoodStatus(status);
        point.setPointName(request.pointName());
        point.setLocation(request.location());
        point.setFoodStatus(status);
        point.setLastUpdateUser(AuthContext.getUserId());
        point.setUpdateTime(LocalDateTime.now());
    }

    public static String valueOr(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
