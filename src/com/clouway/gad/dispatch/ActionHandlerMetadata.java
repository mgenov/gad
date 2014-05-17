package com.clouway.gad.dispatch;

import com.clouway.gad.shared.Action;
import com.clouway.gad.shared.Response;

/**
 * @author Mihail Lesikov (mlesikov@gmail.com)
 */
public class ActionHandlerMetadata {
  private Class<? extends Action> action;
  private Class<? extends ActionHandler<? extends Action, ? extends Response>> handler;

  public ActionHandlerMetadata(Class<? extends Action> action, Class<? extends ActionHandler<? extends Action, ? extends Response>> handler) {
    this.action = action;
    this.handler = handler;
  }

  public Class<? extends Action> getAction() {
    return action;
  }

  public Class<? extends ActionHandler<? extends Action, ? extends Response>> getHandler() {
    return handler;
  }
}
