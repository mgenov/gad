package com.evo.gad.dispatch;

import com.evo.gad.shared.Action;
import com.evo.gad.shared.ActionHandlerNotBoundException;
import com.evo.gad.shared.Response;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import static org.easymock.EasyMock.createMock;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "dispatch")
public class ActionDispatcherTest {

  private Injector injector;

  private ActionHandlerRepository repository;

  @BeforeTest
  public void pre() {
    injector =  Guice.createInjector(new DispatchModule() {
      @Override protected void configureHandlers() {
        dispatch(MockAction.class).through(MockHandler.class).in(Singleton.class);        
      }
    });
    
  }

  @Test
  public void testItShouldBindActionInSingletonScope() {
    Injector injector = Guice.createInjector(new DispatchModule() {
      @Override
      protected void configureHandlers() {
        dispatch(MockAction.class).through(NoScopedActionHandler.class).in(Singleton.class);
      }
    });
    
    NoScopedActionHandler instance1 = (NoScopedActionHandler) injector.getInstance(ActionHandlerRepository.class).getActionHandler(MockAction.class);
    NoScopedActionHandler instance2 = (NoScopedActionHandler) injector.getInstance(ActionHandlerRepository.class).getActionHandler(MockAction.class);

    assert  instance1 == instance2 : "different instances where returned in singleton scope ?";
  }


  @Test
  public void testItShouldDispatchActionToTheActionHandler() {
    
    // inject action dispatcher
    ActionDispatcher dispatcher = injector.getInstance(ActionDispatcher.class);

    MockHandler handler = (MockHandler) injector.getInstance(ActionHandlerRepository.class).getActionHandler(MockAction.class);
    handler.response = new MockResponse();

    MockResponse response = dispatcher.dispatch(new MockAction());

    assert response != null : "response was null";
  }

  @Test
  public void testItShouldSendRequestWithCorrectHandler() {

    // given
    final MockAction action = new MockAction();
    final MockResponse expectedResponse = new MockResponse();

    ActionDispatcher dispatcher = injector.getInstance(ActionDispatcher.class);

    ActionHandlerRepository repo = injector.getInstance(ActionHandlerRepository.class);

    MockHandler bindedHandler = (MockHandler) repo.getActionHandler(MockAction.class);
    bindedHandler.response = expectedResponse;

    MockResponse response = dispatcher.dispatch(action);

    assert response == expectedResponse : "different response has been returned";
  }

  @Test
  public void testItShouldThrowExceptionWhenActionHandlerHasNotBeenBound() {

    repository = createMock(ActionHandlerRepository.class);

    ActionDispatcher dispatcher = injector.getInstance(ActionDispatcher.class);

    try {
      dispatcher.dispatch(new UnMappedMockAction());
      fail("exception must be thrown");
    }
    catch (ActionHandlerNotBoundException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testItShouldHandleActionsBindedInDifferentModules() {
    Injector injector = Guice.createInjector(new FirstModule(), new SecondModule());

    FirstModuleAction firstAction = new FirstModuleAction();
    SecondModuleAction secondAction = new SecondModuleAction();

    ActionDispatcher dispatcher = injector.getInstance(ActionDispatcher.class);

    dispatcher.dispatch(firstAction);
    dispatcher.dispatch(secondAction);

    ActionHandlerRepository repo = injector.getInstance(ActionHandlerRepository.class);

    FirstModuleActionHandler firstHandler = (FirstModuleActionHandler) repo.getActionHandler(firstAction.getClass());
    SecondModuleActionHandler secondHandler = (SecondModuleActionHandler) repo.getActionHandler(secondAction.getClass());

    assert firstHandler.action == firstAction : "first action has not been dispatched";
    assert secondHandler.action == secondAction : "second action has not been dispatched";
  }

  class FirstModule extends DispatchModule {
    @Override
    protected void configureHandlers() {
      // make sure that first handler is bound as singleton
      dispatch(FirstModuleAction.class).through(FirstModuleActionHandler.class).in(Singleton.class);
    }
  }

  class SecondModule extends DispatchModule {

    @Override
    protected void configureHandlers() {
      // make sure that second handler is bound as singleton
      dispatch(SecondModuleAction.class).through(SecondModuleActionHandler.class).in(Singleton.class);
    }

  }

  static class NoScopedActionHandler implements ActionHandler<MockAction, MockResponse> {
    
    public MockResponse handle(MockAction action) {
      return null;
    }

  }

  static class FirstModuleAction implements Action<FirstModuleActionResponse> { }
  static class FirstModuleActionResponse implements Response { }

  static class FirstModuleActionHandler implements ActionHandler<FirstModuleAction, FirstModuleActionResponse> {
    public FirstModuleAction action;
    public FirstModuleActionResponse response;

    public FirstModuleAction getAction() {
      return action;
    }

    public FirstModuleActionResponse handle(FirstModuleAction action) {
      this.action = action;
      return response;
    }
  }



  static class SecondModuleAction implements Action<SecondModuleActionResponse> {  }
  static class SecondModuleActionResponse implements Response  {}


  static class SecondModuleActionHandler implements ActionHandler<SecondModuleAction, SecondModuleActionResponse> {
    public SecondModuleAction action;
    public SecondModuleActionResponse response;

    public SecondModuleActionResponse handle(SecondModuleAction action) {
      this.action = action;
      return response;
    }
  }

  class UnMappedMockAction implements Action {

  }
}
