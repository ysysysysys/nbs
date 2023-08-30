package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileRepository {

    void insert(long noticeLastId, String filename, String dt);

    void delete(long id);

}
