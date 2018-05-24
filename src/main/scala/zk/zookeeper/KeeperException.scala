package zk.zookeeper

import enumeratum.values.{IntEnum, IntEnumEntry}
import KeeperException.Code
import scala.collection.mutable


object KeeperException {

  def create(code:Option[Code], path:Option[String]):KeeperException = {
    val r:KeeperException = create(code)
    r._path = path
    r
  }

  def create(code:Int, path:Option[String]):KeeperException = {
    val r:KeeperException = create(Code.withValueOpt(code))
    r._path = path
    r
  }

  def create(code:Int):KeeperException = create(Code.withValueOpt(code),None)

  def create(code:Option[Code]):KeeperException = code.get match {
    case Code.SYSTEMERROR => new SystemErrorException()
    case Code.RUNTIMEINCONSISTENCY => new RuntimeInconsistencyException()
    case Code.DATAINCONSISTENCY => new DataInconsistencyException()
  }
  /** Codes which represent the various KeeperException
    * types. This enum replaces the deprecated earlier static final int
    * constants. The old, deprecated, values are in "camel case" while the new
    * enum values are in all CAPS.
    */
  sealed abstract class Code(val value:Int, val name:String) extends IntEnumEntry
  case object Code extends IntEnum[Code] {
    /** Everything is OK */
    case object OK extends Code(0, "ok")

    /** System and server-side errors.
      * This is never thrown by the server, it shouldn't be used other than
      * to indicate a range. Specifically error codes greater than this
      * value, but lesser than {@link #APIERROR}, are system errors.
      */
    case object SYSTEMERROR extends Code(-1, "SystemError")
    /** A runtime inconsistency was found */
    case object RUNTIMEINCONSISTENCY extends Code(-2, "RuntimeInconsistency")
    /** A data inconsistency was found */
    case object DATAINCONSISTENCY extends Code(-3, "DataInconsistency")
    /** Connection to the server has been lost */
    case object CONNECTIONLOSS extends Code(-4, "ConnectionLoss")
    /** Error while marshalling or unmarshalling data */
    case object MARSHALLINGERROR extends Code(-5, "MarshallingError")
    /** Operation is unimplemented */
    case object UNIMPLEMENTED extends Code(-6, "Unimplemented")
    /** Operation timeout */
    case object OPERATIONTIMEOUT extends Code(-7, "OperationTimeout")
    /** Invalid arguments */
    case object BADARGUMENTS extends Code(-8, "BadArguments")
    /** No quorum of new config is connected and up-to-date with the leader of last commmitted config - try
      *  invoking reconfiguration after new servers are connected and synced */
    case object NEWCONFIGNOQUORUM extends Code(-13, "NewConfigNoQuorum")
    /** Another reconfiguration is in progress -- concurrent reconfigs not supported (yet) */
    case object RECONFIGINPROGRESS extends Code(-14, "ReconfigInProgress")
    /** Unknown session (internal server use only) */
    case object UNKNOWNSESSION extends Code(-12, "UnknownSession")

    /** API errors.
      * This is never thrown by the server, it shouldn't be used other than
      * to indicate a range. Specifically error codes greater than this
      * value are API errors (while values less than this indicate a
      * {@link #SYSTEMERROR}).
      */
    case object APIERROR extends Code(-100, "APIError")
    /** Node does not exist */
    case object NONODE extends Code(-101, "NoNode")
    /** Not authenticated */
    case object NOAUTH extends Code(-102, "NoAuth")
    /** Version conflict. In case of reconfiguration: reconfig requested from config version X
      * but last seen config has a different version Y */
    case object BADVERSION extends Code(-103, "BadVersion")
    /** Ephemeral nodes may not have children */
    case object NOCHILDRENFOREPHEMERALS extends Code(-108, "NoChildrenForEphemerals")
    /** The node already exists */
    case object NODEEXISTS extends Code(-110, "NodeExists")
    /** The node has children */
    case object NOTEMPTY extends Code(-111, "Directory not empty")
    /** The session has been expired by the server */
    case object SESSIONEXPIRED extends Code(-112, "Session expired")
    /** Invalid callback specified */
    case object INVALIDCALLBACK extends Code(-113, "Invalid callback")
    /** Invalid ACL specified */
    case object INVALIDACL extends Code(-114, "InvalidACL")
    /** Client authentication failed */
    case object AUTHFAILED extends Code(-115, "AuthFailed")
    /** Session moved to another server, so operation is ignored */
    case object SESSIONMOVED extends Code(-118, "Session moved")
    /** State-changing request is passed to read-only server */
    case object NOTREADONLY extends Code(-119, "Not a read-only call")
    /** Attempt to create ephemeral node on a local session */
    case object EPHEMERALONLOCALSESSION extends Code(-120, "Ephemeral node on local session")
    /** Attempts to remove a non-existing watcher */
    case object NOWATCHER extends Code(-121, "No such watcher")
    /** Attempts to perform a reconfiguration operation when reconfiguration feature is disabled. */
    case object RECONFIGDISABLED extends Code(-122, "Reconfig is disabled")

    val values = findValues
  }
}


abstract class KeeperException(c:Option[Code], p:Option[String]) {

  import KeeperException.Code

  /**
    * All multi-requests that result in an exception retain the results
    * here so that it is possible to examine the problems in the catch
    * scope.  Non-multi requests will get a null if they try to access
    * these results.
    */
  private var results:mutable.ListBuffer[OpResult] = _
  private var _code: Option[Code] = c
  private var _path: Option[String] = p


  def setCode(c: Int): Unit = {
    _code = Code.withValueOpt(c)
  }

  def this(c: Code) = this(c, None)

  def code: Option[Code] = _code

  def getMessage: String = {
    if (_path.isEmpty) s"KeeperErrorCode = ${_code.get.name}"
    else s"KeeperErrorCode = ${_code.get.name} for ${_path.get}"
  }

  def getResults:mutable.ListBuffer[OpResult] = if(results.isEmpty) mutable.ListBuffer[OpResult]() else null
}