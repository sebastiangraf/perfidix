<!--
~~
~~ Copyright (c) 2011, University of Konstanz, Distributed Systems Group
~~ All rights reserved.
~~
~~ Redistribution and use in source and binary forms, with or without
~~ modification, are permitted provided that the following conditions are met:
~~     * Redistributions of source code must retain the above copyright
~~       notice, this list of conditions and the following disclaimer.
~~     * Redistributions in binary form must reproduce the above copyright
~~       notice, this list of conditions and the following disclaimer in the
~~       documentation and/or other materials provided with the distribution.
~~     * Neither the name of the University of Konstanz nor the
~~       names of its contributors may be used to endorse or promote products
~~       derived from this software without specific prior written permission.
~~
~~ THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
~~ ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
~~ WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
~~ DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
~~ DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
~~ (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
~~ LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
~~ ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
~~ (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
~~ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
~~
-->

<a href="https://github.com/disy/perfidix"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png" alt="Fork me on GitHub"/></a>

Screenshots
==========
Perfidix comes with a programming interface for invoking the source and with an Eclipse plugin accessible over the update site http://disy.github.com/perclipse/. 

Please note that you need to have the perfidix-jar in your classpath to make Perclipse work. This can either happen over

* maven, by inserting perfidix to your dependency:

```xml
<dependency>
	<groupId>org.perfidix</groupId>
	<artifactId>perfidix</artifactId>
	<version>3.6.6</version>
</dependency>
```

* eclipse, by adding perfidix as library over a QuickFix as shown in the screenshot below

![Insertion as Library](images/library.jpg)

Invocation
----------

The following screenshots represents examples of invoking perfidix over perclipse :

![Invocation example](images/eclipse01.jpg)

Annotating methods
----------

The following image shows the annotation of an existing class:

![Annotation of class](images/eclipse02.jpg)

Results
----------

The output is piped to the commando as an example.

![Result of a bench](images/result.jpg)