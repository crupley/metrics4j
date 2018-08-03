
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.scala.Logging


object Main extends Logging {

  def doStuff(): Unit = {
    logger.info("Doing stuff")
  }
  def doStuffWithLevel(level: Level): Unit = {
    logger(level, "Doing stuff with arbitrary level")
  }


  def main(args: Array[String]): Unit = {
    println("Hello, world!")
    doStuff()
    doStuffWithLevel(Level.INFO)
    doStuffWithLevel(Level.WARN)
    doStuffWithLevel(Level.ERROR)
    doStuffWithLevel(Level.ALL)
    doStuffWithLevel(Level.TRACE)
  }
}