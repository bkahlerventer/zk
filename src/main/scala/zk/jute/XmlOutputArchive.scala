package zk.jute

import java.io.{IOException, OutputStream, PrintStream}

import zk.io.{OutputArchive, Utils}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class XmlOutputArchive(out:OutputStream) extends OutputArchive {
  private val stream = new PrintStream(out)
  private val compoundStack = mutable.ArrayStack[String]()
  private var indent = 0

  private def putIndent(): Unit = {
    @tailrec
    def indents(i:Int, acc:ListBuffer[String]): List[String] = i match{
      case x if x > -1 => indents(x-1, acc += "  ")
      case -1 => acc.toList
    }
    indents(indent, new ListBuffer[String]()).mkString
  }

  private def addIndent():Unit = indent += 1

  private def closeIndent():Unit = indent -= 1

  private def printBeginEnvelope(tag:String):Unit = {
    if(compoundStack.nonEmpty) {
      compoundStack.top match {
        case "struct" =>
          putIndent()
          stream.print("<member>\n")
          addIndent()
          putIndent()
          stream.print(s"<name>$tag</name>\n")
          putIndent()
          stream.print("<value>")
        case "vector" | "map" =>
          stream.print("<value>")
      }
    } else {
      stream.print("<value>")
    }
  }

  private def printEndEnvelope(tag:String):Unit = {
    if(compoundStack.nonEmpty) {
      compoundStack.top match {
        case "struct" =>
          stream.print("</value>\n")
          closeIndent()
          putIndent()
          stream.print("</member>\n")
        case "vector" | "map" =>
          stream.print("</value>")
      }
    } else {
      stream.print("</value>")
    }
  }

  private def doEntry(t:String, v:String, tag:String):Unit = {
    printBeginEnvelope(tag)
    stream.print(s"<$t>$v</$t>")
    printEndEnvelope(tag)
  }

  private def startEntry(t:String, tag:String):Unit = {
    printBeginEnvelope(tag)
    compoundStack.push(t)
    stream.print(s"<$t>")
    addIndent()
  }

  private def endEntry(t:String, tag:String):Unit = {
    closeIndent()
    putIndent()
    stream.print(s"</$t>")
    if (compoundStack.pop() != "struct") throw new IOException("Error serializing record.")
    printEndEnvelope(tag)
  }

  override def writeByte(b: Byte, tag: String): Unit = doEntry("ex:i1", b.toString, tag)

  override def writeBool(b: Boolean, tag: String): Unit = doEntry("boolean", if(b) "1" else "0", tag)

  override def writeInt(i: Int, tag: String): Unit = doEntry("i4", i.toString, tag)

  override def writeLong(l: Long, tag: String): Unit = doEntry("ex:i8", l.toString, tag)

  override def writeFloat(f: Float, tag: String): Unit = doEntry("ex:float", f.toString, tag)

  override def writeDouble(d: Double, tag: String): Unit = doEntry("double", d.toString, tag)

  override def writeString(s: String, tag: String): Unit = doEntry("string", Utils.toXMLString(s), tag)

  override def writeBuffer(buf: Array[Byte], tag: String): Unit = doEntry("string", Utils.toXMLBuffer(buf), tag)

  override def writeRecord(r: Record, tag: String): Unit = r.serialize(this, tag)

  override def startRecord(r: Record, tag: String): Unit = startEntry("struct", tag)

  override def endRecord(r: Record, tag: String): Unit = endEntry("struct", tag)

  override def startVector(v: Vector[_], tag: String): Unit = startEntry("array", tag)

  override def endVector(v: Vector[_], tag: String): Unit = endEntry("array", tag)

  override def startMap(m: Map[_, _], tag: String): Unit = startEntry("array", tag)

  override def endMap(m: Map[_, _], tag: String): Unit = endEntry("array", tag)
}

object XmlOutputArchive {
  def getArchive(strm:OutputStream):XmlOutputArchive = new XmlOutputArchive(strm)
}
