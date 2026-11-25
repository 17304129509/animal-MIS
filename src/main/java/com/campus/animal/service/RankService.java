package com.campus.animal.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RankService {
    private final FeedRecordService feedRecordService;
    private final RescueService rescueService;

    public List<Map<String, Object>> feedRank(int limit) {
        List<Map<String, Object>> all = this.feedRecordService.stats();
        return all.stream().limit(limit).toList();
    }

    public List<Map<String, Object>> rescueRank(int limit) {
        return this.rescueService.rescueRank(limit);
    }

    public RankService(FeedRecordService feedRecordService, RescueService rescueService) {
        this.feedRecordService = feedRecordService;
        this.rescueService = rescueService;
    }
}
