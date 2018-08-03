package metrics4j

import org.apache.logging.log4j.{Level, LogManager}
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.core.config.builder.api._
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration
import org.apache.logging.log4j.scala.Logger


object Configured {
  val indicator: String = "configured"

  // Configuration
  val builder: ConfigurationBuilder[BuiltConfiguration] = ConfigurationBuilderFactory.newConfigurationBuilder()

  // Appenders
  val console: AppenderComponentBuilder = builder.newAppender("stdout", "Console")
  builder.add(console)

  val file: AppenderComponentBuilder = builder.newAppender("log", "File")
  file.addAttribute("fileName", "logging.log")
  builder.add(file)

  val rollingFile: AppenderComponentBuilder = builder.newAppender("rolling", "RollingFile")
  rollingFile.addAttribute("fileName", "rolling.log")
  rollingFile.addAttribute("filePattern", "rolling-%d{MM-dd-yy}.log.gz")
  rollingFile.addAttribute("policy", "whatevs")
  builder.add(rollingFile)

  // Filters
  val flow: FilterComponentBuilder = builder.newFilter("MarkerFilter", Filter.Result.ACCEPT, Filter.Result.DENY)
  flow.addAttribute("marker", "FLOW")
  console.add(flow)
  file.add(flow)

  // Layout
  val standard: LayoutComponentBuilder = builder.newLayout("PatternLayout")
  standard.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable")
  console.add(standard)
  file.add(standard)
  rollingFile.add(standard)

  // Root logger
  val rootLogger: RootLoggerComponentBuilder = builder.newRootLogger(Level.ERROR)
  rootLogger.add(builder.newAppenderRef("stdout"))
  rootLogger.add(builder.newAppenderRef("log"))
  rootLogger.add(builder.newAppenderRef("rolling"))
  builder.add(rootLogger)

  // More loggers
  val logger2: LoggerComponentBuilder = builder.newLogger("com", Level.DEBUG)
  logger2.add(builder.newAppenderRef("log"))
  logger2.addAttribute("additivity", false)
  builder.add(logger2)

  // Triggering
  val triggeringPolicies: ComponentBuilder[Nothing] = builder.newComponent("Policies")
  triggeringPolicies.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
  triggeringPolicies.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"))
  rollingFile.addComponent(triggeringPolicies)
  builder.add(rollingFile)

  // initialize
  Configurator.initialize(builder.build())

  // write xml config
  builder.writeXmlConfiguration(System.out)

  val logger = LogManager.getLogger()
  //val logger = Logger(getClass)

  logger.error("test log")


  def main(args: Array[String]): Unit = {
    println(s"indicator: $indicator")
    val msg = "Doing stuff with arbitrary level"
    print(s"Trying log level: ${Level.DEBUG}\t:  "); logger.debug(msg); println()
    print(s"Trying log level: ${Level.INFO}\t:  "); logger.info(msg); println()
    print(s"Trying log level: ${Level.WARN}\t:  "); logger.warn(msg); println()
    print(s"Trying log level: ${Level.ERROR}\t:  "); logger.error(msg); println()
    print(s"Trying log level: ${Level.FATAL}\t:  "); logger.fatal(msg); println()
    print(s"Trying log level: ${Level.TRACE}\t:  "); logger.trace(msg); println()
  }
}
