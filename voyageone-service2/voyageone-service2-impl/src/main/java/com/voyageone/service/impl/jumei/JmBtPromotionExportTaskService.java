package com.voyageone.service.impl.jumei;
import com.voyageone.common.help.DateHelp;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.impl.Excel.*;
import com.voyageone.service.impl.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class JmBtPromotionExportTaskService {
    @Autowired
    JmBtPromotionExportTaskDao dao;

    public JmBtPromotionExportTaskModel get(int id) {
        return dao.get(id);
    }

    public List<JmBtPromotionExportTaskModel> getList() {
        return dao.getList();
    }

    public int update(JmBtPromotionExportTaskModel entity) {
        return dao.update(entity);
    }

    public int create(JmBtPromotionExportTaskModel entity) {
        return dao.create(entity);
    }

    public  void  Export(int JmBtPromotionExportTaskId) throws FileNotFoundException {
        JmBtPromotionExportTaskModel model= dao.get(1);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            //  LoadRow(map);
        }

        EnumJMProductImportColumn[] listEnumJMProductImportColumn= EnumJMProductImportColumn.values();
        ExportExcelInfo<Map<String, Object>> info = new ExportExcelInfo(list);
        info.setFileName("Product");
        info.addExcelColumn("用户名称", "Name");
        info.addExcelColumn("用户代码", "Code");
        info.addExcelColumn("邮箱", "Mail");
        info.addExcelColumn("手机", "Mobile", EnumExcelColumnType.ColumnType_String);
        info.addExcelColumn("性别", "Sex", (Object value, Map<String, Object> map, Integer index) -> {
            if (value.equals(true)) return "男";
            return "女";
        });
        info.addExcelColumn("是否系统内置", "IsSystem", (Object value, Map<String, Object> map, Integer index) -> {
            if (value.equals(true)) return "是";
            return "否";
        });
        info.addExcelColumn("创建人", "CreatePerson");
        info.addExcelColumn("创建日期", "CreateDate");
        info.addExcelColumn("登陆账号", "Account");
        // return null;
        // HashMap<String, Object> hm = ConvertHelp.ToObjectFromJson(source, HashMap.class);
        // ExportExcelInfo<Map<String, Object>> info = service.exportExcel(hm);
        //  ExportResponseExcelUtil.exportExcel(info, response);
        try {
            String fileName = "/usr/JMExport"+"/Product" + DateHelp.DateToString(new Date(),"yyyyMMddHHmmss") + ".xls";
            ExportFileExcelUtil.exportExcel(info, fileName);
        } catch (ExcelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

