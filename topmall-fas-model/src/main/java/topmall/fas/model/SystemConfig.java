
package topmall.fas.model;

import cn.mercury.domain.BaseEntity;

/**
* fas系统参数配置
**/
public class SystemConfig  extends BaseEntity<String> { 
    /** auto generate start ,don't modify */
    private static final long serialVersionUID = 636493664896473222L;
    
    /**
    *大区编码
    **/
    private String zoneNo;
    public String getZoneNo(){
        return  zoneNo;
    }    
    public void setZoneNo(String val ){
        zoneNo = val;
    }
    
    /**
    *参数编号
    **/
    private String parameterNo;
    public String getParameterNo(){
        return  parameterNo;
    }    
    public void setParameterNo(String val ){
        parameterNo = val;
    }
    
    /**
    *参数名称
    **/
    private String parameterName;
    public String getParameterName(){
        return  parameterName;
    }    
    public void setParameterName(String val ){
        parameterName = val;
    }
    
    /**
    *状态(0:停用;1:启用)
    **/
    private Short status;
    public Short getStatus(){
        return  status;
    }    
    public void setStatus(Short val ){
        status = val;
    }
    
    /**
    *备注
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
		return "SystemConfig ["
            + "id="+getId()+",zoneNo="+zoneNo+",parameterNo="+parameterNo+",parameterName="+parameterName+",status="+status+",remark="+remark 
            + "]";
	}
    
     /** auto generate end,don't modify */
}