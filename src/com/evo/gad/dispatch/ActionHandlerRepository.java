package com.evo.gad.dispatch;

import com.evo.gad.shared.Action;

/**
 * ActionHandlerRepository is a repository of all action classes that are bound to a single injector.
 * <p/> If different injectors are used then they are using different repository per injector and the
 * behaviour of the application could be different.
 * 
 * <p/> ActionHandlerRepository is not a thread-safe
 * 
 * @author mgenov@gmail.com (Miroslav Genov)
 */
public interface ActionHandlerRepository {
  ActionHandler getActionHandler(Class<? extends Action> aClass);
}
