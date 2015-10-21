package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.cms.bean.ProductsFeedUpdate;
import com.voyageone.batch.cms.bean.WsdlResponseBean;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * (临时) 为更新接口提供专门服务
 * Created by Jonas on 10/20/15.
 */
@Service
public class BcbgWsdlUpdate extends BcbgWsdlBase {

    private static final String UPDATE_FLG = "update_flg = 2";

    @Override
    protected String getWhereUpdateFlg() {
        return UPDATE_FLG;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "BcbgAnalysis.Update";
    }

    protected class Context extends ContextBase {

        protected Context(ChannelConfigEnums.Channel channel) {
            super(channel);
        }

        protected void postUpdatedProduct() throws Exception {

            WsdlProductService service = new WsdlProductService(channel);

            ProductsFeedUpdate feedUpdate = new ProductsFeedUpdate();

            feedUpdate.setChannel_id(channel.getId());
            feedUpdate.setBarcode("barcode");
            feedUpdate.setCode("code");
            feedUpdate.setProduct_url_key("product_url_key");
            feedUpdate.setUpdatefields(new HashMap<>());

            WsdlResponseBean result = service.update(feedUpdate);
        }
    }
}
