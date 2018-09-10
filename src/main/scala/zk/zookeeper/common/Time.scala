package zk.zookeeper.common

import java.util.Date

object Time {
  /**
    * Returns time in milliseconds as does System.currentTimeMillis(),
    * but uses elapsed time from an arbitrary epoch more like System.nanoTime().
    * The difference is that if somebody changes the system clock,
    * Time.currentElapsedTime will change but nanoTime won't. On the other hand,
    * all of ZK assumes that time is measured in milliseconds.
    * @return  The time in milliseconds from some arbitrary point in time.
    */
  def currentElapsedTime: Long = System.nanoTime() / 1000000

  /**
    * Explicitly returns system dependent current wall time.
    * @return Current time in msec.
    */
  def currentWallTime: Long = System.currentTimeMillis()

  /**
    * This is to convert the elapsedTime to a Date.
    * @return A date object indicated by the elapsedTime.
    */
  def elapsedTimeToDate(elapsedTime:Long):Date = new Date(currentWallTime + elapsedTime - currentElapsedTime)
}