package com.lt.controller.user.bean;

/**   
* 项目名称：lt-transfer   
* 类名称：UserServiceMapperTransfer   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年5月17日 下午8:35:18      
*/
public class UserServiceMapperTransfer {
	
	private String user_id ;
	
	private String service_code ;
	
	private Integer status ;
	
	private String open_time ;
	
	private String create_date;

	
	public UserServiceMapperTransfer(){
		
	}
	
	public UserServiceMapperTransfer(String user_id,String service_code,Integer status
			,String open_time,String create_date){
		this.user_id = user_id;
		this.service_code = service_code;
		this.status = status;
		this.create_date = create_date;
		this.open_time = open_time ;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the service_code
	 */
	public String getService_code() {
		return service_code;
	}

	/**
	 * @param service_code the service_code to set
	 */
	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the open_time
	 */
	public String getOpen_time() {
		return open_time;
	}

	/**
	 * @param open_time the open_time to set
	 */
	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	/**
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	
}
