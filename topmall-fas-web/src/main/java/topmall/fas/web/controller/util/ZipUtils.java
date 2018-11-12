package topmall.fas.web.controller.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.fusesource.hawtbuf.ByteArrayInputStream;
import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.google.common.io.LineReader;

/**
 * 解压缩工具类
 * 
 * @author zhangcy
 * @date 2015-9-10 下午2:02:24
 * @version 0.1.0 
 * @copyright wonhigh.cn
 */
@SuppressWarnings("deprecation")
public class ZipUtils {
	
	private ZipUtils(){
		
	}
	
	/**
	 * 解压压缩的字符串
	 * @param zipData 压缩的字符串
	 * @return 返回解压后的字符串
	 * @throws ManagerException
	 */
	public static String unzipToString(String zipData) throws Exception{
		if(zipData == null || zipData.trim().length()<1){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		byte[] binaryData = javax.xml.bind.DatatypeConverter.parseBase64Binary(zipData);
		ByteArrayInputStream bis = new ByteArrayInputStream(binaryData);
		ZipInputStream zis = new ZipInputStream(bis);
		ZipEntry zipEntry = null;
		try {
			zipEntry = zis.getNextEntry();
			while (zipEntry != null && (zipEntry.isDirectory() || !zipEntry.getName().equalsIgnoreCase("data.txt"))) {
				zipEntry = zis.getNextEntry();
			}
			if (zipEntry != null) {
				LineReader lr = new LineReader(new InputStreamReader(zis));
				String line = lr.readLine();
			    while (line != null) {
			    	sb.append(line);
			    	line = lr.readLine();
			    }
			    return sb.toString();
			}
			return null;
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			try {
				if(zipEntry !=null)
					zipEntry.clone();
				if(zis !=null)
					zis.close();
				if(bis !=null)
					bis.close();
			} catch (IOException e) {
				throw new Exception(e);
			}
		}
	}
	
	/**
	 * 解压压缩的数组字符串，并返回数组
	 * @param zipData 压缩的数组字符串
	 * @return 返回字符串数组
	 * @throws ManagerException
	 */
	public static String[] unzipToArray(String zipData) throws Exception {
		String strData = unzipToString(zipData);
		if(strData == null || strData.trim().length()<1){
			return null;
		}
		try {
			String[] arrayData = JSON.parse(strData, String[].class);
			return arrayData;
		} catch (ParseException e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * 解压压缩的数组字符串，并返回数组
	 * @param zipData 压缩的数组字符串
	 * @return 返回字符串数组
	 * @throws ManagerException
	 */
	public static String[] unzipToArray(String[] zipData) throws Exception {
		if(zipData == null || zipData.length < 1){
			return null;
		}
		String[] arrayData = new String[zipData.length]; 
		for(int i = 0; i < zipData.length; i++){
			arrayData[i] = unzipToString(zipData[i]);
		}	
		return arrayData;
	}

}
