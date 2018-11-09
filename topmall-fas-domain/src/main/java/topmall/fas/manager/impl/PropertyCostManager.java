package topmall.fas.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IPropertyCostManager;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.model.PropertyCost;
import topmall.fas.service.IPropertyCostService;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;


@Service
public class PropertyCostManager extends BaseManager<PropertyCost,String> implements IPropertyCostManager{
    @Autowired
    private IPropertyCostService service;
    
    protected IService<PropertyCost,String> getService(){
        return service;
    }
    
    @Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public Integer insert(PropertyCost entry) throws ManagerException {
		if (entry.getStatus() == null) {
			entry.setStatus(StatusEnums.INEFFECTIVE.getStatus());
		}
		return super.insert(entry);
	}

	/**
	 * @see topmall.fas.manager.IPropertyCostManager#selectGroupNum(topmall.fas.model.MallBalanceDateDtl)
	 */
	@Override
	public List<PropertyCost> selectGroupNum(MallBalanceDateDtl mallBalanceDateDtl) {
		Query query = Q.where("shopNo", mallBalanceDateDtl.getShopNo())
				.and("mallNo", mallBalanceDateDtl.getMallNo())
				.and("settleMonth", mallBalanceDateDtl.getSettleMonth())
				.and("settleStartDate", mallBalanceDateDtl.getSettleStartDate())
				.and("settleEndDate", mallBalanceDateDtl.getSettleEndDate())
				.and("status", StatusEnums.EFFECTIVE.getStatus());
		return service.selectGroupNum(query);
	}

	/**
	 * @see topmall.fas.manager.IPropertyCostManager#confirm(java.lang.String)
	 */
	@Override
	public PropertyCost confirm(String id) {
		PropertyCost propertyCost = service.findByPrimaryKey(id);
		propertyCost.setAuditor(Authorization.getUser().getName());
		propertyCost.setAuditTime(new Date());
		propertyCost.setStatus(StatusEnums.EFFECTIVE.getStatus());
		update(propertyCost);
		return propertyCost;
	}
}







