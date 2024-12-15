package com.tongji.codejourneycolab.codejourneycolabbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.codejourneycolab.codejourneycolabbackend.dto.DocumentInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.Document;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DocumentMapper extends BaseMapper<Document> {

    @Select("SELECT EXISTS(SELECT 1 FROM user_document WHERE user_id = #{userId} AND document_id = #{documentId})")
    boolean checkAccess(@Param("userId") Integer userId, @Param("documentId") Integer documentId);

    @Select("SELECT owner_id FROM document WHERE id = #{documentId}")
    Integer getDocumentOwner(@Param("documentId") Integer documentId);

    @Insert("INSERT INTO user_document (user_id, document_id) VALUES (#{targetUserId}, #{documentId})")
    void addAccess(@Param("targetUserId") Integer targetUserId, @Param("documentId") Integer documentId);


    @Select("SELECT id, owner_id, create_time, last_modified_time, title " +
            "FROM document WHERE id = #{docId}")
    DocumentInfoDto getDocumentInfo(@Param("docId") Integer docId);

    @Delete("DELETE FROM document WHERE id = #{documentId}")
    void deleteDocument(@Param("documentId") Integer documentId);

    @Insert("INSERT INTO document (owner_id, title, create_time, last_modified_time) " +
            "VALUES (#{ownerId}, #{title}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createDocument(Document document);

    @Update("UPDATE document SET code = #{content}, last_modified_time = NOW() WHERE id = #{documentId}")
    void updateContent(@Param("documentId") Integer documentId, @Param("content") String content);

    @Select("SELECT d.id, d.owner_id, d.create_time, d.last_modified_time, d.title " +
            "FROM document d " +
            "JOIN user_document ud ON d.id = ud.document_id " +
            "WHERE ud.user_id = #{userId} " +
            "ORDER BY d.last_modified_time DESC")
    List<DocumentInfoDto> getDocumentInfoListByUserId(@Param("userId") Integer userId);

}
