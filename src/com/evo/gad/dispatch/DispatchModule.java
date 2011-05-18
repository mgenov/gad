package com.evo.gad.dispatch;

import com.evo.gad.shared.Action;
import com.evo.gad.shared.Response;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Configures {@link com.evo.gad.dispatch.ActionDispatcher} to may dispatch {@link com.evo.gad.dispatch.ActionHandler} classes so
 * you can bind {@link com.evo.gad.shared.Action} classes to there handlers.
 * <p/>
 *
 * @author mgenov@gmail.com (Miroslav Genov)
 */
public class DispatchModule extends AbstractModule {

  /**
   * Constructs a new DispatcherModule module.
   * <p/>
   */
  public DispatchModule() {

  }

  /**
   * Configures binder throught exposed methods.
   */
  @Override
  protected final void configure() {

    // install internal bindings (skipped if already installed)
    install(new InternalDispatchModule());

    // configure handlers
    configureHandlers();

    TypeLiteral<ActionHandler<? extends Action, ? extends Response>> actionHandlerType = new TypeLiteral<ActionHandler<? extends Action, ? extends Response>>() {
    };

    // bind all handlers
    TypeLiteral<ActionHandlerMetadata> actionHandlerPairTypeLiteral = new TypeLiteral<ActionHandlerMetadata>() {
    };

    Multibinder<ActionHandlerMetadata> actionHandlerPairMultibinder = Multibinder.newSetBinder(binder(), actionHandlerPairTypeLiteral);

    for (ActionLinkingBinder binding : bindings) {
      actionHandlerPairMultibinder.addBinding().toInstance(new ActionHandlerMetadata(binding.action, binding.handler));

      if (binding.asEagerSingleton) {
        bind(binding.handler).asEagerSingleton();

      } else if (binding.scope != null) {
        bind(binding.handler).in(binding.scope);

      } else if (binding.annotation != null) {
        bind(binding.handler).in(binding.annotation);

      }

    }

  }

  /**
   * Binds action to a handler using <code>embedded domain-specific language</code> (EDSL).
   * <p/>
   * <p/> Here is a typical example of registering a new handler when creating your Guice injector:
   * <pre>
   *
   *  Injector injector =  Guice.createInjector(new DispatchModule() {
   *
   *   protected void configureHandlers() {
   *     dispatch(GetUsersAction.class).through(GetUsersActionHandler.class).in(Singleton.class);
   *   }
   *
   * }
   *
   * </pre>
   */
  protected void configureHandlers() {

  }

  private final List<ActionLinkingBinder> bindings = Lists.newArrayList();

  private final ActionBindingBuilder actionBuilder = new ActionBindingBuilder();

  public final ActionBinder.HandleThroughtBinding dispatch(Class<? extends Action> action) {
    return actionBuilder.dispatch(action);
  }

  class ActionBindingBuilder implements ActionBinder {

    public final ActionBinder.HandleThroughtBinding dispatch(Class<? extends Action> action) {
      ActionLinkingBinder binding = new ActionLinkingBinder(action);
      bindings.add(binding);
      return binding;
    }

  }


  class ActionLinkingBinder implements ActionBinder.HandleThroughtBinding, ScopedBindingBuilder {

    Class<? extends Action> action;
    Class<? extends ActionHandler<? extends Action, ? extends Response>> handler;

    Class<? extends Annotation> annotation;
    Scope scope;
    boolean asEagerSingleton;

    public ActionLinkingBinder(Class<? extends Action> action) {
      this.action = action;
    }

    public ScopedBindingBuilder through(Class<? extends ActionHandler<? extends Action, ? extends Response>> handler) {
      Preconditions.checkNotNull(handler);
      this.handler = handler;
      return this;
    }

    public void in(Class<? extends Annotation> annotation) {
      Preconditions.checkNotNull(annotation);
      this.annotation = annotation;
    }

    public void in(Scope scope) {
      Preconditions.checkNotNull(scope);
      this.scope = scope;
    }

    public void asEagerSingleton() {
      asEagerSingleton = true;
    }

  }

}
