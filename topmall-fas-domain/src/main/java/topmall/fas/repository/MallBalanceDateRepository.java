package topmall.fas.repository;
import java.util.Map;

import topmall.fas.model.MallBalanceDate;
import org.apache.ibatis.annotations.Mapper;
import topmall.framework.repository.IRepository;
@Mapper
public interface MallBalanceDateRepository extends IRepository<MallBalanceDate,String> {
	
	MallBalanceDate findByUnique(Map<String, Object> asMap);
}

             
             
             
             
             
             
             
             
             
             
             
             
             