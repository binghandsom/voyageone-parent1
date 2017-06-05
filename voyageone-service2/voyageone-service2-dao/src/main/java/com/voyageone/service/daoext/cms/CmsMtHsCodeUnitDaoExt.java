package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsMtHsCodeUnitBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Charis on 2017/6/2.
 */

@Repository
public class CmsMtHsCodeUnitDaoExt extends ServiceBaseDao {

    public CmsMtHsCodeUnitBean getHscodeUnit(String hscode) {
        return selectOne("selectUnitByHsCode", parameters("hscode", hscode));
    }


    public Map<String, String> getHscodeSaleUnit(String unitName) {
        return selectOne("selectSaleUnit", parameters("unitName", unitName));
    }

}
