# zk
zookeeper implementation in scala - a work in progress.

## Status
Jute compiler still needs some refactoring.
Currently moving code generation to the individual Generators and out of the various types defined inside JTypes.scala.  
This will make the generation of an AST possible.  Then we can move over to using scala parser combinators

## changes planned
* move exception handling to exception avoidance
* eliminate null in favour of Option,Some,None

## Enumeration used
* https://github.com/lloydmeta/enumeratum

## XML
* http://scalaxb.org