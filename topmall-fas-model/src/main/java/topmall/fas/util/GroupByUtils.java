package topmall.fas.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

/**
 * 分组工具类
 * 
 * @author zeng.xa
 * @date 2013-12-9 下午6:26:42
 * @version 0.1.0 
 * @copyright yougou.com 
 */
public class GroupByUtils {

	/**
	 * 分组
	 * @param elements 待分组元素
	 * @param keyFunction 分组函数
	 * @return
	 */
	public static <T, K> Map<K, List<T>> groupByKey(Iterable<T> elements, Function<T, K> keyFunction) {
		if(elements==null){
			return new HashMap<K, List<T>>();
		}
		final ImmutableListMultimap<K, T> keysToElements = Multimaps.index(elements, keyFunction);
		return Multimaps.asMap(keysToElements);
	}


}
