package com.evo.gad.dispatch;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
public class MockHandler implements ActionHandler<MockAction, MockResponse> {

  public MockResponse response;
  public MockAction action;

  public MockResponse handle(MockAction action) {
    this.action = action;
    return response;
  }
}
