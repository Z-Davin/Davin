package topmall.fas.service;

import topmall.fas.model.MallMinimumData;
import topmall.framework.service.IService;

public interface IMallMinimumDataService extends IService<MallMinimumData,String>{  
	
	/** 插入或者更新
	 * @param mallMinimumData
	 */
	void insertOrUpdate(MallMinimumData mallMinimumData);
	
    
}







