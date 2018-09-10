package zk.zookeeper

import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class States(val value:Int) extends IntEnumEntry

case object States extends IntEnum[States] {
  case object CONNECTING extends States(0)
  case object ASSOCIATING extends States(1)
  case object CONNECTED extends States(2)
  case object CONNECTEDREADONLY extends States(3)
  case object CLOSED extends States(4)
  case object AUTH_FAILED extends States(5)
  case object NOT_CONNECTED extends States(6)

  def isAlive: Boolean = this match {
    case States.CLOSED || States.AUTH_FAILED => false
    case _ => true
  }

  def isConnected: Boolean = this match {
    case States.CONNECTED || States.CONNECTEDREADONLY => true
    case _ => false
  }

  val values = findValues
}
