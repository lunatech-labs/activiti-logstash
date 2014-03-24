package com.lunatech.activiti

import org.activiti.engine.delegate.event.ActivitiEventType._
import org.activiti.engine.delegate.event.{ActivitiEvent, ActivitiEventListener}
import org.slf4j.LoggerFactory

/**
 * Activity process execution listener that logs ‘activity started’ events.
 */
class LoggingEventListener extends ActivitiEventListener {

  override def onEvent(event: ActivitiEvent): Unit = event.getType match {
    case ACTIVITY_STARTED => log(event)
    case _ => ()
  }

  override def isFailOnException = false

  /**
   * Log the given process event, including process variable names and values.
   */
  private def log(event: ActivitiEvent): Unit = {
    import scala.collection.JavaConverters._
    val processVariables = event.getEngineServices.getRuntimeService.getVariablesLocal(event.getExecutionId).asScala
    val activities = event.getEngineServices.getRuntimeService.getActiveActivityIds(event.getExecutionId).asScala.mkString(",")
    val variables = Map("activity" -> activities, "execution" -> event.getExecutionId) ++ processVariables
    val formattedVariables = variables map formatKeyValue mkString " "
    LoggerFactory.getLogger("process." + event.getProcessDefinitionId).info(formattedVariables)
  }

  /**
   * Formats a key-value tuple as a string with an equals sign, and quotes if the value contains spaces. For example,
   * numbers='one two'. Note that key names are not quoted, and single quotes in values aren’t quoted.
   */
  private val formatKeyValue: PartialFunction[(String, AnyRef), String] = {
    case (key, value) => if (value.toString.contains(" ")) s"$key='$value'" else s"$key=$value"
  }

}