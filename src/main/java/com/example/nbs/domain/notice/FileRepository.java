package com.example.nbs.domain.notice;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileRepository {

    void insert(long noticeLastId, String filename, String dt);

    void delete(long id);

}
