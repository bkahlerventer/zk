package zk.jute.compiler

import java.io.{File, FileWriter, IOException}

/** Creates a new instance of CSharpGenerator
  *
  * @param name possibly full pathname to the file
  * @param ilist included files (as JFile)
  * @param rlist List of records defined within this file
  * @param outDir Output Directory
  */
class CSharpGenerator(name:String, ilist:List[JFile], rlist:List[JRecord], outDir:File) {
  private val mName = new File(name).getName
  private val mInclFiles = ilist
  private val mRecList = rlist
  private final val outputDirectory = outDir

  def genCode(): Unit = {
    if (!outputDirectory.exists() && !outputDirectory.mkdirs())
      throw new IOException(s"unable to create output directory $outputDirectory")
    try {
      for (i <- mRecList) i.genCsharpCode(outputDirectory)
    }
  }
}
