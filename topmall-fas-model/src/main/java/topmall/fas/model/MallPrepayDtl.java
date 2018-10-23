
package topmall.fas.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10; 

/**
* 
**/
public class MallPrepayDtl  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636643996696778147L;
    
    
    /**
    *
    **/
    private String billNo;
    public String getBillNo(){
        return  billNo;
    }    
    public void setBillNo(String val ){
        billNo = val;
    }
    
    /**
    *
    **/
    private String costNo;
    public String getCostNo(){
        return  costNo;
    }    
    public void setCostNo(String val ){
        costNo = val;
    }
    
    /**
    *
    **/
    private String settleMonth;
    public String getSettleMonth(){
        return  settleMonth;
    }    
    public void setSettleMonth(String val ){
        settleMonth = val;
    }
    
    /**
    *
    **/
    @JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
    private Date settleStartDate;
    public Date getSettleStartDate(){
        return  settleStartDate;
    }    
    public void setSettleStartDate(Date val ){
        settleStartDate = val;
    }
    
    /**
    *
    **/
    @JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
    private Date settleEndDate;
    public Date getSettleEndDate(){
        return  settleEndDate;
    }    
    public void setSettleEndDate(Date val ){
        settleEndDate = val;
    }
    
    /**
    *
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal conAmount;
    public BigDecimal getConAmount(){
        return  conAmount;
    }    
    public void setConAmount(BigDecimal val ){
        conAmount = val;
    }
    
    /**
    *
    **/
    @JsonSerialize(using = BigDecimalSerializer$2.class)
    private BigDecimal prepayAmount;
    public BigDecimal getPrepayAmount(){
        return  prepayAmount;
    }    
    public void setPrepayAmount(BigDecimal val ){
        prepayAmount = val;
    }
    
    /**
    *
    **/
    private String remark;
    public String getRemark(){
        return  remark;
    }    
    public void setRemark(String val ){
        remark = val;
    }
    
    
    @Override
	public String toString() {
		return "MallPrepayDtl ["
            + "id="+getId()+",billNo="+billNo+",costNo="+costNo+",settleMonth="+settleMonth+",settleStartDate="+settleStartDate+",settleEndDate="+settleEndDate+",conAmount="+conAmount+",prepayAmount="+prepayAmount+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}