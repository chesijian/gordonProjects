package com.state.exception;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月9日下午5:16:31
 */
public class MsgException extends RuntimeException {

	/**
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午5:16:27
	 */
	private static final long serialVersionUID = 1L;
	public MsgException(String message, Throwable cause) {
	    super(message, cause);
	  }

	  public MsgException(String message) {
	    super(message);
	  }

}
