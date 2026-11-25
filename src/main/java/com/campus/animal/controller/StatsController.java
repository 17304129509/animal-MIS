package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.mapper.AnimalMapper;
import com.campus.animal.mapper.FeedPointMapper;
import com.campus.animal.mapper.FeedRecordMapper;
import com.campus.animal.mapper.RescueOrderMapper;
import com.campus.animal.mapper.StatsMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/stats"})
public class StatsController {
    private final StatsMapper statsMapper;
    private final AnimalMapper animalMapper;
    private final FeedPointMapper feedPointMapper;
    private final RescueOrderMapper rescueOrderMapper;
    private final FeedRecordMapper feedRecordMapper;

    @GetMapping(value={"/dashboard"})
    public ApiResponse<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("animalCount", this.animalMapper.count());
        data.put("feedPointCount", this.feedPointMapper.count());
        data.put("feedRecordCount", this.feedRecordMapper.count());
        data.put("rescueOrderCount", this.rescueOrderMapper.count());
        data.put("pendingRescue", this.rescueOrderMapper.countByOrderStatus("待处理"));
        data.put("completedRescue", this.rescueOrderMapper.countByOrderStatus("已完成"));
        data.put("foodSufficient", this.feedPointMapper.countByFoodStatus("充足"));
        data.put("foodInsufficient", this.feedPointMapper.countByFoodStatus("不足"));
        data.put("foodEmpty", this.feedPointMapper.countByFoodStatus("空缺"));
        data.put("healthyAnimals", this.animalMapper.countByStatus("健康"));
        data.put("injuredAnimals", this.animalMapper.countByStatus("受伤"));
        data.put("sickAnimals", this.animalMapper.countByStatus("生病"));
        return ApiResponse.ok(data);
    }

    @GetMapping(value={"/weekly-feeding"})
    public ApiResponse<List<Map<String, Object>>> weeklyFeeding() {
        return ApiResponse.ok(statsMapper.weeklyFeeding());
    }

    @GetMapping(value={"/food-status"})
    public ApiResponse<List<Map<String, Object>>> foodStatus() {
        return ApiResponse.ok(statsMapper.foodStatus());
    }

    @GetMapping(value={"/rescue-rate"})
    public ApiResponse<Map<String, Object>> rescueRate() {
        long total = this.rescueOrderMapper.count();
        long completed = this.rescueOrderMapper.countByOrderStatus("已完成");
        long pending = this.rescueOrderMapper.countByOrderStatus("待处理");
        long processing = rescueOrderMapper.countByOrderStatus("救助中") + rescueOrderMapper.countByOrderStatus("处理中");
        long closed = this.rescueOrderMapper.countByOrderStatus("已关闭");
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("completed", completed);
        data.put("pending", pending);
        data.put("processing", processing);
        data.put("closed", closed);
        data.put("rate", total > 0L ? Math.round((double)completed * 100.0 / (double)total) : 0L);
        return ApiResponse.ok(data);
    }

    public StatsController(StatsMapper statsMapper, AnimalMapper animalMapper, FeedPointMapper feedPointMapper, RescueOrderMapper rescueOrderMapper, FeedRecordMapper feedRecordMapper) {
        this.statsMapper = statsMapper;
        this.animalMapper = animalMapper;
        this.feedPointMapper = feedPointMapper;
        this.rescueOrderMapper = rescueOrderMapper;
        this.feedRecordMapper = feedRecordMapper;
    }
}
