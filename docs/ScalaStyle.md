<h1 class="header">Effective Scala</h1>

<address>Marius Eriksen, Twitter Inc.<br />marius@twitter.com (<a href="http://twitter.com/marius">@marius</a>)</address>

<h2>Table of Contents</h2>

<ul>
<li><strong><a href="#Introduction">Introduction</a></strong></li>
<li><strong><a href="#Formatting">Formatting</a></strong>: <em><a href="#Formatting-Whitespace">Whitespace</a></em>, <em><a href="#Formatting-Naming">Naming</a></em>, <em><a href="#Formatting-Imports">Imports</a></em>, <em><a href="#Formatting-Braces">Braces</a></em>, <em><a href="#Formatting-Pattern matching">Pattern matching</a></em>, <em><a href="#Formatting-Comments">Comments</a></em></li>
<li><strong><a href="#Types and Generics">Types and Generics</a></strong>: <em><a href="#Types and Generics-Return type annotations">Return type annotations</a></em>, <em><a href="#Types and Generics-Variance">Variance</a></em>, <em><a href="#Types and Generics-Type aliases">Type aliases</a></em>, <em><a href="#Types and Generics-Implicits">Implicits</a></em></li>
<li><strong><a href="#Collections">Collections</a></strong>: <em><a href="#Collections-Hierarchy">Hierarchy</a></em>, <em><a href="#Collections-Use">Use</a></em>, <em><a href="#Collections-Style">Style</a></em>, <em><a href="#Collections-Performance">Performance</a></em>, <em><a href="#Collections-Java Collections">Java Collections</a></em></li>
<li><strong><a href="#Concurrency">Concurrency</a></strong>: <em><a href="#Concurrency-Futures">Futures</a></em>, <em><a href="#Concurrency-Collections">Collections</a></em></li>
<li><strong><a href="#Control structures">Control structures</a></strong>: <em><a href="#Control structures-Recursion">Recursion</a></em>, <em><a href="#Control structures-Returns">Returns</a></em>, <em><a href="#Control structures-`for` loops and comprehensions"><code>for</code> loops and comprehensions</a></em>, <em><a href="#Control structures-`require` and `assert`"><code>require</code> and <code>assert</code></a></em></li>
<li><strong><a href="#Functional programming">Functional programming</a></strong>: <em><a href="#Functional programming-Case classes as algebraic data types">Case classes as algebraic data types</a></em>, <em><a href="#Functional programming-Options">Options</a></em>, <em><a href="#Functional programming-Pattern matching">Pattern matching</a></em>, <em><a href="#Functional programming-Partial functions">Partial functions</a></em>, <em><a href="#Functional programming-Destructuring bindings">Destructuring bindings</a></em>, <em><a href="#Functional programming-Laziness">Laziness</a></em>, <em><a href="#Functional programming-Call by name">Call by name</a></em>, <em><a href="#Functional programming-`flatMap`"><code>flatMap</code></a></em></li>
<li><strong><a href="#Object oriented programming">Object oriented programming</a></strong>: <em><a href="#Object oriented programming-Dependency injection">Dependency injection</a></em>, <em><a href="#Object oriented programming-Traits">Traits</a></em>, <em><a href="#Object oriented programming-Visibility">Visibility</a></em>, <em><a href="#Object oriented programming-Structural typing">Structural typing</a></em></li>
<li><strong><a href="#Error handling">Error handling</a></strong>: <em><a href="#Error handling-Handling exceptions">Handling exceptions</a></em></li>
<li><strong><a href="#Garbage collection">Garbage collection</a></strong></li>
<li><strong><a href="#Java compatibility">Java compatibility</a></strong></li>
<li><strong><a href="#Twitter's standard libraries">Twitter&rsquo;s standard libraries</a></strong>: <em><a href="#Twitter's standard libraries-Futures">Futures</a></em>, <em><a href="#Twitter's standard libraries-Offer/Broker">Offer/Broker</a></em></li>
<li><strong><a href="#Acknowledgments">Acknowledgments</a></strong></li>
</ul>

<h2>Other languages</h2>

<p><a href="index-ja.html">日本語</a>
<a href="index-ru.html">Русский</a>
<a href="index-cn.html">简体中文</a></p>

<p><a id="Introduction" /></p>

<h2>Introduction</h2>

<p><a href="http://www.scala-lang.org/">Scala</a> is one of the main application programming languages
used at Twitter. Much of our infrastructure is written in Scala and
<a href="http://github.com/twitter/">we have several large libraries</a>
supporting our use. While highly effective, Scala is also a large language,
and our experiences have taught us to practice great care in its
application. What are its pitfalls? Which features do we embrace,
which do we eschew? When do we employ &ldquo;purely functional style&rdquo;, and when
do we avoid it? In other words: what have we found to be an effective
use of the language? This guide attempts to distill our experience into short
essays, providing a set of <em>best practices</em>. Our use of Scala is mainly for
creating high volume services that form distributed systems &mdash; and our
advice is thus biased &mdash; but most of the advice herein should translate
naturally to other domains. This is not the law, but deviation should
be well justified.</p>

<p>Scala provides many tools that enable succinct expression. Less typing
is less reading, and less reading is often faster reading, and thus
brevity enhances clarity. However brevity is a blunt tool that can
also deliver the opposite effect: After correctness, think always of
the reader.</p>

<p>Above all, <em>program in Scala</em>. You are not writing Java, nor Haskell,
nor Python; a Scala program is unlike one written in any of these. In
order to use the language effectively, you must phrase your problems
in its terms. There&rsquo;s no use coercing a Java program into Scala, for
it will be inferior in most ways to its original.</p>

<p>This is not an introduction to Scala; we assume the reader
is familiar with the language. Some resources for learning Scala are:</p>

<ul>
<li><a href="http://twitter.github.com/scala_school/">Scala School</a></li>
<li><a href="http://www.scala-lang.org/node/1305">Learning Scala</a></li>
<li><a href="http://matt.might.net/articles/learning-scala-in-small-bites/">Learning Scala in Small Bites</a></li>
</ul>

<p>This is a living document that will change to reflect our current
&ldquo;best practices,&rdquo; but its core ideas are unlikely to change: Always
favor readability; write generic code but not at the expensive of
clarity; take advantage of simple language features that afford great
power but avoid the esoteric ones (especially in the type system).
Above all, be always aware of the trade offs you make. A sophisticated
language requires a complex implementation, and complexity begets
complexity: of reasoning, of semantics, of interaction between
features, and of the understanding of your collaborators. Thus complexity
is the tax of sophistication &mdash; you must always ensure that its utility exceeds its cost.</p>

<p>And have fun.</p>

<p><a id="Formatting" /></p>

<h2>Formatting</h2>

<p>The specifics of code <em>formatting</em> &mdash; so long as they are practical &mdash;
are of little consequence. By definition style cannot be inherently
good or bad and almost everybody differs in personal
preference. However the <em>consistent</em> application of the same
formatting rules will almost always enhance
readability. A reader already familiar with a particular style does
not have to grasp yet another set of local conventions, or decipher
yet another corner of the language grammar.</p>

<p>This is of particular importance to Scala, as its grammar has a high
degree of overlap. One telling example is method invocation: Methods
can be invoked with &ldquo;<code>.</code>&rdquo;, with whitespace, without parenthesis for
nullary or unary methods, with parenthesis for these, and so on.
Furthermore, the different styles of method invocations expose
different ambiguities in its grammar! Surely the consistent
application of a carefully chosen set of formatting rules will resolve
a great deal of ambiguity for both man and machine.</p>

<p>We adhere to the <a href="http://docs.scala-lang.org/style/">Scala style
guide</a> plus the following rules.</p>

<p><a id="Formatting-Whitespace" /></p>

<h3>Whitespace</h3>

<p>Indent by two spaces. Try to avoid lines greater than 100 columns in
length. Use one blank line between method, class, and object definitions.</p>

<p><a id="Formatting-Naming" /></p>

<h3>Naming</h3>

<dl class="rules">
<dt>Use short names for small scopes</dt>
<dd> <code>i</code>s, <code>j</code>s and <code>k</code>s are all but expected
in loops. </dd>
<dt>Use longer names for larger scopes</dt>
<dd>External APIs should have longer and explanatory names that confer meaning.
<code>Future.collect</code> not <code>Future.all</code>.
</dd>
<dt>Use common abbreviations but eschew esoteric ones</dt>
<dd>
Everyone
knows <code>ok</code>, <code>err</code> or <code>defn</code> 
whereas <code>sfri</code> is not so common.
</dd>
<dt>Don't rebind names for different uses</dt>
<dd>Use <code>val</code>s</dd>
<dt>Avoid using <code>`</code>s to overload reserved names.</dt>
<dd><code>typ</code> instead of <code>`type</code>`</dd>
<dt>Use active names for operations with side effects</dt>
<dd><code>user.activate()</code> not <code>user.setActive()</code></dd>
<dt>Use descriptive names for methods that return values</dt>
<dd><code>src.isDefined</code> not <code>src.defined</code></dd>
<dt>Don't prefix getters with <code>get</code></dt>
<dd>As per the previous rule, it's redundant: <code>site.count</code> not <code>site.getCount</code></dd>
<dt>Don't repeat names that are already encapsulated in package or object name</dt>
<dd>Prefer:
<pre><code>object User {
  def get(id: Int): Option[User]
}</code></pre> to
<pre><code>object User {
  def getUser(id: Int): Option[User]
}</code></pre>They are redundant in use: <code>User.getUser</code> provides
no more information than <code>User.get</code>.
</dd>
</dl>

<p><a id="Formatting-Imports" /></p>

<h3>Imports</h3>

<dl class="rules">
<dt>Sort import lines alphabetically</dt>
<dd>This makes it easy to examine visually, and is simple to automate.</dd>
<dt>Use braces when importing several names from a package</dt>
<dd><code>import com.twitter.concurrent.{Broker, Offer}</code></dd>
<dt>Use wildcards when more than six names are imported</dt>
<dd>e.g.: <code>import com.twitter.concurrent._</code>
<br />Don't apply this blindly: some packages export too many names</dd>
<dt>When using collections, qualify names by importing 
<code>scala.collection.immutable</code> and/or <code>scala.collection.mutable</code></dt>
<dd>Mutable and immutable collections have dual names. 
Qualifiying the names makes is obvious to the reader which variant is being used (e.g. "<code>immutable.Map</code>")</dd>
<dt>Do not use relative imports from other packages</dt>
<dd>Avoid <pre><code>import com.twitter
import concurrent</code></pre> in favor of the unambiguous <pre><code>import com.twitter.concurrent</code></pre></dd>
<dt>Put imports at the top of the file</dt>
<dd>The reader can refer to all imports in one place.</dd>
</dl>

<p><a id="Formatting-Braces" /></p>

<h3>Braces</h3>

<p>Braces are used to create compound expressions (they serve other uses
in the &ldquo;module language&rdquo;), where the value of the compound expression
is the last expression in the list. Avoid using braces for simple
expressions; write</p>

<pre><code>def square(x: Int) = x*x
</code></pre>

<p class="LP">but not</p>

<pre><code>def square(x: Int) = {
  x * x
}
</code></pre>

<p class="LP">even though it may be tempting to distinguish the method body syntactically. The first alternative has less clutter and is easier to read. <em>Avoid syntactical ceremony</em> unless it clarifies.</p>

<p><a id="Formatting-Pattern matching" /></p>

<h3>Pattern matching</h3>

<p>Use pattern matching directly in function definitions whenever applicable;
instead of</p>

<pre><code>list map { item =&gt;
  item match {
    case Some(x) =&gt; x
    case None =&gt; default
  }
}
</code></pre>

<p class="LP">collapse the match</p>

<pre><code>list map {
  case Some(x) =&gt; x
  case None =&gt; default
}
</code></pre>

<p class="LP">it's clear that the list items are being mapped over &mdash; the extra indirection does not elucidate.</p>

<p><a id="Formatting-Comments" /></p>

<h3>Comments</h3>

<p>Use <a href="https://wiki.scala-lang.org/display/SW/Scaladoc">ScalaDoc</a> to
provide API documentation. Use the following style:</p>

<pre><code>/**
 * ServiceBuilder builds services 
 * ...
 */
</code></pre>

<p class="LP">but <em>not</em> the standard ScalaDoc style:</p>

<pre><code>/** ServiceBuilder builds services
 * ...
 */
</code></pre>

<p>Do not resort to ASCII art or other visual embellishments. Document
APIs but do not add unnecessary comments. If you find yourself adding
comments to explain the behavior of your code, ask first if it can be
restructured so that it becomes obvious what it does. Prefer
&ldquo;obviously it works&rdquo; to &ldquo;it works, obviously&rdquo; (with apologies to Hoare).</p>

<p><a id="Types and Generics" /></p>

<h2>Types and Generics</h2>

<p>The primary objective of a type system is to detect programming
errors. The type system effectively provides a limited form of static
verification, allowing us to express certain kinds of invariants about
our code that the compiler can verify. Type systems provide other
benefits too of course, but error checking is its Raison d&#146;&Ecirc;tre.</p>

<p>Our use of the type system should reflect this goal, but we must
remain mindful of the reader: judicious use of types can serve to
enhance clarity, being unduly clever only obfuscates.</p>

<p>Scala&rsquo;s powerful type system is a common source of academic
exploration and exercise (eg. <a href="http://apocalisp.wordpress.com/2010/06/08/type-level-programming-in-scala/">Type level programming in
Scala</a>).
While a fascinating academic topic, these techniques rarely find
useful application in production code. They are to be avoided.</p>

<p><a id="Types and Generics-Return type annotations" /></p>

<h3>Return type annotations</h3>

<p>While Scala allows these to be omitted, such annotations provide good
documentation: this is especially important for public methods. Where a
method is not exposed and its return type obvious, omit them.</p>

<p>This is especially important when instantiating objects with mixins as
the scala compiler creates singleton types for these. For example, <code>make</code>
in:</p>

<pre><code>trait Service
def make() = new Service {
  def getId = 123
}
</code></pre>

<p class="LP">does <em>not</em> have a return type of <code>Service</code>; the compiler creates the refinement type <code>Object with Service{def getId: Int}</code>. Instead use an explicit annotation:</p>

<pre><code>def make(): Service = new Service{}
</code></pre>

<p>Now the author is free to mix in more traits without changing the
public type of <code>make</code>, making it easier to manage backwards
compatibility.</p>

<p><a id="Types and Generics-Variance" /></p>

<h3>Variance</h3>

<p>Variance arises when generics are combined with subtyping. Variance defines
how subtyping of the <em>contained</em> type relates to subtyping of the
<em>container</em> type. Because Scala has declaration site variance
annotations, authors of common libraries &mdash; especially collections &mdash;
must be prolific annotators. Such annotations are important for the
usability of shared code, but misapplication can be dangerous.</p>

<p>Invariants are an advanced but necessary aspect of Scala&rsquo;s typesystem,
and should be used widely (and correctly) as it aids the application
of subtyping.</p>

<p><em>Immutable collections should be covariant</em>. Methods that receive
the contained type should &ldquo;downgrade&rdquo; the collection appropriately:</p>

<pre><code>trait Collection[+T] {
  def add[U &gt;: T](other: U): Collection[U]
}
</code></pre>

<p><em>Mutable collections should be invariant</em>. Covariance
is typically invalid with mutable collections. Consider</p>

<pre><code>trait HashSet[+T] {
  def add[U &gt;: T](item: U)
}
</code></pre>

<p class="LP">and the following type hierarchy:</p>

<pre><code>trait Mammal
trait Dog extends Mammal
trait Cat extends Mammal
</code></pre>

<p class="LP">If I now have a hash set of dogs</p>

<pre><code>val dogs: HashSet[Dog]
</code></pre>

<p class="LP">treat it as a set of Mammals and add a cat.</p>

<pre><code>val mammals: HashSet[Mammal] = dogs
mammals.add(new Cat{})
</code></pre>

<p class="LP">This is no longer a HashSet of dogs!</p>

<!--
  * when to use abstract type members?
  * show contravariance trick?
-->

<p><a id="Types and Generics-Type aliases" /></p>

<h3>Type aliases</h3>

<p>Use type aliases when they provide convenient naming or clarify
purpose, but do not alias types that are self-explanatory.</p>

<pre><code>() =&gt; Int
</code></pre>

<p class="LP">is clearer than</p>

<pre><code>type IntMaker = () =&gt; Int
IntMaker
</code></pre>

<p class="LP">since it is both short and uses a common type. However</p>

<pre><code>class ConcurrentPool[K, V] {
  type Queue = ConcurrentLinkedQueue[V]
  type Map   = ConcurrentHashMap[K, Queue]
  ...
}
</code></pre>

<p class="LP">is helpful since it communicates purpose and enhances brevity.</p>

<p>Don&rsquo;t use subclassing when an alias will do.</p>

<pre><code>trait SocketFactory extends (SocketAddress =&gt; Socket)
</code></pre>

<p class="LP">a <code>SocketFactory</code> <em>is</em> a function that produces a <code>Socket</code>. Using a type alias</p>

<pre><code>type SocketFactory = SocketAddress =&gt; Socket
</code></pre>

<p class="LP">is better. We may now provide function literals for values of type <code>SocketFactory</code> and also use function composition:</p>

<pre><code>val addrToInet: SocketAddress =&gt; Long
val inetToSocket: Long =&gt; Socket

val factory: SocketFactory = addrToInet andThen inetToSocket
</code></pre>

<p>Type aliases are bound to toplevel names by using package objects:</p>

<pre><code>package com.twitter
package object net {
  type SocketFactory = (SocketAddress) =&gt; Socket
}
</code></pre>

<p>Note that type aliases are not new types &mdash; they are equivalent to
the syntactically substituting the aliased name for its type.</p>

<p><a id="Types and Generics-Implicits" /></p>

<h3>Implicits</h3>

<p>Implicits are a powerful type system feature, but they should be used
sparingly. They have complicated resolution rules and make it
difficult &mdash; by simple lexical examination &mdash; to grasp what is actually
happening. It&rsquo;s definitely OK to use implicits in the following
situations:</p>

<ul>
<li>Extending or adding a Scala-style collection</li>
<li>Adapting or extending an object (&ldquo;pimp my library&rdquo; pattern)</li>
<li>Use to <em>enhance type safety</em> by providing constraint evidence</li>
<li>To provide type evidence (typeclassing)</li>
<li>For <code>Manifest</code>s</li>
</ul>

<p>If you do find yourself using implicits, always ask yourself if there is
a way to achieve the same thing without their help.</p>

<p>Do not use implicits to do automatic conversions between similar
datatypes (for example, converting a list to a stream); these are
better done explicitly because the types have different semantics, and
the reader should beware of these implications.</p>

<p><a id="Collections" /></p>

<h2>Collections</h2>

<p>Scala has a very generic, rich, powerful, and composable collections
library; collections are high level and expose a large set of
operations. Many collection manipulations and transformations can be
expressed succinctly and readably, but careless application of these
features can often lead to the opposite result. Every Scala programmer
should read the <a href="http://www.scala-lang.org/docu/files/collections-api/collections.html">collections design
document</a>;
it provides great insight and motivation for Scala collections
library.</p>

<p>Always use the simplest collection that meets your needs.</p>

<p><a id="Collections-Hierarchy" /></p>

<h3>Hierarchy</h3>

<p>The collections library is large: in addition to an elaborate
hierarchy &mdash; the root of which being <code>Traversable[T]</code> &mdash; there are
<code>immutable</code> and <code>mutable</code> variants for most collections. Whatever
the complexity, the following diagram contains the important
distinctions for both <code>immutable</code> and <code>mutable</code> hierarchies</p>

<p><img src="coll.png" style="margin-left: 3em;" /></p>

<p class="LP"><code>Iterable[T]</code> is any collection that may be iterated over, they provide an <code>iterator</code> method (and thus <code>foreach</code>). <code>Seq[T]</code>s are collections that are <em>ordered</em>, <code>Set[T]</code>s are mathematical sets (unordered collections of unique items), and <code>Map[T]</code>s are associative arrays, also unordered.</p>

<p><a id="Collections-Use" /></p>

<h3>Use</h3>

<p><em>Prefer using immutable collections.</em> They are applicable in most
circumstances, and make programs easier to reason about since they are
referentially transparent and are thus also threadsafe by default.</p>

<p><em>Use the <code>mutable</code> namespace explicitly.</em> Don&rsquo;t import
<code>scala.collection.mutable._</code> and refer to <code>Set</code>, instead</p>

<pre><code>import scala.collection.mutable
val set = mutable.Set()
</code></pre>

<p class="LP">makes it clear that the mutable variant is being used.</p>

<p><em>Use the default constructor for the collection type.</em> Whenever you
need an ordered sequence (and not necessarily linked list semantics),
use the <code>Seq()</code> constructor, and so on:</p>

<pre><code>val seq = Seq(1, 2, 3)
val set = Set(1, 2, 3)
val map = Map(1 -&gt; &quot;one&quot;, 2 -&gt; &quot;two&quot;, 3 -&gt; &quot;three&quot;)
</code></pre>

<p class="LP">This style separates the semantics of the collection from its implementation, letting the collections library use the most appropriate type: you need a <code>Map</code>, not necessarily a Red-Black Tree. Furthermore, these default constructors will often use specialized representations: for example, <code>Map()</code> will use a 3-field object for maps with 3 keys.</p>

<p>The corollary to the above is: in your own methods and constructors, <em>receive the most generic collection
type appropriate</em>. This typically boils down to one of the above:
<code>Iterable</code>, <code>Seq</code>, <code>Set</code>, or <code>Map</code>. If your method needs a sequence,
use <code>Seq[T]</code>, not <code>List[T]</code>. (A word of caution: the <em>default</em>
<code>Traversable</code>, <code>Iterable</code> and <code>Seq</code> types in scope – defined in
<code>scala.package</code> – are the <code>scala.collection</code> versions, as opposed to
<code>Map</code> and <code>Set</code> – defined in <code>Predef.scala</code> – which are the <code>scala.collection.immutable</code>
versions. This means that, for example, the default <code>Seq</code> type can
be both the immutable <em>and</em> mutable implementations. Thus, if your
method relies on a collection parameter being immutable, and you are
using <code>Traversable</code>, <code>Iterable</code> or <code>Seq</code>, you <em>must</em> specifically
require/import the immutable variant, otherwise someone <em>may</em> pass
you the mutable version.)</p>

<!--
something about buffers for construction?
anything about streams?
-->

<p><a id="Collections-Style" /></p>

<h3>Style</h3>

<p>Functional programming encourages pipelining transformations of an
immutable collection to shape it to its desired result. This often
leads to very succinct solutions, but can also be confusing to the
reader &mdash; it is often difficult to discern the author&rsquo;s intent, or keep
track of all the intermediate results that are only implied. For example,
let&rsquo;s say we wanted to aggregate votes for different programming
languages from a sequence of (language, num votes), showing them
in order of most votes to least, we could write:</p>

<pre><code>val votes = Seq((&quot;scala&quot;, 1), (&quot;java&quot;, 4), (&quot;scala&quot;, 10), (&quot;scala&quot;, 1), (&quot;python&quot;, 10))
val orderedVotes = votes
  .groupBy(_._1)
  .map { case (which, counts) =&gt; 
    (which, counts.foldLeft(0)(_ + _._2))
  }.toSeq
  .sortBy(_._2)
  .reverse
</code></pre>

<p class="LP">this is both succinct and correct, but nearly every reader will have a difficult time recovering the original intent of the author. A strategy that often serves to clarify is to <em>name intermediate results and parameters</em>:</p>

<pre><code>val votesByLang = votes groupBy { case (lang, _) =&gt; lang }
val sumByLang = votesByLang map { case (lang, counts) =&gt;
  val countsOnly = counts map { case (_, count) =&gt; count }
  (lang, countsOnly.sum)
}
val orderedVotes = sumByLang.toSeq
  .sortBy { case (_, count) =&gt; count }
  .reverse
</code></pre>

<p class="LP">the code is nearly as succinct, but much more clearly expresses both the transformations take place (by naming intermediate values), and the structure of the data being operated on (by naming parameters). If you worry about namespace pollution with this style, group expressions with <code>{}</code>:</p>

<pre><code>val orderedVotes = {
  val votesByLang = ...
  ...
}
</code></pre>

<p><a id="Collections-Performance" /></p>

<h3>Performance</h3>

<p>High level collections libraries (as with higher level constructs
generally) make reasoning about performance more difficult: the
further you stray from instructing the computer directly &mdash; in other
words, imperative style &mdash; the harder it is to predict the exact
performance implications of a piece of code. Reasoning about
correctness however, is typically easier; readability is also
enhanced. With Scala the picture is further complicated by the Java
runtime; Scala hides boxing/unboxing operations from you, which can
incur severe performance or space penalties.</p>

<p>Before focusing on low level details, make sure you are using a
collection appropriate for your use. Make sure your datastructure
doesn&rsquo;t have unexpected asymptotic complexity. The complexities of the
various Scala collections are described
<a href="http://www.scala-lang.org/docu/files/collections-api/collections_40.html">here</a>.</p>

<p>The first rule of optimizing for performance is to understand <em>why</em>
your application is slow. Do not operate without data;
profile<a class="noteref" id="fnref1" href="#fn1" title="Jump to note 1">[1]</a> your
application before proceeding. Focus first on hot loops and large data
structures. Excessive focus on optimization is typically wasted
effort. Remember Knuth&rsquo;s maxim: &ldquo;Premature optimisation is the root of
all evil.&rdquo;</p>

<p>It is often appropriate to use lower level collections in situations
that require better performance or space efficiency. Use arrays
instead of lists for large sequences (the immutable <code>Vector</code>
collections provides a referentially transparent interface to arrays);
and use buffers instead of direct sequence construction when
performance matters.</p>

<p><a id="Collections-Java Collections" /></p>

<h3>Java Collections</h3>

<p>Use <code>scala.collection.JavaConverters</code> to interoperate with Java collections.
These are a set of implicits that add <code>asJava</code> and <code>asScala</code> conversion
methods. The use of these ensures that such conversions are explicit, aiding
the reader:</p>

<pre><code>import scala.collection.JavaConverters._

val list: java.util.List[Int] = Seq(1,2,3,4).asJava
val buffer: scala.collection.mutable.Buffer[Int] = list.asScala
</code></pre>

<p><a id="Concurrency" /></p>

<h2>Concurrency</h2>

<p>Modern services are highly concurrent &mdash; it is common for servers to
coordinate 10s&ndash;100s of thousands of simultaneous operations &mdash; and
handling the implied complexity is a central theme in authoring robust
systems software.</p>

<p><em>Threads</em> provide a means of expressing concurrency: they give you
independent, heap-sharing execution contexts that are scheduled by the
operating system. However, thread creation is expensive in Java and is
a resource that must be managed, typically with the use of pools. This
creates additional complexity for the programmer, and also a high
degree of coupling: it&rsquo;s difficult to divorce application logic from
their use of the underlying resources.</p>

<p>This complexity is especially apparent when creating services that
have a high degree of fan-out: each incoming request results in a
multitude of requests to yet another tier of systems. In these
systems, thread pools must be managed so that they are balanced
according to the ratios of requests in each tier: mismanagement of one
thread pool bleeds into another. </p>

<p>Robust systems must also consider timeouts and cancellation, both of
which require the introduction of yet more &ldquo;control&rdquo; threads,
complicating the problem further. Note that if threads were cheap
these problems would be diminished: no pooling would be required,
timed out threads could be discarded, and no additional resource
management would be required.</p>

<p>Thus resource management compromises modularity.</p>

<p><a id="Concurrency-Futures" /></p>

<h3>Futures</h3>

<p>Use Futures to manage concurrency. They decouple
concurrent operations from resource management: for example, <a href="http://github.com/twitter/finagle">Finagle</a>
multiplexes concurrent operations onto few threads in an efficient
manner. Scala has lightweight closure literal syntax, so Futures
introduce little syntactic overhead, and they become second nature to
most programmers.</p>

<p>Futures allow the programmer to express concurrent computation in a
declarative style, are composable, and have principled handling of
failure. These qualities have convinced us that they are especially
well suited for use in functional programming languages, where this is
the encouraged style.</p>

<p><em>Prefer transforming futures over creating your own.</em> Future
transformations ensure that failures are propagated, that
cancellations are signalled, and free the programmer from thinking
about the implications of the Java memory model. Even a careful
programmer might write the following to issue an RPC 10 times in
sequence and then print the results:</p>

<pre><code>val p = new Promise[List[Result]]
var results: List[Result] = Nil
def collect() {
  doRpc() onSuccess { result =&gt;
    results = result :: results
    if (results.length &lt; 10)
      collect()
    else
      p.setValue(results)
  } onFailure { t =&gt;
    p.setException(t)
  }
}

collect()
p onSuccess { results =&gt;
  printf(&quot;Got results %s\n&quot;, results.mkString(&quot;, &quot;))
}
</code></pre>

<p>The programmer had to ensure that RPC failures are propagated,
interspersing the code with control flow; worse, the code is wrong!
Without declaring <code>results</code> volatile, we cannot ensure that <code>results</code>
holds the previous value in each iteration. The Java memory model is a
subtle beast, but luckily we can avoid all of these pitfalls by using
the declarative style:</p>

<pre><code>def collect(results: List[Result] = Nil): Future[List[Result]] =
  doRpc() flatMap { result =&gt;
    if (results.length &lt; 9)
      collect(result :: results)
    else
      Future.value(result :: results)
  }

collect() onSuccess { results =&gt;
  printf(&quot;Got results %s\n&quot;, results.mkString(&quot;, &quot;))
}
</code></pre>

<p>We use <code>flatMap</code> to sequence operations and prepend the result onto
the list as we proceed. This is a common functional programming idiom
translated to Futures. This is correct, requires less boilerplate, is
less error prone, and also reads better.</p>

<p><em>Use the Future combinators</em>. <code>Future.select</code>, <code>Future.join</code>, and
<code>Future.collect</code> codify common patterns when operating over
multiple futures that should be combined.</p>

<p><em>Do not throw your own exceptions in methods that return Futures.</em>
Futures represent both successful and failed computations. Therefore, it&rsquo;s
important that errors involved in that computation are properly encapsulated in
the returned Future. Concretely, return <code>Future.exception</code> instead of
throwing that exception:</p>

<pre><code>def divide(x: Int, y: Int): Future[Result] = {
  if (y == 0)
    return Future.exception(new IllegalArgumentException(&quot;Divisor is 0&quot;))

  Future.value(x/y)
}
</code></pre>

<p>Fatal exceptions should not be represented by Futures. These exceptions
include ones that are thrown when resources are exhausted, like
OutOfMemoryError, and also JVM-level errors like NoSuchMethodError. These
conditions are ones under which the JVM must exit.</p>

<p>The predicates <code>scala.util.control.NonFatal</code> &mdash; or Twitter&rsquo;s version
<code>com.twitter.util.NonFatal</code> &mdash; should be used to identify exceptions
which should be returned as a Future.exception.</p>

<p><a id="Concurrency-Collections" /></p>

<h3>Collections</h3>

<p>The subject of concurrent collections is fraught with opinions,
subtleties, dogma and FUD. In most practical situations they are a
nonissue: Always start with the simplest, most boring, and most
standard collection that serves the purpose. Don&rsquo;t reach for a
concurrent collection before you <em>know</em> that a synchronized one won&rsquo;t
do: the JVM has sophisticated machinery to make synchronization cheap,
so their efficacy may surprise you.</p>

<p>If an immutable collection will do, use it &mdash; they are referentially
transparent, so reasoning about them in a concurrent context is
simple. Mutations in immutable collections are typically handled by
updating a reference to the current value (in a <code>var</code> cell or an
<code>AtomicReference</code>). Care must be taken to apply these correctly:
atomics must be retried, and <code>vars</code> must be declared volatile in order
for them to be published to other threads.</p>

<p>Mutable concurrent collections have complicated semantics, and make
use of subtler aspects of the Java memory model, so make sure you
understand the implications &mdash; especially with respect to publishing
updates &mdash; before you use them. Synchronized collections also compose
better: operations like <code>getOrElseUpdate</code> cannot be implemented
correctly by concurrent collections, and creating composite
collections is especially error prone.</p>

<!--

use the stupid collections first, get fancy only when justified.

serialized? synchronized?

blah blah.

Async*?

-->

<p><a id="Control structures" /></p>

<h2>Control structures</h2>

<p>Programs in the functional style tend to require fewer traditional
control structures, and read better when written in the declarative
style. This typically implies breaking your logic up into several
small methods or functions, and gluing them together with <code>match</code>
expressions. Functional programs also tend to be more
expression-oriented: branches of conditionals compute values of
the same type, <code>for (..) yield</code> computes comprehensions, and recursion
is commonplace.</p>

<p><a id="Control structures-Recursion" /></p>

<h3>Recursion</h3>

<p><em>Phrasing your problem in recursive terms often simplifies it,</em> and if
the tail call optimization applies (which can be checked by the <code>@tailrec</code>
annotation), the compiler will even translate your code into a regular loop.</p>

<p>Consider a fairly standard imperative version of heap <span
class="algo">fix-down</span>:</p>

<pre><code>def fixDown(heap: Array[T], m: Int, n: Int): Unit = {
  var k: Int = m
  while (n &gt;= 2*k) {
    var j = 2*k
    if (j &lt; n &amp;&amp; heap(j) &lt; heap(j + 1))
      j += 1
    if (heap(k) &gt;= heap(j))
      return
    else {
      swap(heap, k, j)
      k = j
    }
  }
}
</code></pre>

<p>Every time the while loop is entered, we&rsquo;re working with state dirtied
by the previous iteration. The value of each variable is a function of
which branches were taken, and it returns in the middle of the loop
when the correct position was found (The keen reader will find similar
arguments in Dijkstra&rsquo;s <a href="http://www.u.arizona.edu/~rubinson/copyright_violations/Go_To_Considered_Harmful.html">&ldquo;Go To Statement Considered Harmful&rdquo;</a>).</p>

<p>Consider a (tail) recursive
implementation<a class="noteref" id="fnref2" href="#fn2" title="Jump to note 2">[2]</a>:</p>

<pre><code>@tailrec
final def fixDown(heap: Array[T], i: Int, j: Int) {
  if (j &lt; i*2) return

  val m = if (j == i*2 || heap(2*i) &lt; heap(2*i+1)) 2*i else 2*i + 1
  if (heap(m) &lt; heap(i)) {
    swap(heap, i, m)
    fixDown(heap, m, j)
  }
}
</code></pre>

<p class="LP">here every iteration starts with a well-defined <em>clean slate</em>, and there are no reference cells: invariants abound. It&rsquo;s much easier to reason about, and easier to read as well. There is also no performance penalty: since the method is tail-recursive, the compiler translates this into a standard imperative loop.</p>

<!--
elaborate..
-->

<p><a id="Control structures-Returns" /></p>

<h3>Returns</h3>

<p>This is not to say that imperative structures are not also valuable.
In many cases they are well suited to terminate computation early
instead of having conditional branches for every possible point of
termination: indeed in the above <code>fixDown</code>, a <code>return</code> is used to
terminate early if we&rsquo;re at the end of the heap.</p>

<p>Returns can be used to cut down on branching and establish invariants.
This helps the reader by reducing nesting (how did I get here?) and
making it easier to reason about the correctness of subsequent code
(the array cannot be accessed out of bounds after this point). This is
especially useful in &ldquo;guard&rdquo; clauses:</p>

<pre><code>def compare(a: AnyRef, b: AnyRef): Int = {
  if (a eq b)
    return 0

  val d = System.identityHashCode(a) compare System.identityHashCode(b)
  if (d != 0)
    return d

  // slow path..
}
</code></pre>

<p>Use <code>return</code>s to clarify and enhance readability, but not as you would
in an imperative language; avoid using them to return the results of a
computation. Instead of</p>

<pre><code>def suffix(i: Int) = {
  if      (i == 1) return &quot;st&quot;
  else if (i == 2) return &quot;nd&quot;
  else if (i == 3) return &quot;rd&quot;
  else             return &quot;th&quot;
}
</code></pre>

<p class="LP">prefer:</p>

<pre><code>def suffix(i: Int) =
  if      (i == 1) &quot;st&quot;
  else if (i == 2) &quot;nd&quot;
  else if (i == 3) &quot;rd&quot;
  else             &quot;th&quot;
</code></pre>

<p class="LP">but using a <code>match</code> expression is superior to either:</p>

<pre><code>def suffix(i: Int) = i match {
  case 1 =&gt; &quot;st&quot;
  case 2 =&gt; &quot;nd&quot;
  case 3 =&gt; &quot;rd&quot;
  case _ =&gt; &quot;th&quot;
}
</code></pre>

<p>Note that returns can have hidden costs: when used inside of a closure,</p>

<pre><code>seq foreach { elem =&gt;
  if (elem.isLast)
    return

  // process...
}
</code></pre>

<p class="LP">this is implemented in bytecode as an exception catching/throwing pair which, used in hot code, has performance implications.</p>

<p><a id="Control structures-`for` loops and comprehensions" /></p>

<h3><code>for</code> loops and comprehensions</h3>

<p><code>for</code> provides both succinct and natural expression for looping and
aggregation. It is especially useful when flattening many sequences.
The syntax of <code>for</code> belies the underlying mechanism as it allocates
and dispatches closures. This can lead to both unexpected costs and
semantics; for example</p>

<pre><code>for (item &lt;- container) {
  if (item != 2) return
}
</code></pre>

<p class="LP">may cause a runtime error if the container delays computation, making the <code>return</code> nonlocal!</p>

<p>For these reasons, it is often preferrable to call <code>foreach</code>,
<code>flatMap</code>, <code>map</code>, and <code>filter</code> directly &mdash; but do use <code>for</code>s when they
clarify.</p>

<p><a id="Control structures-`require` and `assert`" /></p>

<h3><code>require</code> and <code>assert</code></h3>

<p><code>require</code> and <code>assert</code> both serve as executable documentation. Both are
useful for situations in which the type system cannot express the required
invariants. <code>assert</code> is used for <em>invariants</em> that the code assumes (either
internal or external), for example</p>

<pre><code>val stream = getClass.getResourceAsStream(&quot;someclassdata&quot;)
assert(stream != null)
</code></pre>

<p>Whereas <code>require</code> is used to express API contracts:</p>

<pre><code>def fib(n: Int) = {
  require(n &gt; 0)
  ...
}
</code></pre>

<p><a id="Functional programming" /></p>

<h2>Functional programming</h2>

<p><em>Value oriented</em> programming confers many advantages, especially when
used in conjunction with functional programming constructs. This style
emphasizes the transformation of values over stateful mutation,
yielding code that is referentially transparent, providing stronger
invariants and thus also easier to reason about. Case classes, pattern
matching, destructuring bindings, type inference, and lightweight
closure- and method-creation syntax are the tools of this trade.</p>

<p><a id="Functional programming-Case classes as algebraic data types" /></p>

<h3>Case classes as algebraic data types</h3>

<p>Case classes encode ADTs: they are useful for modelling a large number
of data structures and provide for succinct code with strong
invariants, especially when used in conjunction with pattern matching.
The pattern matcher implements exhaustivity analysis providing even
stronger static guarantees.</p>

<p>Use the following pattern when encoding ADTs with case classes:</p>

<pre><code>sealed trait Tree[T]
case class Node[T](left: Tree[T], right: Tree[T]) extends Tree[T]
case class Leaf[T](value: T) extends Tree[T]
</code></pre>

<p class="LP">The type <code>Tree[T]</code> has two constructors: <code>Node</code> and <code>Leaf</code>. Declaring the type <code>sealed</code> allows the compiler to do exhaustivity analysis since constructors cannot be added outside the source file.</p>

<p>Together with pattern matching, such modelling results in code that is
both succinct and &ldquo;obviously correct&rdquo;:</p>

<pre><code>def findMin[T &lt;: Ordered[T]](tree: Tree[T]) = tree match {
  case Node(left, right) =&gt; Seq(findMin(left), findMin(right)).min
  case Leaf(value) =&gt; value
}
</code></pre>

<p>While recursive structures like trees constitute classic applications of
ADTs, their domain of usefulness is much larger. Disjoint unions in particular are
readily modelled with ADTs; these occur frequently in state machines.</p>

<p><a id="Functional programming-Options" /></p>

<h3>Options</h3>

<p>The <code>Option</code> type is a container that is either empty (<code>None</code>) or full
(<code>Some(value)</code>). It provides a safe alternative to the use of <code>null</code>,
and should be used instead of null whenever possible. Options are
collections (of at most one item) and they are embellished with
collection operations &mdash; use them!</p>

<p>Write</p>

<pre><code>var username: Option[String] = None
...
username = Some(&quot;foobar&quot;)
</code></pre>

<p class="LP">instead of</p>

<pre><code>var username: String = null
...
username = &quot;foobar&quot;
</code></pre>

<p class="LP">since the former is safer: the <code>Option</code> type statically enforces that <code>username</code> must be checked for emptyness.</p>

<p>Conditional execution on an <code>Option</code> value should be done with
<code>foreach</code>; instead of</p>

<pre><code>if (opt.isDefined)
  operate(opt.get)
</code></pre>

<p class="LP">write</p>

<pre><code>opt foreach { value =&gt;
  operate(value)
}
</code></pre>

<p>The style may seem odd, but provides greater safety (we don&rsquo;t call the
exceptional <code>get</code>) and brevity. If both branches are taken, use
pattern matching:</p>

<pre><code>opt match {
  case Some(value) =&gt; operate(value)
  case None =&gt; defaultAction()
}
</code></pre>

<p class="LP">but if all that's missing is a default value, use <code>getOrElse</code></p>

<pre><code>operate(opt getOrElse defaultValue)
</code></pre>

<p>Do not overuse <code>Option</code>: if there is a sensible
default &mdash; a <a href="http://en.wikipedia.org/wiki/Null_Object_pattern"><em>Null Object</em></a> &mdash; use that instead.</p>

<p><code>Option</code> also comes with a handy constructor for wrapping nullable values:</p>

<pre><code>Option(getClass.getResourceAsStream(&quot;foo&quot;))
</code></pre>

<p class="LP">is an <code>Option[InputStream]</code> that assumes a value of <code>None</code> should <code>getResourceAsStream</code> return <code>null</code>.</p>

<p><a id="Functional programming-Pattern matching" /></p>

<h3>Pattern matching</h3>

<p>Pattern matches (<code>x match { ...</code>) are pervasive in well written Scala
code: they conflate conditional execution, destructuring, and casting
into one construct. Used well they enhance both clarity and safety.</p>

<p>Use pattern matching to implement type switches:</p>

<pre><code>obj match {
  case str: String =&gt; ...
  case addr: SocketAddress =&gt; ...
</code></pre>

<p>Pattern matching works best when also combined with destructuring (for
example if you are matching case classes); instead of</p>

<pre><code>animal match {
  case dog: Dog =&gt; &quot;dog (%s)&quot;.format(dog.breed)
  case _ =&gt; animal.species
  }
</code></pre>

<p class="LP">write</p>

<pre><code>animal match {
  case Dog(breed) =&gt; &quot;dog (%s)&quot;.format(breed)
  case other =&gt; other.species
}
</code></pre>

<p>Write <a href="http://www.scala-lang.org/node/112">custom extractors</a> but only with
a dual constructor (<code>apply</code>), otherwise their use may be out of place.</p>

<p>Don&rsquo;t use pattern matching for conditional execution when defaults
make more sense. The collections libraries usually provide methods
that return <code>Option</code>s; avoid</p>

<pre><code>val x = list match {
  case head :: _ =&gt; head
  case Nil =&gt; default
}
</code></pre>

<p class="LP">because</p>

<pre><code>val x = list.headOption getOrElse default
</code></pre>

<p class="LP">is both shorter and communicates purpose.</p>

<p><a id="Functional programming-Partial functions" /></p>

<h3>Partial functions</h3>

<p>Scala provides syntactical shorthand for defining a <code>PartialFunction</code>:</p>

<pre><code>val pf: PartialFunction[Int, String] = {
  case i if i%2 == 0 =&gt; &quot;even&quot;
}
</code></pre>

<p class="LP">and they may be composed with <code>orElse</code></p>

<pre><code>val tf: (Int =&gt; String) = pf orElse { case _ =&gt; &quot;odd&quot;}

tf(1) == &quot;odd&quot;
tf(2) == &quot;even&quot;
</code></pre>

<p>Partial functions arise in many situations and are effectively
encoded with <code>PartialFunction</code>, for example as arguments to
methods</p>

<pre><code>trait Publisher[T] {
  def subscribe(f: PartialFunction[T, Unit])
}

val publisher: Publisher[Int] = ...
publisher.subscribe {
  case i if isPrime(i) =&gt; println(&quot;found prime&quot;, i)
  case i if i%2 == 0 =&gt; count += 2
  /* ignore the rest */
}
</code></pre>

<p class="LP">or in situations that might otherwise call for returning an <code>Option</code>:</p>

<pre><code>// Attempt to classify the the throwable for logging.
type Classifier = Throwable =&gt; Option[java.util.logging.Level]
</code></pre>

<p class="LP">might be better expressed with a <code>PartialFunction</code></p>

<pre><code>type Classifier = PartialFunction[Throwable, java.util.Logging.Level]
</code></pre>

<p class="LP">as it affords greater composability:</p>

<pre><code>val classifier1: Classifier
val classifier2: Classifier

val classifier: Classifier = classifier1 orElse classifier2 orElse { case _ =&gt; java.util.Logging.Level.FINEST }
</code></pre>

<p><a id="Functional programming-Destructuring bindings" /></p>

<h3>Destructuring bindings</h3>

<p>Destructuring value bindings are related to pattern matching; they use the same
mechanism but are applicable when there is exactly one option (lest you accept
the possibility of an exception). Destructuring binds are particularly useful for
tuples and case classes.</p>

<pre><code>val tuple = ('a', 1)
val (char, digit) = tuple

val tweet = Tweet(&quot;just tweeting&quot;, Time.now)
val Tweet(text, timestamp) = tweet
</code></pre>

<p><a id="Functional programming-Laziness" /></p>

<h3>Laziness</h3>

<p>Fields in scala are computed <em>by need</em> when <code>val</code> is prefixed with
<code>lazy</code>. Because fields and methods are equivalent in Scala (lest the fields
are <code>private[this]</code>)</p>

<pre><code>lazy val field = computation()
</code></pre>

<p class="LP">is (roughly) short-hand for</p>

<pre><code>var _theField = None
def field = if (_theField.isDefined) _theField.get else {
  _theField = Some(computation())
  _theField.get
}
</code></pre>

<p class="LP">i.e., it computes a results and memoizes it. Use lazy fields for this purpose, but avoid using laziness when laziness is required by semantics. In these cases it's better to be explicit since it makes the cost model explicit, and side effects can be controlled more precisely.</p>

<p>Lazy fields are thread safe.</p>

<p><a id="Functional programming-Call by name" /></p>

<h3>Call by name</h3>

<p>Method parameters may be specified by-name, meaning the parameter is
bound not to a value but to a <em>computation</em> that may be repeated. This
feature must be applied with care; a caller expecting by-value
semantics will be surprised. The motivation for this feature is to
construct syntactically natural DSLs &mdash; new control constructs in
particular can be made to look much like native language features.</p>

<p>Only use call-by-name for such control constructs, where it is obvious
to the caller that what is being passed in is a &ldquo;block&rdquo; rather than
the result of an unsuspecting computation. Only use call-by-name arguments
in the last position of the last argument list. When using call-by-name,
ensure that the method is named so that it is obvious to the caller that
its argument is call-by-name.</p>

<p>When you do want a value to be computed multiple times, and especially
when this computation is side effecting, use explicit functions:</p>

<pre><code>class SSLConnector(mkEngine: () =&gt; SSLEngine)
</code></pre>

<p class="LP">The intent remains obvious and the caller is left without surprises.</p>

<p><a id="Functional programming-`flatMap`" /></p>

<h3><code>flatMap</code></h3>

<p><code>flatMap</code> &mdash; the combination of <code>map</code> with <code>flatten</code> &mdash; deserves special
attention, for it has subtle power and great utility. Like its brethren <code>map</code>, it is frequently
available in nontraditional collections such as <code>Future</code> and <code>Option</code>. Its behavior
is revealed by its signature; for some <code>Container[A]</code></p>

<pre><code>flatMap[B](f: A =&gt; Container[B]): Container[B]
</code></pre>

<p class="LP"><code>flatMap</code> invokes the function <code>f</code> for the element(s) of the collection producing a <em>new</em> collection, (all of) which are flattened into its result. For example, to get all permutations of two character strings that aren't the same character repeated twice:</p>

<pre><code>val chars = 'a' to 'z'
val perms = chars flatMap { a =&gt; 
  chars flatMap { b =&gt; 
    if (a != b) Seq(&quot;%c%c&quot;.format(a, b)) 
    else Seq() 
  }
}
</code></pre>

<p class="LP">which is equivalent to the more concise for-comprehension (which is &mdash; roughly &mdash; syntactical sugar for the above):</p>

<pre><code>val perms = for {
  a &lt;- chars
  b &lt;- chars
  if a != b
} yield &quot;%c%c&quot;.format(a, b)
</code></pre>

<p><code>flatMap</code> is frequently useful when dealing with <code>Options</code> &mdash; it will
collapse chains of options down to one,</p>

<pre><code>val host: Option[String] = ...
val port: Option[Int] = ...

val addr: Option[InetSocketAddress] =
  host flatMap { h =&gt;
    port map { p =&gt;
      new InetSocketAddress(h, p)
    }
  }
</code></pre>

<p class="LP">which is also made more succinct with <code>for</code></p>

<pre><code>val addr: Option[InetSocketAddress] = for {
  h &lt;- host
  p &lt;- port
} yield new InetSocketAddress(h, p)
</code></pre>

<p>The use of <code>flatMap</code> in <code>Future</code>s is discussed in the
<a href="#Twitter's%20standard%20libraries-Futures">futures section</a>.</p>

<p><a id="Object oriented programming" /></p>

<h2>Object oriented programming</h2>

<p>Much of Scala&rsquo;s vastness lies in its object system. Scala is a <em>pure</em>
language in the sense that <em>all values</em> are objects; there is no
distinction between primitive types and composite ones.
Scala also features mixins allowing for more orthogonal and piecemeal
construction of modules that can be flexibly put together at compile
time with all the benefits of static type checking.</p>

<p>A motivation behind the mixin system was to obviate the need for
traditional dependency injection. The culmination of this &ldquo;component
style&rdquo; of programming is <a href="http://jonasboner.com/2008/10/06/real-world-scala-dependency-injection-di/">the cake
pattern</a>.</p>

<p><a id="Object oriented programming-Dependency injection" /></p>

<h3>Dependency injection</h3>

<p>In our use, however, we&rsquo;ve found that Scala itself removes so much of
the syntactical overhead of &ldquo;classic&rdquo; (constructor) dependency
injection that we&rsquo;d rather just use that: it is clearer, the
dependencies are still encoded in the (constructor) type, and class
construction is so syntactically trivial that it becomes a breeze.
It&rsquo;s boring and simple and it works. <em>Use dependency injection for
program modularization</em>, and in particular, <em>prefer composition over
inheritance</em> &mdash; for this leads to more modular and testable programs.
When encountering a situation requiring inheritance, ask yourself: how
would you structure the program if the language lacked support for
inheritance? The answer may be compelling.</p>

<p>Dependency injection typically makes use of traits,</p>

<pre><code>trait TweetStream {
  def subscribe(f: Tweet =&gt; Unit)
}
class HosebirdStream extends TweetStream ...
class FileStream extends TweetStream ...

class TweetCounter(stream: TweetStream) {
  stream.subscribe { tweet =&gt; count += 1 }
}
</code></pre>

<p>It is common to inject <em>factories</em> &mdash; objects that produce other
objects. In these cases, favor the use of simple functions over specialized
factory types.</p>

<pre><code>class FilteredTweetCounter(mkStream: Filter =&gt; TweetStream) {
  mkStream(PublicTweets).subscribe { tweet =&gt; publicCount += 1 }
  mkStream(DMs).subscribe { tweet =&gt; dmCount += 1 }
}
</code></pre>

<p><a id="Object oriented programming-Traits" /></p>

<h3>Traits</h3>

<p>Dependency injection does not at all preclude the use of common <em>interfaces</em>, or
the implementation of common code in traits. Quite the contrary &mdash; the use of traits are
highly encouraged for exactly this reason: multiple interfaces
(traits) may be implemented by a concrete class, and common code can
be reused across all such classes.</p>

<p>Keep traits short and orthogonal: don&rsquo;t lump separable functionality
into a trait, think of the smallest related ideas that fit together. For example,
imagine you have an something that can do IO:</p>

<pre><code>trait IOer {
  def write(bytes: Array[Byte])
  def read(n: Int): Array[Byte]
}
</code></pre>

<p class="LP">separate the two behaviors:</p>

<pre><code>trait Reader {
  def read(n: Int): Array[Byte]
}
trait Writer {
  def write(bytes: Array[Byte])
}
</code></pre>

<p class="LP">and mix them together to form what was an <code>IOer</code>: <code>new Reader with Writer</code>&hellip; Interface minimalism leads to greater orthogonality and cleaner modularization.</p>

<p><a id="Object oriented programming-Visibility" /></p>

<h3>Visibility</h3>

<p>Scala has very expressive visibility modifiers. It&rsquo;s important to use
these as they define what constitutes the <em>public API</em>. Public APIs
should be limited so users don&rsquo;t inadvertently rely on implementation
details and limit the author&rsquo;s ability to change them: They are crucial
to good modularity. As a rule, it&rsquo;s much easier to expand public APIs
than to contract them. Poor annotations can also compromise backwards
binary compatibility of your code.</p>

<h4><code>private[this]</code></h4>

<p>A class member marked <code>private</code>, </p>

<pre><code>private val x: Int = ...
</code></pre>

<p class="LP">is visible to all <em>instances</em> of that class (but not their subclasses). In most cases, you want <code>private[this]</code>.</p>

<pre><code>private[this] val x: Int = ...
</code></pre>

<p class="LP">which limits visibility to the particular instance. The Scala compiler is also able to translate <code>private[this]</code> into a simple field access (since access is limited to the statically defined class) which can sometimes aid performance optimizations.</p>

<h4>Singleton class types</h4>

<p>It&rsquo;s common in Scala to create singleton class types, for example</p>

<pre><code>def foo() = new Foo with Bar with Baz {
  ...
}
</code></pre>

<p class="LP">In these situations, visibility can be constrained by declaring the returned type:</p>

<pre><code>def foo(): Foo with Bar = new Foo with Bar with Baz {
  ...
}
</code></pre>

<p class="LP">where callers of <code>foo()</code> will see a restricted view (<code>Foo with Bar</code>) of the returned instance.</p>

<p><a id="Object oriented programming-Structural typing" /></p>

<h3>Structural typing</h3>

<p>Do not use structural types in normal use. They are a convenient and
powerful feature, but unfortunately do not have an efficient
implementation on the JVM. However &mdash; due to an implementation quirk &mdash;
they provide a very nice shorthand for doing reflection.</p>

<pre><code>val obj: AnyRef
obj.asInstanceOf[{def close()}].close()
</code></pre>

<p><a id="Error handling" /></p>

<h2>Error handling</h2>

<p>Scala provides an exception facility, but do not use it for
commonplace errors, when the programmer must handle errors properly
for correctness. Instead, encode such errors explicitly: using
<code>Option</code> or <code>com.twitter.util.Try</code> are good, idiomatic choices, as
they harness the type system to ensure that the user is properly
considering error handling.</p>

<p>For example, when designing a repository, the following API may
be tempting:</p>

<pre><code>trait Repository[Key, Value] {
  def get(key: Key): Value
}
</code></pre>

<p class="LP">but this would require the implementor to throw an exception when the key is absent. A better approach is to use an <code>Option</code>:</p>

<pre><code>trait Repository[Key, Value] {
  def get(key: Key): Option[Value]
}
</code></pre>

<p class="LP">This interface makes it obvious that the repository may not contain every key, and that the programmer must handle missing keys.  Furthermore, <code>Option</code> has a number of combinators to handle these cases. For example, <code>getOrElse</code> is used to supply a default value for missing keys:</p>

<pre><code>val repo: Repository[Int, String]
repo.get(123) getOrElse &quot;defaultString&quot;
</code></pre>

<p><a id="Error handling-Handling exceptions" /></p>

<h3>Handling exceptions</h3>

<p>Because Scala&rsquo;s exception mechanism isn&rsquo;t <em>checked</em> &mdash; the compiler
cannot statically tell whether the programmer has covered the set of
possible exceptions &mdash; it is often tempting to cast a wide net when
handling exceptions.</p>

<p>However, some exceptions are <em>fatal</em> and should never be caught; the
code</p>

<pre><code>try {
  operation()
} catch {
  case _ =&gt; ...
}
</code></pre>

<p class="LP">is almost always wrong, as it would catch fatal errors that need to be propagated. Instead, use the <code>com.twitter.util.NonFatal</code> extractor to handle only nonfatal exceptions.</p>

<pre><code>try {
  operation()
} catch {
  case NonFatal(exc) =&gt; ...
}
</code></pre>

<p><a id="Garbage collection" /></p>

<h2>Garbage collection</h2>

<p>We spend a lot of time tuning garbage collection in production. The
garbage collection concerns are largely similar to those of Java
though idiomatic Scala code tends to generate more (short-lived)
garbage than idiomatic Java code &mdash; a byproduct of the functional
style. Hotspot&rsquo;s generational garbage collection typically makes this
a nonissue as short-lived garbage is effectively free in most circumstances.</p>

<p>Before tackling GC performance issues, watch
<a href="http://www.infoq.com/presentations/JVM-Performance-Tuning-twitter">this</a>
presentation by Attila that illustrates some of our experiences with
GC tuning.</p>

<p>In Scala proper, your only tool to mitigate GC problems is to generate
less garbage; but do not act without data! Unless you are doing
something obviously degenerate, use the various Java profiling tools
&mdash; our own include
<a href="https://github.com/mariusaeriksen/heapster">heapster</a> and
<a href="https://github.com/twitter/jvmgcprof">gcprof</a>.</p>

<p><a id="Java compatibility" /></p>

<h2>Java compatibility</h2>

<p>When we write code in Scala that is used from Java, we ensure
that usage from Java remains idiomatic. Oftentimes this requires
no extra effort &mdash; classes and pure traits are exactly equivalent
to their Java counterpart &mdash; but sometimes separate Java APIs
need to be provided. A good way to get a feel for your library&rsquo;s Java
API is to write a unittest in Java (just for compilation); this also ensures
that the Java-view of your library remains stable over time as the Scala
compiler can be volatile in this regard.</p>

<p>Traits that contain implementation are not directly
usable from Java: extend an abstract class with the trait
instead.</p>

<pre><code>// Not directly usable from Java
trait Animal {
  def eat(other: Animal)
  def eatMany(animals: Seq[Animal) = animals foreach(eat(_))
}

// But this is:
abstract class JavaAnimal extends Animal
</code></pre>

<p><a id="Twitter's standard libraries" /></p>

<h2>Twitter&rsquo;s standard libraries</h2>

<p>The most important standard libraries at Twitter are
<a href="http://github.com/twitter/util">Util</a> and
<a href="https://github.com/twitter/finagle">Finagle</a>. Util should be
considered an extension to the Scala and Java standard libraries,
providing missing functionality or more appropriate implementations. Finagle
is our RPC system; the kernel distributed systems components.</p>

<p><a id="Twitter's standard libraries-Futures" /></p>

<h3>Futures</h3>

<p>Futures have been <a href="#Concurrency-Futures">discussed</a>
briefly in the <a href="#Concurrency">concurrency section</a>. They
are the central mechanism for coordination asynchronous
processes and are pervasive in our codebase and core to Finagle.
Futures allow for the composition of concurrent events, and simplify
reasoning about highly concurrent operations. They also lend themselves
to a highly efficient implementation on the JVM.</p>

<p>Twitter&rsquo;s futures are <em>asynchronous</em>, so blocking operations &mdash;
basically any operation that can suspend the execution of its thread;
network IO and disk IO are examples &mdash; must be handled by a system
that itself provides futures for the results of said operations.
Finagle provides such a system for network IO.</p>

<p>Futures are plain and simple: they hold the <em>promise</em> for the result
of a computation that is not yet complete. They are a simple container
&mdash; a placeholder. A computation could fail of course, and this must
also be encoded: a Future can be in exactly one of 3 states: <em>pending</em>,
<em>failed</em> or <em>completed</em>.</p>

<div class="explainer">
<h3>Aside: <em>Composition</em></h3>
<p>Let's revisit what we mean by composition: combining simpler components
into more complicated ones. The canonical example of this is function
composition: Given functions <em>f</em> and
<em>g</em>, the composite function <em>(g&#8728;f)(x) = g(f(x))</em> &mdash; the result
of applying <em>f</em> to <em>x</em> first, and then applying <em>g</em> to the result
of that &mdash; can be written in Scala:</p>

<pre><code>val f = (i: Int) => i.toString
val g = (s: String) => s+s+s
val h = g compose f  // : Int => String
    
scala> h(123)
res0: java.lang.String = 123123123</code></pre>

<p class="LP">the function <em>h</em> being the composite. It is a <em>new</em> function that combines both <em>f</em> and <em>g</em> in a predefined way.</p>
</div>

<p>Futures are a type of collection &mdash; they are a container of
either 0 or 1 elements &mdash; and you&rsquo;ll find they have standard
collection methods (eg. <code>map</code>, <code>filter</code>, and <code>foreach</code>). Since a Future&rsquo;s
value is deferred, the result of applying any of these methods
is necessarily also deferred; in</p>

<pre><code>val result: Future[Int]
val resultStr: Future[String] = result map { i =&gt; i.toString }
</code></pre>

<p class="LP">the function <code>{ i => i.toString }</code> is not invoked until the integer value becomes available, and the transformed collection <code>resultStr</code> is also in pending state until that time.</p>

<p>Lists can be flattened;</p>

<pre><code>val listOfList: List[List[Int]] = ...
val list: List[Int] = listOfList.flatten
</code></pre>

<p class="LP">and this makes sense for futures, too:</p>

<pre><code>val futureOfFuture: Future[Future[Int]] = ...
val future: Future[Int] = futureOfFuture.flatten
</code></pre>

<p class="LP">since futures are deferred, the implementation of <code>flatten</code> &mdash; it returns immediately &mdash; has to return a future that is the result of waiting for the completion of the outer future (<code><b>Future[</b>Future[Int]<b>]</b></code>) and after that the inner one (<code>Future[<b>Future[Int]</b>]</code>). If the outer future fails, the flattened future must also fail.</p>

<p>Futures (like Lists) also define <code>flatMap</code>; <code>Future[A]</code> defines its signature as</p>

<pre><code>flatMap[B](f: A =&gt; Future[B]): Future[B]
</code></pre>

<p class="LP">which is like the combination of both <code>map</code> and <code>flatten</code>, and we could implement it that way:</p>

<pre><code>def flatMap[B](f: A =&gt; Future[B]): Future[B] = {
  val mapped: Future[Future[B]] = this map f
  val flattened: Future[B] = mapped.flatten
  flattened
}
</code></pre>

<p>This is a powerful combination! With <code>flatMap</code> we can define a Future that
is the result of two futures sequenced, the second future computed based
on the result of the first one. Imagine we needed to do two RPCs in order
to authenticate a user (id), we could define the composite operation in the
following way:</p>

<pre><code>def getUser(id: Int): Future[User]
def authenticate(user: User): Future[Boolean]

def isIdAuthed(id: Int): Future[Boolean] = 
  getUser(id) flatMap { user =&gt; authenticate(user) }
</code></pre>

<p class="LP">an additional benefit to this type of composition is that error handling is built-in: the future returned from <code>isAuthed(..)</code> will fail if either of <code>getUser(..)</code> or <code>authenticate(..)</code> does with no extra error handling code.</p>

<h4>Style</h4>

<p>Future callback methods (<code>respond</code>, <code>onSuccess</code>, <code>onFailure</code>, <code>ensure</code>)
return a new future that is <em>chained</em> to its parent. This future is guaranteed
to be completed only after its parent, enabling patterns like</p>

<pre><code>acquireResource() onSuccess { value =&gt;
  computeSomething(value)
} ensure {
  freeResource()
}
</code></pre>

<p class="LP">where <code>freeResource()</code> is guaranteed to be executed only after <code>computeSomething</code>, allowing for emulation of the native <code>try .. finally</code> pattern.</p>

<p>Use <code>onSuccess</code> instead of <code>foreach</code> &mdash; it is symmetrical to <code>onFailure</code> and
is a better name for the purpose, and also allows for chaining.</p>

<p>Always try to avoid creating <code>Promise</code> instances directly: nearly every task
can be accomplished via the use of predefined combinators. These
combinators ensure errors and cancellations are propagated, and generally
encourage <em>dataflow style</em> programming which usually <a
href="#Concurrency-Futures">obviates the need for synchronization and
volatility declarations</a>.</p>

<p>Code written in tail-recursive style is not subject to stack-space leaks,
allowing for efficient implementation of loops in dataflow-style:</p>

<pre><code>case class Node(parent: Option[Node], ...)
def getNode(id: Int): Future[Node] = ...

def getHierarchy(id: Int, nodes: List[Node] = Nil): Future[Node] =
  getNode(id) flatMap {
    case n@Node(Some(parent), ..) =&gt; getHierarchy(parent, n :: nodes)
    case n =&gt; Future.value((n :: nodes).reverse)
  }
</code></pre>

<p><code>Future</code> defines many useful methods: Use <code>Future.value()</code> and
<code>Future.exception()</code> to create pre-satisfied futures.
<code>Future.collect()</code>, <code>Future.join()</code> and <code>Future.select()</code> provide
combinators that turn many futures into one (ie. the gather part of a
scatter-gather operation).</p>

<h4>Cancellation</h4>

<p>Futures implement a weak form of cancellation. Invoking <code>Future#cancel</code>
does not directly terminate the computation but instead propagates a
level triggered <em>signal</em> that may be queried by whichever process
ultimately satisfies the future. Cancellation flows in the opposite
direction from values: a cancellation signal set by a consumer is
propagated to its producer. The producer uses <code>onCancellation</code> on
<code>Promise</code> to listen to this signal and act accordingly.</p>

<p>This means that the cancellation semantics depend on the producer,
and there is no default implementation. <em>Cancellation is but a hint</em>.</p>

<h4>Locals</h4>

<p>Util&rsquo;s
<a href="https://github.com/twitter/util/blob/master/util-core/src/main/scala/com/twitter/util/Local.scala#L40"><code>Local</code></a>
provides a reference cell that is local to a particular future dispatch tree. Setting the value of a local makes this
value available to any computation deferred by a Future in the same thread. They are analogous to thread locals,
except their scope is not a Java thread but a tree of &ldquo;future threads&rdquo;. In</p>

<pre><code>trait User {
  def name: String
  def incrCost(points: Int)
}
val user = new Local[User]

...

user() = currentUser
rpc() ensure {
  user().incrCost(10)
}
</code></pre>

<p class="LP"><code>user()</code> in the <code>ensure</code> block will refer to the value of the <code>user</code> local at the time the callback was added.</p>

<p>As with thread locals, <code>Local</code>s can be very convenient, but should
almost always be avoided: make sure the problem cannot be sufficiently
solved by passing data around explicitly, even if it is somewhat
burdensome.</p>

<p>Locals are used effectively by core libraries for <em>very</em> common
concerns &mdash; threading through RPC traces, propagating monitors,
creating &ldquo;stack traces&rdquo; for future callbacks &mdash; where any other solution
would unduly burden the user. Locals are inappropriate in almost any
other situation.</p>

<p><a id="Twitter's standard libraries-Offer/Broker" /></p>

<h3>Offer/Broker</h3>

<p>Concurrent systems are greatly complicated by the need to coordinate
access to shared data and resources.
<a href="http://doc.akka.io/api/akka/current/index.html#akka.actor.Actor">Actors</a>
present one strategy of simplification: each actor is a sequential process
that maintains its own state and resources, and data is shared by
messaging with other actors. Sharing data requires communicating between
actors.</p>

<p>Offer/Broker builds on this in three important ways. First,
communication channels (Brokers) are first class &mdash; that is, you send
messages via Brokers, not to an actor directly. Secondly, Offer/Broker
is a synchronous mechanism: to communicate is to synchronize. This
means we can use Brokers as a coordination mechanism: when process <code>a</code>
has sent a message to process <code>b</code>; both <code>a</code> and <code>b</code> agree on the state
of the system. Lastly, communication can be performed <em>selectively</em>: a
process can propose several different communications, and exactly one
of them will obtain.</p>

<p>In order to support selective communication (as well as other
composition) in a general way, we need to decouple the description of
a communication from the act of communicating. This is what an <code>Offer</code>
does &mdash; it is a persistent value that describes a communication; in
order to perform that communication (act on the offer), we synchronize
via its <code>sync()</code> method</p>

<pre><code>trait Offer[T] {
  def sync(): Future[T]
}
</code></pre>

<p class="LP">which returns a <code>Future[T]</code> that yields the exchanged value when the communication obtains.</p>

<p>A <code>Broker</code> coordinates the exchange of values through offers &mdash; it is the channel of communications:</p>

<pre><code>trait Broker[T] {
  def send(msg: T): Offer[Unit]
  val recv: Offer[T]
}
</code></pre>

<p class="LP">so that, when creating two offers</p>

<pre><code>val b: Broker[Int]
val sendOf = b.send(1)
val recvOf = b.recv
</code></pre>

<p class="LP">and <code>sendOf</code> and <code>recvOf</code> are both synchronized</p>

<pre><code>// In process 1:
sendOf.sync()

// In process 2:
recvOf.sync()
</code></pre>

<p class="LP">both offers obtain and the value <code>1</code> is exchanged.</p>

<p>Selective communication is performed by combining several offers with
<code>Offer.choose</code></p>

<pre><code>def choose[T](ofs: Offer[T]*): Offer[T]
</code></pre>

<p class="LP">which yields a new offer that, when synchronized, obtains exactly one of <code>ofs</code> &mdash; the first one to become available. When several are available immediately, one is chosen at random to obtain.</p>

<p>The <code>Offer</code> object has a number of one-off Offers that are used to compose with Offers from a Broker.</p>

<pre><code>Offer.timeout(duration): Offer[Unit]
</code></pre>

<p class="LP">is an offer that activates after the given duration. <code>Offer.never</code> will never obtain, and <code>Offer.const(value)</code> obtains immediately with the given value. These are useful for composition via selective communication. For example to apply a timeout on a send operation:</p>

<pre><code>Offer.choose(
  Offer.timeout(10.seconds),
  broker.send(&quot;my value&quot;)
).sync()
</code></pre>

<p>It may be tempting to compare the use of Offer/Broker to
<a href="http://docs.oracle.com/javase/6/docs/api/java/util/concurrent/SynchronousQueue.html">SynchronousQueue</a>,
but they are different in subtle but important ways. Offers can be composed in ways that such queues simply cannot. For example, consider a set of queues, represented as Brokers:</p>

<pre><code>val q0 = new Broker[Int]
val q1 = new Broker[Int]
val q2 = new Broker[Int]
</code></pre>

<p class="LP">Now let's create a merged queue for reading:</p>

<pre><code>val anyq: Offer[Int] = Offer.choose(q0.recv, q1.recv, q2.recv)
</code></pre>

<p class="LP"><code>anyq</code> is an offer that will read from first available queue. Note that <code>anyq</code> is <em>still synchronous</em> &mdash; we still have the semantics of the underlying queues. Such composition is simply not possible using queues.</p>

<h4>Example: A Simple Connection Pool</h4>

<p>Connection pools are common in network applications, and they&rsquo;re often
tricky to implement &mdash; for example, it&rsquo;s often desirable to have
timeouts on acquisition from the pool since various clients have different latency
requirements. Pools are simple in principle: we maintain a queue of
connections, and we satisfy waiters as they come in. With traditional
synchronization primitives this typically involves keeping two queues:
one of waiters (when there are no connections), and one of connections
(when there are no waiters).</p>

<p>Using Offer/Brokers, we can express this quite naturally:</p>

<pre><code>class Pool(conns: Seq[Conn]) {
  private[this] val waiters = new Broker[Conn]
  private[this] val returnConn = new Broker[Conn]

  val get: Offer[Conn] = waiters.recv
  def put(c: Conn) { returnConn ! c }

  private[this] def loop(connq: Queue[Conn]) {
    Offer.choose(
      if (connq.isEmpty) Offer.never else {
        val (head, rest) = connq.dequeue()
        waiters.send(head) map { _ =&gt; loop(rest) }
      },
      returnConn.recv map { c =&gt; loop(connq.enqueue(c)) }
    ).sync()
  }

  loop(Queue.empty ++ conns)
}
</code></pre>

<p><code>loop</code> will always offer to have a connection returned, but only offer
to send one when the queue is nonempty. Using a persistent queue simplifies
reasoning further. The interface to the pool is also through an Offer, so if a caller
wishes to apply a timeout, they can do so through the use of combinators:</p>

<pre><code>val conn: Future[Option[Conn]] = Offer.choose(
  pool.get map { conn =&gt; Some(conn) },
  Offer.timeout(1.second) map { _ =&gt; None }
).sync()
</code></pre>

<p>No extra bookkeeping was required to implement timeouts; this is due to
the semantics of Offers: if <code>Offer.timeout</code> is selected, there is no
longer an offer to receive from the pool &mdash; the pool and its caller
never simultaneously agreed to receive and send, respectively, on the
<code>waiters</code> broker.</p>

<h4>Example: Sieve of Eratosthenes</h4>

<p>It is often useful &mdash; and sometimes vastly simplifying &mdash; to structure
concurrent programs as a set of sequential processes that communicate
synchronously. Offers and Brokers provide a set of tools to make this simple
and uniform. Indeed, their application transcends what one might think
of as &ldquo;classic&rdquo; concurrency problems &mdash; concurrent programming (with
the aid of Offer/Broker) is a useful <em>structuring</em> tool, just as
subroutines, classes, and modules are &mdash; another important
idea from CSP.</p>

<p>One example of this is the <a href="http://en.wikipedia.org/wiki/Sieve_of_Eratosthenes">Sieve of
Eratosthenes</a>,
which can be structured as a successive application of filters to a
stream of integers. First, we&rsquo;ll need a source of integers:</p>

<pre><code>def integers(from: Int): Offer[Int] = {
  val b = new Broker[Int]
  def gen(n: Int): Unit = b.send(n).sync() ensure gen(n + 1)
  gen(from)
  b.recv
}
</code></pre>

<p class="LP"><code>integers(n)</code> is simply the offer of all consecutive integers starting at <code>n</code>. Then we need a filter:</p>

<pre><code>def filter(in: Offer[Int], prime: Int): Offer[Int] = {
  val b = new Broker[Int]
  def loop() {
    in.sync() onSuccess { i =&gt;
      if (i % prime != 0)
        b.send(i).sync() ensure loop()
      else
        loop()
    }
  }
  loop()

  b.recv
}
</code></pre>

<p class="LP"><code>filter(in, p)</code> returns the offer that removes multiples of the prime <code>p</code> from <code>in</code>. Finally, we define our sieve:</p>

<pre><code>def sieve = {
  val b = new Broker[Int]
  def loop(of: Offer[Int]) {
    for (prime &lt;- of.sync(); _ &lt;- b.send(prime).sync())
      loop(filter(of, prime))
  }
  loop(integers(2))
  b.recv
}
</code></pre>

<p class="LP"><code>loop()</code> works simply: it reads the next (prime) number from <code>of</code>, and then applies a filter to <code>of</code> that excludes this prime. As <code>loop</code> recurses, successive primes are filtered, and we have a Sieve. We can now print out the first 10000 primes:</p>

<pre><code>val primes = sieve
0 until 10000 foreach { _ =&gt;
  println(primes.sync()())
}
</code></pre>

<p>Besides being structured into simple, orthogonal components, this
approach gives you a streaming Sieve: you do not a priori need to
compute the set of primes you are interested in, further enhancing
modularity.</p>

<p><a id="Acknowledgments" /></p>

<h2>Acknowledgments</h2>

<p>The lessons herein are those of Twitter&rsquo;s Scala community &mdash; I hope
I&rsquo;ve been a faithful chronicler.</p>

<p>Blake Matheny, Nick Kallen, Steve Gury, and Raghavendra Prabhu
provided much helpful guidance and many excellent suggestions.</p>

<hr/>
<ol id="notes">

<li id="fn1">
<a href="http://yourkit.com">Yourkit</a> is a good profiler <a href="#fnref1" title="Jump back to reference">[back]</a></li>
<li id="fn2">
From <a href="https://github.com/twitter/finagle/blob/master/finagle-core/src/main/scala/com/twitter/finagle/loadbalancer/Heap.scala#L41">Finagle&rsquo;s heap
balancer</a> <a href="#fnref2" title="Jump back to reference">[back]</a></li>
</ol>

<center class="footer">
Copyright &copy; 2012 Twitter Inc.<br>
Licensed under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>
<!--
<a href="http://creativecommons.org/licenses/by/3.0/"><img style="bottom: 0; right: 0; border: 0;" src="http://i.creativecommons.org/l/by/3.0/80x15.png" /></a>
-->
</center>
