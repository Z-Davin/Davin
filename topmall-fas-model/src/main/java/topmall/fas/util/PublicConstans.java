package topmall.fas.util;

/**
 * 常量类
 */
public class PublicConstans {

	// 单据状态 
	public static final Short STATUS_CREATE = 0; //制单
	public static final Short STATUS_SUBMIT = 10; // 提交
	public static final Short STATUS_INVALID = 99; //作废
	public static final Short STATUS_VERIFY = 100; //审核
	
	//验证数据时异常
	public static final String VALIDATE_ERROR = "Validate error";
	
	public static final String SYSTEM_ERROR = "系统异常";
	//用于生成费用单的流水号
	public static final String COUNTER_COST_NO="COUNTER_COST";
	//用于销售费用生成流水号
	public static final String COUNTER_SALE_COST="COUNTER_SALE_COST";
	
	//用于产生物业费用的流水号
	public static final String MALL_COST_NO="MALL_COST";
	//用于产生物业销售费用的流水号
	public static final String MALL_SALE_COST="MALL_SALE_COST";
	
	public static final  String DOWNLOAD_DIR = "classpath:/static/download/"; //导入excel的模板路径
	public static final String ENCODE = "utf-8"; //utf-8
	// 1224298 提货券分摊   
	public static final String TICKET_ITEMNO="1224298";
	//1103108 VIP分摊
	public static final String VIP_ITEMNO="1103108";
	//1001170 物料扣费
	public static final String MATERIAL_ITEMNO="1001170";
	// 1001180 积分抵现费用
	public static final String POINTS_ITEMNO="1001180";
	
	//专柜结算单是否生成净收入和毛收入只差
	public static final String COUNTER_BALANCE_DIFF="0001";
	//金额小数位
	public static final int DECIMAL_PLACE=4;
	//2位金额小数
	public static final  int TWO_DECIMAL_PLACE = 2;
}
