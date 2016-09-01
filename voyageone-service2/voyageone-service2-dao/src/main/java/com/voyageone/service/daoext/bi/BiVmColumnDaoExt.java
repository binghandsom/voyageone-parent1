package com.voyageone.service.daoext.bi;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BiVmColumnDaoExt {

    List<Map<String, Object>> selectList(Map<String, Object> mapParameter);

}
