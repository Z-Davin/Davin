package topmall.fas.repository;
import java.util.Map;

import topmall.fas.model.ShopBalanceDate;
import org.apache.ibatis.annotations.Mapper;
import topmall.framework.repository.IRepository;
@Mapper
public interface ShopBalanceDateRepository extends IRepository<ShopBalanceDate,String> {

	ShopBalanceDate findByUnique(Map<String, Object> asMap);
}

             
             
             
             
             
             
             
             
             
             
             
             
             