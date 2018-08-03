
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.{Level, LogManager, Logger}
import org.apache.logging.log4j.scala.Logging


object Main extends Logging {

  // val log: Logger = LogManager.getRootLogger
  Configurator.setRootLevel(Level.ALL)

  def doStuff(): Unit = {
    logger.info("Doing stuff")
  }
  def doStuffWithLevel(level: Level): Unit = {
    print(s"Trying log level: ${level.name}\t:  ")
    logger(level, "Doing stuff with arbitrary level")
  }


  def main(args: Array[String]): Unit = {
    println("Hello, world!")
    doStuffWithLevel(Level.ALL)
    doStuffWithLevel(Level.INFO)
    doStuffWithLevel(Level.DEBUG)
    doStuffWithLevel(Level.WARN)
    doStuffWithLevel(Level.ERROR)
    doStuffWithLevel(Level.FATAL)
    doStuffWithLevel(Level.TRACE)
    doStuffWithLevel(Level.OFF)
  }
}