package com.clouway.gad.dispatch;

import com.clouway.gad.shared.Action;
import com.clouway.gad.shared.ActionHandlerNotBoundException;
import com.clouway.gad.shared.Response;
import com.google.inject.Guice;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "dispatch")
public class AnnotationBasedDispatchingTest {

  @com.clouway.gad.shared.ActionHandler(DummyActionHandler.class)
  static class AnnotatedAction implements Action<AnnotatedResponse> {
    public String value;

    @SuppressWarnings("unused")
    public AnnotatedAction() {
    }

    public AnnotatedAction(String value) {
      this.value = value;
    }
  }

  static class AnnotatedResponse implements Response {
    public String name;

    @SuppressWarnings("unused")
    AnnotatedResponse() {

    }

    public AnnotatedResponse(String name) {
      this.name = name;
    }
  }


  @com.clouway.gad.shared.ActionHandler(DummyClass.class)
  static class WrongAction implements Action<AnnotatedResponse> {

  }


  static class DummyActionHandler implements ActionHandler<AnnotatedAction, AnnotatedResponse> {

    public AnnotatedResponse handle(AnnotatedAction action) {
      return new AnnotatedResponse(action.value);
    }

  }

  static class DummyClass {

  }

  @Test
  public void happyPath() {
    DefaultActionDispatcher dispatcher = Guice.createInjector(new DispatchModule()).getInstance(DefaultActionDispatcher.class);

    AnnotatedAction action = new AnnotatedAction("value1");
    AnnotatedResponse response = dispatcher.dispatch(action);

    assert response.name.equals(action.value) : "first action has not been dispatched";
  }

  @Test
  public void annotatedTargetClassIsNotActionHandler() {
    DefaultActionDispatcher dispatcher = Guice.createInjector(
            new DispatchModule()).getInstance(DefaultActionDispatcher.class);

    try {
      dispatcher.dispatch(new WrongAction());
      fail("exception must be thrown");
    } catch (ActionHandlerNotBoundException e) {
      assertTrue(true);
    }
  }


}
