package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.AnimalDtos;
import com.campus.animal.service.AnimalService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/animal"})
public class AnimalController {
    private final AnimalService animalService;

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@Valid @RequestBody AnimalDtos.AnimalRequest req) {
        return ApiResponse.ok(this.animalService.create(req));
    }

    @GetMapping
    public ApiResponse<PageResult<Map<String, Object>>> list(@RequestParam(required=false) String type, @RequestParam(required=false) String status, @RequestParam(required=false) String location, @RequestParam(required=false) Boolean approvedOnly, @RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int pageSize) {
        boolean onlyApproved = approvedOnly == null ? !RoleCheck.hasRole("admin") : approvedOnly;
        return ApiResponse.ok(this.animalService.list(type, status, location, onlyApproved, page, pageSize));
    }

    @GetMapping(value={"/{id}"})
    public ApiResponse<Map<String, Object>> get(@PathVariable Integer id) {
        return ApiResponse.ok(this.animalService.getById(id));
    }

    @PutMapping(value={"/{id}"})
    public ApiResponse<Map<String, Object>> update(@PathVariable Integer id, @Valid @RequestBody AnimalDtos.AnimalRequest req) {
        return ApiResponse.ok(this.animalService.update(id, req));
    }

    @PatchMapping(value={"/{id}/status"})
    public ApiResponse<Map<String, Object>> updateStatus(@PathVariable Integer id, @Valid @RequestBody AnimalDtos.StatusRequest req) {
        return ApiResponse.ok(this.animalService.updateStatus(id, req));
    }

    @DeleteMapping(value={"/{id}"})
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        this.animalService.delete(id);
        return ApiResponse.ok();
    }

    @PatchMapping(value={"/{id}/approve"})
    public ApiResponse<Map<String, Object>> approve(@PathVariable Integer id, @RequestBody AnimalDtos.ApproveRequest req) {
        return ApiResponse.ok(this.animalService.approve(id, req));
    }

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }
}
