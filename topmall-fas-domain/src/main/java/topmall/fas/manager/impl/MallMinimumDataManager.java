package topmall.fas.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.manager.IMallMinimumDataManager;
import topmall.fas.model.MallMinimumData;
import topmall.fas.service.IMallMinimumDataService;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;


@Service
public class MallMinimumDataManager extends BaseManager<MallMinimumData,String> implements IMallMinimumDataManager{
    @Autowired
    private IMallMinimumDataService service;
    
    protected IService<MallMinimumData,String> getService(){
        return service;
    }
    
}







