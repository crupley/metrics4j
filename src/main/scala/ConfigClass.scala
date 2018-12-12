package metrics4j

import java.net.URI

import org.apache.logging.log4j.{Level, LogManager}
import org.apache.logging.log4j.core.{Filter, LoggerContext}
import org.apache.logging.log4j.core.config.{Configuration, ConfigurationFactory, ConfigurationSource, Configurator}
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration
import org.apache.logging.log4j.core.config.builder.api._
import org.apache.logging.log4j.scala.LoggingContext
import org.apache.logging.log4j.scala.Logger
import org.apache.logging.log4j.scala.LoggerMacro



object ConfigClass {
  val indicator = "configClass"

  ConfigurationFactory.setConfigurationFactory(new CustomConfigFactory)
  val logger = LogManager.getLogger("com")

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




class CustomConfigFactory extends ConfigurationFactory {

  override def getConfiguration(loggerContext: LoggerContext, source: ConfigurationSource): Configuration = {
    // Configuration
    val builder: ConfigurationBuilder[BuiltConfiguration] = ConfigurationBuilderFactory.newConfigurationBuilder()

    // Appenders
    val console: AppenderComponentBuilder = builder.newAppender("stdout", "Console")

    val file: AppenderComponentBuilder = builder.newAppender("log", "File")
    file.addAttribute("fileName", "logging.log")
    file.addAttribute("append", "false") // overwrite file each time

    val rollingFile: AppenderComponentBuilder = builder.newAppender("rolling", "RollingFile")
    rollingFile.addAttribute("fileName", "rolling.log")
    rollingFile.addAttribute("filePattern", "rolling-%d{MM-dd-yy}.log.gz")

    // Filters
    val flow: FilterComponentBuilder = builder.newFilter("MarkerFilter", Filter.Result.ACCEPT, Filter.Result.DENY)
    flow.addAttribute("marker", "FLOW")
    // this filter filters all my log statements
    // console.add(flow)
    // file.add(flow)

    // Layout
    val standard: LayoutComponentBuilder = builder.newLayout("PatternLayout")
    standard.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable")
    console.add(standard)
    // file.add(standard)
    rollingFile.add(standard)

    val jpattern: LayoutComponentBuilder = builder.newLayout("PatternLayout")
    jpattern.addAttribute("header", "[ // this is a new json file\n")
    jpattern.addAttribute("pattern","""\t{"date" : "%d", "message" : "%msg"},\n""")
    jpattern.addAttribute("footer", "] // this is the end of the json\n")
    file.add(jpattern)

    val json: LayoutComponentBuilder = builder.newLayout("JSONLayout")
    // file.add(json) // doesn't work yet

    // Root logger
    val rootLogger: RootLoggerComponentBuilder = builder.newRootLogger(Level.WARN)
    rootLogger.add(builder.newAppenderRef("stdout"))
    builder.add(rootLogger)

    // More loggers
    val logger2: LoggerComponentBuilder = builder.newLogger("com", Level.DEBUG)
    logger2.add(builder.newAppenderRef("stdout"))
    logger2.add(builder.newAppenderRef("log"))
    logger2.add(builder.newAppenderRef("rolling"))
    logger2.addAttribute("additivity", false)
    builder.add(logger2)

    // Triggering
    val triggeringPolicies: ComponentBuilder[Nothing] = builder.newComponent("Policies")
    triggeringPolicies.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
    triggeringPolicies.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"))
    rollingFile.addComponent(triggeringPolicies)

    // must add AFTER all configs are set
    builder.add(console)
    builder.add(file)
    builder.add(rollingFile)

    // initialize
    Configurator.initialize(builder.build())

    // write xml config
    builder.writeXmlConfiguration(System.out)

    val log = LogManager.getRootLogger
    log.info("log xml config...")

    builder.build
  }

  override def getSupportedTypes: Array[String] = Array[String]("*")
}