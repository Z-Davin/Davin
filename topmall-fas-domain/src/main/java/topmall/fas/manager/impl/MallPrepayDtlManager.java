package topmall.fas.manager.impl;

import org.springframework.stereotype.Service;
import topmall.fas.model.MallPrepayDtl;
import topmall.fas.service.IMallPrepayDtlService;

import topmall.framework.service.IService;
import topmall.fas.manager.IMallPrepayDtlManager;
import topmall.framework.manager.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class MallPrepayDtlManager extends BaseManager<MallPrepayDtl,String> implements IMallPrepayDtlManager{
    @Autowired
    private IMallPrepayDtlService service;
    
    protected IService<MallPrepayDtl,String> getService(){
        return service;
    }
    
}







