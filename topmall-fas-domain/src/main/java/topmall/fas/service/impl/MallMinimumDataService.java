package topmall.fas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.MallMinimumData;
import topmall.fas.repository.MallMinimumDataRepository;
import topmall.fas.service.IMallMinimumDataService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;

@Service
public class MallMinimumDataService extends BaseService<MallMinimumData,String> implements  IMallMinimumDataService{
    @Autowired
    private MallMinimumDataRepository repository;
    
    protected IRepository<MallMinimumData,String> getRepository(){
        return repository;
    }

	@Override
	public void insertOrUpdate(MallMinimumData mallMinimumData) {
		repository.insertOrUpdate(mallMinimumData);
	}
    
}







