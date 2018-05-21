package zk.jute

import java.io.{IOException, InputStream}
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory


import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

import scala.collection.mutable.ArrayBuffer

class XmlInputArchive(in: InputStream) extends InputArchive {
  import XmlInputArchive.{XMLParser,Value}

  private case object XmlIndex extends Index {
    override def done(): Boolean = {
      val v = valList(vIdx)
      if(v.getType == "/array") {
        valList(vIdx) = null
        vIdx += 1
        true
      } else false
    }

    override def incr(): Unit = {}
  }

  private val valList = ArrayBuffer[Value]()
  private var vIdx = 0
  private val vLen = valList.size

  private val handler:DefaultHandler = XMLParser(valList)
  private val factory:SAXParserFactory = SAXParserFactory.newInstance()
  private val parser:SAXParser = factory.newSAXParser()
  parser.parse(in, handler)

  private def next: Value = {
    if(vIdx < vLen) {
      val v = valList(vIdx)
      valList(vIdx) = null
      vIdx += 1
      v
    } else throw new IOException("Error in deserialization.")
  }

  override def readByte(tag: String): Byte = {
    val v = next
    if(v.getType != "ex:i1") throw new IOException(s"Error deserializing $tag")
    v.getValue.toByte
  }

  override def readBool(tag: String): Boolean = {
    val v = next
    if(v.getType != "boolean") throw new IOException(s"Error deserializing $tag")
    v.getValue == "1"
  }

  override def readInt(tag: String): Int = {
    val v = next
    if(v.getType != "i4" && v.getType != "int") throw new IOException(s"Error deserializing $tag")
    v.getValue.toInt
  }

  override def readLong(tag: String): Long = {
    val v = next
    if(v.getType != "ex:i8") throw new IOException(s"Error deserializing $tag")
    v.getValue.toLong
  }

  override def readFloat(tag: String): Float = {
    val v = next
    if(v.getType != "ex:float") throw new IOException(s"Error deserializing $tag")
    v.getValue.toFloat
  }

  override def readDouble(tag: String): Double = {
    val v = next
    if(v.getType != "double") throw new IOException(s"Error deserializing $tag")
    v.getValue.toDouble
  }

  override def readString(tag: String): String = {
    val v = next
    if(v.getType != "string") throw new IOException(s"Error deserializing $tag")
    Utils.fromXMLString(v.getValue)
  }

  override def readBuffer(tag: String): Array[Byte] = {
    val v = next
    if(v.getType != "string") throw new IOException(s"Error deserializing $tag")
    Utils.fromXMLBuffer(v.getValue)
  }

  override def readRecord(r: Record, tag: String): Unit = r.deserialize(this, tag)

  override def startRecord(r: Record, tag: String): Unit = {
    val v = next
    if(v.getType != "struct") throw new IOException(s"Error deserializing $tag")
  }

  override def endRecord(tag: String): Unit = {
    val v = next
    if(v.getType != "/struct") throw new IOException(s"Error deserializing $tag")
  }

  override def startVector(tag: String): Index = {
    val v = next
    if(v.getType != "array") throw new IOException(s"Error deserializing $tag")
    XmlIndex
  }


  override def endVector(tag: String): Unit = {}

  override def startMap(tag: String): Index = startVector(tag)

  override def endMap(tag: String): Unit = endVector(tag)
}

object XmlInputArchive {
  private case class Value(t:String) {
    private val typ = t
    private val sb = new StringBuffer()

    def addChars(buf:Array[Char], offset:Int, len:Int):Unit = sb.append(buf, offset, len)
    def getValue:String = sb.toString
    def getType:String = typ
  }

  private case class XMLParser(vlist:ArrayBuffer[Value]) extends DefaultHandler {
    var valList:ArrayBuffer[Value] = vlist
    private var charsValid = false

    override def startDocument():Unit = {}
    override def endDocument():Unit = {}
    override def startElement(ns:String, sname:String, qname:String, attrs:Attributes):Unit = {
      charsValid = false
      qname match {
        case "boolean" | "i4" | "int" | "string" | "double" | "ex:i1" | "ex:i8" | "ex:float" =>
          charsValid = true
          valList += Value(qname)
        case "struct" | "array" =>
          valList += Value(qname)
      }
    }
    override def endElement(s:String, sname:String, qname:String):Unit = {
      charsValid = false
      qname match {
        case "struct" | "array" => valList += Value("/"+qname)
      }
    }

    override def characters(buff:Array[Char], offset:Int, len:Int):Unit = if(charsValid) valList.last.addChars(buff,offset,len)

  }

  def getArchive(strm: InputStream): XmlInputArchive = new XmlInputArchive(strm)
}

