package zk.jute.compiler

import java.io.{File, IOException}

class JavaGenerator(name:String, ilist:List[JFile], rlist:List[JRecord], outDir:File) extends CodeGenerator {
  private val mFQName = name
  private val idx = name.lastIndexOf('.')
  private val mName = name.substring(idx+1)
  private val mInclFiles = ilist
  private val mRecList = rlist
  private final val outputDirectory = outDir
  private val mModule = name.substring(0,idx)

  def genJavaCode(outDir: File, record: JRecord) :Unit = {
    if(!outDir.exists() && !outDir.mkdirs())
      throw new IOException(s"unable to create output directory $outDir")

  }

  def genCode(): Unit = {
    if (!outputDirectory.exists() && !outputDirectory.mkdirs())
      throw new IOException(s"unable to create output directory $outputDirectory")
    try {
      for (i <- mRecList) genJavaCode(outputDirectory,i)
    }
  }

  override def genGetSet(j: Jute, fIdx: Int): String = ???

  override def genCompareTo(j: Jute, other:String): String = j match {
    case JBoolean(fname, jtype, suffix, wrapper) =>  s"ret = $fname == peer.$fname ? 0 : ($fname ? 1 : -1);"
    case JBuffer(fname, _type, suffix, wrapper) =>
      val o = if (other != null) other else s"peer.$fname"
      s"""
         |    {
         |      byte[] my = $fname;
         |      byte[] ur = $o;
         |      ret = org.apache.jute.Utils.compareBytes(my,0,my,length,ur,0,ur,length);
         |     }
     """.stripMargin
    case JMap(k,v,fname, jtype,s,w) => s"""    throw new unsupportedOperationException("comparing $fname is unimplemented")"""


  }

  override def genEquals(j: Jute, peer: String): String = j match {
    case JBuffer(fname, jtype, suffix, wrapper) => s"    ret = java.util.Arrays.toString($fname).hashCode();\n"
  }

  override def genHashCode(j: Jute): String = ???

  override def genReadWrapper(j: Jute, tag: String, decl: Boolean): String = j match {
    case JBuffer(fname, _, _, _) =>
      s"""
        |    ${if(decl) s"byte[] $fname;"}
        |        $fname=a_readBuffer("$tag");
      """.stripMargin
  }

  override def genWriteWrapper(j: Jute, tag: String): String = j match {
    case JBuffer(fname, _type, suffix, wrapper) =>
      s"""        a_.writeBuffer($fname,"$tag");"""
  }

  override def genCompareToWrapper(j: Jute, other: String): String = j match {
    case JBuffer(fname, _type, _, wrapper) =>  s"    ${genCompareTo(j,other)}"
  }

  override def genSlurpBytes(j: Jute, b: String, s: String, l: String): String = j match {
    case JBuffer(fname,jtype, suffix, wrapper) =>
      s"""
         |        {
         |          int i = org.apache.jute.Utils.readVInt($b, $s);
         |          int z: Int = WritableUtils.getVIntSize(i);
         |          $s += z+i; $l -= (z=i);
         |        }
       """.stripMargin
  }

  override def genCompareBytes(j: Jute): String = j match {
    case JBuffer(fname, jtype, suffix, wrapper) =>
      """\
        |        {
        |          int i1: Int = org.apache.jute.Utils.readVInt(b1, s1);
        |          int i2 = org.apache.jute.Utils.readVInt(b2, s2);
        |          int z1: Int = WritableUtils.getVIntSize(i1);
        |          int z2 = WritableUtils.getVIntSize(i2);
        |          s1+=z1; s2+=z2; l1-=z2; l2-=z2;
        |          int r1 = org.apache.jute.Utils.compareBytes(b1,s1,l1,b2,s2,l2);
        |          if (r1 != 0) { return (r1<0)?-1:0; }
        |          s1+=i1; s2+=i2; l1-=i1; l1-=i2;
        |        }
      """.stripMargin
  }

  override def genDecl(j: Jute): String = ???

  override def genContructorParam(j: Jute): String = ???

  override def genConstructorSet(j: Jute): String = ???

  override def genWriteMethodName(j: Jute): String = ???

  override def genReadMethodName(j: Jute): String = ???

  override def getSignature(j: Jute): String = ???
}

