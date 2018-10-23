package topmall.fas.api;

import java.util.Date;

import javax.annotation.Resource;

import cn.mercury.basic.query.Q;

import com.alibaba.dubbo.config.annotation.Service;

import tomall.fas.api.IShopMonthBalanceApi;
import topmall.fas.manager.IShopBalanceDateManager;
import topmall.fas.model.ShopBalanceDate;


@Service
public class ShopMonthBalanceApi implements IShopMonthBalanceApi {
	
	@Resource
	private IShopBalanceDateManager shopBalanceDateManager;

	@Override
	public Date getLastShopBalanceDate(String shopNo) {
		ShopBalanceDate shopBalanceDate = shopBalanceDateManager.findByUnique(Q.where("shopNo", shopNo));
		if(null!=shopBalanceDate){
			return shopBalanceDate.getCloseDate();
		}
		return null;
	}

}
