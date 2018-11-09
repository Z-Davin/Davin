
package topmall.fas.model;

import java.util.Date;
import java.math.BigDecimal; 

/**
 * 自收银销售支付方式汇总表
 * @author Administrator
 *
 */
public class DaysalePaywaySum  extends cn.mercury.domain.BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636421318445073066L;
    
    
    /**
    *
    **/
    private String companyNo;
    public String getCompanyNo(){
        return  companyNo;
    }    
    public void setCompanyNo(String val ){
        companyNo = val;
    }
    
    /**
    *
    **/
    private String companyName;
    public String getCompanyName(){
        return  companyName;
    }    
    public void setCompanyName(String val ){
        companyName = val;
    }
    
    /**
    *
    **/
    private String storeNo;
    public String getStoreNo(){
        return  storeNo;
    }    
    public void setStoreNo(String val ){
        storeNo = val;
    }
    
    /**
    *
    **/
    private String storeName;
    public String getStoreName(){
        return  storeName;
    }    
    public void setStoreName(String val ){
        storeName = val;
    }
    
    /**
    *
    **/
    private Date outDate;
    public Date getOutDate(){
        return  outDate;
    }    
    public void setOutDate(Date val ){
        outDate = val;
    }
    
    /**
    *
    **/
    private BigDecimal cashSaleAmount;
    public BigDecimal getCashSaleAmount(){
        return  cashSaleAmount;
    }    
    public void setCashSaleAmount(BigDecimal val ){
        cashSaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal cashDepositAmount;
    public BigDecimal getCashDepositAmount(){
        return  cashDepositAmount;
    }    
    public void setCashDepositAmount(BigDecimal val ){
        cashDepositAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal cashDebitAmount;
    public BigDecimal getCashDebitAmount(){
        return  cashDebitAmount;
    }    
    public void setCashDebitAmount(BigDecimal val ){
        cashDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal bankCardSaleAmount;
    public BigDecimal getBankCardSaleAmount(){
        return  bankCardSaleAmount;
    }    
    public void setBankCardSaleAmount(BigDecimal val ){
        bankCardSaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal bankCardDebitAmount;
    public BigDecimal getBankCardDebitAmount(){
        return  bankCardDebitAmount;
    }    
    public void setBankCardDebitAmount(BigDecimal val ){
        bankCardDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal aliPaySaleAmount;
    public BigDecimal getAliPaySaleAmount(){
        return  aliPaySaleAmount;
    }    
    public void setAliPaySaleAmount(BigDecimal val ){
        aliPaySaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal aliPayDebitAmount;
    public BigDecimal getAliPayDebitAmount(){
        return  aliPayDebitAmount;
    }    
    public void setAliPayDebitAmount(BigDecimal val ){
        aliPayDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal wechatPaySaleAmount;
    public BigDecimal getWechatPaySaleAmount(){
        return  wechatPaySaleAmount;
    }    
    public void setWechatPaySaleAmount(BigDecimal val ){
        wechatPaySaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal wechatPayDebitAmount;
    public BigDecimal getWechatPayDebitAmount(){
        return  wechatPayDebitAmount;
    }    
    public void setWechatPayDebitAmount(BigDecimal val ){
        wechatPayDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal mallCardSaleAmount;
    public BigDecimal getMallCardSaleAmount(){
        return  mallCardSaleAmount;
    }    
    public void setMallCardSaleAmount(BigDecimal val ){
        mallCardSaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal mallCardDebitAmount;
    public BigDecimal getMallCardDebitAmount(){
        return  mallCardDebitAmount;
    }    
    public void setMallCardDebitAmount(BigDecimal val ){
        mallCardDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal mallVoucherSaleAmount;
    public BigDecimal getMallVoucherSaleAmount(){
        return  mallVoucherSaleAmount;
    }    
    public void setMallVoucherSaleAmount(BigDecimal val ){
        mallVoucherSaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal mallVoucherDebitAmount;
    public BigDecimal getMallVoucherDebitAmount(){
        return  mallVoucherDebitAmount;
    }    
    public void setMallVoucherDebitAmount(BigDecimal val ){
        mallVoucherDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal otherMoneySaleAmount;
    public BigDecimal getOtherMoneySaleAmount(){
        return  otherMoneySaleAmount;
    }    
    public void setOtherMoneySaleAmount(BigDecimal val ){
        otherMoneySaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal otherMoneyDebitAmount;
    public BigDecimal getOtherMoneyDebitAmount(){
        return  otherMoneyDebitAmount;
    }    
    public void setOtherMoneyDebitAmount(BigDecimal val ){
        otherMoneyDebitAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal otherSaleAmount;
    public BigDecimal getOtherSaleAmount(){
        return  otherSaleAmount;
    }    
    public void setOtherSaleAmount(BigDecimal val ){
        otherSaleAmount = val;
    }
    
    /**
    *
    **/
    private BigDecimal otherDebitAmount;
    public BigDecimal getOtherDebitAmount(){
        return  otherDebitAmount;
    }    
    public void setOtherDebitAmount(BigDecimal val ){
        otherDebitAmount = val;
    }
    
    /**
    *
    **/
    private String shardingFlag;
    public String getShardingFlag(){
        return  shardingFlag;
    }    
    public void setShardingFlag(String val ){
        shardingFlag = val;
    }
    
    /**
    *
    **/
    private String storeGroupName;
    public String getStoreGroupName(){
        return  storeGroupName;
    }    
    public void setStoreGroupName(String val ){
        storeGroupName = val;
    }
    
    /**
    *
    **/
    private String storeGroupNo;
    public String getStoreGroupNo(){
        return  storeGroupNo;
    }    
    public void setStoreGroupNo(String val ){
        storeGroupNo = val;
    }
    
    
    @Override
	public String toString() {
		return "DaysalePaywaySum ["
            + "id="+super.getId()+",companyNo="+companyNo+",companyName="+companyName+",storeNo="+storeNo+",storeName="+storeName+",outDate="+outDate+",cashSaleAmount="+cashSaleAmount+",cashDepositAmount="+cashDepositAmount+",cashDebitAmount="+cashDebitAmount+",bankCardSaleAmount="+bankCardSaleAmount+",bankCardDebitAmount="+bankCardDebitAmount+",aliPaySaleAmount="+aliPaySaleAmount+",aliPayDebitAmount="+aliPayDebitAmount+",wechatPaySaleAmount="+wechatPaySaleAmount+",wechatPayDebitAmount="+wechatPayDebitAmount+",mallCardSaleAmount="+mallCardSaleAmount+",mallCardDebitAmount="+mallCardDebitAmount+",mallVoucherSaleAmount="+mallVoucherSaleAmount+",mallVoucherDebitAmount="+mallVoucherDebitAmount+",otherMoneySaleAmount="+otherMoneySaleAmount+",otherMoneyDebitAmount="+otherMoneyDebitAmount+",otherSaleAmount="+otherSaleAmount+",otherDebitAmount="+otherDebitAmount+",shardingFlag="+shardingFlag+",storeGroupName="+storeGroupName+",storeGroupNo="+storeGroupNo 
            + "]";
	}
    
     /** auto generate end,don't modify */
}