package topmall.fas.repository;

import org.apache.ibatis.annotations.Mapper;
import topmall.fas.model.MallMinimumData;
import topmall.framework.repository.IRepository;
@Mapper
public interface MallMinimumDataRepository extends IRepository<MallMinimumData,String> {
	
	/** 
	 * 插入或者更新
	 * @param mallMinimumData
	 */
	public void insertOrUpdate(MallMinimumData mallMinimumData);
	
}

             
             
             
             
             
             
             
             
             
             
             
             
             