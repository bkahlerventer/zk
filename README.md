# zk
zookeeper implementation in scala - a work in progress.

## Status
Jute compiler still needs some refactoring.
Currently moving code generation to the individual Generators and out of the various types defined inside JTypes.scala.  
This will make the generation of an AST possible.  Then we can move over to using scala parser combinators

This README currently documents the coding effort, will change later with better information pertaining to project

## changes planned
* move exception handling to exception avoidance
* eliminate null in favour of Option,Some,None

## Enumeration used
* https://github.com/lloydmeta/enumeratum

## XML
* http://scalaxb.org

## Testing
* ScalaCheck https://www.scalacheck.org

## Exceptions: Try, Option or Either
http://blog.xebia.com/try-option-or-either/

