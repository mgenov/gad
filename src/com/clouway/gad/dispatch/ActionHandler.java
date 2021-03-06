package com.clouway.gad.dispatch;

import com.clouway.gad.shared.Action;
import com.clouway.gad.shared.Response;

/**
 * ActionHandler is an handler class which is handling incomming actions.
 *
 *
 *
 * @author mgenov@gmail.com (Miroslav Genov)
 */
public interface ActionHandler<K extends Action, T extends Response> {

  /**
   * Handles the provided action and retuns response of it.
   *
   * @param action the action to be handled
   * @return the response to be returned
   */
  T handle(K action);

}
