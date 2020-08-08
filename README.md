# Multi Level Networks Model

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/multi-level-network-example1.png?raw=true"/>

Graphs are used to represents data in many situations, especially where it is important do understand how data is interconnected. Each application has its own needs and it is important that the model and the query language are well designed.

The aim of this project is to create a model and query language for multi-level networks analysis. For example we have a network (e.g. LinkedIn) where people are connected if they are colleagues, and another network (e.g. Facebook) where people are connected if they are friends). These two networks are often very different. Let's see few examples:

 - Mark and Peter are colleagues but not friends.
 - Mark and Steve are colleagues.
 - Steve and Peter don't know each other but they might become friends thanks to Mark.

This project is exhaustively described in my [Master Thesis](https://github.com/miromannino/multi-level-networks-model/raw/master/docs/Tesi%20Magistrale.pdf) (in Italian).

This repository contains the code for the implementation of the model, and the algebra to query the data. 

## Query Language

The query language is based on paths and walks.

 - A **path** is a series of nodes in the graph without repetitions. In the example above 
 - A **walk** is a series of nodes in the graph where nodes can be repeated.

Similarly to regular expressions, patterns can be defined in order to find specific paths. 

Now we will quickly describe the algebra operators, but the thesis provides formal definitions, with properties and detailed examples. 

### Concatenation (.)

Concatenation P.P' simply concatenates two paths.

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/concatenation-definition.png?raw=true" />

### Projection (π)

It is a vertical cut on paths. For example if we have a set of paths, even if they are of different lenghts:

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/projection-example.png?raw=true" />

### Path patterns

Similarly to regular expressions, patterns use these operators:

 - `%` to idenfify a generic node that has to be in the path
 - `?` to identify optional generic node
 - `*` to identify a series of nodes (or an empty path)

Concatenation: P<sub>1</sub> → P<sub>2</sub> it is a path pattern used to describe a node P<sub>2</sub> followed by a node P<sub>1</sub>. 


Alternation: P<sub>1</sub> | P<sub>2</sub> it is a path pattern used to describe an alternation between P<sub>1</sub> and P<sub>2</sub>.

Examples:

 - `a → % → b` identifies a path that has a beginning in the node a and an end in the node b. The paths that this pattern can find can only have two edges. For example: (a,d,b), or (a,z,b).

 - `a → ? → b` identifies a path that has a beginning in the node a and an end in the node b. In this case the node at the center is optional, so results can be for example: (a,b), or (a,z,b).

 - `(a → b)|(d → c)` identifies either the edge (a,b) or the edge (d,c).

 - `a → *` identifies all paths starting from the node a. These can have various lenghts, and projection can be used to set a limit on the path legths.


A more formal and complete definition of all operators and their use can be found in the thesis.


### Selection (&sigma;)

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/selection-example.png?raw=true" />

The selection operator uses path patterns in order to retrieve results. For example σ<sub>a→∗→b</sub>(G) selects all paths of any length starting from <strong>a</strong> and ending in <strong>b</strong>. 

### Synthesis (&eta;)

This operator builds a sub-graph given a series of paths. Let's for example assume that a selection operator retrieved the following paths: ```{(a,b,c), (a,b,e)}```.

The synthesis operator builds a sub-graph given these paths:

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/synthesis-example.png?raw=true" />

This enables operators to be combined together for more complex queries.

### Aggregation (&alpha;)

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/aggregation-example.png?raw=true" />

Aggregation joins nodes together, and similarly to the aggregation operator in relational databases, data contained in these nodes is aggregated. In the example above the operation is: 

<code>
α<sub>% . π<sub>2</sub>(p); Sum(π<sub>1</sub>(p)) = 1</sub>(P).
</code>
<br/><br/>

Another example is where we want to know how many hops there are to reach specific nodes. For example:

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/aggregation-example2.png?raw=true" />


### Join (&#10781;)

Differently than the operators defined before that only works in one network level, the join operator is able to join paths that are in multiple levels. 


<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/join-example.png?raw=true" />


Join is also able by definition to group multiple nodes in one network level to one node to another network level.

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/join-example2.png?raw=true" />

An example of use can be, for example to understand what's the minimum path between two nodes n<sub>1</sub> and n<sub>2</sub> despite these two are not connected in a specific network level (e.g. n<sub>1</sub> and n<sub>2</sub> are not friends on Facebook, but are connected through other platforms).

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/join-example3-1.png?raw=true" />

We can join nodes between these two network levels and group paths in order to get the minimum path between n<sub>1</sub> and n<sub>2</sub>:

<img src="https://github.com/miromannino/multi-level-networks-model/blob/master/docs/join-example3-2.png?raw=true" />

## Other info

The thesis also discusses about implementation of the model to represent the data, and implementation of the operators.

Furthermore, the thesis also discusses about optimizations, since some of the operators (e.g. join) are expensive to compute.

Path patterns are implemented using finite automata.


## Project

The prototype of this model and its operator is done in Java. 

Folders:

 - <strong>src</strong> source code
 - <strong>test</strong> tests
 - <strong>out</strong> output generated by tests in dot format. These can be visualized using Graphviz dot.

### Model

`Arc` class defines an arc, with the starting point `fromNode`, the arrival point `toNode` and the associated data `data`. It holds all the methods to be able to retrieve and manipulate this information, to compare arcs, and to generate a hashcode (useful for storing arcs in a HashMap).

`ArcIterator` the complex iterator that enumerates all the arcs of a Network Level (with the possibility of eliminating the current element), starting from a completely different representation made up of nodes and links.

`Couple` class which defines a Couple, which maintains a list of associations (defined as `CoupleLinks`) that start at one level and end at another.

`CoupleLink` class that defines a link of a Couple, with a starting and ending node, and the associated data.

`LevelsCoupling` class that defines a Levels Coupling, which stores a list of Couples.

`Link` class that defines an outgoing arch, stored by autumn. It keeps the information on the destination node and the data associated with the arch, and holds all the methods to be able to retrieve and manipulate this information, to compare links, and to generate a hashcode (useful for storing links in a HashMap).

```java
	class Link {
		Node destinationNode;
		Object data;
		
		Link(Node destinationNode, Object data) { /* ... */ } 
		Link(Node destinationNode) { /* ... */ }
		
		public Object getData() { /* ... */ }
		public void setData(Object data) { /* ... */ } 
		public Node getDestinationNode() { /* ... */ } 

		/* ... */
	}
```

`NetworkLevel` class which defines a Network Level, keeping an identifier and a set of nodes stored on a map. This class provides methods for adding, removing, and iterating nodes and edges. Remember that the addition or removal of the arcs, as well as their iteration, is virtual and mapped on the real storage, made up of Nodes and Links.

```java
	class NetworkLevel {
		String id;
		Map<String, Node> N;

		NetworkLevel(String id) { /* ... */ }
		String getId() { /* ... */ }
		Node addNewNode(String id, Object data) { /* ... */ }
		Node addNewNode(String id) { /* ... */ }
		boolean addNewArc(Node fromNode, Node toNode, Object data) { /* ... */ } 
		boolean addNewArc(Node fromNode, Node toNode) { /* ... */ }
		boolean containsNode(Node n) { /* ... */ }
		boolean containsArc(Node fromNode, Node toNode) { /* ... */ } 
		Iterator<Node> getNodeIterator() { /* ... */ }
		ArcIterator getArcIterator() { /* ... */ }

		/* ... */
	}
```

`Node` class that defines a node. It stores an identifier, an associated datum, and a link map (i.e. outgoing arcs). It also contains all the methods to manipulate and retrieve this information, to iterate the outgoing arcs, and to generate a hashcode (useful for storing nodes in a HashMap).

```java
	class Node {
		String id;
		Object data;
		Map<Node, Link> links;

		Node(String id, Object data, Link[] links) { /* ... */ } 
		Node(String id, Object data) { /* ... */ }
		Node(String id) { /* ... */ }
		
		Object getData() { /* ... */ }
		void setData(Object data) { /* ... */ }
		boolean addNeighbor(Node n) { /* ... */ }
		boolean addNeighbor(Node n, Object data) { /* ... */ }
		Link removeNeighbor(Node n) { /* ... */ }
		boolean hasNeighbor(Node n) { /* ... */ }
		Object getNeighborLinkData(Node n) { /* ... */ }
		boolean setNeighborLinkData(Node n, Object data) { /* ... */ } 
		Iterator<Link> getLinksIterator() { /* ... */ }
		Iterator<Node> getNeighborIterator() { /* ... */ }

		/* ... */
	}
```

`Path` class that defines a path. It stores to its internal list of files and sets the methods to retrieve and manipulate these nodes, to know the length of the path, to compare two paths and to generate a hashcode (useful for storing paths in a HashMap).

```java
class Path implements Iterable<Node> {
	List<Node> nodes;
	Path(Node[] nodes) { /* ... */ }
	public Path(int initialCapacity) { /* ... */ } 
	public Path() { /* ... */ }
	public Path(Path path) { /* ... */ }
	Node getNodeAtPosition(int pos) { /* ... */ }
	void setNodeAtPosition(int pos, Node n) { /* ... */ }
	void appendNode(Node n) { /* ... */ }
	Node removeLastNode() { /* ... */ }
	boolean contains(Node n) { /* ... */ }
	int getLength() { /* ... */ }
	@Override Iterator<Node> iterator() { /* ... */ } 

	/* ... */
}
```

`PathSet` class that defines a set of paths, with the related methods for the manipulation and recovery of such paths, for their iteration and for quickly determining whether a path belongs to this set.

### Operators

The `pathpattern` package defines all the classes useful for the construction of a path pattern and the construction of the relative automaton.

`Automaton` class defines an automaton related to a path pattern. Its constructor takes a path pattern as a parameter and has the task of generating the nodes and links of the automaton. It only keeps track of the initial and final node, since the other nodes can be recovered by navigating the automaton.

`AutomatonLink` class defines a link between one automata node and another. It also stores the action necessary to perform the transition.

`AutomatonLinkActionType` enum defines the type of action: epsilon transition, generic node or specific node.

`AutomatonNode` class defines an automaton node. It stores a list of `AutomatonLinks` to neighbors.

`PathPattern` abstract class contains all the specific classes of path patterns: `EmptyPath`, `GraphNode`, `GenericNode`, `OptionalGenericNode`, `GenericPath`, `Concatenation`, and `Alternation`.

`Aggregation` defines the method that implements the aggregation operator. It also defines the abstract class `CFunction` and the abstract class `Assignements`, useful for defining the callback functions necessary to call this operator.

`Concatenation` defines the method for the concatenation operator.

`Join` defines the methods that implement the join operator. Inside it is also defined the abstract `DataJoin` class, useful for defining the call-back function needed to call this operator.

`Pathcondition` defines the abstract class to build a predicate on a path, useful in a totally exclusive way for the selection operator. Within it, concrete path conditions have been defined such as: `TruePredicate` which always returns `true`, or `SpecificLengthPredicate` which efficiently defines the predicate that controls the length of a path.

`Projection` defines the methods that implement the projection operator. Inside it is also defined the abstract class `Index`, useful for defining the indices needed to call this operator. The concrete class `ConstantIndex` is also defined which realizes a constant index.

`Selection` defines the methods that implement the selection operator. 

`Synthetis` defines the methods that implement the synthesis operator.




