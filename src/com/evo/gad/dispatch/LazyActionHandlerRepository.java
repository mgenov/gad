package com.evo.gad.dispatch;

import com.evo.gad.shared.Action;
import com.evo.gad.shared.Response;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Map;

/**
 * LazyActionHandlerRepository is a ActionHandlerRepository storage that is holding all bounded
 * action handlers that where used by the {@link ActionDispatcher} class.
 * <p> The dispatcher class is using the {@link ActionHandlerRepository} to
 * retrieve the {@link ActionHandler} by providing the type of the action.
 *
 * <p> The implementation is <code>lazy</code> because it uses Guice's provider to instantiate the
 * proper handler when it is required.
 *
 * <p> LazyActionHandlerRepository is using all action handlers that are bound by{@link com.google.inject.multibindings.MapBinder}
 * class to {@code  Map<Class<? extends Action>, Provider<ActionHandler<? extends Action, ? extends Response>>>}
 *
 * @author mgenov@gmail.com (Miroslav Genov)
 */
class LazyActionHandlerRepository implements ActionHandlerRepository {
  private final Map<Class<? extends Action>, Provider<ActionHandler<? extends Action, ? extends Response>>> handlers;

  @Inject
  public LazyActionHandlerRepository(
      Map<Class<? extends Action>, Provider<ActionHandler<? extends Action, ? extends Response>>> handlers) {
    this.handlers = handlers;
  }

  /**
   * Gets {@link com.evo.gad.dispatch.ActionHandler} from the repository by providing the type of the
   * action class.
   *
   * <p/> 
   *
   * @param actionClass the action class
   * @return the ActionHandler that is responsible for handling of the provided action 
   */
  public ActionHandler getActionHandler(Class<? extends Action> actionClass) {

    Provider<ActionHandler<? extends Action, ? extends Response>> provider = handlers
        .get(actionClass);

    if (provider == null) {

      return null;
    }

    return provider.get();
  }
}
