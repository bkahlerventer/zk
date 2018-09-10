package zk.io

import java.io._

class CsvInputArchive(in:InputStream) extends InputArchive {

  private case object CsvIndex extends Index {
    override def done(): Boolean = {
      var c: Char = 0
      try {
        c = stream.read().toChar
        stream.unread(c)
      } catch {
        case IOException =>
      }
      if(c == '}') true else false
    }

    override def incr(): Unit = {}
  }


  private val stream = new PushbackReader(new InputStreamReader(in,"UTF-8"))

  private def readField(tag:String): String = {
    val buf = new StringBuilder
    var continue = true
    var c: Char = '\0'
    try {
      while({c = stream.read().toChar; continue}) {
        c match {
          case ',' => continue=false
          case '}' | '\n' | '\r' =>
            stream.unread(c)
            continue=false
          case _ => buf.append(c)
        }
      }
      buf.toString()
  } catch {
      case _: IOException => throw new IOException(s"Error reading $tag")
    }
  }
  override def readByte(tag: String): Byte = readByte(tag)

  override def readBool(tag: String): Boolean = if("T".equals(readField(tag))) true else false

  override def readInt(tag: String): Int = readInt(tag)

  override def readLong(tag: String): Long = {
    try {
      java.lang.Long.parseLong(readField(tag))
    } catch {
      case _: NumberFormatException => throw new IOException(s"Error deserializing $tag")
    }
  }

  override def readFloat(tag: String): Float = readFloat(tag)

  override def readDouble(tag: String): Double = {
    try {
      java.lang.Double.parseDouble(readField(tag))
    } catch {
      case _: NumberFormatException => throw new IOException(s"Error deserializing $tag")
    }
  }

  override def readString(tag: String): String = Utils.fromCSVString(readField(tag))

  override def readBuffer(tag: String): Array[Byte] = Utils.fromCSVBuffer(readField(tag))

  override def readRecord(r: Record, tag: String): Unit = r.deserialize(this, tag)

  override def startRecord(r: Record, tag: String): Unit = {
    val c1 = stream.read().asInstanceOf[Char]
    val c2 = stream.read().asInstanceOf[Char]
    if(!(c1 == 's' && c2 == '{')) throw new IOException("Error deserializing record")
  }

  override def endRecord(tag: String): Unit = {
    val c = stream.read().asInstanceOf[Char]

    if (tag == null || tag == "") {
      if (!(c == '\n' || c == '\r')) throw new IOException("Error deserializing record")
    } else if (c == '}') {
      val c1 = stream.read().asInstanceOf[Char]
      if (c != ',') stream.unread(c1)
    }
  }

  override def startVector(tag: String): Index = {
    val c1 = stream.read().asInstanceOf[Char]
    val c2 = stream.read().asInstanceOf[Char]
    if(!(c1 == 'v' && c2 == '{')) throw new IOException("Error deserializing vector")
    CsvIndex
  }

  override def endVector(tag: String): Unit = {
    val c = stream.read().asInstanceOf[Char]

      if (c != '}' ) throw new IOException(s"Error deserializing vector $tag")
    else  {
      val c1 = stream.read().asInstanceOf[Char]
      if (c != ',') stream.unread(c1)
    }
  }

  override def startMap(tag: String): Index = {
    val c1 = stream.read().asInstanceOf[Char]
    val c2 = stream.read().asInstanceOf[Char]
    if (!(c1 == 'm' && c2 == '{')) throw new IOException(s"Error deserializing $tag")
    CsvIndex
  }

  override def endMap(tag: String): Unit = {
    val c = stream.read().asInstanceOf[Char]

    if (c != '}' ) throw new IOException(s"Error deserializing vector $tag")
    else  {
      val c1 = stream.read().asInstanceOf[Char]
      if (c != ',') stream.unread(c1)
    }

  }
}

object CsvInputArchive {
  def getArchive(strm: InputStream): CsvInputArchive = new CsvInputArchive(strm)
}
