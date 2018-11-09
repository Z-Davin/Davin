package topmall.fas.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.repository.MallBalanceDateDtlRepository;
import topmall.fas.service.IMallBalanceDateDtlService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;

@Service
public class MallBalanceDateDtlService extends BaseService<MallBalanceDateDtl,String> implements  IMallBalanceDateDtlService{
    @Autowired
    private MallBalanceDateDtlRepository repository;
    
    protected IRepository<MallBalanceDateDtl,String> getRepository(){
        return repository;
    }

	/**
	 * @see topmall.fas.service.IMallBalanceDateDtlService#selectContractBillNo(topmall.fas.model.MallBalanceDateDtl)
	 */
	@Override
	public ContractMainDto selectContractBillNo(MallBalanceDateDtl mallBalanceDateDtl) {
		
		Query query = Q.where("shopNo", mallBalanceDateDtl.getShopNo())
				.and("mallNo", mallBalanceDateDtl.getMallNo()).and("queryType", 1)
				.and("settleStartDate", mallBalanceDateDtl.getSettleStartDate()).and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());
		ContractMainDto contractMainDto = repository.selectContractBillNo(query.asMap());

		// 如果当前有效的合同不能适配这个结算期，就需要寻找终结合同中适配这个结算期的合同
		if (null == contractMainDto) {
			Query query2 = Q.where("shopNo", mallBalanceDateDtl.getShopNo())
					.and("mallNo", mallBalanceDateDtl.getMallNo()).and("queryType", 2)
					.and("settleStartDate", mallBalanceDateDtl.getSettleStartDate())
					.and("settleEndDate", mallBalanceDateDtl.getSettleEndDate()).and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());
			contractMainDto = repository.selectContractBillNo(query2.asMap());
		}
		return contractMainDto;
	}

	/**
	 * @see topmall.fas.service.IMallBalanceDateDtlService#getUnGeneralCostMinDate(cn.mercury.basic.query.Query)
	 */
	@Override
	public Date getUnGeneralCostMinDate(Query query) {
		return repository.getUnGeneralCostMinDate(query.asMap());
	}  
}