package com.clouway.gad.dispatch;

import com.clouway.gad.shared.Action;
import com.clouway.gad.shared.Response;
import com.clouway.gad.shared.ActionHandlerNotBoundException;

/**
 * ActionDispatcher represents a Dispatcher class whose responsibility is to dispatch
 * the incomming actions by proper action handlers.
 * <p/>

 * @author Miroslav Genov (mgenov@gmail.com)
 */
public interface ActionDispatcher {

  /**
   * Dispatches the provided action to a proper handler that is responsible for handling of that action.
   * <p/> To may dispatch the incomming action a proper handler needs to be bound
   * to the {@link com.clouway.gad.dispatch.ActionHandlerRepository} to may the dispatch method dispatch the
   * incomming request to it.
   *    
   * @param action the action to be handled
   * @param <T> a generic response type
   * @return response from the provided execution
   * @throws ActionHandlerNotBoundException is thrown in cases where no handler has been bound to handle that action
   */
  <T extends Response> T dispatch(Action<T> action) throws ActionHandlerNotBoundException;
  
}
