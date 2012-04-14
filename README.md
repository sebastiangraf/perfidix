Perfidix - No discussions, Just facts.
=============

Perfidix is a light-weight java library enabling users to benchmark sourcecode.
Similar to JUnit, annotations can be placed before methods.
Within the invocation of these methods, flexible measurements are performed.

[![Build Status](https://secure.travis-ci.org/disy/perfidix.png)](http://travis-ci.org/disy/perfidix)

5 steps how to use Perfidix
-------

* Get the lastest jar over Github or Maven

```xml
<!-- Only needed when accessing perfidix 3.6.4 or older. Note that the group changed within 3.6.5 from "perfidix" to "org.perfidix" -->
<!--<repository>
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
</repository>-->


<dependency>
	<groupId>org.perfidix</groupId>
	<artifactId>perfidix</artifactId>
	<version>3.6.5</version>
</dependency>
```

* Annotate your methods to bench with "@Bench". Note that these methods have to have the following layout: "public (final) void method()" 
* Create a new "Benchmark" object and add the class with the annotated methods to it.
* Get the BenchmarkResult by typing "benchmarkObj.run()"
* Display the result with the TabularSummaryOutput.visit(benchmarkResultObj). 

For further documentation and as an example, please refer to the org.perfidix.example package.

Content
-------

* README					this readme file
* LICENSE	 				license file
* Perclipse				Source folder for Eclipse-Perfidix Plugin
* src						Source folder for perfidix itself
* pom.xml					Simple pom (yes we do use Maven)

License
-------

This work is released in the public domain under the BSD 3-clause license

Further information
-------

The documentation so far is accessible under http://perfidix.org (pointing to http://disy.github.com/perfidix/).

The framework was presented at the Jazoon '07 as work in progress. The paper can be found over [here](http://nbn-resolving.de/urn:nbn:de:bsz:352-opus-84446).


Any questions, just contact sebastian.graf AT uni-konstanz.de

Involved People
-------

Perfidix is maintained by:

* Sebastian Graf (Perfidix Core & Project Lead)

Concluded subprojects were:

* Nuray Gürler (Mocking and Maven-Website)
* Lukas Lewandowski (Perclipse Plugin)
* Bastian Lemke (Graph Output)
* Alexander Onea (first release of the core)
* Tim Petrowski (first release of the core)
* Marc Kramis (Project Lead until 2007)


