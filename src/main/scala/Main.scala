package metrics4j


import org.apache.logging.log4j.{Level, LogManager}
import org.apache.logging.log4j.core.config.Configurator
import org.apache.logging.log4j.scala.{Logger, Logging}


class HelloWorld {

  val indicator: String = "hello world"
  val logger: Logger = Logger(getClass)

  Configurator.setRootLevel(Level.ALL)

  def doStuff(): Unit = {
    logger.info("Doing stuff")
  }
  def doStuffWithLevel(level: Level): Unit = {
    print(s"Trying log level: ${level.name}\t:  ")
    logger(level, "Doing stuff with arbitrary level")
    println()
  }


  def main(args: Array[String]): Unit = {
    println(s"indicator: $indicator")
    doStuffWithLevel(Level.ALL)
    doStuffWithLevel(Level.DEBUG)
    doStuffWithLevel(Level.INFO)
    doStuffWithLevel(Level.WARN)
    doStuffWithLevel(Level.ERROR)
    doStuffWithLevel(Level.FATAL)
    doStuffWithLevel(Level.TRACE)
    doStuffWithLevel(Level.OFF)
  }
}
