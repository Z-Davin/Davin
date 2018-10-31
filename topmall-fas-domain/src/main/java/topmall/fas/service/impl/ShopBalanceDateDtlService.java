package topmall.fas.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.dto.ContractMainDto;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.repository.ShopBalanceDateDtlRepository;
import topmall.fas.service.IShopBalanceDateDtlService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;

/**
 * dai.xw
 */
@Service
public class ShopBalanceDateDtlService extends BaseService<ShopBalanceDateDtl, String> implements
		IShopBalanceDateDtlService {
	@Autowired
	private ShopBalanceDateDtlRepository repository;

	protected IRepository<ShopBalanceDateDtl, String> getRepository() {
		return repository;
	}

	/**
	 * @see topmall.fas.service.IShopBalanceDateDtlService#selectContractBillNo(topmall.fas.model.ShopBalanceDateDtl)
	 */
	@Override
	public ContractMainDto selectContractBillNo(ShopBalanceDateDtl shopBalanceDateDtl) {

		Query query = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
				.and("counterNo", shopBalanceDateDtl.getCounterNo()).and("queryType", 1)
				.and("settleStartDate", shopBalanceDateDtl.getSettleStartDate());
		ContractMainDto contractMainDto = repository.selectContractBillNo(query.asMap());

		// 如果当前有效的合同不能适配这个结算期，就需要寻找终结合同中适配这个结算期的合同
		if (null == contractMainDto) {
			Query query2 = Q.where("shopNo", shopBalanceDateDtl.getShopNo())
					.and("counterNo", shopBalanceDateDtl.getCounterNo()).and("queryType", 2)
					.and("settleStartDate", shopBalanceDateDtl.getSettleStartDate())
					.and("settleEndDate", shopBalanceDateDtl.getSettleEndDate());
			contractMainDto = repository.selectContractBillNo(query2.asMap());
		}
		return contractMainDto;
	}

	/**
	 * @see topmall.fas.service.IShopBalanceDateDtlService#findByUnique(cn.mercury.basic.query.Query)
	 */
	@Override
	public ShopBalanceDateDtl findByUnique(Query query) {
		return repository.findByUnique(query.asMap());
	}

	@Override
	public String getUnGeneralCostMinMonth(Query query) {
		return repository.getUnGeneralCostMinMonth(query.asMap());
	}

	@Override
	public Date getUnGeneralCostMaxDate(Query query) {
		return repository.getUnGeneralCostMaxDate(query.asMap());
	}
	

}
