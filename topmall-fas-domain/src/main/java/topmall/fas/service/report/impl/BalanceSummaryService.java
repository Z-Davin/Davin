package topmall.fas.service.report.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import topmall.fas.dto.BalanceSummary;
import topmall.fas.dto.CostTypeDto;
import topmall.fas.dto.PriceTypeDto;
import topmall.fas.repository.report.BalanceSummaryRepostitory;
import topmall.fas.service.report.IBalanceSummaryService;
import topmall.framework.repository.IRepository;
import topmall.framework.service.BaseService;

@Service
public class BalanceSummaryService extends BaseService<BalanceSummary, String> implements IBalanceSummaryService {

	@Autowired
	private BalanceSummaryRepostitory repository;

	@Override
	protected IRepository<BalanceSummary, String> getRepository() {
		return repository;
	}

	@Override
	public List<PriceTypeDto> queryPriceTypeList(Query query) {
		return repository.queryPriceTypeList(query.asMap());
	}

	@Override
	public List<Map<String, Object>> selectReportByPage(Query query,Pagenation page) {
		return repository.selectReportByPage(query.asMap(), page, query.getSort());
	}

	@Override
	public List<Map<String, Object>> selectCostInfo(Query query) {
		return repository.selectCostInfo(query.asMap());
	}

	@Override
	public List<CostTypeDto> queryCostTypeList(Query query) {
		return repository.queryCostTypeList(query.asMap());
	}

	@Override
	public List<Map<String, Object>> selectGuaraInfo(Query query) {
		return repository.selectGuaraInfo(query.asMap());
	}

	@Override
	public List<Map<String, Object>> selectSaleCostInfo(Query query) {
		return repository.selectSaleCostInfo(query.asMap());
	}

	@Override
	public List<Map<String, Object>> selectSaleAmount(Query query) {
		return repository.selectSaleAmount(query.asMap());
	}

}
