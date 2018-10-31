/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.exception.ApiServiceException;
import cn.mercury.utils.DateUtil;
import topmall.fas.enums.StatusEnums;
import topmall.fas.manager.IShopBalanceDateDtlManager;
import topmall.fas.model.ShopBalanceDateDtl;

/**
 * 结算期明细测试类
 * @author dai.j
 * @date 2018-1-17 下午12:14:07
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ShopBalanceDateDtlTest {

	@Autowired
	private IShopBalanceDateDtlManager manager;

	@Test
	public void splitBalanceDateTest() throws Exception {
		String counterNo = "SHWH00126204";
		Date splitDate = DateUtil.getdate("2017-12-15");
		int splitType = 3;

		Query query = Q.And(Q.LessThenEquals("settleStartDate", splitDate),
				Q.GreatThenEquals("settleEndDate", splitDate)).asQuery();
		query.where("counterNo", counterNo).and("status", StatusEnums.EFFECTIVE.getStatus());
		ShopBalanceDateDtl shopBalanceDateDtl = manager.findByParam(query);
		if (null == shopBalanceDateDtl) {
			throw new ApiServiceException("未获取到生效状态的结算期明细");
		} else {
			if (1 == splitType) {
				shopBalanceDateDtl.setRemark("合同截断结算期");
				shopBalanceDateDtl.setSettleStartDate(splitDate);
				manager.update(shopBalanceDateDtl);
			} else if (2 == splitType) {
				ShopBalanceDateDtl newShopBalanceDateDtl = new ShopBalanceDateDtl();
				BeanUtils.copyProperties(shopBalanceDateDtl, newShopBalanceDateDtl, new String[] { "id" });

				shopBalanceDateDtl.setSettleEndDate(splitDate);
				manager.update(shopBalanceDateDtl);

				newShopBalanceDateDtl.setRemark("合同拆分结算期");
				newShopBalanceDateDtl.setSettleStartDate(DateUtil.addDate(splitDate, 1));
				manager.insert(newShopBalanceDateDtl);
			} else if (3 == splitType) {
				shopBalanceDateDtl.setSettleEndDate(splitDate);
				shopBalanceDateDtl.setRemark("合同截断结算期");
				manager.update(shopBalanceDateDtl);
			} else {
				throw new ApiServiceException("无效的拆分类型：" + splitType);
			}
		}
	}
}
