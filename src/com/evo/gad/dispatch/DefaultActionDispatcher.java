package com.evo.gad.dispatch;

import com.evo.gad.shared.Action;
import com.evo.gad.shared.ActionHandlerNotBoundException;
import com.evo.gad.shared.Response;
import com.google.inject.Inject;

/**
 * @author mgenov@gmail.com (Miroslav Genov)
 * @since 1.0
 */
public class DefaultActionDispatcher implements ActionDispatcher {

  protected static final String HANDLER_NOT_BOUND_MESSAGE = "A handler for action [%s] has not been bound.";
  private final ActionHandlerRepository handlerRepository;

  /**
   * Constructs new DefaultActionDispatcher by providing the repository
   *
   * @param handlerRepository the handler repository
   */
  @Inject
  public DefaultActionDispatcher(ActionHandlerRepository handlerRepository) {
    this.handlerRepository = handlerRepository;
  }

  /**
   * Executes the provided action by finding the right {@link ActionHandler}
   * that is responsible for the handling of the provided action. After the ActionHandler is found
   *
   * @param action the action to be executed
   * @param <T>    the type of the response
   * @return the result of the provided action
   */
  public <T extends Response> T dispatch(Action<T> action) {

    ActionHandler handler = handlerRepository.getActionHandler(action.getClass());

    if (handler == null) {
      String errorMessage = String.format(HANDLER_NOT_BOUND_MESSAGE, action.getClass().getName());
      throw new ActionHandlerNotBoundException(errorMessage);
    }

    T result = (T) handler.handle(action);

    return result;
  }
}
