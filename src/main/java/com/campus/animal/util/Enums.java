package com.campus.animal.util;

import com.campus.animal.common.BusinessException;
import java.util.Set;

public final class Enums {
    public static final Set<String> ROLES = Set.of("student", "volunteer", "admin");
    public static final Set<String> ANIMAL_TYPES = Set.of("猫咪", "狗狗", "其他");
    public static final Set<String> ANIMAL_STATUS = Set.of("健康", "受伤", "康复", "已领养");
    public static final Set<String> FOOD_STATUS = Set.of("充足", "不足", "空缺");
    public static final Set<String> ORDER_STATUS = Set.of("待处理", "救助中", "已完成", "已关闭");

    private Enums() {
    }

    public static void checkRole(String role) {
        if (!ROLES.contains(role)) {
            throw BusinessException.badRequest("无效角色");
        }
    }

    public static void checkAnimalType(String type) {
        if (!ANIMAL_TYPES.contains(type)) {
            throw BusinessException.badRequest("无效动物类型");
        }
    }

    public static void checkAnimalStatus(String status) {
        if (!ANIMAL_STATUS.contains(status)) {
            throw BusinessException.badRequest("无效动物状态");
        }
    }

    public static void checkFoodStatus(String status) {
        if (!FOOD_STATUS.contains(status)) {
            throw BusinessException.badRequest("无效物资状态");
        }
    }

    public static void checkOrderStatus(String status) {
        if (!ORDER_STATUS.contains(status)) {
            throw BusinessException.badRequest("无效工单状态");
        }
    }
}
