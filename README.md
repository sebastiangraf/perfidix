Perfidix - No discussions, Just facts.
=============

Perfidix is a light-weight java library enabling users to benchmark sourcecode.
Similar to JUnit, annotations can be placed before methods.
Within the invocation of these methods, flexible measurements are performed.

5 steps how to use Perfidix
-------

1. Download the latest .jar from github and Insert the jar into your classpath OR insert the disy-repo in your pom.xml

```xml
<repository>
	<id>disyInternal</id>
	<name>Internal Repository for the Distributed System Group</name>
	<url>http://mavenrepo.disy.inf.uni-konstanz.de/repository/disyInternal</url>
	<releases>
		<enabled>true</enabled>
	</releases>
	<snapshots>
		<enabled>false</enabled>
	</snapshots>
</repository>
<repository>
	<id>disyInternalSnapshot</id>
	<name>Internal Snapshot Repository for the Distributed System Group</name>
	<url>http://mavenrepo.disy.inf.uni-konstanz.de/repository/disyInternalSnapshot</url>
	<releases>
		<enabled>true</enabled>
	</releases>
	<snapshots>
		<enabled>true</enabled>
	</snapshots>
</repository>
```
2. Annotate your methods to bench with "@Bench". Note that these methods have to have the following layout: "public (final) void method()" 
3. Create a new "Benchmark" object and add the class with the annotated methods to it.
4. Get the BenchmarkResult by typing "benchmarkObj.run()"
5. Display the result with the TabularSummaryOutput.visit(benchmarkResultObj). 

For further documentation and as an example, please refer to the org.perfidix.example package.

Content
-------

README					this readme file
LICENSE	 				license file
Perclipse				Source folder for Eclipse-Perfidix Plugin
perfidix				Source folder for perfidix itself
scripts					bash scripts for syncing against disy-internal repo.
pom.xml					Simple pom (yes we do use Maven)

License
-------

This work is released in the public domain under the BSD 3-clause license

Further information
-------

The documentation so far is accessible under http://perfidix.org (pointing to http://disy.github.com/perfidix/).

The framework was presented at the Jazoon '07 as work in progress:
http://kops.ub.uni-konstanz.de/handle/urn:nbn:de:bsz:352-opus-84446

Any questions, just contact sebastian.graf AT uni-konstanz.de

Involved People
-------

Perfidix is maintained by:

* Sebastian Graf (Perfidix Core & Project Lead)

Concluded subprojects were:

* Nuray Gürler (Mocking)
* Lukas Lewandowski (Perclipse Plugin)
* Bastian Lemke (Graph Output)
* Alexander Onea (first release of the core)
* Tim Petrowski (first release of the core)


