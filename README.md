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

### Option
Use it when a value can be absent or some validation can fail and you don't care about the exact cause. Typically in data retrieval and validation logic

### Either[L,R]
Similar use case as Option but when you do need to provide some information about the error.

### Try[T]
Use when something Exceptional can happen that you cannot handle in the function. This, in general, excludes validation logic and data retrieval failures but can be used to report unexpected failures.

### Exceptions
Use only as a last resort. When catching exceptions use the facility methods Scala provides and never catch { _ => }, instead use catch { NonFatal(_) => }

## Parsing data
https://github.com/fasterxml/jackson

## Integration
https://github.com/apache/camel

## Configuration
https://commons.apache.org/proper/commons-configuration/index.html

