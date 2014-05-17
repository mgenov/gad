package com.clouway.gad.dispatch;

import com.clouway.gad.shared.Action;
import com.clouway.gad.shared.Response;
import com.google.inject.binder.ScopedBindingBuilder;

/**
 * ActionBinder is a binding interface which provides the {@link DispatchModule} with
 * ability to bind {@link Action}'s and there handlers in declarative manner.
 *
 * @author mgenov@gmail.com (Miroslav Genov)
 */
interface ActionBinder {

  HandleThroughtBinding dispatch(Class<? extends Action> action);

  static interface HandleThroughtBinding {

    ScopedBindingBuilder through(Class<? extends ActionHandler<? extends Action,? extends Response>> handler);

  }

}
