package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.common.PageResult;
import com.campus.animal.dto.RescueDtos;
import com.campus.animal.service.RescueService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/rescue"})
public class RescueController {
    private final RescueService rescueService;

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@Valid @RequestBody RescueDtos.RescueRequest req) {
        return ApiResponse.ok(this.rescueService.create(req));
    }

    @GetMapping
    public ApiResponse<PageResult<Map<String, Object>>> list(@RequestParam(required=false) String orderStatus, @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.rescueService.list(orderStatus, page, pageSize));
    }

    @GetMapping(value={"/my"})
    public ApiResponse<PageResult<Map<String, Object>>> my(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.rescueService.myOrders(page, pageSize));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<Map<String, Object>> get(@PathVariable Integer id) {
        return ApiResponse.ok(this.rescueService.getById(id));
    }

    @PatchMapping(value={"/{id}/claim"})
    public ApiResponse<Map<String, Object>> claim(@PathVariable Integer id) {
        return ApiResponse.ok(this.rescueService.claim(id));
    }

    @PatchMapping(value={"/{id}/status"})
    public ApiResponse<Map<String, Object>> updateStatus(@PathVariable Integer id, @Valid @RequestBody RescueDtos.StatusUpdateRequest req) {
        return ApiResponse.ok(this.rescueService.updateStatus(id, req));
    }

    @PatchMapping(value={"/{id}/close"})
    public ApiResponse<Map<String, Object>> close(@PathVariable Integer id, @RequestBody RescueDtos.StatusUpdateRequest req) {
        return ApiResponse.ok(this.rescueService.close(id, req));
    }

    public RescueController(RescueService rescueService) {
        this.rescueService = rescueService;
    }
}
