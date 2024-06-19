package org.example.backend.codeeditor.service;

import org.example.backend.codeeditor.dto.FileDto;
import org.example.backend.codeeditor.entity.FileEntity;
import org.example.backend.codeeditor.entity.ProjectEntity;
import org.example.backend.codeeditor.exception.ResourceNotFoundException;
import org.example.backend.codeeditor.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    // 파일 생성
    public FileDto createFile(String name, String content, String fileType, ProjectEntity project) {
        FileEntity file = new FileEntity(name, content, fileType, project);
        FileEntity savedFile = fileRepository.save(file);
        return toDto(savedFile);
    }

    // 파일 id와 프로젝트 id로 파일 찾기 (Dto)
    public Optional<FileDto> getFileByIdAndProjectId(Long fileId, Long projectId) {
        return fileRepository.findByIdAndProjectId(fileId, projectId)
                .map(this::toDto);
    }

    // 파일 id와 프로젝트 id로 파일 찾기 (Entity)
    public Optional<FileEntity> getFileEntityByIdAndProjectId(Long fileId, Long projectId) {
        return fileRepository.findByIdAndProjectId(fileId, projectId);
    }

    // 프로젝트 id로 파일 찾기
    public List<FileDto> getFilesByProjectId(Long projectId) {
        return fileRepository.findByProjectId(projectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 프로젝트 내 같은 이름의 파일 존재 여부 확인
    public boolean isFileExists(String name, Long projectId) {
        return fileRepository.findByNameAndProjectId(name, projectId).isPresent();
    }

    // 파일 id로 파일 삭제
    public void deleteFile(Long fileId) {
        fileRepository.deleteById(fileId);
    }

    // 파일 저장 (업데이트)
    public FileDto saveFile(FileEntity fileEntity) {
        FileEntity updatedFile = fileRepository.save(fileEntity);
        return toDto(updatedFile);
    }

    // 파일 내용 읽기
    public String getFileContent(Long fileId, Long projectId) {
        Optional<FileEntity> fileEntityOptional = fileRepository.findByIdAndProjectId(fileId, projectId);
        if (!fileEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("File not found with id: " + fileId + " in project with id: " + projectId);
        }
        return fileEntityOptional.get().getContent();
    }

    // FileEntity를 FileDto로 변환
    private FileDto toDto(FileEntity fileEntity) {
        FileDto fileDto = new FileDto();
        fileDto.setId(fileEntity.getId());
        fileDto.setName(fileEntity.getName());
        fileDto.setContent(fileEntity.getContent());
        fileDto.setFileType(fileEntity.getFileType());
        fileDto.setCreatedAt(fileEntity.getCreatedAt());
        fileDto.setUpdatedAt(fileEntity.getUpdatedAt());
        fileDto.setProjectId(fileEntity.getProject().getId());
        return fileDto;
    }
}
