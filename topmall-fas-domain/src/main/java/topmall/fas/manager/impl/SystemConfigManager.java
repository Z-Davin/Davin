package topmall.fas.manager.impl;

import org.springframework.stereotype.Service;
import topmall.fas.model.SystemConfig;
import topmall.fas.service.ISystemConfigService;
import topmall.framework.service.IService;
import topmall.fas.manager.ISystemConfigManager;
import topmall.framework.manager.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class SystemConfigManager extends BaseManager<SystemConfig,String> implements ISystemConfigManager{
    @Autowired
    private ISystemConfigService service;
    
    protected IService<SystemConfig,String> getService(){
        return service;
    }
    
}







