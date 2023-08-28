package com.example.nbs.domain.draft;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DraftFileRepository {

    void insert(long noticeLastId, String filename, String dt);

    void delete(long id);

}
