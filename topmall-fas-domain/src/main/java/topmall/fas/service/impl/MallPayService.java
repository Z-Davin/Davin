package topmall.fas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import topmall.fas.model.MallPay;
import topmall.fas.repository.MallPayRepository;
import topmall.fas.service.IMallPayService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;
import cn.mercury.basic.query.Query;

@Service
public class MallPayService extends BaseService<MallPay,String> implements  IMallPayService{
    @Autowired
    private MallPayRepository repository;
    
    protected IRepository<MallPay,String> getRepository(){
        return repository;
    }

	@Override
	public List<MallPay> selectMallPayList(Query query) {
		return repository.selectMallPayList(query.asMap());
	}
	
	@Override
	public void updateToUnsettled(String id) {
		repository.updateToUnsettled(id);
	}

	@Override
	public List<MallPay> getMallPayGroupPaidWay(Query query) {
		return repository.getMallPayGroupPaidWay(query.asMap());
	}

	@Override
	public List<MallPay> queryConditionSum(Query q) {
		return repository.queryConditionSum(q.asMap());
	}
    
}







