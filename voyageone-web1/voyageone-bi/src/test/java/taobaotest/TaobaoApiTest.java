package taobaotest;

import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;

/**
 * Created by Kylin on 2015/7/15.
 */
public class TaobaoApiTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	getOnsale();
    }

    private static ItemsOnsaleGetResponse getOnsale(){
        ItemsOnsaleGetRequest req=new ItemsOnsaleGetRequest();
        req.setFields("num_iid,title,price");

        return null;
    }
}
