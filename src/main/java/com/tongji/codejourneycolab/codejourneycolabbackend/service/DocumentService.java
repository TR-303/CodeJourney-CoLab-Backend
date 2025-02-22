package com.tongji.codejourneycolab.codejourneycolabbackend.service;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.DocumentInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.dto.FileInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.dto.UserInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.User;

import java.util.List;

public interface DocumentService {

    /// TODO: remove some of them, and make private

    Boolean isOwner(Integer userId, Integer documentId);

    Boolean isCollaborator(Integer userId, Integer documentId);

    Integer joinCollaborationByCode(Integer userId, String colabCode);

    String joinCollaborationById(Integer userId, Integer documentId);

    void createSharedbService(Integer userId,String colabCode,String content);

    String getContent(Integer userId, Integer documentId);

    DocumentInfoDto getDocumentInfo(Integer userId , Integer documentId);

    void updateContent(Integer userId ,String documentCode, String new_content);

    void deleteDocument(Integer ownerId, Integer documentId);

    DocumentInfoDto createDocument(Integer ownerId,String title);

    String getDocumentShareCode(Integer ownerId, Integer documentId);

    List<DocumentInfoDto> getDocumentInfoList(Integer userId);

    List<User> getUsersByDocumentId(Integer documentId);

    FileInfoDto getFileInfo(Integer id, Integer documentId);
}
