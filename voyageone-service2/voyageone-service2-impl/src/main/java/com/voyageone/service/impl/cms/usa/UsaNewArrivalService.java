package com.voyageone.service.impl.cms.usa;

import com.voyageone.service.impl.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by james on 2017/7/20.
 */
@Service
public class UsaNewArrivalService extends BaseService {
    final String defaultCategory = "New Arrivals";
    public enum NewArrivalMapping {
        Mens(Arrays.asList("Shoes"), Arrays.asList("Men"), "Men's>Men's New Arrivals"),
        Womens(Arrays.asList("Shoes"), Arrays.asList("Women"), "Women's>Women's New Arrivals"),
        Kids(Arrays.asList("Shoes"), Arrays.asList("Big Kids", "Preschool", "Toddler"), "Kids'>Kids' New Arrivals"),
        ApparelMens(Arrays.asList("Apparel"), Arrays.asList("Men"), "Apparel>Men's>Men's New Arrivals"),
        ApparelWomens(Arrays.asList("Apparel"), Arrays.asList("Women"), "Apparel>Women's>Women's New Arrivals"),
        Gear(Arrays.asList("Watches", "Bags", "Sunglasses", "Hats", "Knives", "Accessories"), Arrays.asList("One Size","OneSize"), "Gear>Gear New Arrivals");

        List<String> productTypes;
        List<String> sizeTypes;
        String category;
        NewArrivalMapping(List<String> productTypes, List<String> sizeTypes, String category) {
            this.productTypes = productTypes;
            this.sizeTypes = sizeTypes;
            this.category = category;
        }

        public List<String> getProductTypes() {
            return productTypes;
        }

        public void setProductTypes(List<String> productTypes) {
            this.productTypes = productTypes;
        }

        public List<String> getSizeTypes() {
            return sizeTypes;
        }

        public void setSizeTypes(List<String> sizeTypes) {
            this.sizeTypes = sizeTypes;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public List<String> getNewArrivalCategory(String productType, String sizeType){
        List<String> newArrivalCategorys = new ArrayList<>();
        newArrivalCategorys.add(defaultCategory);
        for(NewArrivalMapping newArrivalMapping : NewArrivalMapping.values()){
            if(newArrivalMapping.getProductTypes().stream().anyMatch(s->s.equalsIgnoreCase(productType))){
                if(newArrivalMapping.getSizeTypes().stream().anyMatch(s->s.equalsIgnoreCase(sizeType))){
                    newArrivalCategorys.add(newArrivalMapping.getCategory());
                }
            }
        }
        return newArrivalCategorys;
    }
}
