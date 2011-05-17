package com.evo.gad.dispatch;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 *
 * This module contains the bindings common to all DispatchModule, and is bound exactly once per injector.
 * 
 * @author Miroslav Genov (mgenov@gmail.com)
 */
class InternalDispatchModule extends AbstractModule {


  protected void configure() {

    // using lazy action loading (using of guice providers to instantiate them only when they are needed)
    bind(ActionHandlerRepository.class).to(LazyActionHandlerRepository.class).in(Singleton.class);
    
    // default action dispatcher
    bind(ActionDispatcher.class).to(DefaultActionDispatcher.class).in(Singleton.class);

  }

   @Override
  public boolean equals(Object o) {
    // Is only ever installed internally, so we don't need to check state.
    return o instanceof InternalDispatchModule;
  }

  @Override
  public int hashCode() {
    return InternalDispatchModule.class.hashCode();
  }
}
