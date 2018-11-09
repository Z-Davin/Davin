package topmall.fas.api;



import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import cn.mercury.service.api.AbstractApiService;
import cn.mercury.manager.IManager;
import tomall.fas.api.IMallSaleCostApiService;
import topmall.fas.model.MallSaleCost;
import topmall.fas.manager.IMallSaleCostManager;
 
@Service 
public class MallSaleCostApiService extends AbstractApiService<MallSaleCost,String> implements IMallSaleCostApiService {
    @Autowired
    private IMallSaleCostManager manager;
    
    @Override
    protected IManager<MallSaleCost,String> getManager(){
        return manager;
    }
    
}


