package topmall.fas.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Function;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.manager.ManagerException;
import cn.mercury.security.IUser;
import cn.mercury.utils.DateUtil;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.model.ContractDiscoPool;
import topmall.fas.model.ContractGuaraPool;
import topmall.fas.model.ContractOtherPool;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.model.ContractRentPool;
import topmall.fas.model.ShopBalanceDate;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.service.IShopBalanceDateDtlService;
import topmall.fas.service.IShopBalanceDateService;
import topmall.fas.util.CommonUtil;
import topmall.fas.util.GroupByUtils;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;
import topmall.mdm.model.Counter;
import topmall.mdm.open.api.ICounterApiService;

/**
 * dai.xw
 */
@Service
public class ShopBalanceDateDtlManager extends BaseManager<ShopBalanceDateDtl, String>
		implements IShopBalanceDateDtlManager {

	private static final Integer NATURAL_MONTH = 32;

	@Autowired
	private IShopBalanceDateDtlService service;
	@Autowired
	private IShopBalanceDateService shopBalanceDateService;
	@Reference
	private ICounterApiService counterApiService;

	@Autowired
	private ContractDiscoPoolManager contractDiscoPoolManager;

	@Autowired
	private ContractGuaraPoolManager contractGuaraPoolManager;

	@Autowired
	private ContractOtherPoolManager contractOtherPoolManager;

	@Autowired
	private ContractProfitPoolManager contractProfitPoolManager;

	@Autowired
	private ContractRentPoolManager contractRentPoolManager;

	protected IService<ShopBalanceDateDtl, String> getService() {
		return service;
	}

	@Override
	public ShopBalanceDate generateShopBalanceDateDtl(ShopBalanceDate shopBalanceDate) {
		if (!shopBalanceDate.getStatus().equals(StatusEnums.EFFECTIVE.getStatus())) {// 校验生效状态
			return shopBalanceDate;
		}
		String shopNo = shopBalanceDate.getShopNo();
		if (StringUtils.isBlank(shopNo)) {
			throw new ManagerException("[根据门店结算期设置表生成门店结算期设置表明细]门店编号shopNo不能为空！");
		}

		// 需要生成1:"启用",2:"停用",3:"待清退"状态的结算期明细
		Query query = Q.where(Q.And(Q.Equals("shopNo", shopNo), Q.In("status", new int[] { 1, 2, 3 })));
		List<Counter> counters = counterApiService.selectByParams(query);
		if (counters == null || counters.isEmpty()) {
			return null;
		}
		List<ShopBalanceDateDtl> shopBalanceDateDtls = new ArrayList<>();
		for (Counter counter : counters) {
			ShopBalanceDateDtl shopBalanceDateDtl = new ShopBalanceDateDtl();
			BeanUtils.copyProperties(shopBalanceDate, shopBalanceDateDtl);
			shopBalanceDateDtl.setId(null);
			shopBalanceDateDtl.setCreateTime(null);
			shopBalanceDateDtl.setUpdateTime(null);
			shopBalanceDateDtl.setCounterNo(counter.getCounterNo());

			// 日期处理
			Date currentDate = DateUtil.getFirstDayOfMonth(new Date());// 结算月 取当前月日期第一天20170801
			Date prevDate = DateUtil.getFirstDayOfMonth(DateUtil.addMonth(currentDate, -1));// 结算月上一个月1号 20170701
			Date nextDate = DateUtil.getFirstDayOfMonth(DateUtil.addMonth(currentDate, 1));// 结算月下一个月1号 20170901
			String currentDateStr = DateUtil.format3(currentDate);

			// 校验当前专柜有没有生成结算日
			query = Q.where("shopNo", shopNo).and("counterNo", counter.getCounterNo()).and("settleMonth",
					currentDateStr);
			List<ShopBalanceDateDtl> balanceDateDtls = this.selectByParams(query);
			if (CommonUtil.hasValue(balanceDateDtls)) {
				continue;
			}

			// 计算专柜结算明细的开始和结束日期
			Date settleStartDate = currentDate;// 默认 结算开始日期 存当月日期 20170801
			Date settleEndDate = DateUtil.addDate(nextDate, -1);// 结算结束日期 存日期 20170831
			// 如果不是按照自然月设置,则取用户设置的时候
			if (shopBalanceDate.getEndDate() != NATURAL_MONTH) {
				settleStartDate = DateUtil.addDate(prevDate, shopBalanceDate.getEndDate());// 结算开始日期 存日期 20170724
				settleEndDate = DateUtil.addDate(currentDate, shopBalanceDate.getEndDate() - 1);// 结算结束日期 存日期 20170823
			}
			shopBalanceDateDtl.setSettleMonth(currentDateStr);
			shopBalanceDateDtl.setSettleStartDate(settleStartDate);
			shopBalanceDateDtl.setSettleEndDate(settleEndDate);
			shopBalanceDateDtl.setStatus(StatusEnums.EFFECTIVE.getStatus());
			shopBalanceDateDtls.add(shopBalanceDateDtl);

			IUser user = Authorization.getUser();
			if (user == null) {
				shopBalanceDateDtl.setCreateTime(new Date());
				shopBalanceDateDtl.setCreateUser("system");
				shopBalanceDateDtl.setUpdateTime(new Date());
				shopBalanceDateDtl.setUpdateUser("system");
			}
		}
		if (!shopBalanceDateDtls.isEmpty()) {
			this.batchSave(shopBalanceDateDtls, null, null);
		}
		return shopBalanceDate;
	}

	@Override
	public void generateShopBalanceDateDtlAuto() {
		// 查询生效状态的专柜费用
		Query query = Q.where("status", StatusEnums.EFFECTIVE.getStatus());// 默认生成生效状态的
		List<ShopBalanceDate> shopBalanceDates = shopBalanceDateService.selectByParams(query);
		for (ShopBalanceDate shopBalanceDate : shopBalanceDates) {
			this.generateShopBalanceDateDtl(shopBalanceDate);
		}
	}

	/**
	 * @throws Exception
	 * @see topmall.fas.manager.IShopBalanceDateDtlManager#genarateCreateValidContract(topmall.fas.model.ShopBalanceDateDtl)
	 */
	@Override
	public ContractMainDto genarateCreateValidContract(ShopBalanceDateDtl shopBalanceDateDtl) {
		/* 在设置结算期的时候 同时要去获取有效的合同条款 */
		Query query = Q.where("shopNo", shopBalanceDateDtl.getShopNo()).and("counterNo",
				shopBalanceDateDtl.getCounterNo());

		// 1.根据店铺和专柜编码去获取合同
		ContractMainDto contractMainDto = service.selectContractBillNo(shopBalanceDateDtl);

		if (null != contractMainDto) {

			// 2.根据合同编码和结算期的开始日期和结束日期去获取合同的有效条款
			query.and("billNo", contractMainDto.getBillNo());
			query.and("startDate", shopBalanceDateDtl.getSettleStartDate());
			query.and("endDate", shopBalanceDateDtl.getSettleEndDate());
			List<ContractDiscoPool> contractDiscoList = contractDiscoPoolManager.selectValidDisco(query);// 财务结算期专柜合同扣率池表
			List<ContractGuaraPool> contractGuaraList = contractGuaraPoolManager.selectValidGuara(query);// 财务结算期专柜合同保底池表
			List<ContractOtherPool> contractOtherList = contractOtherPoolManager.selectValidOther(query);// 财务结算期专柜合同其他池表
			List<ContractProfitPool> contractProfitList = contractProfitPoolManager.selectValidProfit(query);// 财务结算期专柜合同抽成池表
			List<ContractRentPool> contractRentList = contractRentPoolManager.selectValidRent(query);// 财务结算期专柜合同租金池表

			// 3.把有效的合同条款放入 结算期有效合同条款池表中
			if (CommonUtil.hasValue(contractDiscoList)) {
				for (ContractDiscoPool contractDiscoPool : contractDiscoList) {
					contractDiscoPool.setBalanceDateId(shopBalanceDateDtl.getId());
					contractDiscoPool.setCreateUser("系统生成");
					contractDiscoPool.setCreateTime(new Date());
					contractDiscoPoolManager.insert(contractDiscoPool);
				}
				contractMainDto.setHasEnaleItem(true);

				if (CommonUtil.hasValue(contractGuaraList)) {
					for (ContractGuaraPool contractGuaraPool : contractGuaraList) {
						contractGuaraPool.setBalanceDateId(shopBalanceDateDtl.getId());
						contractGuaraPool.setCreateUser("系统生成");
						contractGuaraPool.setCreateTime(new Date());
						contractGuaraPoolManager.insert(contractGuaraPool);
					}
				}

				if (CommonUtil.hasValue(contractOtherList)) {
					for (ContractOtherPool contractOtherPool : contractOtherList) {
						contractOtherPool.setBalanceDateId(shopBalanceDateDtl.getId());
						contractOtherPool.setCreateUser("系统生成");
						contractOtherPool.setCreateTime(new Date());
						contractOtherPoolManager.insert(contractOtherPool);
					}
				}

				if (CommonUtil.hasValue(contractProfitList)) {
					for (ContractProfitPool contractProfitPool : contractProfitList) {
						contractProfitPool.setBalanceDateId(shopBalanceDateDtl.getId());
						contractProfitPool.setCreateUser("系统生成");
						contractProfitPool.setCreateTime(new Date());
						contractProfitPoolManager.insert(contractProfitPool);
					}
				}

				if (CommonUtil.hasValue(contractRentList)) {
					for (ContractRentPool contractRentPool : contractRentList) {
						contractRentPool.setBalanceDateId(shopBalanceDateDtl.getId());
						contractRentPool.setCreateUser("系统生成");
						contractRentPool.setCreateTime(new Date());
						contractRentPoolManager.insert(contractRentPool);
					}
				}

			} else {
				logger.info("结算期：" + shopBalanceDateDtl.toString() + " 内未获取到有效的合同条款");
				contractMainDto.setHasEnaleItem(false);
			}
		} else {
			
			
			
			
			logger.info("结算期：" + shopBalanceDateDtl.toString() + " 内未获取到有效的合同");
		}
		return contractMainDto;
	}

	/**
	 * @see topmall.fas.manager.IShopBalanceDateDtlManager#rebuildValidContract(topmall.fas.model.ShopBalanceDateDtl)
	 */
	@Override
	public ContractMainDto rebuildValidContract(ShopBalanceDateDtl shopBalanceDateDtl) {
		// 先删除结算期的有效合同条款
		Query delQuery = Q.where("balanceDateId", shopBalanceDateDtl.getId());
		contractDiscoPoolManager.deleteByParams(delQuery);
		contractGuaraPoolManager.deleteByParams(delQuery);
		contractOtherPoolManager.deleteByParams(delQuery);
		contractProfitPoolManager.deleteByParams(delQuery);
		contractRentPoolManager.deleteByParams(delQuery);

		// 再重新获取有效合同条款
		return genarateCreateValidContract(shopBalanceDateDtl);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void modifyBalanceDateDtl(List<ShopBalanceDateDtl> dtlList) {
		Map<Integer, List<ShopBalanceDateDtl>> groupMap = GroupByUtils.groupByKey(dtlList,
				new Function<ShopBalanceDateDtl, Integer>() {
					public Integer apply(ShopBalanceDateDtl dtl) {
						return dtl.getAction();
					}
				});

		List<ShopBalanceDateDtl> deleted = groupMap.get(0);// 要删除的结算期明细
		List<ShopBalanceDateDtl> inserted = groupMap.get(1);// 要插入的结算期明细
		List<ShopBalanceDateDtl> updated = groupMap.get(2); // 要修改的结算期明细
		batchSave(inserted, updated, deleted);

		List<ShopBalanceDateDtl> splited = groupMap.get(3);
		if (splited != null && splited.size() > 0) {
			for (ShopBalanceDateDtl shopBalanceDateDtl : splited) {
				Date splitDate = shopBalanceDateDtl.getSplitDate();
				ShopBalanceDateDtl newDtl = new ShopBalanceDateDtl();
				BeanUtils.copyProperties(shopBalanceDateDtl, newDtl, "id");
				shopBalanceDateDtl.setSettleEndDate(splitDate);
				newDtl.setSettleStartDate(DateUtil.addDate(splitDate, 1));
				newDtl.setStatus(StatusEnums.EFFECTIVE.getStatus());
				update(shopBalanceDateDtl);
				insert(newDtl);
			}
		}
	}
}
