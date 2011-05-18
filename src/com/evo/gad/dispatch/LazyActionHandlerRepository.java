package com.evo.gad.dispatch;

import com.evo.gad.shared.Action;
import com.evo.gad.shared.Response;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * LazyActionHandlerRepository is a ActionHandlerRepository storage that is holding all bounded
 * action handlers that where used by the {@link ActionDispatcher} class.
 * <p> The dispatcher class is using the {@link ActionHandlerRepository} to
 * retrieve the {@link ActionHandler} by providing the type of the action.
 * <p/>
 * <p> The implementation is <code>lazy</code> because it uses Guice's provider to instantiate the
 * proper handler when it is required.
 * <p/>
 * <p> LazyActionHandlerRepository is using all action handlers that are bound by{@link com.google.inject.multibindings.MapBinder}
 * class to {@code Map<Class<? extends Action>, Provider<ActionHandler<? extends Action, ? extends Response>>>}
 *
 * @author mgenov@gmail.com (Miroslav Genov)
 */
class LazyActionHandlerRepository implements ActionHandlerRepository {
  private final Injector injector;
  private final ImmutableMap<Class<? extends Action>, Class<? extends ActionHandler<? extends Action, ? extends Response>>> handlers;

  @Inject
  public LazyActionHandlerRepository(
          Set<ActionHandlerMetadata> actionHandlerMetadata,
          Injector injector) {

    this.injector = injector;
    Map<Class<? extends Action>, Class<? extends ActionHandler<? extends Action, ? extends Response>>> map
            = new HashMap<Class<? extends Action>, Class<? extends ActionHandler<? extends Action, ? extends Response>>>();
    for (ActionHandlerMetadata meta : actionHandlerMetadata) {
      map.put(meta.getAction(), meta.getHandler());
    }
    handlers = ImmutableMap.copyOf(map);
  }


  /**
   * Gets {@link com.evo.gad.dispatch.ActionHandler} from the repository by providing the type of the
   * action class.
   * <p/>
   * <p/>
   *
   * @param actionClass the action class
   * @return the ActionHandler that is responsible for handling of the provided action
   */
  public ActionHandler getActionHandler(Class<? extends Action> actionClass) {

    if (!handlers.containsKey(actionClass)) {
      return null;
    }

    Class<? extends ActionHandler<? extends Action, ? extends Response>> handler = handlers.get(actionClass);

    ActionHandler<? extends Action, ? extends Response> instance = injector.getInstance(Key.get(handler));

    return instance;
  }
}
