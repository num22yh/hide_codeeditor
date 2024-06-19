package org.example.backend.codeeditor.repository;

import org.example.backend.codeeditor.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByProjectId(Long projectId); //프로젝트 id로 속해 있는 파일들 찾기

    Optional<FileEntity> findByNameAndProjectId(String name, Long projectId);

    Optional<FileEntity> findByIdAndProjectId(Long id, Long projectId); // 파일id + 프로젝트 id로 파일 찾기
}
