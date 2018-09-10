package zk.io

import java.io.IOException

import scala.annotation.tailrec

object Utils {
  val hexchars = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')


  def bufEquals(oneArray:Array[Byte], twoArray:Array[Byte]): Boolean = oneArray sameElements twoArray

  def toXMLString(s:String): String = {
    // Implemented per XML spec:
    // http://www.w3.org/International/questions/qa-controls
    @tailrec
    def escape(tl:List[Char], acc:String): String = tl match {
      case Nil => acc
      case x :: tail if x < 20 => escape(tail, acc + s"%${hexchars(x/16)}${hexchars(x%16)}")
      case '<' :: tail => escape(tail, acc + "&lt;")
      case '>' :: tail => escape(tail, acc + "&rt;")
      case '%' :: tail => escape(tail, acc + "%25")
      case '&' :: tail => escape(tail, acc + "&amp;")
      case '"' :: tail => escape(tail, acc + "&qout")
      case _ => acc
    }
    escape(s.toList, "")
  }

  def fromXMLString(s:String): String = {
    def h2c(ch:Char): Int = ch match {
      case x if x >= '0' && x <= '9' => ch - '0'
      case x if x >= 'A' && x <= 'F' => ch - 'A'
      case x if x >= 'a' && x <= 'f' => ch - 'a'
      case _ => 0
    }
    @tailrec
    def unescape(tl:List[Char], acc:String): String = tl match{
      case Nil => acc
      case '%' :: d1 :: d2 :: tail => unescape(tail, acc + (h2c(d1)*16 + h2c(d2)).asInstanceOf[Char])
      case '&' :: 'a' :: 'm' :: 'p' :: ';' :: tail => unescape(tail, acc + "&")
      case '&' :: 'q' :: 'u' :: 'o' :: 't' :: ';' :: tail => unescape(tail, acc + "\"")
      case '&' :: 'l' :: 't' :: ';' :: tail => unescape(tail, acc + "<")
      case '&' :: 'g' :: 't' :: ';' :: tail => unescape(tail, acc + ">")
      case x :: tail => unescape(tail, acc + x)
      case _ => acc
    }
    unescape(s.toList, "")
  }

  def toCSVString(s:String): String = {
    @tailrec
    def escape(tl:List[Char], acc:String): String = tl match {
      case '\0' :: tail => escape(tail, acc + "%00")
      case '\n' :: tail => escape(tail, acc + "%0A")
      case '\r' :: tail => escape(tail, acc + "%0D")
      case ',' :: tail => escape(tail, acc + "%2C")
      case '}' :: tail => escape(tail, acc + "%7D")
      case '%' :: tail => escape(tail, acc + "%25")
      case x :: tail => escape(tail, acc + x)
      case Nil => acc
      case _ => acc
    }
    escape(s.toList, "'")
  }

  def fromCSVString(s:String):String = {
    @tailrec
    def unescape(tl:List[Char], acc:String): String = tl match{
      case Nil => acc
      case '%' :: '0' :: '0' :: tail => unescape(tail, acc + '\0')
      case '%' :: '0' :: 'A' :: tail => unescape(tail, acc + '\n')
      case '%' :: '0' :: 'D' :: tail => unescape(tail, acc + '\r')
      case '%' :: '2' :: 'C' :: tail => unescape(tail, acc + ',')
      case '%' :: '7' :: 'D' :: tail => unescape(tail, acc + '}')
      case '%' :: '2' :: '5' :: tail => unescape(tail, acc + '%')
      case '%' :: tail => throw new IOException("Error deserializing string.")
      case x :: tail => unescape(tail, acc + x)
      case _ => acc
    }
    unescape(s.toList, "'")
  }

  def toXMLBuffer(barr:Array[Byte]): String = barr.map("%02x".format(_)).mkString

  def fromXMLBuffer(s:String): Array[Byte] = s.replaceAll("[^0-9A-Fa-f]", "").sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)

  def toCSVBuffer(buff:Array[Byte]):String = '#' + buff.map("%02x".format(_)).mkString

  def fromCSVBuffer(s:String): Array[Byte] =  {
    s.toList match {
      case '#' :: tail => tail.mkString.replaceAll("[^0-9A-Fa-f]", "").sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
      case _ => throw new IOException("Error deserializing buffer.")
    }
  }

  def compareBytes(b1:Array[Byte], off1:Int, len1:Int, b2:Array[Byte], off2:Int, len2:Int): Int = {
    if (len1 != len2) return if (len1 < len2) -1 else 1
    else {
      for (i <- 0 to len1 if b1(off1 + i) != b2(off2 + i)) {
        return if (b1(off1 + i) < b2(off2 + i)) -1 else 1
      }
    }
    // arrays match return 0
    0
  }
}

