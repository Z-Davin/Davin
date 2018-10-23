package topmall.fas.repository;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.model.MallPrepay;
import topmall.framework.repository.IRepository;
@Mapper
public interface MallPrepayRepository extends IRepository<MallPrepay,String> {
	/** 根据唯一索引查询
	 * @param query
	 * @return
	 */
	public MallPrepay findByUnique(@Param("params") Map<String, Object> params);
	
}

             
             
             
             
             
             
             
             
             
             
             
             
             