package zk.zookeeper.data {
  case class Id(scheme:String, id:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[Id]
    override def equals(that: scala.Any): Boolean = that match {
      case that: Id => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 35 + scheme.hashCode + id.hashCode
  }
  case class ACL(perms:Int, id:Id) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ACL]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ACL => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 31 * perms + id.hashCode
  }
  case class Stat(czxid:Long, mzxid:Long, ctime:Long, mtime:Long, version:Int, cversion:Int, aversion:Int, ephemeralOwner:Long, dataLength:Int, numChildren:Int, pzxid:Long) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[Stat]
    override def equals(that: scala.Any): Boolean = that match {
      case that: Stat => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 35  + mzxid.toInt + czxid.toInt + ctime.toInt
  }
  case class StatPersisted(czxid:Long, mzxid:Long, ctime:Long, mtime:Long, version:Int, cversion:Int, aversion:Int, ephemeralOwner:Long, pzxid:Long) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[StatPersisted]
    override def equals(that: scala.Any): Boolean = that match {
      case that: StatPersisted => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 35 + mzxid.toInt + czxid.toInt + ctime.toInt
  }
}

package zk.zookeeper.proto {
  import zk.zookeeper.data.{Stat,ACL}
  case class ConnectRequest(protocolVersion:Int, lastZxidSeen:Long, timeOut:Int, sessionId:Long, passwd:Array[Byte]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ConnectRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ConnectRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 31 * timeOut + sessionId.toInt
  }
  case class ConnectResponse(protocolVersion:Int, timeOut:Int, sessionId:Long, passwd:Array[Byte]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ConnectResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ConnectResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 35 * timeOut + sessionId.toInt
  }
  case class SetWatches(relativeZxid:Long, dataWatches:Vector[String], existWatches:Vector[String], childWatches:Vector[String]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetWatches]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetWatches => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int =  31 * relativeZxid.toInt
  }
  case class RequestHeader(xid:Int, ztype:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[RequestHeader]
    override def equals(that: scala.Any): Boolean = that match {
      case that: RequestHeader => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int =  31 * ztype + xid
  }
  case class MultiHeader(ztype:Int, done:Boolean, err:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[MultiHeader]
    override def equals(that: scala.Any): Boolean = that match {
      case that: MultiHeader => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 35 * ztype + err
  }
  case class AuthPacket(ztype:Int, scheme:String, auth:Array[Byte]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[AuthPacket]
    override def equals(that: scala.Any): Boolean = that match {
      case that: AuthPacket => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 33 * ztype + scheme.hashCode
  }
  case class ReplyHeader(xid:Int, zxid:Long, err:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ReplyHeader]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ReplyHeader => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 11 * zxid.toInt + xid + err
  }
  case class GetDataRequest(path:String, watch:Boolean) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetDataRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetDataRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 177 + path.hashCode
  }
  case class SetDataRequest(path:String, data:Array[Byte], version:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetDataRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 172 + path.hashCode + version
  }
  case class ReconfigRequest(joiningServers:String, leavingServers:String, newMembers:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ReconfigRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ReconfigRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 1024 + joiningServers.hashCode + leavingServers.hashCode + newMembers.hashCode
  }
  case class SetDataResponse(stat:Stat) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetDataResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = stat.hashCode()
  }
  case class GetSASLRequest(token:Array[Byte]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetSASLRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetSASLRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 7 * token.hashCode()
  }
  case class SetSASLRequest(token:Array[Byte]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetSASLRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetSASLRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 9 * token.hashCode()
  }
  case class CreateRequest(path:String, data:Array[Byte], acl:Vector[ACL]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 33 * path.hashCode
  }
  case class CreateTTLRequest(path:String, data:Array[Byte], acl:Vector[ACL], flags:Int, ttl:Long) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTTLRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateTTLRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 113 * path.hashCode + flags + ttl.toInt
  }
  case class DeleteRequest(path:String, version:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[DeleteRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: DeleteRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 441 * version + path.hashCode
  }
  case class GetChildrenRequest(path:String, watch:Boolean) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildrenRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetChildrenRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 7531 + path.hashCode
  }
  case class GetChildren2Request(path:String, watch:Boolean) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildren2Request]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetChildren2Request => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 11234 + path.hashCode
  }
  case class CheckVersionRequest(path:String, version:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckVersionRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CheckVersionRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 77 * version + path.hashCode
  }
  case class GetMaxChildrenRequest(path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetMaxChildrenRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetMaxChildrenRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 27513 + path.hashCode
  }
  case class GetMaxChildrenResponse(max:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetMaxChildrenResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetMaxChildrenResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 312 * max
  }
  case class SetMaxChildrenResponse(path:String, max:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetMaxChildrenResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetMaxChildrenResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 27 * max + path.hashCode
  }
  case class SyncRequest(path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SyncRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SyncRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 18 * path.hashCode
  }
  case class SyncResponse(path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SyncResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SyncResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 15 * path.hashCode
  }
  case class GetACLRequest(path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetACLRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetACLRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 17 * path.hashCode
  }
  case class WatcherEvent(ztype:Int, state:Int, path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[WatcherEvent]
    override def equals(that: scala.Any): Boolean = that match {
      case that: WatcherEvent => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = path.hashCode + ztype + state
  }
  case class ErrorResponse(err:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ErrorResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ErrorResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 31 * err
  }
  case class CreateResponse(path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 7644 + path.hashCode
  }
  case class Create2Response(path:String, stat:Stat) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[Create2Response]
    override def equals(that: scala.Any): Boolean = that match {
      case that: Create2Response => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 3131 + path.hashCode + stat.hashCode()
  }
  case class ExistsRequest(path:String, watch:Boolean) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ExistsRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ExistsRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 21 * path.hashCode
  }
  case class ExistsResponse(stat:Stat) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ExistsResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ExistsResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 89898 + stat.hashCode
  }
  case class GetDataResponse(data:Array[Byte], stat:Stat) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetDataResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetDataResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 71 * data.length + stat.hashCode
  }
  case class GetChildrenResponse(children:Vector[String]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildrenResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetChildrenResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = children.toList.map(s => s.hashCode).sum
  }
  case class GetChildren2Response(children:Vector[String]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetChildren2Response]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetChildren2Response => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = children.toList.map(s => s.hashCode).sum
  }
  case class GetACLResponse(acl:Vector[ACL], stat:Stat) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[GetACLResponse]
    override def equals(that: scala.Any): Boolean = that match {
      case that: GetACLResponse => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = acl.toList.map(a => a.hashCode()).sum + stat.hashCode()
  }
  case class CheckWatchesRequest(path:String, ztype:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckWatchesRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CheckWatchesRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 55 * ztype + path.hashCode
  }
  case class RemoveWatchesRequest(path:String, ztype:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[RemoveWatchesRequest]
    override def equals(that: scala.Any): Boolean = that match {
      case that: RemoveWatchesRequest => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 77 * ztype + path.hashCode
  }
}
package zk.zookeper.server.quorum {
  import zk.zookeeper.data.Id
  case class LearnerInfo(serverId:Long, protocolVersion:Int, configVersion:Long) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[LearnerInfo]
    override def equals(that: scala.Any): Boolean = that match {
      case that: LearnerInfo => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 17 * protocolVersion + serverId.toInt + configVersion.toInt
  }
  case class QuorumPacket(ztype:Int, zxid:Long, data:Array[Byte], authinfo:Vector[Id]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[QuorumPacket]
    override def equals(that: scala.Any): Boolean = that match {
      case that: QuorumPacket => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 71 * ztype + data.hashCode()
  }
  case class QuorumAuthPacket(magic:Int, version:Int, dbid:Long) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[QuorumAuthPacket]
    override def equals(that: scala.Any): Boolean = that match {
      case that: QuorumAuthPacket => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 81 * magic + version + dbid.toInt
  }
}
package zk.zookeeper.server.persistence {
  case class FileHeader(magic:Int, version:Int, dbid:Long) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[FileHeader]
    override def equals(that: scala.Any): Boolean = that match {
      case that: FileHeader => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 98 * magic + version + dbid.toInt
  }
}
package zk.zookeeper.txn {
  import zk.zookeeper.data.ACL
  case class TxnHeader(clientId:Long, cxid:Int, zxid:Long, time:Long, ztype:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[TxnHeader]
    override def equals(that: scala.Any): Boolean = that match {
      case that: TxnHeader => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 67 * clientId.toInt + cxid + zxid.toInt + time.toInt + ztype
  }
  case class CreateTxnV0(path:String, data:Array[Byte], acl:Vector[ACL]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTxnV0]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateTxnV0 => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + path.hashCode + data.hashCode()
  }
  case class CreateTxn(path:String, data:Array[Byte], acl:Vector[ACL]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + path.hashCode + data.hashCode()
  }
  case class CreateTTLTxn(path:String, data:Array[Byte], acl:Vector[ACL], parentCVersion:Int, ttl:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateTTLTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateTTLTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + path.hashCode + parentCVersion + ttl
  }
  case class CreateContainerTxn(path:String, data:Array[Byte], acl:Vector[ACL], parentCVersion:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateContainerTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateContainerTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + parentCVersion
  }
  case class DeleteTxn(path:String) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[DeleteTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: DeleteTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 45361 + path.hashCode
  }
  case class SetDataTxn(path:String, data:Array[Byte], version:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetDataTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetDataTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 90 * version + path.hashCode + data.hashCode()
  }
  case class CheckVersionTxn(path:String, version:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CheckVersionTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CheckVersionTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 99 * version + path.hashCode
  }
  case class SetACLTxn(path:String, acl:Vector[ACL], version:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetACLTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetACLTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = acl.toList.map(s => s.hashCode()).sum + version + path.hashCode
  }
  case class SetMaxChildrenTxn(path:String, max:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[SetMaxChildrenTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: SetMaxChildrenTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 13 * max + path.hashCode
  }
  case class CreateSessionTxn(timeOut:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[CreateSessionTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: CreateSessionTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 16457 * timeOut
  }
  case class ErrorTxn(err:Int) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[ErrorTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: ErrorTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 6778 * err
  }
  case class Txn(ztype:Int, data:Array[Byte]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[Txn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: Txn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = 6678 * ztype + data.hashCode()
  }
  case class MultiTxn(txns:Vector[Txn]) {
    override def canEqual(that: Any): Boolean = that.isInstanceOf[MultiTxn]
    override def equals(that: scala.Any): Boolean = that match {
      case that: MultiTxn => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
    override def hashCode(): Int = txns.toList.map(s => s.hashCode()).sum
  }
}

