package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SequenceDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.batch.synship.sql";
    }

    /**
     * 取得顺番
     *
     * @param seq_name
     * @return String
     */
    public String getNextVal(String seq_name) {
        Map<String, Object> params = new HashMap<>();

        params.put("seq_name", seq_name);

        return updateTemplate.selectOne("synShip_getNextVal", params);
    }


}
