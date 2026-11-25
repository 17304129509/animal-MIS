package com.campus.animal.service;

import com.campus.animal.common.BusinessException;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.AuthContext;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.AdoptionDtos;
import com.campus.animal.entity.Adoption;
import com.campus.animal.entity.User;
import com.campus.animal.mapper.AdoptionMapper;
import com.campus.animal.mapper.AnimalMapper;
import com.campus.animal.util.PageHelper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionService {
    private final AdoptionMapper adoptionMapper;
    private final UserService userService;
    private final AnimalMapper animalMapper;

    public Map<String, Object> apply(AdoptionDtos.ApplyRequest req) {
        RoleCheck.requireLogin();
        if (!animalMapper.existsById(req.getAnimalId())) {
            throw BusinessException.notFound("档案不存在");
        }
        Adoption adoption = new Adoption();
        adoption.setAnimalId(req.getAnimalId());
        adoption.setApplicantId(AuthContext.getUserId());
        adoption.setReason(req.getReason());
        adoption.setContact(req.getContact());
        adoption.setStatus("待审核");
        adoptionMapper.insert(adoption);
        return this.toMap(adoption);
    }

    public PageResult<Map<String, Object>> list(String status, int page, int pageSize) {
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(adoptionMapper.findPage(status, null, paging.offset(), paging.pageSize()),
                adoptionMapper.countFiltered(status, null), this::toMap);
    }

    public PageResult<Map<String, Object>> myApplications(int page, int pageSize) {
        RoleCheck.requireLogin();
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(adoptionMapper.findPage(null, AuthContext.getUserId(), paging.offset(), paging.pageSize()),
                adoptionMapper.countFiltered(null, AuthContext.getUserId()), this::toMap);
    }

    public Map<String, Object> get(Integer id) {
        Adoption adoption = this.adoptionMapper.findById(id).orElseThrow(() -> BusinessException.notFound("领养申请不存在"));
        return this.toMap(adoption);
    }

    @Transactional
    public Map<String, Object> review(Integer id, AdoptionDtos.ReviewRequest req) {
        RoleCheck.requireRole("admin", "volunteer");
        return toMap(review(id, req.getStatus(), req.getReviewComment(), true));
    }

    @Transactional
    public void adminReview(int id, String status, String comment) {
        RoleCheck.requireRole("admin");
        review(id, status, comment, false);
    }

    private Adoption review(int id, String status, String comment, boolean awardPoints) {
        if (!"已通过".equals(status) && !"已驳回".equals(status)) {
            throw BusinessException.badRequest("审核结果只能为已通过或已驳回");
        }
        Adoption adoption = this.adoptionMapper.lockById(id).orElseThrow(() -> BusinessException.notFound("领养申请不存在"));
        if (!"待审核".equals(adoption.getStatus())) {
            throw BusinessException.badRequest("该申请已处理");
        }
        if ("已通过".equals(status)) {
            var animal = animalMapper.lockById(adoption.getAnimalId())
                    .orElseThrow(() -> BusinessException.notFound("档案不存在"));
            if ("已领养".equals(animal.getStatus())) {
                throw BusinessException.badRequest("该动物已领养");
            }
            animal.setStatus("已领养");
            animalMapper.update(animal);
            if (awardPoints) {
                userService.deductLovePoints(adoption.getApplicantId(), -50);
            }
        }
        adoption.setStatus(status);
        adoption.setReviewComment(comment);
        adoption.setReviewUserId(AuthContext.getUserId());
        adoption.setReviewTime(LocalDateTime.now());
        this.adoptionMapper.update(adoption);
        return adoption;
    }

    private Map<String, Object> toMap(Adoption a) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("animalId", a.getAnimalId());
        m.put("applicantId", a.getApplicantId());
        m.put("reason", a.getReason());
        m.put("contact", a.getContact());
        m.put("status", a.getStatus());
        m.put("reviewComment", a.getReviewComment());
        m.put("applyTime", a.getApplyTime());
        m.put("reviewTime", a.getReviewTime());
        User applicant = this.userService.findById(a.getApplicantId());
        if (applicant != null) {
            m.put("applicantName", applicant.getNickname());
        }
        User reviewer = userService.findById(a.getReviewUserId());
        if (reviewer != null) {
            m.put("reviewerName", reviewer.getNickname());
        }
        return m;
    }

    public AdoptionService(AdoptionMapper adoptionMapper, UserService userService, AnimalMapper animalMapper) {
        this.adoptionMapper = adoptionMapper;
        this.userService = userService;
        this.animalMapper = animalMapper;
    }
}
