package zk.zookeeper.common

object StringUtils {
  def split(value:String, separator:String):List[String] = value.split(separator).toList
  def joinStrings(list:List[String], delim:String):String = list.mkString(delim)
}
