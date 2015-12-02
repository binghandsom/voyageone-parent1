package com.voyageone.cms.service;

import java.io.IOException;
import java.util.List;

import com.voyageone.cms.formbean.MasterPropertyFormBean;
import com.voyageone.cms.modelbean.MasterProperty;
import com.voyageone.core.modelbean.UserSessionBean;

public interface MasterPropValueSettingService {
    
    public Object init(MasterPropertyFormBean formData) throws NumberFormatException, IOException ;
    
    public Object search(MasterPropertyFormBean formData)  throws IOException ;
    
    public boolean submit(MasterPropertyFormBean formData, UserSessionBean userSession);
    
    public Object getCategoryNav(UserSessionBean userSession);
    
    public List<MasterProperty> buildPorpertyTrees(List<MasterProperty> masterProperties);
    
    public boolean switchCagetgory(String channelId, int level, String levelValue, UserSessionBean userSession);
    
}
