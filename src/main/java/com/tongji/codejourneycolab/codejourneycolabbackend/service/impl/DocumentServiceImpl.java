package com.tongji.codejourneycolab.codejourneycolabbackend.service.impl;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.DocumentInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.Document;
import com.tongji.codejourneycolab.codejourneycolabbackend.mapper.DocumentMapper;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.DocumentService;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public Boolean isOwner(Integer userId, Integer documentId) {
        Integer ownerId = documentMapper.getDocumentOwner(documentId);
        return ownerId != null && ownerId.equals(userId);
    }


    @Override
    public Boolean hasAccess(Integer userId, Integer documentId) {
        // 检查用户是否有访问权限
        return documentMapper.checkAccess(userId, documentId);
    }


    @Override
    public void addAccess(Integer ownerId, Integer documentId, Integer targetUserId) {

        // 1. 检查当前用户是否是文档所有者
        if (!isOwner(ownerId, documentId)) {
            throw new RuntimeException("发起人不是文档的主人");
        }

        // 2. 检查目标用户是否已有权限
        if (documentMapper.checkAccess(targetUserId, documentId)) {
            throw new RuntimeException("目标用户已是协作者");
        }

        try {
            documentMapper.addAccess(targetUserId, documentId);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("未能添加协作者");
        }

    }


    @Override
    public void deleteAccess(Integer ownerId, Integer documentId, Integer targetUserId) {
        if (!isOwner(ownerId, documentId)) {
            throw new RuntimeException("发起人不是文档的主人");
        }

        if (!documentMapper.checkAccess(targetUserId, documentId)) {
            throw new RuntimeException("目标用户不是协作者");
        }

        try {
            documentMapper.addAccess(targetUserId, documentId);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("未能删除协作者");
        }
    }

    @Override
    public Document getDocument(Integer userId, Integer documentId) {
        if (!isOwner(userId, documentId) && !hasAccess(userId, documentId)) {
            throw new RuntimeException("无权查看文档");
        }

        try {
            return documentMapper.selectById(documentId);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("未能获取文档");
        }
    }

    @Override
    public DocumentInfoDto getDocumentInfo(Integer userId, Integer documentId) {
        if (!isOwner(userId, documentId) && !hasAccess(userId, documentId)) {
            throw new RuntimeException("无权查看文档信息");
        }

        try {
            return documentMapper.getDocumentInfo(documentId);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("未能获取文档信息");
        }
    }

    @Override
    public void deleteDocument(Integer ownerId, Integer documentId) {

        // 检查是否是文档所有者
        if (!isOwner(ownerId, documentId)) {
            throw new RuntimeException("你没有权限删除此文档");
        }

//        if (!documentMapper.exists(documentId)) {
//            throw new ResourceNotFoundException("文档不存在，文档 ID：" + documentId);
//        }

        // 删除文档
        documentMapper.deleteDocument(documentId);
    }

    @Override
    public DocumentInfoDto createDocument(Integer ownerId, String title) {
        // 创建默认文档对象
        Document newDocument = new Document();
        newDocument.setOwnerId(ownerId);
        newDocument.setTitle(title);

        // 插入新文档记录并获取生成的文档 ID
        documentMapper.createDocument(newDocument);

        // 返回文档信息
        return documentMapper.getDocumentInfo(newDocument.getId());
    }

    public void updateContent(Integer userId, Integer documentId, String newContent) {
//        // 检查文档是否存在
//        if (!documentMapper.exists(documentId)) {
//            throw new RuntimeException("文档不存在，文档 ID：" + documentId);
//        }

        // 检查用户是否有权限更新文档
        if (!isOwner(userId, documentId) && !hasAccess(userId, documentId)) {
            throw new RuntimeException("你没有权限修改此文档");
        }

        // 更新文档内容
        documentMapper.updateContent(documentId, newContent);
    }

    @Override
    public String getDocumentShareCode(Integer ownerId, Integer documentId) {
//        // 检查是否是文档所有者
//        if (!isOwner(ownerId, documentId)) {
//            throw new RuntimeException("你没有权限生成共享码");
//        }

        try {
            // 生成共享码
            return createSharingCode(documentId);
        } catch (Exception e) {
            throw new RuntimeException("生成共享码失败", e);
        }
    }

    public Integer getDocumentIdBySharingCode(String sharingCode) {

        try {
            // 生成共享码
            return getSharedId(sharingCode);
        } catch (Exception e) {
            throw new RuntimeException("共享码还原失败", e);
        }
    }

    @Override
    public List<DocumentInfoDto> getDocumentInfoList(Integer userId) {
        // 调用 Mapper 查询文档信息列表
        List<DocumentInfoDto> documentList = documentMapper.getDocumentInfoListByUserId(userId);

        // 如果没有文档，返回空列表
        if (documentList == null || documentList.isEmpty()) {
            return List.of(); // 返回空列表
        }

        return documentList;
    }


    private static Integer getSharedId(String sharingCode) throws Exception {
        String key = "d0_n0t_PUbL1SH_TH1S_K3Y";
// 补全密钥长度
        key = padKeyToValidLength(key);
        // Initialize Cipher for AES decryption
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] keyBytes = key.getBytes();
        SecretKeySpec secret = new SecretKeySpec(keyBytes, "AES");

        // Decode the sharingCode from Base64
        byte[] decodedBytes = Base64.getDecoder().decode(sharingCode);

        // Decrypt the bytes
        cipher.init(Cipher.DECRYPT_MODE, secret);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        // Convert the decrypted bytes to a string (the original documentId)
        String documentIdStr = new String(decryptedBytes);

        // Return as an Integer
        return Integer.parseInt(documentIdStr);
    }


    private static String createSharingCode(Integer documentId) throws Exception {
        String data = documentId.toString();
        String key = "d0_n0t_PUbL1SH_TH1S_K3Y";       //key AES-16/24/32byte DES-8byte 3DES-8/16/24byte // DO NOT PUBLISH
        key = padKeyToValidLength(key);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] keyBytes = key.getBytes();
        SecretKeySpec secret = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] bytes = cipher.doFinal(data.getBytes());
        String sharingCode = Base64.getEncoder().encodeToString(bytes);

        System.out.println(sharingCode);
        return sharingCode;
    }

    private static String padKeyToValidLength(String key) {
        // AES 密钥长度要求：16、24 或 32 字节
        int[] validLengths = {16, 24, 32};
        for (int validLength : validLengths) {
            if (key.length() <= validLength) {
                return String.format("%1$-" + validLength + "s", key).replace(' ', '0'); // 使用 '0' 填充
            }
        }
        // 如果密钥超过 32 字节，则截断
        return key.substring(0, 32);
    }
}

