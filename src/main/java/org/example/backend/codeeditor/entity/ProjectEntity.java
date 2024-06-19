package org.example.backend.codeeditor.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="projects")
@EntityListeners(AuditingEntityListener.class)
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id; //기본키 id

    @Column(name = "project_subject", nullable = false)
    private String subject; // 프로젝트 제목

    @CreatedDate
    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt; // 프로젝트 생성 시간

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 마지막으로 수정된 시간

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable=false)
    private UserEntity user; //유저 id를 외래키로 설정

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileEntity> files = new HashSet<>();

    //생성자
    public ProjectEntity() {}

    public ProjectEntity(String subject, UserEntity user){
        this.subject = subject;
        this.user = user;
    }

    // Getter 와 Setter

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(Set<FileEntity> files) {
        this.files = files;
    }






}
