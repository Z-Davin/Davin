package topmall.fas.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.mercury.basic.query.Q;
import cn.mercury.manager.ManagerException;
import cn.mercury.security.IUser;
import topmall.common.enums.BillTypeEnums;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IMallPrepayManager;
import topmall.fas.model.MallPrepay;
import topmall.fas.model.MallPrepayDtl;
import topmall.fas.service.IMallPrepayService;
import topmall.fas.service.impl.MallPrepayDtlService;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.framework.core.CodingRuleHelper;
import topmall.framework.manager.BaseManager;
import topmall.framework.security.Authorization;
import topmall.framework.service.IService;

@Service
public class MallPrepayManager extends BaseManager<MallPrepay, String> implements IMallPrepayManager {
	@Autowired
	private IMallPrepayService service;
	@Autowired
	private MallPrepayDtlService mallPrepayDtlService;

	protected IService<MallPrepay, String> getService() {
		return service;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult save(MallPrepay mallPrepay) {
		if (StringUtils.isEmpty(mallPrepay.getBillNo())) {
			String billNo = mallPrepay.getShopNo() + CodingRuleHelper
					.getBasicCoding(BillTypeEnums.MALL_PREPAY.getRequestId().toString(), mallPrepay.getShopNo(), null);
			mallPrepay.setBillNo(billNo);
			mallPrepay.setBillType(BillTypeEnums.MALL_PREPAY.getRequestId());
			mallPrepay.setStatus(StatusEnums.MAKEBILL.getStatus());
			super.insert(mallPrepay);
		} else {
			
			// 新增的物业费用
			List<MallPrepayDtl> inserteDtlList = mallPrepay.getInsertMallPrepayDtlList();
			if (CommonUtil.hasValue(inserteDtlList)) {
				for (MallPrepayDtl mallPrepayDtl : inserteDtlList) {
					mallPrepayDtl.setId(generateId());
					mallPrepayDtl.setBillNo(mallPrepay.getBillNo());
					mallPrepayDtlService.insert(mallPrepayDtl);
				}
			}
			// 删除物业费用
			List<MallPrepayDtl> deleteDtlList = mallPrepay.getDeleteMallPrepayDtlList();
			if (CommonUtil.hasValue(deleteDtlList)) {
				for (MallPrepayDtl mallPrepayDtl : deleteDtlList) {
					mallPrepayDtlService.deleteByPrimaryKey(mallPrepayDtl.getId());
				}
			}
			// 更新物业费用
			List<MallPrepayDtl> updateDtlList = mallPrepay.getUpdateMallPrepayDtlList();
			if (CommonUtil.hasValue(updateDtlList)){
				for (MallPrepayDtl mallPrepayDtl : updateDtlList) {
					mallPrepayDtlService.update(mallPrepayDtl);
				}
			}
			BigDecimal prepayAmount = new BigDecimal(0);
			List<MallPrepayDtl> list = mallPrepayDtlService.selectByParams(Q.where("billNo", mallPrepay.getBillNo()));
			for(MallPrepayDtl dtl:list){
				prepayAmount = prepayAmount.add(dtl.getPrepayAmount());
			}
			mallPrepay.setPrepayAmount(prepayAmount);
			super.update(mallPrepay);
		}
		 mallPrepay = service.findByUnique(Q.where("billNo", mallPrepay.getBillNo()));
		return CommonResult.sucess(mallPrepay);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult verify(String billNo) {
		MallPrepay mallPrepay = service.findByUnique(Q.where("billNo", billNo));
		mallPrepay.setStatus(StatusEnums.verify.getStatus());
		IUser user = Authorization.getUser();
		if(null!=user){
			mallPrepay.setAuditor(user.getName());
		}
		mallPrepay.setAuditTime(new Date());
		service.update(mallPrepay);
		return CommonResult.sucess(mallPrepay);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ManagerException.class)
	public CommonResult deleteBill(String billNo) {
		MallPrepay mallPrepay = service.findByUnique(Q.where("billNo", billNo));
		service.deleteByPrimaryKey(mallPrepay.getId());
		List<MallPrepayDtl> list = mallPrepayDtlService.selectByParams(Q.where("billNo", billNo));
		for(MallPrepayDtl dtl:list){
			mallPrepayDtlService.deleteByPrimaryKey(dtl.getId());
		}
		return CommonResult.getSucessResult();
	}

}
