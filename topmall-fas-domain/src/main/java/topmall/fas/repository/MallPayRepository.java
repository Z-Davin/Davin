package topmall.fas.repository;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import topmall.fas.model.MallPay;
import topmall.framework.repository.IRepository;
@Mapper
public interface MallPayRepository extends IRepository<MallPay,String> {

	List<MallPay> selectMallPayList(@Param("params") Map<String, Object> map);
	
	/** 解除结算单的关系
	 * @param id
	 */
	public void updateToUnsettled(String id);
	
	/** 根据收银方式汇总数据
	 * @param map
	 * @return
	 */
	public List<MallPay> getMallPayGroupPaidWay(@Param("params") Map<String, Object> map);
	/** 合计
	 * @param q
	 * @return
	 */
	public List<MallPay> queryConditionSum(@Param("params") Map<String, Object> map);
}

             
             
             
             
             
             
             
             
             
             
             
             
             