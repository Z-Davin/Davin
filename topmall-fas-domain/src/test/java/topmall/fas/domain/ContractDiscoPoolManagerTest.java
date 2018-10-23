package topmall.fas.domain;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import topmall.fas.manager.IContractDiscoPoolManager;
import topmall.fas.model.ShopBalanceDateDtl;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ContractDiscoPoolManagerTest {

	@Autowired
	private IContractDiscoPoolManager contractDiscoPoolManager;
	
	@Test
	public void recalculateCounterCostTest(){
		ShopBalanceDateDtl shopBalanceDateDtl = new ShopBalanceDateDtl();
		shopBalanceDateDtl.setCounterNo("EZZ00104333");
		shopBalanceDateDtl.setShopNo("KZZ001");
		shopBalanceDateDtl.setSettleEndDate(new Date());
		shopBalanceDateDtl.setSettleStartDate(new Date());
		shopBalanceDateDtl.setSettleMonth("201709");
		
		ShopBalanceDateDtl newDateDtl = new ShopBalanceDateDtl();
		newDateDtl.setCounterNo("EZZ00104333");
		newDateDtl.setShopNo("KZZ001");
		newDateDtl.setSettleEndDate(new Date());
		newDateDtl.setSettleStartDate(new Date());
		newDateDtl.setSettleMonth("201709");
		
		contractDiscoPoolManager.createContractDiscoCost(shopBalanceDateDtl,false,newDateDtl);
	}
	
}
