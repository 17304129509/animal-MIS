package com.campus.animal.controller;

import static com.campus.animal.service.AdminService.valueOr;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.config.AuthContext;
import com.campus.animal.dto.AdminDtos;
import com.campus.animal.dto.UserDtos;
import com.campus.animal.mapper.AdminMapper;
import com.campus.animal.service.AdminService;
import com.campus.animal.service.AdoptionService;
import com.campus.animal.service.AnimalService;
import com.campus.animal.service.FeedPointService;
import com.campus.animal.service.RescueService;
import com.campus.animal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminMapper adminMapper;
    private final AdminService adminService;
    private final AdoptionService adoptionService;
    private final AnimalService animalService;
    private final FeedPointService feedPointService;
    private final RescueService rescueService;
    private final UserService userService;

    @Value("${app.admin-port}")
    private int adminPort;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody UserDtos.LoginRequest body) {
        var login = userService.adminLogin(body);
        return ApiResponse.ok("登录成功", Map.of("token", login.getToken(), "user", Map.of(
                "user_id", login.getUserId(), "username", login.getUsername(),
                "nickname", login.getNickname(), "role", login.getRole())));
    }

    @GetMapping("/info")
    public ApiResponse<?> info() {
        var user = userService.findById(AuthContext.getUserId());
        return ApiResponse.ok(Map.of("userId", user.getUserId(), "username", user.getUsername(),
                "nickname", user.getNickname(), "role", user.getRole()));
    }

    @GetMapping("/users")
    public ApiResponse<?> users(HttpServletRequest request, @RequestParam(required = false) String role,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int pageSize) {
        if (request.getLocalPort() == adminPort && request.getParameterMap().isEmpty()) {
            return ApiResponse.ok(adminMapper.users());
        }
        return ApiResponse.ok(userService.listUsers(role, page, pageSize));
    }

    @GetMapping("/stats")
    public ApiResponse<?> stats() {
        return ApiResponse.ok(adminMapper.stats());
    }

    @GetMapping("/animals")
    public ApiResponse<?> animals(@RequestParam(defaultValue = "all") String status) {
        return ApiResponse.ok(adminService.animals(status));
    }

    @PostMapping("/animals/create")
    public ApiResponse<?> createAnimal(@Valid @RequestBody AdminDtos.AnimalRequest body) {
        return ApiResponse.ok("建档成功，已自动通过", Map.of("id", adminService.createAnimal(body)));
    }

    @PutMapping("/animals/{id}/edit")
    public ApiResponse<?> editAnimal(@PathVariable int id, @Valid @RequestBody AdminDtos.AnimalRequest body) {
        adminService.editAnimal(id, body);
        return ApiResponse.ok("档案更新成功", null);
    }

    @PutMapping("/animals/{id}/approve")
    public ApiResponse<?> approveAnimal(@PathVariable int id) {
        adminService.decideAnimal(id, true, null);
        return ApiResponse.ok("审批通过", null);
    }

    @PutMapping("/animals/{id}/reject")
    public ApiResponse<?> rejectAnimal(@PathVariable int id, @Valid @RequestBody AdminDtos.Decision body) {
        adminService.decideAnimal(id, false, body.reason());
        return ApiResponse.ok("已驳回", null);
    }

    @DeleteMapping("/animals/{id}")
    public ApiResponse<?> deleteAnimal(@PathVariable int id) {
        animalService.delete(id);
        return ApiResponse.ok("删除成功", null);
    }

    @GetMapping("/rescues")
    public ApiResponse<?> rescues(@RequestParam(defaultValue = "all") String status) {
        return ApiResponse.ok(adminService.rescues(status));
    }

    @PostMapping("/rescues/create")
    public ApiResponse<?> createRescue(@Valid @RequestBody AdminDtos.RescueRequest body) {
        return ApiResponse.ok("工单发布成功", Map.of("id", adminService.createRescue(body)));
    }

    @PutMapping("/rescues/{id}/process")
    public ApiResponse<?> processRescue(@PathVariable int id) {
        rescueService.claim(id);
        return ApiResponse.ok("已接单，状态更新为处理中", null);
    }

    @PutMapping("/rescues/{id}/complete")
    public ApiResponse<?> completeRescue(@PathVariable int id, @Valid @RequestBody AdminDtos.Decision body) {
        rescueService.adminTransition(id, "已完成", valueOr(body.result(), "救助完成"));
        return ApiResponse.ok("救助完成", null);
    }

    @PutMapping("/rescues/{id}/reject")
    public ApiResponse<?> rejectRescue(@PathVariable int id, @Valid @RequestBody AdminDtos.Decision body) {
        rescueService.adminTransition(id, "已驳回", valueOr(body.reason(), "经审核，该救助申请不符合条件"));
        return ApiResponse.ok("已驳回", null);
    }

    @GetMapping("/adoptions")
    public ApiResponse<?> adoptions(@RequestParam(defaultValue = "all") String status) {
        return ApiResponse.ok(adminService.adoptions(status));
    }

    @PutMapping("/adoptions/{id}/approve")
    public ApiResponse<?> approveAdoption(@PathVariable int id, @Valid @RequestBody AdminDtos.Decision body) {
        adoptionService.adminReview(id, "已通过", valueOr(body.comment(), "审核通过，同意领养"));
        return ApiResponse.ok("领养申请已通过", null);
    }

    @PutMapping("/adoptions/{id}/reject")
    public ApiResponse<?> rejectAdoption(@PathVariable int id, @Valid @RequestBody AdminDtos.Decision body) {
        adoptionService.adminReview(id, "已驳回", valueOr(body.reason(), "抱歉，您的领养申请未通过"));
        return ApiResponse.ok("已驳回领养申请", null);
    }

    @GetMapping("/feed-points")
    public ApiResponse<?> feedPoints() {
        return ApiResponse.ok(adminMapper.feedPoints());
    }

    @PostMapping("/feed-points")
    public ApiResponse<?> createFeedPoint(@Valid @RequestBody AdminDtos.FeedPointRequest body) {
        adminService.createFeedPoint(body);
        return ApiResponse.ok("投喂点添加成功", null);
    }

    @PutMapping("/feed-points/{id}")
    public ApiResponse<?> editFeedPoint(@PathVariable int id, @Valid @RequestBody AdminDtos.FeedPointRequest body) {
        adminService.editFeedPoint(id, body);
        return ApiResponse.ok("投喂点更新成功", null);
    }

    @DeleteMapping("/feed-points/{id}")
    public ApiResponse<?> deleteFeedPoint(@PathVariable int id) {
        feedPointService.delete(id);
        return ApiResponse.ok("投喂点删除成功", null);
    }

    @PutMapping("/users/{id}/role")
    public ApiResponse<?> updateRole(@PathVariable int id, @Valid @RequestBody UserDtos.RoleUpdateRequest body) {
        userService.updateRole(id, body);
        return ApiResponse.ok("角色更新成功", null);
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<?> deleteUser(@PathVariable int id) {
        adminService.deleteUser(id);
        return ApiResponse.ok("用户删除成功", null);
    }

    @GetMapping("/feed-records")
    public ApiResponse<?> feedRecords() {
        return ApiResponse.ok(adminMapper.feedRecords());
    }

    @GetMapping("/volunteers")
    public ApiResponse<?> volunteers() {
        return ApiResponse.ok(adminMapper.volunteers());
    }
}
