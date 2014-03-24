package com.lunatech.activiti

import org.h2.jdbcx.JdbcDataSource
import org.slf4j.LoggerFactory
import scala.util.Random

/**
 * Executes a simple BPMN business process.
 */
object Main {

  // Set-up an H2 database.
  Class.forName("org.h2.Driver")
  val jdbcUrl = "jdbc:h2:activiti"
  implicit val ds = new JdbcDataSource()
  ds.setURL(jdbcUrl)

  val logger = LoggerFactory.getLogger("application")

  /**
   * Executes a simple Activiti process three times.
   */
  def main(arguments: Array[String]) {
    logger.info("Main")

    val processEngine = new ProcessEngineWrapper

    logger.info("Deploy business process")
    // (start) --flow1--> [example-task] --flow2--> (end)
    processEngine deploy {
      <process id="logging-test" name="Logging Test" isExecutable="true">
        <startEvent id="start" name="Start"></startEvent>
        <sequenceFlow id="flow1" sourceRef="start" targetRef="example-task"></sequenceFlow>
        <serviceTask id="example-task" name="Service Task" activiti:class={classOf[ExampleServiceTask].getName}></serviceTask>
        <sequenceFlow id="flow2" sourceRef="example-task" targetRef="end"></sequenceFlow>
        <endEvent id="end" name="End"></endEvent>
      </process>
    }

    logger.info("Execute business processes")
    for (i <- 1 to 3) {
      val processVariables = Map("number" -> number, "country" -> country, "currency" -> currency)
      processEngine execute ("logging-test", processVariables)
    }

    logger.info("Done")
  }

  // Random test data…

  private def number = Int.box(Random.nextInt.abs % 1000)

  private def currency: String = {
    import scala.collection.JavaConverters._
    val currencies = java.util.Currency.getAvailableCurrencies.asScala.map(_.getDisplayName).toArray
    currencies(Random.nextInt.abs % currencies.size)
  }

  private def country: String = {
    val countries = java.util.Locale.getISOCountries
    countries(Random.nextInt.abs % countries.size)
  }
}