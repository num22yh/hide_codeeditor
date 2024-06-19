package org.example.backend.codeeditor.controller;

import org.example.backend.codeeditor.dto.FileDto;
import org.example.backend.codeeditor.entity.FileEntity;
import org.example.backend.codeeditor.entity.ProjectEntity;
import org.example.backend.codeeditor.exception.BadRequestException;
import org.example.backend.codeeditor.exception.ConflictException;
import org.example.backend.codeeditor.exception.ResourceNotFoundException;
import org.example.backend.codeeditor.service.FileService;
import org.example.backend.codeeditor.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/projects/{projectId}/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ProjectService projectService;

    // 파일 생성
    @PostMapping
    public ResponseEntity<FileDto> createFile(@PathVariable("projectId") Long projectId, @RequestParam("file_name") String name, @RequestParam("file_type") String fileType) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("File name cannot be null or empty"); // 파일 이름 없음
        }
        if (fileType == null || fileType.trim().isEmpty()) {
            throw new BadRequestException("File type cannot be null or empty"); //파일 확장자(타입) 없음
        }

        Optional<ProjectEntity> projectOptional = projectService.getProjectEntityById(projectId);
        if (projectOptional.isPresent()) {
            if (fileService.isFileExists(name, projectId)) {
                throw new ConflictException("A file with the same name already exists."); // 같은 이름의 파일 존재
            }
            FileDto file = fileService.createFile(name, "", fileType, projectOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(file);
        } else {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); //해당하는 프로젝트 id 없음
        }
    }

    // 파일 ID와 프로젝트 ID로 조회
    @GetMapping("/{fileId}")
    public ResponseEntity<FileDto> getFileByIdAndProjectId(@PathVariable("projectId") Long projectId, @PathVariable("fileId") Long fileId) {
        if (!projectService.getProjectEntityById(projectId).isPresent()) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); // 해당하는 프로젝트 id 없음
        }

        Optional<FileDto> file = fileService.getFileByIdAndProjectId(fileId, projectId);
        if (!file.isPresent()) {
            throw new ResourceNotFoundException("File not found with id: " + fileId + " in project with id: " + projectId); // 프로젝트 내에 해당하는 파일 id 없음
        }

        return ResponseEntity.ok(file.get());
    }

    // 프로젝트 ID로 파일 목록 조회
    @GetMapping
    public ResponseEntity<List<FileDto>> getFilesByProjectId(@PathVariable("projectId") Long projectId) {
        List<FileDto> files = fileService.getFilesByProjectId(projectId);
        return ResponseEntity.ok(files);
    }

    // 파일 ID와 프로젝트 ID로 파일 업데이트
    @PutMapping("/{fileId}")
    public ResponseEntity<FileDto> updateFile(@PathVariable("projectId") Long projectId, @PathVariable("fileId") Long fileId, @RequestBody FileDto fileDto) {
        if (!projectService.getProjectEntityById(projectId).isPresent()) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); //프로젝트 id 일치하지 않을 시 예외처리
        }

        Optional<FileEntity> fileEntityOptional = fileService.getFileEntityByIdAndProjectId(fileId, projectId);
        if (!fileEntityOptional.isPresent()) {
            throw new ResourceNotFoundException("File not found with id: " + fileId + " in project with id: " + projectId); //해당 프로젝트 내에 파일 id 없을 시 예외처리
        }

        FileEntity fileEntity = fileEntityOptional.get();

        // 파일 이름이 업데이트되려는 경우 같은 프로젝트 내에서 중복 이름 확인
        if (fileDto.getName() != null && !fileDto.getName().trim().isEmpty() && !fileEntity.getName().equals(fileDto.getName())) {
            if (fileService.isFileExists(fileDto.getName(), projectId)) {
                throw new ConflictException("A file with the same name already exists."); // 같은 이름의 파일이 이미 존재할 경우 예외 처리
            }
            fileEntity.setName(fileDto.getName());
        }

        // 파일 내용 업데이트
        if (fileDto.getContent() != null) {
            fileEntity.setContent(fileDto.getContent());
        }

        // 파일 확장자 업데이트
        if (fileDto.getFileType() != null && !fileDto.getFileType().trim().isEmpty()) {
            fileEntity.setFileType(fileDto.getFileType());
        }

        FileDto updatedFile = fileService.saveFile(fileEntity);
        return ResponseEntity.ok(updatedFile);
    }

    // 파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable("projectId") Long projectId, @PathVariable("fileId") Long fileId) {
        if (!projectService.getProjectEntityById(projectId).isPresent()) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId); //프로젝트 id 존재 안함
        }

        Optional<FileDto> file = fileService.getFileByIdAndProjectId(fileId, projectId);
        if (!file.isPresent()) {
            throw new ResourceNotFoundException("File not found with id: " + fileId + " in project with id: " + projectId); // 프로젝트 내에 해당하는 파일id 없음
        }

        fileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
