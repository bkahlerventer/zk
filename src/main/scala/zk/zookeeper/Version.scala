package zk.zookeeper

import zk.zookeeper.version.Info

// TODO - instead of creating/loading created loaded trait, load from properties file
object Version extends Info {
  @Deprecated
  def getRevision:String = s"$REVISION"

  def getRevisionHash = s"$REVISION_HASH"

  def getBuildDate:String = s"$BUILD_DATE"

  def getVersion:String = s"$MAJOR.$MINOR.$MICRO${if(QUALIFIER.isEmpty) "" else "-" + QUALIFIER}"

  def getVersionRevision:String = s"$getVersion-$getRevisionHash"

  def getFullVersion:String = s"$getVersionRevision, built on $getBuildDate"

  def printUsage():Unit = {
    println("Usage:\tjava -cp ... org.apache.zookeeper.Version [--full | --short | --revision],")
    println("\tPrints --full version info if no arg specified.")
    System.exit(1)
  }

  def main(args: Array[String]): Unit = {
    if(args.length > 1) printUsage()

    if(args.length == 0 || args.length == 1) args(0) match {
      case "--full" =>
        println(getFullVersion)
      case "--short" =>
        println(getVersion)
      case "--revision" =>
        println(getVersionRevision)
      case _ =>
        printUsage()
    }
    System.exit(0)
  }
}