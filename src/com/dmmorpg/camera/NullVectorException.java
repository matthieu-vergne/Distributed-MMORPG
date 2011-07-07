package com.dmmorpg.camera;

/**
 * This exception is used to indicate the given vector is the null vector
 * (0,0,0).
 * 
 * @author Matthieu VERGNE <matthieu.vergne@gmail.com>
 * 
 */
@SuppressWarnings("serial")
public class NullVectorException extends RuntimeException {
	public NullVectorException() {
		super("the axis cannot be null");
	}

}
