package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.common.PageResult;
import com.campus.animal.dto.FeedDtos;
import com.campus.animal.service.FeedRecordService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/feed-record"})
public class FeedRecordController {
    private final FeedRecordService feedRecordService;

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@Valid @RequestBody FeedDtos.FeedRecordRequest req) {
        return ApiResponse.ok(this.feedRecordService.create(req));
    }

    @GetMapping(value={"/my"})
    public ApiResponse<PageResult<Map<String, Object>>> my(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.feedRecordService.myRecords(page, pageSize));
    }

    @GetMapping(value={"/point/{pointId}"})
    public ApiResponse<PageResult<Map<String, Object>>> byPoint(@PathVariable Integer pointId, @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.feedRecordService.byPoint(pointId, page, pageSize));
    }

    @GetMapping(value={"/stats"})
    public ApiResponse<List<Map<String, Object>>> stats() {
        return ApiResponse.ok(this.feedRecordService.stats());
    }

    public FeedRecordController(FeedRecordService feedRecordService) {
        this.feedRecordService = feedRecordService;
    }
}
