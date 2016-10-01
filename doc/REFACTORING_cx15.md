Charles Xu (cx15)

### Commit <b9c94f693ad7a4354ae052331eeaa6bf3681eba2>

In XMLParser, instead of having a static method to parse XML into a Document and then pass this Document back to itself, have such procedure be executed in constructor. Ditto for the enum class and factory pattern.

Make XMLParser abstract and have XpathXMLParser extends it, which is makes it much easier to which to other XML query language. 

Instead of catching and swallowing exceptions in the parser, propagate them to the front end and have
appropriate dialog conveying such exceptions.

Make XMLParser abstract and have XpathXMLParser extends it, which is makes it much easier to which to other XML query language. 

Correct quite a few typo in spellings.



### Commit <f9250c09acb95817281270558d06781c9f41283f>

I refactored XMLParser.java to eliminate the duplicated code in getItem() and getNodeList().

The solution is to extract out a common method and each of these methods call it with different params.

The only downside of this approach is that String and Nodelist have different ways to determine if is Empty
so we have to multiplex using if tree.