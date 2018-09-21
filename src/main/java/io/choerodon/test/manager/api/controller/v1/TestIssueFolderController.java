package io.choerodon.test.manager.api.controller.v1;

import java.util.List;
import java.util.Optional;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.choerodon.test.manager.api.dto.TestIssueFolderDTO;
import io.choerodon.test.manager.app.service.TestIssueFolderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zongw.lee@gmail.com on 08/30/2018
 */
@RestController
@RequestMapping(value = "/v1/projects/{project_id}/issueFolder")
public class TestIssueFolderController {

    @Autowired
    TestIssueFolderService testIssueFolderService;

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("查询文件夹")
    @GetMapping("/query")
    public ResponseEntity query(@PathVariable(name = "project_id") Long projectId) {
        return Optional.ofNullable(testIssueFolderService.getTestIssueFolder(projectId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.testIssueFolder.query"));
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("查询version下文件夹")
    @GetMapping("/query/{versionId}")
    public ResponseEntity<List<TestIssueFolderDTO>> queryByVersion(@PathVariable(name = "project_id") Long projectId,
                                         @PathVariable(name = "versionId") Long versionId) {
        return Optional.ofNullable(testIssueFolderService.queryByVersion(projectId,versionId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.testIssueFolder.query"));

    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("删除文件夹")
    @DeleteMapping("/{folderId}")
    public ResponseEntity delete(@PathVariable(name = "project_id") Long projectId,
                                 @PathVariable(name = "folderId") Long folderId) {
        testIssueFolderService.delete(projectId,folderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("插入文件夹")
    @PostMapping
    public ResponseEntity<TestIssueFolderDTO> insert(@PathVariable(name = "project_id") Long projectId,
                                                     @RequestBody TestIssueFolderDTO testIssueFolderDTO) {
        testIssueFolderDTO.setProjectId(projectId);
        return Optional.ofNullable(testIssueFolderService.insert(testIssueFolderDTO))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new CommonException("error.testIssueFolder.insert"));
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("更新文件夹")
    @PutMapping("/update")
    public ResponseEntity<TestIssueFolderDTO> update(@PathVariable(name = "project_id") Long projectId,
                                                     @RequestBody TestIssueFolderDTO testIssueFolderDTO) {
        return Optional.ofNullable(testIssueFolderService.update(testIssueFolderDTO))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new CommonException("error.testIssueFolder.update"));
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("复制文件夹")
    @PutMapping("/copy")
    public ResponseEntity<TestIssueFolderDTO> copyFolder(@PathVariable(name = "project_id") Long projectId,
                                     @RequestParam(name = "folder_id") Long folderId,
                                     @RequestParam(name = "version_id") Long versionId) {
        return Optional.ofNullable(testIssueFolderService.copyFolder(projectId,versionId,folderId))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new CommonException("error.testIssueFolder.copy"));
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER, InitRoleCode.PROJECT_OWNER})
    @ApiOperation("移动文件夹")
    @PutMapping("/move")
    public ResponseEntity<TestIssueFolderDTO> moveFolder(@PathVariable(name = "project_id") Long projectId,
                                     @RequestBody TestIssueFolderDTO testIssueFolderDTO){
        return Optional.ofNullable(testIssueFolderService.moveFolder(projectId,testIssueFolderDTO))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElseThrow(() -> new CommonException("error.testIssueFolder.move"));
    }
}