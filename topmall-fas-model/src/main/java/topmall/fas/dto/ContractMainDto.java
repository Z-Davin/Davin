/**
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
package topmall.fas.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

/**
 * 合同主信息
 * 
 * @author dai.j
 * @date 2017-9-21 下午1:47:52
 * @version 0.1.0 
 * @copyright Wonhigh Information Technology (Shenzhen) Co.,Ltd.
 */
public class ContractMainDto implements Serializable{

	private static final long serialVersionUID = 1011479772220558154L;
	
	private String billNo;
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String val) {
		billNo = val;
	}
	
	private String shopNo;
    public String getShopNo(){
        return  shopNo;
    }    
    public void setShopNo(String val ){
        shopNo = val;
    }
    
    private String counterNo;
    public String getCounterNo(){
        return  counterNo;
    }
    public void setCounterNo(String val ){
        counterNo = val;
    }
    
    /**
     * 物业编码
     */
    private String mallNo;
	public String getMallNo() {
		return mallNo;
	}
	public void setMallNo(String mallNo) {
		this.mallNo = mallNo;
	}
	
	/**
	 * 铺位组编码
	 */
	private String bunkGroupNo;
	public String getBunkGroupNo() {
		return bunkGroupNo;
	}
	public void setBunkGroupNo(String bunkGroupNo) {
		this.bunkGroupNo = bunkGroupNo;
	}

	/**
     * 物理合同号
     */
    private String physicsContractNo;
    public String getPhysicsContractNo(){
        return  physicsContractNo;
    }    
    public void setPhysicsContractNo(String val ){
        physicsContractNo = val;
    }
    
    /**
     * 经营类型：1：联营(客户)，2：租赁（供应商）
     */
    private Short businessType;
    public Short getBusinessType(){
        return  businessType;
    }    
    public void setBusinessType(Short val ){
        businessType = val;
    }
    
    /**
     * 基础费用扣取方式:1,租金+抽成;2,租金、抽成取高:3.租金、抽成+保底取高
     */
    private Short baseCostType;
    public Short getBaseCostType(){
        return  baseCostType;
    }    
    public void setBaseCostType(Short val ){
        baseCostType = val;
    }
    
    /**
     * 是否有有效的合同条款
     */
    private boolean hasEnaleItem;
	public boolean isHasEnaleItem() {
		return hasEnaleItem;
	}
	public void setHasEnaleItem(boolean hasEnaleItem) {
		this.hasEnaleItem = hasEnaleItem;
	}
	
	/**
	 * 未达保底，不计算抽成(0-否，1-是)
	 */
	private short unGuaraUnProfit;
	public short getUnGuaraUnProfit() {
		return unGuaraUnProfit;
	}
	public void setUnGuaraUnProfit(short unGuaraUnProfit) {
		this.unGuaraUnProfit = unGuaraUnProfit;
	}
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
   	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date startDate;
    @JsonDeserialize(using = JsonDateDeserializer$10.class)
   	@JsonSerialize(using = JsonDateSerializer$10.class)
	private Date endDate;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    
	
    
    

}
