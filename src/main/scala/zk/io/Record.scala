package zk.io

/**
  * Interface that is implemented by generated classes.
  *
  */
trait Record {
  def serialize(archive:OutputArchive, tag:String)
  def deserialize(archive:InputArchive, tag:String)
}
