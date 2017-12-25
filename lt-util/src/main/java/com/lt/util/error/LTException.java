package com.lt.util.error;

public class LTException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7149402466125864688L;

	public LTException(){
		super();
	}
	
	public LTException(String message){
		super(message);
	}

	public LTException(Throwable cause) {
        super(cause);
    }

    public LTException(String message, Throwable cause) {
        super(message, cause);
    }
}
