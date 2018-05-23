package zk.zookeeper

import enumeratum.values.{IntEnum, IntEnumEntry}

abstract class KeeperException {
  import KeeperException.Code

}

object KeeperException {
  sealed abstract class Code(val value:Int, val name:String) extends IntEnumEntry
  case object Code extends IntEnum[Code] {
    case object OK extends Code(0, "ok")
    case object SYSTEMERROR extends Code(-1, "SystemError")
    case object RUNTIMEINCONSISTENCY extends Code(-2, "RuntimeInconsistency")
    case object DATAINCONSISTENCY extends Code(-3, "DataInconsistency")
    case object CONNECTIONLOSS extends Code(-4, "ConnectionLoss")
    case object MARSHALLINGERROR extends Code(-5, "MarshallingError")
    case object UNIMPLEMENTED extends Code(-6, "Unimplemented")
    case object OPERATIONTIMEOUT extends Code(-7, "OperationTimeout")
    case object BADARGUMENTS extends Code(-8, "BadArguments")
    case object NEWCONFIGNOQUORUM extends Code(-13, "NewConfigNoQuorum")
    case object RECONFIGINPROGRESS extends Code(-14, "ReconfigInProgress")
    case object UNKNOWNSESSION extends Code(-12, "UnknownSession")
    case object APIERROR extends Code(-100, "APIError")
    case object NONODE extends Code(-101, "NoNode")
    case object NOAUTH extends Code(-102, "NoAuth")
    case object BADVERSION extends Code(-103, "BadVersion")
    case object NOCHILDRENFOREPHEMERALS extends Code(-108, "NoChildrenForEphemerals")
    case object NODEEXISTS extends Code(-110, "NodeExists")
    case object NOTEMPTY extends Code(-111, "Directory not empty")
    case object SESSIONEXPIRED extends Code(-112, "Session expired")
    case object INVALIDCALLBACK extends Code(-113, "Invalid callback")
    case object INVALIDACL extends Code(-114, "InvalidACL")
    case object AUTHFAILED extends Code(-115, "AuthFailed")
    case object SESSIONMOVED extends Code(-118, "Session moved")
    case object NOTREADONLY extends Code(-119, "Not a read-only call")
    case object EPHEMERALONLOCALSESSION extends Code(-120, "Ephemeral node on local session")
    case object NOWATCHER extends Code(-121, "No such watcher")
    case object RECONFIGDISABLED extends Code(-122, "Reconfig is disabled")

    val values = findValues
  }
}
