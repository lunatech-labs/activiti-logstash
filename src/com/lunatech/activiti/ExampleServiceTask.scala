package com.lunatech.activiti

import org.activiti.engine.delegate.{DelegateExecution, JavaDelegate}

/**
 * Example service task that does nothing to demonstrate logging, via the `LoggingEventListener`.
 */
class ExampleServiceTask extends JavaDelegate {

  override def execute(execution: DelegateExecution): Unit = {
    // Do nothing
  }
}