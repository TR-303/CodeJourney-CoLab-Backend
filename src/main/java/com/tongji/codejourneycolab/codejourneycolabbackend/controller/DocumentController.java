package com.tongji.codejourneycolab.codejourneycolabbackend.controller;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.DocumentContentDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.dto.DocumentInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.Document;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/document")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    /**
     * 获取编辑过的文件列表
     */
    @GetMapping("/getfilelist")
    public ResponseEntity<List<DocumentInfoDto>> getFileList(@RequestAttribute Integer id) {
        List<DocumentInfoDto> documentList = documentService.getDocumentInfoList(id);
        return ResponseEntity.ok(documentList);
    }

    /**
     * 获取文件内容
     */
    @GetMapping("/getcontent")
    public ResponseEntity<String> getContent(@RequestAttribute Integer id,
                                             @RequestParam Integer documentId) {
        Document document = documentService.getDocument(id, documentId);
        return ResponseEntity.ok(document.getCode());
    }

    /**
     * 保存文件内容
     */
    @PostMapping("/savecontent")
    public ResponseEntity<Map<String, String>> saveContent(@RequestAttribute Integer id, @RequestBody DocumentContentDto documentContentDto) {
        Map<String, String> response = new HashMap<>();

        String documentCode = documentContentDto.getDocumentCode();
        String code = documentContentDto.getCode();

        try {
            if (code.length() > 50000) { // 假设最大长度限制
                response.put("status", "invalid length");
            } else {
                documentService.updateContent(id, documentCode, code);
                response.put("status", "success");
            }
        } catch (Exception e) {
            response.put("status", "fail");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 创建新的文件
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createDocument(@RequestAttribute Integer id,
                                                              @RequestParam String title) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (title.isEmpty() || title.length() > 50) {
                response.put("status", "invalid title length");
            } else {
                DocumentInfoDto newDocument = documentService.createDocument(id, title);
                response.put("status", "success");
                response.put("documentID", newDocument.getId());
            }
        } catch (Exception e) {
            response.put("status", "fail");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 删除现有文件
     */
    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteDocument(@RequestAttribute Integer id,
                                                              @RequestParam Integer documentId) {
        Map<String, String> response = new HashMap<>();
        try {
            documentService.deleteDocument(id, documentId);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "fail");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 获取某个文件的邀请码
     */
    @GetMapping("/generatecode")
    public ResponseEntity<Map<String, String>> generateCode(@RequestAttribute Integer id,
                                                            @RequestParam Integer documentId) {
        Map<String, String> response = new HashMap<>();
        try {
            String invitationCode = documentService.getDocumentShareCode(id, documentId);
            response.put("invitationCode", invitationCode);
        } catch (Exception e) {
            response.put("status", "fail");
        }

        return ResponseEntity.ok(response);
    }



//    /// INFO:  Only For Debug Use, Remove In future
//    @GetMapping("/revertcoding")
//    public ResponseEntity<Map<String, String>> connectService( @RequestParam String invitationCode) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            Integer documentId = documentService.getDocumentIdBySharingCode(invitationCode);
//            response.put("sharedURL", documentId.toString());
//        } catch (Exception e) {
//            response.put("status", "fail");
//        }
//
//        return ResponseEntity.ok(response);
//    }

    /// TODO: insert into relation table
    @PostMapping("/createSharedbService")
    public ResponseEntity<String> connectService(@RequestAttribute Integer id, @RequestParam String invitationCode) {
        try {
            documentService.openShareDBService(invitationCode);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
