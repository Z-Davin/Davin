package topmall.fas.repository;
import topmall.fas.model.SupplierAccount;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.framework.repository.IRepository;
@Mapper
public interface SupplierAccountRepository extends IRepository<SupplierAccount,String> {
	
	/**
	 * 更新单据状态
	 * @param params
	 */
	public Integer updateStatus(@Param("params") Map<String, Object> params);
	
}

             
             
             
             
             
             
             
             
             
             
             
             
             