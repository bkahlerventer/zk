package zk.zookeeper

import enumeratum.values.{IntEnum, IntEnumEntry}

import scala.util._


sealed abstract class CreateMode(val value:Int, val ephemeral:Boolean, val sequential:Boolean, val isContainer:Boolean, val isTTL:Boolean) extends IntEnumEntry

case object CreateMode extends IntEnum[CreateMode] {
  case object PERSISTENT extends CreateMode(0,false,false, false, false)
  case object PERSISTANT_SEQUENTIAL extends CreateMode(2, false, true, false, false)
  case object EPHEMERAL extends CreateMode(1, true, false, false, false)
  case object EPHEMERAL_SEQUENTIAL extends CreateMode(3, true, true, false, false)
  case object CONTAINER extends CreateMode(4, false, false, true, false)
  case object PERSISTENT_WITH_TTL extends CreateMode(5, false, false, false, true)
  case object PERSISTENT_SEQUENTIAL_WITH_TTL extends CreateMode(6, false, true, false, true)

  val values = findValues

  def withValueOrElse(i:Int, default:CreateMode):CreateMode = {
    Try(CreateMode.withValue(i)) match {
      case Success(createMode) => createMode
      case Failure(_) => default
    }

  }
}

