package org.example.backend.codeeditor.repository;

import org.example.backend.codeeditor.entity.ProjectEntity;
import org.example.backend.codeeditor.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByUserId(Long userId); //유저id로 해당 유저의 프로젝트 찾기

    // 사용자와 주제로 프로젝트를 찾는 메서드 추가
    boolean existsBySubjectAndUser(String subject, UserEntity user);
}
