/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import topmall.fas.manager.IMallPayManager;
import topmall.fas.model.MallBalanceDateDtl;
import cn.mercury.utils.DateUtil;

/**
 * 专柜费用登记测试类
 * @author dai.j
 * @date 2017-11-7 下午4:57:54
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MallPayManagerTest {
	
	@Autowired
	private IMallPayManager mallPayManager;
	
	@Test
	public void shopCostQueryTest() throws Exception{
		
		MallBalanceDateDtl mallBalanceDateDtl = new MallBalanceDateDtl();
		mallBalanceDateDtl.setMallNo("WY000000");
		mallBalanceDateDtl.setShopNo("KZZ001");
		mallBalanceDateDtl.setSettleMonth("201710");
		mallBalanceDateDtl.setSettleStartDate(DateUtil.getdate("2017-10-01"));
		mallBalanceDateDtl.setSettleEndDate(DateUtil.getdate("2017-10-30"));
		
		mallPayManager.generateMallPayData(mallBalanceDateDtl);
	}
	
}
