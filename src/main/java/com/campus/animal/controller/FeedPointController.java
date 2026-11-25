package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.common.PageResult;
import com.campus.animal.dto.FeedDtos;
import com.campus.animal.service.FeedPointService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/feed-point"})
public class FeedPointController {
    private final FeedPointService feedPointService;

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@Valid @RequestBody FeedDtos.FeedPointRequest req) {
        return ApiResponse.ok(this.feedPointService.create(req));
    }

    @GetMapping
    public ApiResponse<PageResult<Map<String, Object>>> list(@RequestParam(required=false) String foodStatus, @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.feedPointService.list(foodStatus, page, pageSize));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<Map<String, Object>> get(@PathVariable Integer id, @RequestParam(defaultValue="1") int recordPage, @RequestParam(defaultValue="5") int recordPageSize) {
        return ApiResponse.ok(this.feedPointService.getById(id, recordPage, recordPageSize));
    }

    @PatchMapping(value={"/{id}/status"})
    public ApiResponse<Map<String, Object>> updateStatus(@PathVariable Integer id, @Valid @RequestBody FeedDtos.FoodStatusRequest req) {
        return ApiResponse.ok(this.feedPointService.updateStatus(id, req));
    }

    @DeleteMapping(value={"/{id}"})
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        this.feedPointService.delete(id);
        return ApiResponse.ok();
    }

    public FeedPointController(FeedPointService feedPointService) {
        this.feedPointService = feedPointService;
    }
}
