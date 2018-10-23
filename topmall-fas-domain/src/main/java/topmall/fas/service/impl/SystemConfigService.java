package topmall.fas.service.impl;

import org.springframework.stereotype.Service;
import topmall.fas.model.SystemConfig;
import topmall.fas.repository.SystemConfigRepository;

import topmall.fas.service.ISystemConfigService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SystemConfigService extends BaseService<SystemConfig,String> implements  ISystemConfigService{
    @Autowired
    private SystemConfigRepository repository;
    
    protected IRepository<SystemConfig,String> getRepository(){
        return repository;
    }
    
}







