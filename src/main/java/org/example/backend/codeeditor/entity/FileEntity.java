package org.example.backend.codeeditor.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="files")
@EntityListeners(AuditingEntityListener.class)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="files_id")
    private Long id; // 기본키 id

    @Column(name="file_name",nullable = false)
    private String name; //파일 이름

    @Column(name="content", columnDefinition ="TEXT" )
    private String content; //파일 내용

    @Column(name="file_type")
    private String fileType; // 파일 확장자

    @CreatedDate
    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt; // 파일 생성 시간

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 마지막으로 수정된 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "project_id", nullable = false)
    private ProjectEntity project;

    //생성자

    public FileEntity() {}

    public FileEntity(String name, String content, String fileType, ProjectEntity project){
        this.name = name;
        this.content = content;
        this.fileType = fileType;
        this.project = project;
    }

    // Getter 와 Setter

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
