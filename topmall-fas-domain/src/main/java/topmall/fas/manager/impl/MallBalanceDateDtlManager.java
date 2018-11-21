package topmall.fas.manager.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.mercury.basic.UUID;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.utils.DateUtil;
import topmall.fas.dto.ContractMainDto;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IMallBalanceDateDtlManager;
import topmall.fas.model.ContractDiscoPool;
import topmall.fas.model.ContractGuaraPool;
import topmall.fas.model.ContractOtherPool;
import topmall.fas.model.ContractProfitPool;
import topmall.fas.model.ContractRentPool;
import topmall.fas.model.MallBalanceDate;
import topmall.fas.model.MallBalanceDateDtl;
import topmall.fas.service.IMallBalanceDateDtlService;
import topmall.fas.service.IMallBalanceDateService;
import topmall.fas.util.CommonUtil;
import topmall.framework.manager.BaseManager;
import topmall.framework.service.IService;

@Service
public class MallBalanceDateDtlManager extends BaseManager<MallBalanceDateDtl, String> implements
		IMallBalanceDateDtlManager {

	private static final Integer NATURAL_MONTH = 32;

	@Autowired
	private IMallBalanceDateDtlService service;
	@Autowired
	private IMallBalanceDateService mallBalanceDateService;

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

	protected IService<MallBalanceDateDtl, String> getService() {
		return service;
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void generateMallBalanceDateDtl(){
		Query query = Q.where("status", StatusEnums.EFFECTIVE.getStatus());//默认生成生效状态的
		List<MallBalanceDate> list = mallBalanceDateService.selectByParams(query);
		for (MallBalanceDate mallBalanceDate : list) {
			MallBalanceDateDtl mallBalanceDateDtl = new MallBalanceDateDtl();
			mallBalanceDateDtl.setShopNo(mallBalanceDate.getShopNo());
			mallBalanceDateDtl.setMallNo(mallBalanceDate.getMallNo());
			mallBalanceDateDtl.setZoneNo(mallBalanceDate.getZoneNo());
			mallBalanceDateDtl.setCompanyNo(mallBalanceDate.getCompanyNo());
			mallBalanceDateDtl.setBunkGroupNo(mallBalanceDate.getBunkGroupNo());
			mallBalanceDateDtl.setPointsCalculateFlag(mallBalanceDate.getPointsCalculateFlag());
			//	
			Date currentDate = DateUtil.getFirstDayOfMonth(new Date());//结算月 取当前月日期第一天20170801
			Date prevDate = DateUtil.getFirstDayOfMonth(DateUtil.addMonth(currentDate, -1));//结算月上一个月1号  20170701
			Date nextDate = DateUtil.getFirstDayOfMonth(DateUtil.addMonth(currentDate, 1));//结算月下一个月1号  20170901
			String currentDateStr = DateUtil.format3(currentDate);
			mallBalanceDateDtl.setSettleMonth(currentDateStr);
			//根据卖场,物业,结算期,铺位组去查,如果有则不生成
			query = Q.where("shopNo", mallBalanceDate.getShopNo()).and("mallNo", mallBalanceDate.getMallNo())
					.and("settleMonth", currentDateStr).and("bunkGroupNo",mallBalanceDate.getBunkGroupNo());
			MallBalanceDateDtl dtl = service.findByParam(query);
			if (null != dtl) {
				continue;
			}
			String[] endDates=mallBalanceDate.getEndDate().split(",");
			int[] dates= new int[endDates.length];
			for(int i=0;i<endDates.length;i++){
				dates[i]=Integer.parseInt(endDates[i]);
			}
			Arrays.sort(dates);
			//如果结算期只有一个
			if(dates.length==1){
				//计算结算明细的开始和结束日期
				Date settleStartDate = currentDate;//默认 结算开始日期 存当月日期 20170801
				Date settleEndDate = DateUtil.addDate(nextDate, -1);//结算结束日期 存日期 20170831
				int endDate = dates[0];
				if (endDate != NATURAL_MONTH) {
					settleStartDate = DateUtil.addDate(prevDate, endDate);//结算开始日期 存日期 20170724
					settleEndDate = DateUtil.addDate(currentDate, endDate - 1);//结算结束日期 存日期 20170823
				}
				this.createMallBalanceDateDtl(mallBalanceDateDtl, settleStartDate, settleEndDate);
			}else{
				//多个结算期的流程
				// 先判断最后一个
				for(int i=0;i<dates.length;i++){
					if(i==0){
						Date settleStartDate = currentDate;//默认 结算开始日期 存当月日期 20170801
						//如果最后一个不是自然月
						if(dates[dates.length-1] != NATURAL_MONTH){
							settleStartDate = DateUtil.addDate(prevDate, dates[dates.length-1]);
						}else{
							settleStartDate = currentDate;
						}
						Date settleEndDate = DateUtil.addDate(currentDate, dates[i] - 1);
						this.createMallBalanceDateDtl(mallBalanceDateDtl, settleStartDate, settleEndDate);
					}else if(i==dates.length-1){
						//倒数第一个也需要特殊处理
						Date settleStartDate =DateUtil.addDate(currentDate, dates[i-1]);
						Date settleEndDate=DateUtil.addDate(nextDate, -1);
						if(dates[dates.length-1] != NATURAL_MONTH){
							 settleEndDate = DateUtil.addDate(currentDate, dates[i] - 1);
						}
						this.createMallBalanceDateDtl(mallBalanceDateDtl, settleStartDate, settleEndDate);
					}else{
						Date settleStartDate =DateUtil.addDate(currentDate, dates[i-1]);
						Date settleEndDate = DateUtil.addDate(currentDate, dates[i] - 1);
						this.createMallBalanceDateDtl(mallBalanceDateDtl, settleStartDate, settleEndDate);
					}
				}
			}
		}
	}
	
	private void createMallBalanceDateDtl(MallBalanceDateDtl dtl,Date settleStartDate,Date settleEndDate){
		dtl.setSettleStartDate(settleStartDate);
		dtl.setSettleEndDate(settleEndDate);
		dtl.setCreateTime(new Date());
		dtl.setStatus(StatusEnums.EFFECTIVE.getStatus());
		dtl.setId(UUID.newUUID().toString());
		insert(dtl);
	}

	/**
	 * @see topmall.fas.manager.IMallBalanceDateDtlManager#genarateCreateValidContract(topmall.fas.model.MallBalanceDateDtl)
	 */
	@Override
	public ContractMainDto genarateCreateValidContract(MallBalanceDateDtl mallBalanceDateDtl){
		/*在设置结算期的时候  同时要去获取有效的合同条款 */
		Query query = Q.where("shopNo", mallBalanceDateDtl.getShopNo());
		query.and("mallNo", mallBalanceDateDtl.getMallNo()).and("bunkGroupNo", mallBalanceDateDtl.getBunkGroupNo());

		//1.根据店铺和物业编码去获取合同
		ContractMainDto contractMainDto = service.selectContractBillNo(mallBalanceDateDtl);

		if (null != contractMainDto) {

			//2.根据合同编码和结算期的开始日期和结束日期去获取合同的有效条款
			query.and("billNo", contractMainDto.getBillNo());
			query.and("startDate", mallBalanceDateDtl.getSettleStartDate());
			query.and("endDate", mallBalanceDateDtl.getSettleEndDate());
			List<ContractDiscoPool> contractDiscoList = contractDiscoPoolManager.selectValidDisco(query);//财务结算期合同扣率池表
			List<ContractGuaraPool> contractGuaraList = contractGuaraPoolManager.selectValidGuara(query);//财务结算期合同保底池表
			List<ContractOtherPool> contractOtherList = contractOtherPoolManager.selectValidOther(query);//财务结算期合同其他池表
			List<ContractProfitPool> contractProfitList = contractProfitPoolManager.selectValidProfit(query);//财务结算期合同抽成池表
			List<ContractRentPool> contractRentList = contractRentPoolManager.selectValidRent(query);//财务结算期合同租金池表

			//3.把有效的合同条款放入 结算期有效合同条款池表中
			if (CommonUtil.hasValue(contractDiscoList)) {
				for (ContractDiscoPool contractDiscoPool : contractDiscoList) {
					contractDiscoPool.setBalanceDateId(mallBalanceDateDtl.getId());
					contractDiscoPool.setCreateUser("系统生成");
					contractDiscoPool.setCreateTime(new Date());
					contractDiscoPoolManager.insert(contractDiscoPool);
				}
				contractMainDto.setHasEnaleItem(true);

				if (CommonUtil.hasValue(contractGuaraList)) {
					for (ContractGuaraPool contractGuaraPool : contractGuaraList) {
						contractGuaraPool.setBalanceDateId(mallBalanceDateDtl.getId());
						contractGuaraPool.setCreateUser("系统生成");
						contractGuaraPool.setCreateTime(new Date());
						contractGuaraPoolManager.insert(contractGuaraPool);
					}
				}

				if (CommonUtil.hasValue(contractOtherList)) {
					for (ContractOtherPool contractOtherPool : contractOtherList) {
						contractOtherPool.setBalanceDateId(mallBalanceDateDtl.getId());
						contractOtherPool.setCreateUser("系统生成");
						contractOtherPool.setCreateTime(new Date());
						contractOtherPoolManager.insert(contractOtherPool);
					}
				}

				if (CommonUtil.hasValue(contractProfitList)) {
					for (ContractProfitPool contractProfitPool : contractProfitList) {
						contractProfitPool.setBalanceDateId(mallBalanceDateDtl.getId());
						contractProfitPool.setCreateUser("系统生成");
						contractProfitPool.setCreateTime(new Date());
						contractProfitPoolManager.insert(contractProfitPool);
					}
				}

				if (CommonUtil.hasValue(contractRentList)) {
					for (ContractRentPool contractRentPool : contractRentList) {
						contractRentPool.setBalanceDateId(mallBalanceDateDtl.getId());
						contractRentPool.setCreateUser("系统生成");
						contractRentPool.setCreateTime(new Date());
						contractRentPoolManager.insert(contractRentPool);
					}
				}
			} else {
				logger.info("结算期：" + mallBalanceDateDtl.toString() + " 内未获取到有效的合同条款");
				contractMainDto.setHasEnaleItem(false);
			}
		} else {
			logger.info("结算期：" + mallBalanceDateDtl.toString() + " 内未获取到有效的合同");
		}
		return contractMainDto;
	}

	/**
	 * @see topmall.fas.manager.IMallBalanceDateDtlManager#rebuildValidContract(topmall.fas.model.MallBalanceDateDtl)
	 */
	@Override
	public ContractMainDto rebuildValidContract(MallBalanceDateDtl mallBalanceDateDtl){

		// 先删除结算期的有效合同条款
		Query delQuery = Q.where("balanceDateId", mallBalanceDateDtl.getId());
		contractDiscoPoolManager.deleteByParams(delQuery);
		contractGuaraPoolManager.deleteByParams(delQuery);
		contractOtherPoolManager.deleteByParams(delQuery);
		contractProfitPoolManager.deleteByParams(delQuery);
		contractRentPoolManager.deleteByParams(delQuery);
		
		// 重新获取有效的合同条款
		return genarateCreateValidContract(mallBalanceDateDtl);
	}

	/**
	 * @see topmall.fas.manager.IMallBalanceDateDtlManager#getUnGeneralCostMinDate(cn.mercury.basic.query.Query)
	 */
	@Override
	public Date getUnGeneralCostMinDate(Query query) {
		return service.getUnGeneralCostMinDate(query);
	}
}
