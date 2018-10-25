"use strict";
/**
 * 结算单打印
 */
define(function (require, exports, module) {
    let config = require('../config');
    let template = require('lib/template');
    let url = config.rootUrl;
    let acrossTemplate = `
<div class="receipt">
    <div class="bTitle">{{shopName}}</div>
    <div class="title">结算单</div>
    <table class="border" cellspacing="0">        
        <tbody>
            <tr>
	            <td align="center">结算单号:</td>
	            <td align="center">{{billNo}}</td>
                <td align="center">公司税号:</td>
                <td align="center">{{suTaxNo}}</td>                
            </tr>
            <tr>
	            <td align="center">公司名称</td>
				<td align="center">{{suCompanyName}}</td>
	            <td align="center">银行账号</td>
	            <td align="center">{{suBankAccount}}</td>
            </tr>
            <tr>
	            <td align="center">公司地址</td>
	            <td align="center">{{suAddress}}</td>
	            <td align="center">制单日期</td>
                <td align="center">{{printDate}}</td>	            
	        </tr>
	        <tr>
		        <td align="center">开户行及行号</td>
	            <td align="center">{{suBankName}}</td>
	            <td align="center">结算区间</td>
	            <td align="center">{{settleMonth}}</td>	            
	        </tr> 
	        <tr>
		        <td align="center">专柜名称</td>
	            <td align="center">{{counterName}}</td>
	            <td align="center">合作方式</td>
	            <td align="center">{{businessType}}</td>	            
	        </tr>
	        <tr>
		        <td align="center">专柜编码</td>
	            <td align="center">{{counterNo}}</td>
	            <td align="center">银行账户名</td>
	            <td align="center">{{subBankAccountName}}</td>	            
	        </tr>              
        </tbody>
    </table>
    <div>一、货款结算部分</div>
    <table class="border" cellspacing="0">            
        <tbody>            
            <tr>
            	<td align="center">结算项目</td>
            	<td align="center">销售金额</td>
            	<td align="center">销售扣款</td>              
            </tr>
            {{each counterSaleCostList as counterSale index}}
            <tr>            	           
              	<td align="center">{{counterSale.divisionNo}}{{counterSale.divisionName}}</td>
              	<td align="center">{{counterSale.settleSum}}</td>
              	<td align="center">{{counterSale.profitAmount}}</td>             	
            </tr>
            {{/each}}
            <tr>            	
	          	<td align="center">合计</td>
	          	<td align="center">{{settleSumTotal}}</td>
	          	<td align="center">{{profitAmountTotal}}</td>
	        </tr>
        </tbody>
    </table>
    <div>二、费用结算部分</div>
    <table class="secondpart" cellspacing="0" border="0">
	    <tr>
	    	<td>
			    <table class="border" cellspacing="0">     	     
			        <tbody>            
			            <tr>
			            	<td align="center">序号</td>
			                <td align="center">费用项目</td>
			              	<td align="center">金额</td>
			              	<td align="center">费用类别</td> 
			            </tr>
			            {{each counterCostList1 as counter index}}
			            <tr>	            
			              	<td align="center">{{index+1}}</td>
			              	<td align="center">{{counter.costName}}</td>
			              	<td align="center">{{counter.ableSum}}</td>
			              	<td align="center">{{counter.billDebit}}</td>
			            </tr>
			            {{/each}}   
			        	<tr>
					        <td align="center" colspan="2">票扣费用合计</td>
					        <td align="center" colspan="2">{{billDebitTotal}}</td>
				        </tr>	        
			    	</tbody>
			    </table>
		    </td>
	    <td>
		    <table class="leftborder border"  cellspacing="0">   
			    <tbody>            
			        <tr>
			        	<td align="center">序号</td>
			            <td align="center">费用项目</td>
			          	<td align="center">金额</td>
			          	<td align="center">费用类别</td>
			        </tr>
			        {{each counterCostList2 as counter index}}
			        <tr>	            
			          	<td align="center">{{counterCostList1.length+index + 1}}</td>
			          	<td align="center">{{counter.costName}}</td>
			          	<td align="center">{{counter.ableSum}}</td>
			          	<td align="center">{{counter.billDebit}}</td>
			        </tr>
			        {{/each}}  
			        <tr>
				        <td align="center" colspan="2">非票扣费用合计</td>
				        <td align="center" colspan="2">{{notBillDebitTotal}}</td>
			        </tr>
		    	</tbody>
		    	</table>
	    	</td>
	    </tr>
    </table>
	<div>三、其他部分</div>
	<table class="secondpart" cellspacing="0" border="0">
	    <tr>
	    	<td>
			    <table class="border" cellspacing="0">     	     
				    <tbody>            
				        <tr>
				        	<td align="center">序号</td>
				            <td align="center">项目名称</td>
				          	<td align="center">金额</td>
				          	<td align="center">交款方式</td> 
				        </tr>
				        {{each otherCostList1 as other index}}
				        <tr>	            
				          	<td align="center">{{index+1}}</td>
				          	<td align="center">{{other.costName}}</td>
				          	<td align="center">{{other.ableSum}}</td>
				          	<td align="center">{{other.accountDebit}}</td>
				        </tr>
				        {{/each}}   
				    	<tr>
					        <td align="center" colspan="2">账扣合计</td>
					        <td align="center" colspan="2">{{accountDebitTotal}}</td>
				        </tr>	        
					</tbody>
				</table>
			</td>
	    	<td>
				<table class="leftborder border" cellspacing="0">   
				    <tbody>            
				        <tr>
					        <td align="center">序号</td>
				            <td align="center">项目名称</td>
				          	<td align="center">金额</td>
				          	<td align="center">交款方式</td> 
				        </tr>
				        {{each otherCostList2 as other index}}
				        <tr>	            
				          	<td align="center">{{otherCostList1.length + index + 1}}</td>
				          	<td align="center">{{other.costName}}</td>
				          	<td align="center">{{other.ableSum}}</td>
				          	<td align="center">{{other.costType}}</td>
				        </tr>
				        {{/each}}  
				        <tr>
					        <td align="center" colspan="2">现金合计</td>
					        <td align="center" colspan="2">{{cashTotal}}</td>
				        </tr>
					</tbody>
				</table>
			</td>
	    </tr>
    </table>
	{{if !(businessType == '租赁' && (zoneNo == 'C' || zoneNo == 'E'))}}
	<table class="border" cellspacing="0">    			
        <tbody>
            <tr>
            	<td align="left" width="40%">四、开发票金额</td>
                <td align="center">开票数量</td>
                <td align="center">{{saleQty}}</td>
                <td align="center">开票总额</td>
	            <td align="center">{{ableBillingSum}}</td>  
            </tr>
        </tbody>
    </table>
    {{/if}}
    <table class="border" cellspacing="0">
    	<tbody>
    		<tr>
    			<td width="200" align="left">
    				{{if (businessType == '租赁' && zoneNo == 'C') || (businessType == '租赁' && zoneNo == 'E')}}
    				四、
    				{{else}}
    				五、
    				{{/if}}
					应付款项(大写)
				</td>
    			<td width="50%" align="center">{{payName}}</td>
    			<td align="center">{{ableSum}}</td>
    		</tr>
    	</tbody>
    </table>
    <table class="border" cellspacing="0">    			
	    <tbody>
	        <tr>
	        	<td align="left" rowspan="9">说明</td>
	            <td align="center" width="50" rowspan="2">1 </td>
	            <td align="left" colspan="4">{{remark1}}</td>
	            <td align="left" rowspan="9">供应商负责人签字盖章</td>
	        </tr>
	        <tr> 
	        	<td align="left" colspan="4">{{remark2}}</td>
	        </tr>
	        <tr>
	            <td align="center" width="50" rowspan="6">2 </td>
	            <td align="left" colspan="4">请一定按我司提供资料开票，如开错发票，由贵司负责所造成之一切问题</td>
	        </tr>
	        <tr> 
	        	<td align="center" width="100">公司全称：</td> 
	        	<td align="center" colspan="3">{{companyName}}</td>
	        </tr>
	        <tr> 
	    		<td align="center" width="100">公司地址：</td>
	    		<td align="center" colspan="3">{{address}}</td>
	    	</tr>
	    	<tr> 
	    		<td align="center" width="100">开户行：</td> 
	    		<td align="center" colspan="3">{{bankName}}</td>
	    	</tr>
	    	<tr> 
				<td align="center" width="100">账号：</td> 
				<td align="center" colspan="3">{{bankAccount}}</td>
			</tr>
			<tr> 
				<td align="center" width="100">税号：</td> 
				<td align="center" colspan="3">{{taxNo}}</td>
			</tr>
	        <tr>
	        	<td align="center" width="50">3 </td>
	        	<td align="center" width="100">制单人：</td>
	    		<td align="center" width="150">{{createUser}}</td>
	    		<td align="center" width="100">店长：</td>
	    		<td align="center" width="150"></td>
	        </tr>
	    </tbody>
	</table>
</div>
    `;
    let verticalTemplate = `
    <div class="receipt">
    	<div class="bTitle">{{shopName}}</div>
    	<div class="title">结算单</div>
        <table class="border" cellspacing="0">           
            <tbody>            
                <tr>
                	<td align="center">结算单号:</td>
                	<td align="center">{{billNo}}</td>                           	
                  	<td align="center">公司税号:</td>
                  	<td align="center">{{suTaxNo}}</td>
                </tr>
                <tr>
            		<td align="center">公司名称:</td>
            		<td align="center">{{suCompanyName}}</td>                          	
                	<td align="center">银行账号:</td>
                	<td align="center">{{suBankAccount}}</td>
                </tr>
                <tr>
                	<td align="center">公司地址:</td>
                	<td align="center">{{suAddress}}</td>                            
                	<td align="center">制单日期:</td>
                	<td align="center">{{printDate}}</td>
                </tr>
                <tr>
                	<td align="center">开户行及行号:</td>
                	<td align="center">{{suBankName}}</td>                  
                	<td align="center">结算区间:</td>
                	<td align="center">{{settleMonth}}</td>
                </tr>
                <tr>
	            	<td align="center">供应商:</td>
	            	<td align="center">{{supplierName}}</td>                   
	            	<td align="center">专柜:</td>
	            	<td align="center">{{counterName}}</td>
            	</tr>
            	<tr>
	 		        <td align="center">专柜编码</td>
	 	            <td align="center">{{counterNo}}</td>
	 	            <td align="center">银行账户名</td>
	 	            <td align="center">{{subBankAccountName}}</td>	            
	        	</tr> 
            </tbody>
        </table>
        <table class="border" style="width:98%;margin-left:1%;margin-bottom:5px;border:1px solid #000;border-top:none;border-right:none;" cellspacing="0">     
            <tbody>            
                <tr>
                	<td align="center">结算项目</td>
                    <td align="center">营业额</td>
                  	<td align="center">扣率</td> 
                  	<td align="center">抽成</td> 
                </tr>
                {{each counterSaleCostList as counterSale index}}
                <tr>	                     	
                  	<td align="center">{{counterSale.divisionNo}}{{counterSale.divisionName}}</td>
                  	<td align="center">{{counterSale.settleSum}}</td>
                  	<td align="center">{{counterSale.rateValue}}</td>   
                  	<td align="center">{{counterSale.profitAmount}}</td>                  	
                </tr>
                {{/each}}
        		<tr> 
              		<td align="center">合计1:</td>
              		<td align="center">{{settleSumTotal}}</td>
              		<td align="center">{{rateValueTotal}}</td>
              		<td align="center">{{profitAmountTotal}}</td>
              	</tr>
            </tbody>
        </table>
        <table class="border" cellspacing="0">   
    	    <tbody>            
    	        <tr>
    	        	<td align="center" rowspan={{UniqcounterCostList.length+1}}>费用扣款明细</td>
    	            <td align="center">扣款项目</td>
    	          	<td align="center">相关税费</td>
    	          	<td align="center">扣款金额</td>
    	          	<td align="center">含税金额</td>               	
    	        </tr>   	                 	        	
	        	{{each UniqcounterCostList as counter index}}
	        	<tr>	
    	          	<td align="center">{{counter.costName}}</td>
    	          	<td align="center">{{counter.taxAmount}}</td>
    	          	<td align="center">{{counter.ableAmount}}</td>
    	          	<td align="center">{{counter.ableSum}}</td>
	          	</tr>
	          	{{/each}}  	        
    	        <tr>
            		<td align="center"></td>
                	<td align="center">费用总计</td>
              		<td align="center">税费总计</td>
              		<td align="center">扣款金额总计</td> 
              		<td align="center">含税金额总计</td>
              	</tr>
              	<tr> 
              		<td align="center">合计2:</td> 
              		<td align="center">{{costTotal}}</td>
              		<td align="center">{{taxAmountTotal}}</td>
              		<td align="center">{{ableAmountTotal}}</td>
              		<td align="center">{{ableSumTotal}}</td>
        		</tr>
    	    </tbody>
    	</table>
    	<table class="border" cellspacing="0">   
	    <tbody>            
	        <tr>
	        	<td align="center" rowspan={{UniqOtherCostList.length+1}}>其他部分</td>
	            <td align="center">扣款项目</td>
	          	<td align="center">相关税费</td>
	          	<td align="center">扣款金额</td>              	
	        </tr>   	                 	        	
        	{{each UniqOtherCostList as other index}}
        	<tr>	
	          	<td align="center">{{other.costName}}</td>
	          	<td align="center">{{other.taxAmount}}</td>
	          	<td align="center">{{other.ableAmount}}</td>
          	</tr>
          	{{/each}}  	        
	        <tr>
        		<td align="center"></td>
            	<td align="center">费用总计</td>
          		<td align="center">税费总计</td>
          		<td align="center">扣款金额总计</td>              	
          	</tr>
          	<tr> 
          		<td align="center">合计3:</td> 
          		<td align="center">{{otherCostTotal}}</td>
          		<td align="center">{{otherTaxAmountTotal}}</td>
          		<td align="center">{{otherAbleAmountTotal}}</td>
    		</tr>
	    </tbody>
	</table>
    	<table class="border" cellspacing="0">    			
            <tbody>
                <tr>
                    <td align="center" colspan="2" width="50%">本期实际应付款</td>
                    <td align="center" colspan="2">{{ableSum}}</td>             
                </tr>
                <tr>
                    <td align="center" colspan="2">本期销售数量</td>
                    <td align="center" colspan="2">{{saleQty}}</td>              
                </tr>
                <tr>
    	            <td align="center" colspan="2">本期提供增值税发票金额</td>
    	            <td align="center" colspan="2">{{currentInvoice}}</td>              
    	        </tr>
    	        <tr>
	                <td align="center" colspan="2">税额</td>
	                <td align="center" colspan="2">{{currentTaxValue}}</td>              
                </tr>
                <tr>
	                <td align="center" colspan="2">价税合计</td>
	                <td align="center" colspan="2">{{ableBillingSum}}</td>              
                </tr>
            </tbody>
        </table>
        <table class="border" cellspacing="0">    			
        <tbody>
            <tr>
            	<td align="left" rowspan="11">注明</td>
                <td align="center" width="50">1 </td>
                <td align="left" colspan="4">我司将与各专柜确认结算单金额</td>             
            </tr>
            <tr>
                <td align="center" width="50" rowspan="2">2 </td>
                <td align="left" colspan="4">{{remark1}}</td>
            </tr>
            <tr> 
            	<td align="left" colspan="4">{{remark2}}</td>
            </tr>
            <tr>
                <td align="center" width="50" rowspan="6">3 </td>
                <td align="left" colspan="4">请一定按我司提供资料开票，如开错发票，由贵司负责所造成之一切问题。</td>
            </tr>
            <tr> 
            	<td align="center" width="100">公司全称：</td> 
            	<td align="center" colspan="3">{{companyName}}</td>
            </tr>
            <tr> 
        		<td align="center" width="100">公司地址：</td>
        		<td align="center" colspan="3">{{address}}</td>
        	</tr>
        	<tr> 
        		<td align="center" width="100">开户行：</td> 
        		<td align="center" colspan="3">{{bankName}}</td>
        	</tr>
        	<tr> 
    			<td align="center" width="100">账号：</td> 
    			<td align="center"colspan="3">{{bankAccount}}</td>
    		</tr>
    		<tr> 
    			<td align="center" width="100">税号：</td> 
    			<td align="center"colspan="3">{{taxNo}}</td>
    		</tr>
            <tr>
            	<td align="center" width="50" rowspan="2">4 </td>
            	<td align="left"colspan="4">厂商对本结算单有疑义，请于发票送达前与我司商务部联系。</td>
            </tr>
            <tr>      
        		<td align="center" width="100">联系人：</td>
        		<td align="center" width="150">{{createUser}}</td>     
    			<td align="center" width="100">联系人电话：</td>
    			<td align="center" width="150">{{tel}}</td>
    		</tr>
    		<tr>      
	    		<td align="center" colspan="3">厂商负责人签字、盖章：</td>  
				<td align="center" colspan="3">（另：请确认付款方式：A电汇 B汇票 C支票）</td>
			</tr>
        </tbody>
    </table>
    </div>
        `;
let supplierTemplate = `    
    <div class="receipt">
		<div class="bTitle">{{supplierName}}</div>
		<div class="title">结算单</div>
	    <table class="border" cellspacing="0">           
	        <tbody>            
	            <tr>
	            	<td align="center">结算单号:</td>
	            	<td align="center">{{billNo}}</td>                           	
	              	<td align="center">供应商税号:</td>
	              	<td align="center">{{suTaxNo}}</td>
	            </tr>
	            <tr>
	        		<td align="center">供应商名称:</td>
	        		<td align="center">{{supplierName}}</td>                          	
	            	<td align="center">银行账号:</td>
	            	<td align="center">{{suBankAccount}}</td>
	            </tr>
	            <tr>
	            	<td align="center">供应商地址:</td>
	            	<td align="center">{{suAddress}}</td>                            
	            	<td align="center">制单日期:</td>
	            	<td align="center">{{printDate}}</td>
	            </tr>
	            <tr>
	            	<td align="center">供应商开户行:</td>
	            	<td align="center">{{suBankName}}</td>                  
	            	<td align="center">结算区间:</td>
	            	<td align="center">{{settleMonth}}</td>
	            </tr>
	        </tbody>
	    </table>
	    <table class="border" cellspacing="0">
	    	<tbody>
		    	<tr>
		        	<td align="center">本期销售额</td>
		            <td align="center">{{saleAmount}}</td>
		          	<td align="center">本期销售数量</td> 
		          	<td align="center">{{saleQty}}</td>
		          	<td align="center">应结总额</td> 
		          	<td align="center">{{ableSum}}</td>
		        </tr>
		        <tr>
		        	<td align="center">开票价款</td>
		            <td align="center">{{currentInvoice}}</td>
		          	<td align="center">开票税款</td> 
		          	<td align="center">{{currentTaxValue}}</td>
		          	<td align="center">开票额</td> 
		          	<td align="center">{{ableBillingSum}}</td>
		        </tr>
	    	</tbody>
	    </table>
	    <table class="border" cellspacing="0">     
	        <tbody>            
	            <tr>
	            	<td align="center">门店名称</td>
	            	<td align="center">专柜名称</td>
	            	<td align="center">结算项目</td>
	                <td align="center">营业额</td>
	              	<td align="center">扣率</td> 
	              	<td align="center">抽成</td> 
	            </tr>
	            {{each counterSaleCostList as counterSale index}}
	            <tr>
	            	<td align="center">{{counterSale.shopName}}</td>   
	            	<td align="center">{{counterSale.counterName}}</td>
	              	<td align="center">{{counterSale.divisionNo}}{{counterSale.divisionName}}</td>
	              	<td align="center">{{counterSale.settleSum}}</td>
	              	<td align="center">{{counterSale.rateValue}}</td>   
	              	<td align="center">{{counterSale.profitAmount}}</td>                  	
	            </tr>
	            {{/each}}
	    		<tr> 
	          		<td align="center">合计:</td>
	          		<td align="center"></td>
	          		<td align="center"></td>
	          		<td align="center">{{settleSumTotal}}</td>
	          		<td align="center"></td>
	          		<td align="center">{{profitAmountTotal}}</td>
	          	</tr>
	        </tbody>
	    </table>
	    <table class="border" cellspacing="0">     
	        <tbody>            
	            <tr>
	            	<td align="center">门店名称</td>
	            	<td align="center">专柜名称</td>
	            	<td align="center">扣项名称</td>
	                <td align="center">扣项总额</td>
	              	<td align="center">扣项类别</td> 
	              	<td align="center">支付方式</td> 
	            </tr>
	            {{each UniqcounterCostList as counter index}}
	            <tr>
	            	<td align="center">{{counter.shopName}}</td>   
	            	<td align="center">{{counter.counterName}}</td>
	              	<td align="center">{{counter.costName}}</td>
	              	<td align="center">{{counter.ableSum}}</td>
	              	<td align="center">{{counter.billDebit}}</td>   
	              	<td align="center">{{counter.accountDebit}}</td>                  	
	            </tr>
	            {{/each}}
	    		<tr> 
	          		<td align="center">合计:</td>
	          		<td align="center"></td>
	          		<td align="center"></td>
	          		<td align="center">{{ableSumTotal}}</td>
	          		<td align="center">{{billDebitTotal}}</td>
	          		<td align="center">{{notBillDebitTotal}}</td>
	          	</tr>
	        </tbody>
	    </table>
	    <table class="border" cellspacing="0">    			
	    <tbody>
	        <tr>
	        	<td align="left" rowspan="10">注明</td>
	            <td align="center" width="50">1 </td>
	            <td align="left" colspan="2">我司将与各专柜确认结算单金额</td>             
	        </tr>
	        <tr>
	            <td align="center" width="50" rowspan="2">2 </td>
	            <td align="left" colspan="2">{{remark1}}</td>
	        </tr>
	        <tr> 
	        	<td align="left" colspan="2">{{remark2}}</td>
	        </tr>
	        <tr>
	            <td align="center" width="50" rowspan="6">3 </td>
	            <td align="left" colspan="2">请一定按我司提供资料开票，如开错发票，由贵司负责所造成之一切问题。</td>
	        </tr>
	        <tr> 
	        	<td align="center" width="100">公司全称：</td> 
	        	<td align="center">{{companyName}}</td>
	        </tr>
	        <tr> 
	    		<td align="center" width="100">公司地址：</td>
	    		<td align="center">{{address}}</td>
	    	</tr>
	    	<tr> 
	    		<td align="center" width="100">开户行：</td> 
	    		<td align="center">{{bankName}}</td>
	    	</tr>
	    	<tr> 
				<td align="center" width="100">账号：</td> 
				<td align="center">{{bankAccount}}</td>
			</tr>
			<tr> 
				<td align="center" width="100">税号：</td> 
				<td align="center">{{taxNo}}</td>
			</tr>
	        <tr>
	        	<td align="center" width="50">4 </td>
	        	<td align="left"colspan="4">供应商对本结算单有疑义，请于发票送达前与我司商务部联系。</td>
	        </tr>
			<tr>      
	    		<td align="center" colspan="3">供应商负责人签字、盖章：</td>  
				<td align="center">（另：请确认付款方式：A电汇 B支票 C汇票）</td>
			</tr>
	    </tbody>
	</table>
</div>
    `;
    function getTemplate(tmpl) {
        var html = `
        <style>
            /** 小票打印样式 **/
        	.title{
        		text-align: center;
        		font-size:16px;
        		margin-bottom:10px;
        		font-weight: 600;
        	}
            .bTitle{
				margin: 0;
				margin-top:5px ;
				padding: 0;
            	font-size:22px;
            	font-weight:600;
            	text-align: center;
			}
            div{
            	font-size:12px;
            	font-weight:600;
            }
			table,div{
				width:98%;
				margin-left: 1%;
				margin-bottom:5px;
				font-size: 10px;
				font-family:'Microsoft YaHei';
			}
			table.half{
				width:48.8%;				
				float:left;
			}						
			table.border,.border td{
				border:1px solid #000;
			}
			table.leftborder{
				margin-left:0;
				border-left:none;
			}
			table.border{
				border-top:none;
				border-right:none;
			}
			table.border td{
				border-left:none;
				border-bottom:none;
			}
			table.space{
				margin-bottom:5px;
			}
			.secondpart {
				width: 98%;
			}
			.secondpart table {
				margin-left: 0;
				width: 100%;
			}
			.receipt .rightTd{	 
				text-align: right;
				text-valign: bottom;
			}
			.receipt .leftTd{	 
				text-align: left;
				text-valign: bottom;
			}
        </style>
        <body>
        ${tmpl}
        </body>
        `;
        var render = template.compile(html);
        return render;
    }

    class CounterBalancePrinter {
        constructor(billInfo) {
            this.billInfo = billInfo;
            this.template = null;
            this.init();
        }

        init() {
            if (this.billInfo.templateType == 0) {
            	this.template = acrossTemplate;
                               
            }else if(this.billInfo.templateType == 1){
            	this.template = verticalTemplate;
            }else if(this.billInfo.templateType == 2){
            	this.template = supplierTemplate;
            }
            this.renderer = getTemplate(this.template);
        }

        initPrint() {
            if (this.LODOP == null) {
                this.LODOP = window.getLodop();
                LODOP.PRINT_INITA(0, 0, 210, 297, "结算单打印")
                LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "A4") ; //A4纸张纵向打印
            }
        }

        getPrintHtml(order) {
            this.initPrint();
            var data = $.extend({}, order);     	     
            let html = this.renderer(data);
            return html;
        }
        
        mergeCostNo(counterCostList){
        	let UniqcounterCostList = [];
        	let a = _.groupBy(counterCostList,function(e){
        		return e.costNo+e.counterNo;        	
    		});
        	$.each(a,(i,d)=>{        		
    			if(d.length == 1){			
    				if(this.billInfo.templateType == 0 && d[0].ableSum != 0)
    					UniqcounterCostList = UniqcounterCostList.concat(d);
    				if(this.billInfo.templateType == 1 && (d[0].taxAmount != 0 || d[0].ableAmount != 0))
    					UniqcounterCostList = UniqcounterCostList.concat(d);
    				if(this.billInfo.templateType == 2 && d[0].ableSum != 0)
    					UniqcounterCostList = UniqcounterCostList.concat(d);
    			}else if(d.length >= 1){
    				let list = {};
    				list.taxAmount = 0;
    				list.ableAmount = 0;
    				list.ableSum = 0;
    				list.shopName=d[0].shopName;
    				list.counterName=d[0].counterName;
    				$.each(d,(j,o)=>{   					
    					list.taxAmount += Number(o.taxAmount);
    					list.ableAmount += Number(o.ableAmount);
    					list.ableSum += Number(o.ableSum);
    					list.costName = o.costName;
    					list.billDebit = o.billDebit;
    					list.accountDebit = o.accountDebit;
    				});
    				list.taxAmount=list.taxAmount.toFixed(2);
    				list.ableAmount=list.ableAmount.toFixed(2);
    				list.ableSum=list.ableSum.toFixed(2);
    				if(this.billInfo.templateType == 0 && list.ableSum != 0)
    					UniqcounterCostList = UniqcounterCostList.concat(list);
    				if(this.billInfo.templateType == 1 && (list.taxAmount != 0 || list.ableAmount != 0))
    					UniqcounterCostList = UniqcounterCostList.concat(list);
    				if(this.billInfo.templateType == 2 && list.ableSum != 0)
    					UniqcounterCostList = UniqcounterCostList.concat(list);
    			}
    		});
        	return UniqcounterCostList;
        }
        //费用结算部分
        dealCounterCostList(order){
        	order.UniqcounterCostList = this.mergeCostNo(order.counterCostList);
        	//横排类型,费用结算分为两个list
        	var counterCostList1,counterCostList2;
        	if(_.isArray(order.UniqcounterCostList) && (order.UniqcounterCostList.length) % 2 == 0){
        		counterCostList1 = order.UniqcounterCostList.slice(0,(order.UniqcounterCostList.length)/2);
        		counterCostList2 = order.UniqcounterCostList.slice((order.UniqcounterCostList.length)/2);
        	}else if(_.isArray(order.UniqcounterCostList) && (order.UniqcounterCostList.length) % 2 != 0){
        		counterCostList1 = order.UniqcounterCostList.slice(0,(order.UniqcounterCostList.length + 1)/2);
        		counterCostList2 = order.UniqcounterCostList.slice((order.UniqcounterCostList.length + 1)/2);
        		counterCostList2.push({ableSum:'0.00',costName:null,billDebit:null});       		        		
        	}
        	order.taxAmountTotal = 0; //税费合计
        	order.ableAmountTotal=0;  //扣款金额合计        	
        	order.ableSumTotal = 0 //供应商--扣项总额
        	//按票扣方式分组计算
        	let billDebitGroup = _.groupBy(order.UniqcounterCostList,function(e){
        		return e.billDebit;        	
    		});
        	let flagDebit = 'billDebit';
        	//格式化票扣
        	order.billDebitTotal = 0;  //票扣费用合计
        	order.notBillDebitTotal = 0;  //非票扣费用合计 
        	this.formaterDebit(order,billDebitGroup,flagDebit);       	       	
        	//竖排税费合计        	        	
        	$.each(order.UniqcounterCostList,(i,c)=>{
    			order.taxAmountTotal += Number(c.taxAmount);
        		order.ableAmountTotal += Number(c.ableAmount);
        		order.ableSumTotal += Number(c.ableSum);
        	});  		
    		
        	order.taxAmountTotal = order.taxAmountTotal.toFixed(2);
        	order.ableAmountTotal = order.ableAmountTotal.toFixed(2);
        	order.ableSumTotal = order.ableSumTotal.toFixed(2);
        	
    		//开票总额 = 销售金额合计 - 销售扣项合计-票扣费用合计
    		order.invoiceAmountTotal = order.settleSumTotal - order.profitAmountTotal - order.billDebitTotal;    		
    		order.invoiceAmountTotal = order.invoiceAmountTotal.toFixed(2);
    		//竖排费用总计
        	order.costTotal = (Number(order.ableAmountTotal) + Number(order.taxAmountTotal)).toFixed(2);
        	$.extend(order,{counterCostList1:counterCostList1},{counterCostList2:counterCostList2});
        	//本次提供增值税发票金额: 价税合计/(1+税率)
        	order.currentInvoice = (Number(order.ableBillingSum) / (1 + order.raxRate /100)).toFixed(2);
        	//税额
        	order.currentTaxValue = (Number(order.ableBillingSum) - Number(order.currentInvoice)).toFixed(2);
        }
        
        //其他部分
        dealOtherCostList(order){
        	order.UniqOtherCostList = this.mergeCostNo(order.depositList);
        	//横排类型,费用结算分为两个list
        	var otherCostList1,otherCostList2;
        	if(_.isArray(order.UniqOtherCostList) && (order.UniqOtherCostList.length) % 2 == 0){
        		otherCostList1 = order.UniqOtherCostList.slice(0,(order.UniqOtherCostList.length)/2);
        		otherCostList2 = order.UniqOtherCostList.slice((order.UniqOtherCostList.length)/2);
        	}else if(_.isArray(order.UniqOtherCostList) && (order.UniqOtherCostList.length) % 2 != 0){
        		otherCostList1 = order.UniqOtherCostList.slice(0,(order.UniqOtherCostList.length + 1)/2);
        		otherCostList2 = order.UniqOtherCostList.slice((order.UniqOtherCostList.length + 1)/2);
        		otherCostList2.push({ableSum:'0.00',costName:null,accountDebit:null});       		        		
        	}        	
        	order.otherTaxAmountTotal = 0;  //其他税费合计
        	order.otherAbleAmountTotal = 0; //其他扣款金额合计
        	//按账扣方式分组计算
        	let accountDebitGroup = _.groupBy(order.UniqOtherCostList,function(e){
        		return e.accountDebit;        	
    		});
        	let flagDebit = 'accountDebit';
        	//格式化账扣
        	order.accountDebitTotal = 0;  //账扣扣费用合计
        	order.cashTotal = 0;  //现金费用合计 
        	this.formaterDebit(order,accountDebitGroup,flagDebit);
        	$.extend(order,{otherCostList1:otherCostList1},{otherCostList2:otherCostList2});
        	//应付款项  = 销售金额合计 - 销售扣项合计-费用合计{票扣+非票扣}-其他部分账扣合计
    		order.shouldSettleTotal = order.settleSumTotal - order.profitAmountTotal - order.billDebitTotal - order.notBillDebitTotal - order.accountDebitTotal;
    		order.shouldSettleTotal = order.shouldSettleTotal.toFixed(2);
    		//应付款项大写
        	order.payName = convertCurrency(order.ableSum);
        	//竖排其他部分税费合计        	        	
        	$.each(order.UniqOtherCostList,(i,c)=>{
    			order.otherTaxAmountTotal += Number(c.taxAmount);
        		order.otherAbleAmountTotal += Number(c.ableAmount);
        	}); 
        	order.otherTaxAmountTotal = order.otherTaxAmountTotal.toFixed(2);
        	order.otherAbleAmountTotal = order.otherAbleAmountTotal.toFixed(2);
        	//竖排其他费用总计
        	order.otherCostTotal = (Number(order.otherAbleAmountTotal) + Number(order.otherTaxAmountTotal)).toFixed(2);
        }
        //贷款结算部分
        dealCounterSaleCostList(order){
        	//合作方式转化
        	if(order.businessType == 1){
        		order.businessType = "联营";
    		}else if(order.businessType == 2){
    			order.businessType = "租赁";
    		}else if(order.businessType == 3){
    			order.businessType = "场地出租";
    		}else if(order.businessType == 4){
    			order.businessType = "其它";
    		}
        	order.settleSumTotal = 0;     //销售金额合计
        	order.saleQtyTotal = 0;       //开票数量合计
        	order.profitAmountTotal = 0;  //销售扣项合计
        	order.sellingCostTotal = 0;   
        	order.rateValueTotal = 0;
        	order.invoiceAmountTotal = 0;
        	order.shouldSettleTotal = 0;   //应付款项合计
        	if(_.isArray(order.counterSaleCostList)  && order.counterSaleCostList.length>0 ){
        		$.each(order.counterSaleCostList,(i,c)=>{
        			order.settleSumTotal += Number(Number(c.settleSum));
        			order.saleQtyTotal += Number(c.saleQty);
        			order.sellingCostTotal += Number(c.sellingCost);
        			order.rateValueTotal += Number(Number(c.rateValue));
        			order.profitAmountTotal += Number(c.profitAmount);
    			});
        		
        		//开票价额
//        		order.invoiceValue = (Number(order.sellingCostTotal) / (1 + Number(order.raxRate / 100))).toFixed(2);
        		//开票税额
//        		order.taxValue = (Number(order.sellingCostTotal) - order.invoiceValue).toFixed(2);
        		order.settleSumTotal = order.settleSumTotal.toFixed(2);
        		order.sellingCostTotal = order.sellingCostTotal.toFixed(2); 
        		order.rateValueTotal = order.rateValueTotal.toFixed(2);
        		order.profitAmountTotal = order.profitAmountTotal.toFixed(2); //销售扣项
        	}        	
        }
        //格式化账扣票扣等
        formaterDebit(order,list,flagDebit){
        	$.each(list,(i,c)=>{
        		if(i == 1){
        			$.each(c,(j,o)=>{
        				if(flagDebit == 'billDebit'){
        					o.billDebit = "票扣";
        					if(o.accountDebit == 1)
        						o.accountDebit = '账扣';
        					else if(o.accountDebit == 2)
        						o.accountDebit = '现金';
        					order.billDebitTotal += Number(o.ableSum);
        				}else if(flagDebit == 'accountDebit'){
        					o.accountDebit = "账扣";          				
            				order.accountDebitTotal += Number(o.ableSum);
        				}       				
    				});       			
        		}    			
        		if(i == 2){
        			$.each(c,(j,o)=>{        				
        				if(flagDebit == 'billDebit'){
        					o.billDebit = "非票扣";
        					if(o.accountDebit == 1)
        						o.accountDebit = '账扣';
        					else if(o.accountDebit == 2)
        						o.accountDebit = '现金';
        					order.notBillDebitTotal += Number(o.ableSum);
        				}else if(flagDebit == 'accountDebit'){
        					o.accountDebit = "现金";
        					order.cashTotal += Number(o.ableSum);
        				}       				        				
    				});       			
        		}   
        	});
        	order.billDebitTotal = Number(order.billDebitTotal).toFixed(2);
        	order.accountDebitTotal = Number(order.accountDebitTotal).toFixed(2);
        	order.notBillDebitTotal = Number(order.notBillDebitTotal).toFixed(2);
        	order.cashTotal = Number(order.cashTotal).toFixed(2);
        }
        print(order) {       	
        	this.dealCounterSaleCostList(order); 
        	this.dealCounterCostList(order);
        	this.dealOtherCostList(order);     	
    		var def = $.Deferred();
            window.setTimeout(()=>{
	    		let html = this.getPrintHtml(order);
	            LODOP.SET_PRINT_STYLE("FontSize", 10);
	            LODOP.SET_PRINT_STYLE("Alignment", 2);
	            LODOP.ADD_PRINT_HTM(20, 0, "100%", "100%", html);
//	            LODOP.PREVIEW();
	//          LODOP.PRINT_DESIGN();
	            LODOP.PRINT();
                def.resolve(true);
            },50);
            return def;
        }
         
        preview(order) {
            var def = $.Deferred();
            window.setTimeout(()=> {
                let html = this.getPrintHtml(order);
                LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", html);
                LODOP.PREVIEW();
                def.resolve(true);
            },50);
            return def;
        }
        
        prePdf(order){
        	this.dealCounterSaleCostList(order); 
        	this.dealCounterCostList(order);
        	this.dealOtherCostList(order); 
        	return this.getPrintHtml(order);
        }
    }

    module.exports = CounterBalancePrinter;
});