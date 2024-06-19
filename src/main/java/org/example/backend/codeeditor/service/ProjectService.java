package org.example.backend.codeeditor.service;

import org.example.backend.codeeditor.dto.FileDto;
import org.example.backend.codeeditor.dto.ProjectDto;
import org.example.backend.codeeditor.entity.ProjectEntity;
import org.example.backend.codeeditor.entity.UserEntity;
import org.example.backend.codeeditor.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    // 프로젝트 생성
    public ProjectDto createProject(String subject, UserEntity user) {
        ProjectEntity projectEntity = new ProjectEntity(subject, user);
        ProjectEntity savedProject = projectRepository.save(projectEntity);
        return toDto(savedProject);
    }

    // 프로젝트 id로 프로젝트 찾기 (DTO)
    public Optional<ProjectDto> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(this::toDto);
    }

    // 프로젝트 id로 프로젝트 찾기 (Entity)
    public Optional<ProjectEntity> getProjectEntityById(Long id) {
        return projectRepository.findById(id);
    }

    // 사용자 id로 프로젝트 찾기
    public List<ProjectDto> getProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 프로젝트 삭제
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // 사용자와 주제로 프로젝트 존재 여부 확인
    public boolean isProjectExists(String subject, UserEntity user) {
        return projectRepository.existsBySubjectAndUser(subject, user);
    }

    // 프로젝트 제목 업데이트
    public Optional<ProjectDto> updateProjectSubject(Long projectId, String newSubject) {
        Optional<ProjectEntity> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isPresent()) {
            ProjectEntity project = projectOptional.get();
            project.setSubject(newSubject);
            ProjectEntity updatedProject = projectRepository.save(project);
            return Optional.of(toDto(updatedProject));
        } else {
            return Optional.empty();
        }
    }

    // ProjectEntity를 ProjectDto로 변환
    private ProjectDto toDto(ProjectEntity projectEntity) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(projectEntity.getId());
        projectDto.setSubject(projectEntity.getSubject());
        projectDto.setCreatedAt(projectEntity.getCreatedAt());
        projectDto.setUpdatedAt(projectEntity.getUpdatedAt());
        projectDto.setUserId(projectEntity.getUser().getId());
        projectDto.setUserName(projectEntity.getUser().getUsername());
        projectDto.setFiles(projectEntity.getFiles().stream()
                .map(fileEntity -> {
                    FileDto fileDto = new FileDto();
                    fileDto.setId(fileEntity.getId());
                    fileDto.setName(fileEntity.getName());
                    fileDto.setContent(fileEntity.getContent());
                    return fileDto;
                })
                .collect(Collectors.toSet()));
        return projectDto;
    }
}
