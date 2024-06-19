package org.example.backend.codeeditor.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class ProjectDto {
    private Long id; //기본키id
    private String subject; //프로젝트 제목
    private LocalDateTime createdAt; //프로젝트가 생성된 시간
    private LocalDateTime updatedAt; //프로젝트가 수정된 시간
    private Long userId; //사용자id
    private String userName; //사용자이름
    private Set<FileDto> files; //속한 파일

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<FileDto> getFiles() {
        return files;
    }

    public void setFiles(Set<FileDto> files) {
        this.files = files;
    }
}
