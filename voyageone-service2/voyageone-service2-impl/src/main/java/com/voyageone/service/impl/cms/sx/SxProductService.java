package com.voyageone.service.impl.cms.sx;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.CmsBtSizeMapDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.ims.ImsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtSizeMapModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.service.model.ims.ImsBtProductModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上新相关共通逻辑
 *
 * @author morse.lu 16/04/19
 */
@Service
public class SxProductService extends BaseService {

    @Autowired
    private CmsBtSxWorkloadDao sxWorkloadDao;

    @Autowired
    private ImsBtProductDao imsBtProductDao;

    @Autowired
    private CmsBtSizeMapDao cmsBtSizeMapDao;

    private enum SkuSort {
        DIGIT("digit", 1), // 纯数字系列
        DIGIT_UNITS("digitUnits", 2), // 纯数字系列(cm)
        XXX("XXX", 3), // XXX
        XXS("XXS", 4), // XXS
        XS("XS", 5), // XS
        XS_S("XS/S", 6), // XS/S
        XSS("XSS", 7), // XSS
        S("S", 8), // S
        S_M("S/M", 9), // S/M
        M("M", 10), // M

        M_L("M/L", 11), // M/L
        L("L", 12), // L
        XL("XL", 13), // XL
        XXL("XXL", 14), // XXL
        N_S("N/S", 15), // N/S
        O_S("O/S", 16), // O/S
        ONE_SIZE("OneSize", 17), // OneSize

        OTHER("Other", 18), // 以外
        ;

        private final String size;
        private final int sort;

        private SkuSort(String size, int sort) {
            this.size = size;
            this.sort = sort;
        }

        private String getSize() {
            return this.size;
        }

        private int getSort() {
            return this.sort;
        }
    }

    /**
     * sku根据size排序<br>
     * 已知size：<br>
     * 纯数字系列，纯数字系列(cm)，服饰系列(XL,L,M,S等)，其他(N/S,O/S,OneSize)
     *
     * @param skuSourceList 排序对象
     */
    public void sortSkuInfo(List<CmsBtProductModel_Sku> skuSourceList) {

        // Map<size, sort> 为了将来可能会从DB取得设定，先做成Map
        Map<String, Integer> mapSort = new HashMap<>();
        for (SkuSort s : SkuSort.values()) {
            mapSort.put(s.getSize(), Integer.valueOf(s.getSort()));
        }

        skuSourceList.sort((a, b) -> {
            String sizeA = a.getSize();
            String sizeB = b.getSize();

            Integer sortA = getSizeSort(sizeA, mapSort);
            Integer sortB = getSizeSort(sizeB, mapSort);

            if (mapSort.get(SkuSort.DIGIT.getSize()).intValue() == sortA.intValue() && mapSort.get(SkuSort.DIGIT.getSize()).intValue() == sortB.intValue()) {
                // 纯数字系列
                return Float.valueOf(sizeA).compareTo(Float.valueOf(sizeB));
            }

            if (mapSort.get(SkuSort.DIGIT_UNITS.getSize()).intValue() == sortA.intValue() && mapSort.get(SkuSort.DIGIT_UNITS.getSize()).intValue() == sortB.intValue()) {
                // 纯数字系列(cm)
                return Float.valueOf(sizeA.substring(0, sizeA.length() - 2)).compareTo(
                        Float.valueOf(sizeB.substring(0, sizeB.length() - 2)));
            }

            return sortA.compareTo(sortB);
        });
    }

    /**
     * 取得size对应设定的sort
     *
     * @param size    size
     * @param mapSort Map<size, sort>
     * @return sort
     */
    private Integer getSizeSort(String size, Map<String, Integer> mapSort) {
        Integer sort;
        if (StringUtils.isNumeric(size)) {
            // 纯数字系列
            sort = mapSort.get(SkuSort.DIGIT.getSize());
        } else if (size.length() > 2 && size.lastIndexOf("cm") == size.length() - 2 && StringUtils.isNumeric(size.substring(0, size.length() - 2))) {
            // 纯数字系列(cm)
            // 最后两位是cm，并且去除cm后剩下的是数字
            sort = mapSort.get(SkuSort.DIGIT_UNITS.getSize());
        } else {
            sort = mapSort.get(size);
            if (sort == null) {
                sort = mapSort.get(SkuSort.OTHER.getSize());
            }
        }
        return sort;
    }

    /**
     * 回写cms_bt_sx_workload表
     *
     * @param sxWorkloadModel bean
     * @param publishStatus   status
     * @param modifier 更新者
     */
    public int updateSxWorkload(CmsBtSxWorkloadModel sxWorkloadModel, int publishStatus, String modifier) {
        CmsBtSxWorkloadModel upModel = new CmsBtSxWorkloadModel();
        BeanUtils.copyProperties(sxWorkloadModel, upModel);
        upModel.setPublishStatus(publishStatus);
        upModel.setModifier(modifier);
        return sxWorkloadDao.updateSxWorkloadModelWithModifier(upModel);
    }

    /**
     * 回写ims_bt_product表
     *
     * @param sxData 上新数据
     * @param updateType s:sku级别, p:product级别
     * @param modifier 更新者
     */
    public void updateImsBtProduct(SxData sxData, String updateType, String modifier) {
        // voyageone_ims.ims_bt_product表的更新, 用来给wms更新库存时候用的
        List<CmsBtProductModel> sxProductList = sxData.getProductList();
        for (CmsBtProductModel sxProduct : sxProductList) {
            String code = sxProduct.getFields().getCode();

            ImsBtProductModel imsBtProductModel = imsBtProductDao.selectImsBtProductByChannelCartCode(
                    sxData.getChannelId(),
                    sxData.getCartId(),
                    code);
            if (imsBtProductModel == null) {
                // 没找到就插入
                imsBtProductModel = new ImsBtProductModel();
                imsBtProductModel.setChannelId(sxData.getChannelId());
                imsBtProductModel.setCartId(sxData.getCartId());
                imsBtProductModel.setCode(code);
                imsBtProductModel.setNumIid(sxData.getPlatform().getNumIId());
                imsBtProductModel.setQuantityUpdateType(updateType);

                imsBtProductDao.insertImsBtProduct(imsBtProductModel, modifier);
            } else {
                // 找到了, 更新
                imsBtProductModel.setNumIid(sxData.getPlatform().getNumIId());
                imsBtProductModel.setQuantityUpdateType(updateType);

                imsBtProductDao.updateImsBtProductBySeq(imsBtProductModel, modifier);
            }
        }
    }

    /**
     * 尺码转换
     *
     * @param sizeMapGroupId 尺码对照表id
     * @param originalSize   转换前size
     * @return 转后后size
     */
    public String changeSize(int sizeMapGroupId, String originalSize) {
        // cms_bt_size_map
        CmsBtSizeMapModel result = cmsBtSizeMapDao.selectSizeMap(sizeMapGroupId, originalSize);
        if (result != null) {
            return result.getAdjustSize();
        }

        return null;
    }

}
