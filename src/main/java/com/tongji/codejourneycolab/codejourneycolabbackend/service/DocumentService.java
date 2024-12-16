package com.tongji.codejourneycolab.codejourneycolabbackend.service;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.DocumentInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.Document;

import java.util.List;

public interface DocumentService {

    /// TODO: remove some of them, and make private

    Boolean isOwner(Integer userId, Integer documentId);

    Boolean hasAccess(Integer userId, Integer documentId);

    void addAccess(Integer ownerId, Integer documentId, Integer targetUserId);

    void deleteAccess(Integer ownerId, Integer documentId, Integer targetUserId);

    Document getDocument(Integer userId, Integer documentId);

    DocumentInfoDto getDocumentInfo(Integer userId , Integer documentId);

    void updateContent(Integer userId ,String documentCode, String new_content);

    void deleteDocument(Integer ownerId, Integer documentId);

    DocumentInfoDto createDocument(Integer ownerId,String title);

    String getDocumentShareCode(Integer ownerId, Integer documentId);

    Integer getDocumentIdBySharingCode(String invitationCode);

    List<DocumentInfoDto> getDocumentInfoList(Integer userId);

    void openShareDBService(String code);
}
