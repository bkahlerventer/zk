package zk.jc

import scala.util.parsing.combinator.syntactical.StandardTokenParsers


class HadoopDataDescLangParser extends StandardTokenParsers {
  lexical.reserved += ("module", "class", "ustring", "boolean", "include")
  lexical.delimiters += ("*", "<", ">", "(", ")", ";", "{", "}")

}
