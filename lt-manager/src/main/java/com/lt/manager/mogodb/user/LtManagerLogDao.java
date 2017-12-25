package com.lt.manager.mogodb.user;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.lt.manager.bean.log.LtManagerLog;

public interface LtManagerLogDao {
	public void saveLog(LtManagerLog log);

	public Long queryLogCount(Integer staffId, Integer type,
			Boolean isSuccessed, Date startTime);

	public List<LtManagerLog> queryLogList(Integer staffId, Integer type,
			Boolean isSuccessed, Date startTime);

	public LtManagerLog queryLtManagerLog(Integer staffId,
			Integer type, Boolean isSuccessed);

	/**
	 * 
	 *
	 * 描述:通过条件查询多个日志
	 *
	 * @author 郭达望
	 * @created 2015年6月29日 上午9:17:32
	 * @since v1.0.0
	 * @param userId
	 *            用户id
	 * @param type
	 *            日志类型
	 * @param isSuccessed
	 *            是否成功
	 * @param pageSize
	 *            条数
	 * @return
	 * @return List<LtManagerLog>
	 */
	List<LtManagerLog> findLtManagerLogForList(Integer userId,
			Integer type, Boolean isSuccessed, Integer pageSize);

	long selectAllByParmCount(Boolean isSuccessed, String name,
			String lastLoginName, Integer id, Integer lastLoginId, String ip,
			String startDate, String endDate, int index, int size)
			throws ParseException;

	List<LtManagerLog> selectAllByParm(Boolean isSuccessed, String name,
			String lastLoginName, Integer id, Integer lastLoginId, String ip,
			String startDate, String endDate, int index, int size)
			throws ParseException;

}
