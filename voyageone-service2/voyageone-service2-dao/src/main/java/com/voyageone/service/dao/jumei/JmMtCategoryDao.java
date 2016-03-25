package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface JmMtCategoryDao {
    public List<JmMtCategoryModel>  selectList();
    public  JmMtCategoryModel select(long id);
    public int insert(JmMtCategoryModel entity);
    public  int update(JmMtCategoryModel entity);

    public  int delete(long id);
    }
