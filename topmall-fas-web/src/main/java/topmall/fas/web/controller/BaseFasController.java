package topmall.fas.web.controller;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import topmall.fas.enums.BillExceptionTypeEnums;
import topmall.fas.util.CommonResult;
import topmall.fas.util.CommonUtil;
import topmall.fas.web.controller.util.ZipUtils;
import topmall.framework.io.pdf.PdfBuilder;
import topmall.framework.web.controller.BaseController;
import cn.mercury.basic.query.PageResult;
import cn.mercury.basic.query.Pagenation;
import cn.mercury.basic.query.Query;
import cn.mercury.domain.BaseEntity;
import cn.mercury.manager.IManager;

@SuppressWarnings("rawtypes")
@Controller
public abstract class BaseFasController<B extends BaseEntity,K> extends BaseController<B, K> {

	protected abstract IManager<B, K> getManager();
	
	protected abstract String getTemplateFolder();
	
	/**
	 * test抛异常提示信息
	 * @param ex
	 * @param request
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public CommonResult handleServiceException(Exception ex, HttpServletRequest request) {
		String errorDetail = getStackTrace(ex);
		logger.info("业务系统内部异常>>>>>>>>>" + request.getRequestURI());
		logger.error(ex.getMessage());
		logger.error(errorDetail);
		CommonResult resultVo = new CommonResult();
		if (StringUtils.isNotBlank(ex.getMessage())) {
			if (ex.getMessage().indexOf(":") > 0) {
				if (ex.getMessage().indexOf("Exception:") > 0) {
					if (ex.getMessage().indexOf("dal.database") > 0) {
						resultVo.setErrorMessage(BillExceptionTypeEnums.DATAEXCEPTION.getText());
					} else if (ex.getMessage().indexOf("exception.RpcException") > 0 || ex.getMessage().indexOf(".api.") > 0) {
						resultVo.setErrorMessage(BillExceptionTypeEnums.APIEXCEPTION.getText());
					} else {
						resultVo.setErrorMessage(BillExceptionTypeEnums.SYSTEMEXCEPTION.getText());
					}
				} else {
					String message = ex.getMessage().substring(0, ex.getMessage().indexOf(":"));
						resultVo.setErrorMessage(message);
				}
				if (StringUtils.isBlank(resultVo.getErrorDefined())) {
					String defined = ex.getMessage().substring(ex.getMessage().lastIndexOf(":") + 1);
					resultVo.setErrorDefined(defined);
				}
			} else {
				resultVo.setErrorMessage(ex.getMessage());
			}
		} else {
			resultVo.setErrorMessage("业务系统内部异常");
		}
		resultVo.setErrorDetail(errorDetail);// 堆栈信息不在页面展示
		if (resultVo.getErrorDefined() == null) {
			resultVo.setErrorDefined("");
		}
		return resultVo;
	}
	
	/**
	 * 获取异常的堆栈信息
	 * 
	 * @param t
	 * @return
	 */
	protected static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/list")
	@Override
	public PageResult<B> selectByPage(Query query, Pagenation page) {

		long total = page.getTotal();
		if (total < 0) {
			total = getManager().selectCount(query);
		}
		
		if(!CommonUtil.hasValue(query.getSort())){
			query.orderby("update_time",true);
		}
		List<B> rows = getManager().selectByPage(query, page);
		return new PageResult<>(rows, total);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/getPdf")
	public void getPdf(String fileName, String data,HttpServletResponse response)throws Exception  {
		String html = ZipUtils.unzipToString(data);
		exportPdf(html,fileName,response);
	}
	
	public void exportPdf(String html,String fileName,HttpServletResponse response) throws Exception{
		response.setContentType("application/pdf");  
        response.setHeader("Content-Disposition", "attachment; filename="+fileName+".pdf");
        response.setHeader("Pragma", "no-cache");
        try {
			PdfBuilder.html2Pdf(new ByteArrayInputStream(html.getBytes()) , response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			throw new Exception("导出pdt时异常！"+e.getMessage(),e);
		}finally {
			 response.getOutputStream().close();
        }
	}
	


}
