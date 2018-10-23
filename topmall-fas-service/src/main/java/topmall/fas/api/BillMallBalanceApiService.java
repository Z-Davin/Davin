/** build at 2017-10-25 10:22:04 by Administrator **/
package topmall.fas.api;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import cn.mercury.service.api.AbstractApiService;
import cn.mercury.manager.IManager;
import tomall.fas.api.IBillMallBalanceApiService;
import topmall.fas.model.BillMallBalance;
import topmall.fas.manager.IBillMallBalanceManager;

@Service 
public class BillMallBalanceApiService extends AbstractApiService<BillMallBalance,String>  implements IBillMallBalanceApiService {
    @Autowired
    private IBillMallBalanceManager manager;
    
    @Override
    protected IManager<BillMallBalance,String> getManager(){
        return manager;
    }
    
}







