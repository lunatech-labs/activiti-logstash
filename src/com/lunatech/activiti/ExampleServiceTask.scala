package com.lunatech.activiti

import org.activiti.engine.delegate.{DelegateExecution, JavaDelegate}
import org.slf4j.LoggerFactory

/**
 * Example service task to demonstrate logging.
 */
class ExampleServiceTask extends JavaDelegate {

  override def execute(execution: DelegateExecution): Unit = {
    log(execution)
  }

  /**
   * Log the given process execution, including process variable names and values.
   */
  private def log(execution: DelegateExecution): Unit = {
    import scala.collection.JavaConverters._
    val processVariables = execution.getVariables.asScala map formatKeyValue mkString " "
    LoggerFactory.getLogger("process." + execution.getProcessDefinitionId).info(processVariables)
  }

  /**
   * Formats a key-value tuple as a string with an equals sign, and quotes if the value contains spaces. For example,
   * numbers='one two'. Note that key names are not quoted, and single quotes in values aren’t quoted.
   */
  private val formatKeyValue: PartialFunction[(String, AnyRef), String] = {
    case (key, value) => if (value.toString.contains(" ")) s"$key='$value'" else s"$key=$value"
  }
}