package com.dmmorpg.camera;

/**
 * This exception is used to indicate the given several vectors are not orthogonal.
 * 
 * @author Matthieu VERGNE <matthieu.vergne@gmail.com>
 * 
 */
@SuppressWarnings("serial")
public class NotOrthogonalVectorsException extends RuntimeException {
	public NotOrthogonalVectorsException() {
		super("the vectors are not orthogonal");
	}

}
