package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.service.RankService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/rank"})
public class RankController {
    private final RankService rankService;

    @GetMapping(value={"/feed"})
    public ApiResponse<List<Map<String, Object>>> feedRank(@RequestParam(defaultValue="10") int limit) {
        return ApiResponse.ok(this.rankService.feedRank(limit));
    }

    @GetMapping(value={"/rescue"})
    public ApiResponse<List<Map<String, Object>>> rescueRank(@RequestParam(defaultValue="10") int limit) {
        return ApiResponse.ok(this.rankService.rescueRank(limit));
    }

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }
}
