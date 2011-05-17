package com.evo.gad.shared;


/**
 * Used to indicate that no handler has been bound for a specified action.
 * 
 * @author mgenov@gmail.com (Miroslav Genov)
 */
public class ActionHandlerNotBoundException extends RuntimeException {

  public ActionHandlerNotBoundException(String message) {
    super(message);
  }
}
