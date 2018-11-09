package topmall.fas.repository;
import topmall.fas.model.MallPrepayDtl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.framework.repository.IRepository;
@Mapper
public interface MallPrepayDtlRepository extends IRepository<MallPrepayDtl,String> {
	
	public List<MallPrepayDtl> queryByMallBalance(@Param("params") Map<String, Object> params);
	
	
}

             
             
             
             
             
             
             
             
             
             
             
             
             