package com.clouway.gad.dispatch;

import com.clouway.gad.shared.Action;
import com.clouway.gad.shared.ActionHandlerNotBoundException;
import com.clouway.gad.shared.Response;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author mgenov@gmail.com (Miroslav Genov)
 * @since 1.0
 */
public class DefaultActionDispatcher implements ActionDispatcher {

  protected static final String HANDLER_NOT_BOUND_MESSAGE = "A handler for action [%s] has not been bound.";

  private final ActionHandlerRepository handlerRepository;
  private final Injector injector;

  /**
   * Constructs new DefaultActionDispatcher by providing the repository
   *
   * @param handlerRepository the handler repository
   */
  @Inject
  public DefaultActionDispatcher(ActionHandlerRepository handlerRepository, Injector injector) {
    this.handlerRepository = handlerRepository;
    this.injector = injector;
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


    com.clouway.gad.shared.ActionHandler actionHandler = action.getClass().getAnnotation(com.clouway.gad.shared.ActionHandler.class);

    ActionHandler handler = null;

    // actionHandler was provided for the selected action, so we can try to
    if (actionHandler != null) {

      Object injectedClass = injector.getInstance(actionHandler.value());

      if (!(injectedClass instanceof ActionHandler)) {
        throw new ActionHandlerNotBoundException(
                "The annotated ActionHandler is not implementing the ActionHandler interface. Be sure that the" +
                        " annotated class implements the ActionHandler interface.");
      }

      handler = (ActionHandler) injectedClass;

    } else {

      // search through the repository of registered action handlers
      handler = handlerRepository.getActionHandler(action.getClass());
    }

    // handler was not found, so we should indicate error
    if (handler == null) {
      String errorMessage = String.format(HANDLER_NOT_BOUND_MESSAGE, action.getClass().getName());
      throw new ActionHandlerNotBoundException(errorMessage);
    }

    T result = (T) handler.handle(action);

    return result;
  }


}
