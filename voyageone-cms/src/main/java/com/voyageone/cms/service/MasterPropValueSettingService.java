package com.voyageone.cms.service;

import com.voyageone.cms.formbean.MasterPropertyFormBean;

public interface MasterPropValueSettingService {
    
    public Object init(MasterPropertyFormBean formData);
    
    public Object search(MasterPropertyFormBean formData);
    
    public boolean submit(MasterPropertyFormBean formData);
    
    
}
