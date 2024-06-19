package org.example.backend.codeeditor.controller;

import org.example.backend.codeeditor.dto.ProjectDto;
import org.example.backend.codeeditor.entity.UserEntity;
import org.example.backend.codeeditor.exception.BadRequestException;
import org.example.backend.codeeditor.exception.ConflictException;
import org.example.backend.codeeditor.exception.ResourceNotFoundException;
import org.example.backend.codeeditor.service.ProjectService;
import org.example.backend.codeeditor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    // 프로젝트 생성
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestParam("projects_subject") String subject, @RequestParam("userId") Long userId) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new BadRequestException("Project subject cannot be null or empty"); // 프로젝트 제목 입력 필수
        }
        Optional<UserEntity> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            if (projectService.isProjectExists(subject, userOptional.get())) {
                throw new ConflictException("A project with the same name already exists."); // 409 에러 출력, 같은 이름의 프로젝트 존재
            }
            ProjectDto project = projectService.createProject(subject, userOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(project);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + userId); // id 값에 해당하는 사용자 정보 없음
        }
    }


    // 사용자 ID로 프로젝트 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByUserId(@PathVariable("userId") Long userId) {
        Optional<UserEntity> userOptional = userService.getUserById(userId);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + userId); // id 값에 해당하는 사용자 정보 없음
        }
        List<ProjectDto> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    // 특정 유저의 특정 프로젝트 조회
    @GetMapping("/{userId}/{projectId}")
    public ResponseEntity<ProjectDto> getUserProjectById(@PathVariable("userId") Long userId, @PathVariable("projectId") Long projectId) {
        Optional<UserEntity> userOptional = userService.getUserById(userId);
        if (!userOptional.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + userId); // id 값에 해당하는 사용자 정보 없음
        }
        Optional<ProjectDto> projectOptional = projectService.getProjectById(projectId);
        if (!projectOptional.isPresent()) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); // 프로젝트 정보 없음
        }
        ProjectDto project = projectOptional.get();
        if (!project.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Project does not belong to the user with id: " + userId); // 프로젝트 소유자 불일치
        }
        return ResponseEntity.ok(project);
    }

    // 프로젝트 제목 수정
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProjectSubject(@PathVariable("projectId") Long projectId, @RequestParam("projects_subject") String newSubject) {
        if (newSubject == null || newSubject.trim().isEmpty()) {
            throw new BadRequestException("Project subject cannot be null or empty"); // 프로젝트 제목 필수
        }
        Optional<ProjectDto> updatedProject;
        try {
            updatedProject = projectService.updateProjectSubject(projectId, newSubject);
        } catch (ConflictException e) {
            throw new ConflictException("같은 이름의 프로젝트가 존재합니다."); // 409 에러 출력
        }
        if (updatedProject.isPresent()) {
            return ResponseEntity.ok(updatedProject.get());
        } else {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); // 일치하는 프로젝트 id 없음
        }
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("projectId") Long projectId) {
        if (!projectService.getProjectEntityById(projectId).isPresent()) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); // 일치하는 프로젝트 id 없음
        }
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
