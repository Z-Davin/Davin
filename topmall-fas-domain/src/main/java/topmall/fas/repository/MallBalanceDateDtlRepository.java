package topmall.fas.repository;

import java.util.Date;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.framework.repository.IRepository;

@Mapper
public interface MallBalanceDateDtlRepository extends IRepository<MallBalanceDateDtl, String> {

	/**
	 * 根据卖场编码和物业编码获取有效合同编号
	 * @param params 卖场编码和物业编码
	 * @return 物业合同的主要数据
	 */
	ContractMainDto selectContractBillNo(@Param("params") Map<String, Object> params);
	
	/**
	 * 查询没有生成费用最小的结算结束时间	
	 * @param params 卖场编码和物业编码
	 * @return 最小的结束时间
	 */
	Date getUnGeneralCostMinDate(@Param("params") Map<String, Object> params);
}
