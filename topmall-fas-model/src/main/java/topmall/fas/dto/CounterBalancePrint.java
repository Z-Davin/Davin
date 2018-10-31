package topmall.fas.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import topmall.fas.model.CounterCost;
import topmall.fas.model.CounterSaleCost;
import cn.mercury.domain.BaseEntity;
import cn.mercury.utils.BigDecimalSerializer$2;
import cn.mercury.utils.JsonDateDeserializer$10;
import cn.mercury.utils.JsonDateSerializer$10;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class CounterBalancePrint extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4991560462949047953L;

	/**
	 * 供应商编码
	 */
	private String supplierNo;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 卖场名称
	 */
	private String shopName;

	/**
	 * 卖场编码
	 */
	private String shopNo;

	/**
	 * 楼层
	 */
	private Integer floorNo;

	/**
	 * 专柜编码
	 */
	private String counterNo;

	/**
	 * 专柜名称
	 */
	private String counterName;

	/**
	 * 结算单号
	 */
	private String billNo;

	/**
	 * 结算期开始时间
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date settleStartDate;

	/**
	 * 结算结束时间
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date settleEndDate;

	/**
	 * 纳税号 company.taxNo
	 */
	private String taxNo;

	/**
	 * 开户银行 company.bankName
	 */
	private String bankName;

	/**
	 * 银行帐号 company.bankAccount
	 */
	private String bankAccount;

	/**
	 * 开票地址 company.address
	 */
	private String address;

	/**
	 * 联系电话 auth_user_detail.tel
	 */
	private String tel;

	/**
	 * 制表人
	 */
	private String createUser;

	/**
	 * 邮政编码
	 */
	private String zipCode;

	/**
	 * 结算月
	 */
	private String settleMonth;

	/**
	 * 扣项
	 */
	private List<CounterCost> counterCostList;
	/**
	 * 销售扣费
	 */
	private List<CounterSaleCost> CounterSaleCostList;

	/**
	 * 保证金
	 */
	private List<CounterCost> depositList;

	/**
	 * 公司名称 company.companyName
	 */
	private String companyName;

	/**
	 * 销售数量
	 */
	private Integer saleQty;

	/**
	 * 公司名称 supplier.companyName
	 */
	private String suCompanyName;

	/**
	 * 地址 supplier.address
	 */
	private String suAddress;

	/**
	 * 开户银行 supplier.bankName
	 */
	private String suBankName;

	/**
	 * 纳税号 supplier.taxNo
	 */
	private String suTaxNo;

	/**
	 * 银行帐号 supplier.bankAccount
	 */
	private String suBankAccount;
	
	/**
	 * 银行帐号名 supplier.bankAccountName
	 */
	private String subBankAccountName;

	/**
	 * 税率
	 **/
	private BigDecimal raxRate;
	
	/**
	 * 合作性质
	 */
	private Short businessType;
	
	/**
	 * 大区
	 */
	private String zoneNo;
	
	public String getZoneNo() {
		return zoneNo;
	}

	public void setZoneNo(String zoneNo) {
		this.zoneNo = zoneNo;
	}
	

	/**
	 * 制单日期
	 */
	@JsonSerialize(using = JsonDateSerializer$10.class)
	@JsonDeserialize(using = JsonDateDeserializer$10.class)
	private Date printDate;
	
	/**
	 * 应结款总额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal ableSum;
	
	/**
	 * 应开票总额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal ableBillingSum;
	
	/**
	 * 备注1
	 */
	private String remark1;
	
	/**
	 * 备注2
	 */
	private String remark2;
	
	/**
	 * 本期销售额
	 */
	@JsonSerialize(using = BigDecimalSerializer$2.class)
	private BigDecimal saleAmount;
	

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public BigDecimal getAbleSum() {
		return  ableSum==null?new BigDecimal(0):ableSum;
	}

	public void setAbleSum(BigDecimal ableSum) {
		this.ableSum = ableSum;
	}

	public BigDecimal getAbleBillingSum() {
		return ableBillingSum==null?new BigDecimal(0):ableBillingSum;
	}

	public void setAbleBillingSum(BigDecimal ableBillingSum) {
		this.ableBillingSum = ableBillingSum;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public BigDecimal getRaxRate() {
		return raxRate;
	}

	public void setRaxRate(BigDecimal val) {
		raxRate = val;
	}

	public String getSuCompanyName() {
		return suCompanyName;
	}

	public void setSuCompanyName(String suCompanyName) {
		this.suCompanyName = suCompanyName;
	}

	public String getSuAddress() {
		return suAddress;
	}

	public void setSuAddress(String suAddress) {
		this.suAddress = suAddress;
	}

	public String getSuBankName() {
		return suBankName;
	}

	public void setSuBankName(String suBankName) {
		this.suBankName = suBankName;
	}

	public String getSuTaxNo() {
		return suTaxNo;
	}

	public void setSuTaxNo(String suTaxNo) {
		this.suTaxNo = suTaxNo;
	}

	public String getSuBankAccount() {
		return suBankAccount;
	}

	public void setSuBankAccount(String suBankAccount) {
		this.suBankAccount = suBankAccount;
	}

	public Integer getSaleQty() {
		return saleQty;
	}

	public void setSaleQty(Integer saleQty) {
		this.saleQty = saleQty;
	}

	public List<CounterCost> getDepositList() {
		return depositList;
	}

	public void setDepositList(List<CounterCost> depositList) {
		this.depositList = depositList;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public Integer getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(Integer floorNo) {
		this.floorNo = floorNo;
	}

	public String getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getSettleStartDate() {
		return settleStartDate;
	}

	public void setSettleStartDate(Date settleStartDate) {
		this.settleStartDate = settleStartDate;
	}

	public Date getSettleEndDate() {
		return settleEndDate;
	}

	public void setSettleEndDate(Date settleEndDate) {
		this.settleEndDate = settleEndDate;
	}

	public String getTaxNo() {
		return taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public List<CounterCost> getCounterCostList() {
		return counterCostList;
	}

	public void setCounterCostList(List<CounterCost> counterCostList) {
		this.counterCostList = counterCostList;
	}

	public List<CounterSaleCost> getCounterSaleCostList() {
		return CounterSaleCostList;
	}

	public void setCounterSaleCostList(List<CounterSaleCost> counterSaleCostList) {
		CounterSaleCostList = counterSaleCostList;
	}

	public String getSettleMonth() {
		return settleMonth;
	}

	public void setSettleMonth(String settleMonth) {
		this.settleMonth = settleMonth;
	}
	
	public Short getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Short businessType) {
		this.businessType = businessType;
	}
	public String getSubBankAccountName() {
		return subBankAccountName;
	}

	public void setSubBankAccountName(String subBankAccountName) {
		this.subBankAccountName = subBankAccountName;
	}
}
