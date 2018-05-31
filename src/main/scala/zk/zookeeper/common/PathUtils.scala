package zk.zookeeper.common

/**
  * Path related utilities
  */
object PathUtils {
  /**
    * validate the provided znode path string
    * @param path znode path string
    * @param isSequential if the path is being created with a sequential flag
    * @throws IllegalArgumentException if the path is invalid
    */
  def validatePath(path:String, isSequential:Boolean):Unit = validatePath(if(isSequential) path+"1" else path)
  def validatePath(path:String):Unit =  {
    var reason:String = ""
    if(path == null) throw new IllegalArgumentException("Path cannot be null")
    if(path.length == 0) throw new IllegalArgumentException("Path length must be > 0")
    if(!(path startsWith "/")) throw new IllegalArgumentException("Path must start with / character")
    if(path.length == 1) return
    if(path endsWith "/") reason = "Path must not end with / character"
    if(path contains "//") reason = s"""empty node name specified @${path indexOf "//"}"""
    if(path contains '\0') reason = s"""null character not allowed @${path indexOf '\0' }"""

    if(path contains "./") reason = s"""relative paths not allowed @${path indexOf "./"}"""
    if(path endsWith "/.") reason = s"""relative paths not allowed @${path indexOf "/."}"""

    for(c <- path
        if c > '\u0000' && c <= '\u001f'
        if c >= '\u007f' && c<= '\u009f'
        if c >= '\ud800' && c <= '\uf8ff'
        if c >= '\ufff0' && c <= '\uffff') reason = s"""invalid character @${path indexOf c}"""

    if(!reason.isEmpty) throw new IllegalArgumentException(s"Invalid path string $path caused by $reason")

  }

  def normalizeFileSystemPath(path:String):String = {
    if(!path.isEmpty) {
      if(java.lang.System.getProperty("os.name").toLowerCase.contains("windows")) {
        return path.replace('\\', '/')
      }
    }
    path
  }

}