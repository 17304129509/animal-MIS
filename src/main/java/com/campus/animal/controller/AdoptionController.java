package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.common.PageResult;
import com.campus.animal.dto.AdoptionDtos;
import com.campus.animal.service.AdoptionService;
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
@RequestMapping(value={"/api/adoption"})
public class AdoptionController {
    private final AdoptionService adoptionService;

    @PostMapping
    public ApiResponse<Map<String, Object>> apply(@Valid @RequestBody AdoptionDtos.ApplyRequest req) {
        return ApiResponse.ok(this.adoptionService.apply(req));
    }

    @GetMapping
    public ApiResponse<PageResult<Map<String, Object>>> list(@RequestParam(required=false) String status, @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.adoptionService.list(status, page, pageSize));
    }

    @GetMapping(value={"/my"})
    public ApiResponse<PageResult<Map<String, Object>>> my(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        return ApiResponse.ok(this.adoptionService.myApplications(page, pageSize));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<Map<String, Object>> get(@PathVariable Integer id) {
        return ApiResponse.ok(this.adoptionService.get(id));
    }

    @PatchMapping(value={"/{id}/review"})
    public ApiResponse<Map<String, Object>> review(@PathVariable Integer id, @Valid @RequestBody AdoptionDtos.ReviewRequest req) {
        return ApiResponse.ok(this.adoptionService.review(id, req));
    }

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }
}
