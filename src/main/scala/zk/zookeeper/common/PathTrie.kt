package zk.zookeeper.common

import com.typesafe.scalalogging.Logger
import convertedFromScala.lib.MatchError

open class PathTrie() {
    private val rootNode: TrieNode = TrieNode(null)
    fun apply(): PathTrie = PathTrie()
    fun addPath(path: String): Unit {
        val match = path
        when {
            match == null -> {
                LOG.info("Tried to add null path to PathTrie")
            }
            match is String -> {
                val pathComponents: List<String> = path.split("/").toList()
                var parent: TrieNode? = rootNode
                if (pathComponents.size <= 1) /* ERROR converting `throw new IllegalArgumentException(s"Invalid path $path")`*/
                    for (part: String in pathComponents) {
                        val match1 = parent

                        data class `Some(pTrieNode)_data`(public val p: TrieNode)

                        val `Some(pTrieNode)` by lazy {
                            if (match1 != null) {
                                val (p) = match1
                                if (p is TrieNode) {
                                    return@lazy `Some(pTrieNode)_data`(p)
                                }
                            }
                            return@lazy null
                        }
                        when {
                            `Some(pTrieNode)` != null -> {
                                val (p) = `Some(pTrieNode)`
                                if (p.getChild(part).isEmpty()) p.addChild(part, TrieNode(p))
                                parent = p.getChild(part)
                            }
                            else -> throw MatchError(match1)
                        }
                    }
                if (parent.isDefined()) parent!!.property() = true
            }
            else -> throw MatchError(match)
        }
    }

    fun deletePath(path: String): Unit {
        val match = path
        when {
            match == null -> {
                LOG.info("Tried to delete null path on PathTrie")
            }
            match is String -> {
                var pathComponents: scala.Array<String> = path.split("/")
                var pstr: String = ""
                var parent: TrieNode? = rootNode
                if (pathComponents.length <= 1) /* ERROR converting `throw new IllegalArgumentException(s"Invalid path $path")`*/
                    for (part: String in pathComponents) {
                        val match1 = parent

                        data class `Some(p TrieNode)_data`(public val p: TrieNode)

                        val `Some(p TrieNode)` by lazy {
                            if (match1 != null) {
                                val (p) = match1
                                if (p is TrieNode) {
                                    return@lazy `Some(p TrieNode)_data`(p)
                                }
                            }
                            return@lazy null
                        }
                        when {
                            `Some(p TrieNode)` != null -> {
                                val (p) = `Some(p TrieNode)`
                                val parnt: TrieNode? = p.getChild(part)
                                if (parnt.isEmpty()) return
                                parent = parnt
                                pstr = part
                                LOG.info("$parent")
                            }
                            else -> throw MatchError(match1)
                        }
                    }
                run {
                    val match2 = parent

                    data class `Some(p TrieNode)_data`(public val p: TrieNode)

                    val `Some(p TrieNode)` by lazy {
                        if (match2 != null) {
                            val (p) = match2
                            if (p is TrieNode) {
                                return@lazy `Some(p TrieNode)_data`(p)
                            }
                        }
                        return@lazy null
                    }
                    when {
                        `Some(p TrieNode)` != null -> {
                            val (p) = `Some(p TrieNode)`
                            run {
                                val match3 = p.parent()

                                data class `Some(realParent TrieNode)_data`(public val realParent: TrieNode)

                                val `Some(realParent TrieNode)` by lazy {
                                    if (match3 != null) {
                                        val (realParent) = match3
                                        if (realParent is TrieNode) {
                                            return@lazy `Some(realParent TrieNode)_data`(realParent)
                                        }
                                    }
                                    return@lazy null
                                }
                                when {
                                    `Some(realParent TrieNode)` != null -> {
                                        val (realParent) = `Some(realParent TrieNode)`
                                        realParent.deleteChild(pstr)
                                    }
                                    else -> throw MatchError(match3)
                                }
                            }
                        }
                        else -> throw MatchError(match2)
                    }
                }
            }
            else -> throw MatchError(match)
        }
    }

    fun findMaxPrefix(path: String): String? {
        if (path == null) return null
        if (path.equals("/")) return path
        val pathComponents: List<String> = arrayToList(path.split("/"))
        var parent: TrieNode = rootNode
        var components: List<String> = emptyList()
        var n: TrieNode = TrieNode(null)
        var property: Boolean = false
        if (pathComponents.length <= 1) /* ERROR converting `throw new IllegalArgumentException(s"Invalid path $path")`*/
            for (pc: String in pathComponents) {
                if (parent.hasChild(pc)) {
                    parent = parent.getChild(pc)!!
                    components = listOf(pc) + components
                    property = property || parent.property
                } else break()
            }
        return components.joinToString("/")
    }

    fun clear(): Unit = for (child: String in rootNode.children) {
        rootNode.deleteChild(child)
    }

    companion object {
        val LOG: Logger = Logger.apply(classOf<PathTrie>())
        fun <A> arrayToList(a: scala.Array<A>): List<A> = a.toList()
    }
}