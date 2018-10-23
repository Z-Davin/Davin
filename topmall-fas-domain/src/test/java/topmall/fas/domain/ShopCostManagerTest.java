/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.mercury.basic.query.IStatement;
import cn.mercury.basic.query.Q;
import cn.mercury.basic.query.Query;
import cn.mercury.utils.DateUtil;
import topmall.fas.manager.IShopCostManager;
import topmall.fas.manager.impl.CommonStaticManager;
import topmall.fas.model.ShopBalanceDateDtl;
import topmall.fas.model.ShopCost;
import topmall.pos.dto.query.DaySaleQueryDto;

/**
 * 专柜费用登记测试类
 * @author dai.j
 * @date 2017-11-7 下午4:57:54
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ShopCostManagerTest {

	@Autowired
	private IShopCostManager shopCostManager;

	@Test
	public void shopCostQueryTest() throws Exception {

		ShopBalanceDateDtl shopBalanceDateDtl = new ShopBalanceDateDtl();
		shopBalanceDateDtl.setShopNo("KSZ004");
		shopBalanceDateDtl.setCounterNo("KSZ00404305");
		shopBalanceDateDtl.setSettleMonth("201709");
		shopBalanceDateDtl.setSettleStartDate(DateUtil.getdate("2017-08-12"));
		shopBalanceDateDtl.setSettleEndDate(DateUtil.getdate("2017-09-11"));

		List<ShopCost> result = shopCostManager.queryByBalanceDate(shopBalanceDateDtl);

		System.out.println(result.size());

		for (ShopCost shopCost : result) {
			System.out.println(shopCost.getCostNo() + " : " + shopCost.getNumber());
		}
	}

	@Test
	public void counterCosttest() throws Exception {
		DaySaleQueryDto saleBaseQuery = new DaySaleQueryDto("SGMY001", DateUtil.getdate("2017-10-01"),
				DateUtil.getdate("2017-10-19"));
		saleBaseQuery.setBunkGroupNo("01");
		saleBaseQuery.setPriceNo("00");
		saleBaseQuery.setQueryType(2);

		String[] payNoList = new String[] { "aaa", "bbb", "ccc" };
		IStatement is = Q.In("dtl.payNo", payNoList);
		saleBaseQuery.setStatement(is);

		BigDecimal result = CommonStaticManager.getShopDaySaleApiService().queryBalancePaySum(saleBaseQuery, null);
		
		System.out.println(result);
	}
	
	@Test
	public void querySaleProfitTest() throws Exception {
		Query saleProfitQuery = Q.where("shopNo", "123")
				.and("counterNo", "1123")
				.and("startDate", DateUtil.getdate("2018-07-01"))
				.and("endDate",  DateUtil.getdate("2018-07-01")).and("isHisCost", false);
		BigDecimal saleProfitSum = CommonStaticManager.getCounterSaleCostDtlManager().querySaleProfit(saleProfitQuery);
		System.out.println(saleProfitSum);
	}

}
