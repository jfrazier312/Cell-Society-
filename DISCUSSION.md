Charles Xu (cx15)
Jordan Frazier (jrf30)

I refactored XMLParser.java to eliminate the duplicated code in getItem() and getNodeList().

The solution is to extract out a common method and each of these methods call it with different params.

The only downside of this approach is that String and Nodelist have different ways to determine if is Empty
so we have to multiplex using if tree.