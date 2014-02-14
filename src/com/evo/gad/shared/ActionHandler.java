package com.evo.gad.shared;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation on {@link com.evo.gad.shared.Action} classes specifying the domain
 * (server-side) object type.
 *
 * @author Miroslav Genov (mgenov@gmail.com)
 * @see com.evo.gad.shared.ActionHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActionHandler {

  /**
   * The ActionHandler type that handles the annotated action
   */
  Class<?> value();
}
