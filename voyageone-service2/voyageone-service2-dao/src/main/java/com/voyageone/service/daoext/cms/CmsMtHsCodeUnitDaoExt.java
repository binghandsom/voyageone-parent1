package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsMtHsCodeUnitBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtHscodeSaleUnitModel;
import org.springframework.stereotype.Repository;

/**
 * Created by Charis on 2017/6/2.
 */

@Repository
public class CmsMtHsCodeUnitDaoExt extends ServiceBaseDao {

    public int insertHsCodeUnit(CmsMtHsCodeUnitBean bean) {
        return insert("insertHsCodeUnit", bean);
    }

    public CmsMtHsCodeUnitBean getHscodeUnit(String hscode) {
        return selectOne("selectUnitByHsCode", parameters("hscode", hscode));
    }

    public CmsMtHscodeSaleUnitModel getHscodeSaleUnit(String unitName) {
        return selectOne("selectSaleUnit", parameters("unitName", unitName));
    }

}
