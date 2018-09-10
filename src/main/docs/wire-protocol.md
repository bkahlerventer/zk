There are no documentation that describes the Apache ZooKeeper wire protocol in any great detail. Internally, our codebase is using a framework called Jute, which is based on code originally adapted from Apache Hadoop. The framework allows definition of structured records, generates code based on those definitions, and then provides serialization/deserialization routines called by the rest of the ZooKeeper code.

The Jute record definitions are visible here:

https://github.com/apache/zookeeper/blob/release-3.4.9/src/zookeeper.jute

The Jute framework code for handling these record definitions is visible here:

https://github.com/apache/zookeeper/tree/release-3.4.9/src/java/main/org/apache/jute

I think the only option for a deep understanding of the wire protocol would be to dig into this code.

After digging through a few layers of raw socket handling code (which uses either NIO or Netty depending on configuration), the real work of deserializing the payload happens in ZooKeeperServer#processPacket(ServerCnxn, ByteBuffer):

https://github.com/apache/zookeeper/blob/release-3.4.9/src/java/main/org/apache/zookeeper/server/ZooKeeperServer.java#L941

This is where it deserializes a RequestHeader, which is a common header of metadata at the front of all of the protocol's messages. The definition of RequestHeader is shown here:

https://github.com/apache/zookeeper/blob/release-3.4.9/src/zookeeper.jute#L88-L91

We can see it consists of 2 4-byte integer fields: a connection ID followed by the type of the message. The type values are defined in ZooDefs here:

https://github.com/apache/zookeeper/blob/release-3.4.9/src/java/main/org/apache/zookeeper/ZooDefs.java#L28

Knowing all of this, let's go back to your packet capture and try to make sense of it:

Data: 00000008fffffffe0000000b

00000008 - payload length
fffffffe - connection ID
0000000b - op code ("ping")
At the front of each message (even before the RequestHeader), there is the length of the payload. Here we see a length of 8 bytes.

The next 4 bytes are the connection ID, fffffffe.

The final 4 bytes are the op code, 0000000b (or 11 in decimal). Reading ZooDefs, we can see that this is the "ping" operation. The "ping" operation is used for periodic heartbeats between client and server. There is no additional data required in the payload for the "ping" operation, so this is the end of this packet, and there is no additional data after it. For different operations, there would be additional data in the payload, representing the arguments to the operation.

I hope this helps.