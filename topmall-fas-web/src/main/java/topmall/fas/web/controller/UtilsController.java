/** build at 2017-02-15 13:59:58 by user **/
package topmall.fas.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/utils")
public class UtilsController {
    
	private String getTemplateFolder() {
		 return "/fas/utils/";
	}
    
    @RequestMapping(value = "/{path}")
	public String toPage(@PathVariable String path) {
		return getTemplateFolder() + path;
	}
    
}







