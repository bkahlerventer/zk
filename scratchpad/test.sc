val s = "fdgsfgsdfgsdfgsdfg"


val h = s.map(c => c.toByte.toHexString).mkString("")
def hex2bytes(hex: String): Array[Byte] = hex.replaceAll("[^0-9A-Fa-f]", "").sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
val b = hex2bytes(h)
b.map("%02x".format(_)).mkString