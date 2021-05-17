[! [en] (https://img.shields.io/badge/lang-en-red.svg)] (https://github.com/denisbenjamim/perfidix/blob/master/README.md)

# Perfidix - Sem discussões, apenas fatos.

Perfidix é uma biblioteca java leve que permite aos usuários avaliar o código fonte.
Semelhante ao JUnit, as anotações podem ser colocadas antes dos métodos.
Dentro da invocação desses métodos, medições flexíveis são executadas.
Um plugin do eclipse (Perclipse, disponível em https://github.com/sebastiangraf/perclipse ) oferece fácil uso de qualquer método anotado.

[![Build Status](https://secure.travis-ci.org/sebastiangraf/perfidix.png)](http://travis-ci.org/sebastiangraf/perfidix)

##5 etapas para usar o Perfidix

* Pegue o último jar no Github ou Maven

```xml
<dependency>
	<groupId>org.perfidix</groupId>
	<artifactId>perfidix</artifactId>
	<version>4.0.0</version>
</dependency>
```

* Anote seus métodos de bancada `@Bench`.  Nota-se que estes métodos têm de ter o seguinte assinatura: `public (final) void method()`. 
* Crie um novo objecto do tipo `Benchmark`.
* Adicione a classe com os métodos anotados desta forma `benchmarkObj.add(MyBenchmarkTest.class)`.
* Obtenha o `BenchmarkResult` com `benchmarkObj.run()`.
* Exiba o resultado desta forma `new TabularSummaryOutput (). VisitBenchmark (benchmarkResultObj)`. 

OU

* Instale o plug-in Perclipse no site (http://sebastiangraf.github.com/perclipse/) e execute "Executar como" (consulte o Perclipse-Readme para obter mais informações: https://github.com/sebastiangraf/perclipse ).

Para obter mais documentação e exemplo, consulte o pacote `org.perfidix.example`.

##Conteúdo

* README:				 	arquivo leia-me original
* README.pt-BR:				este arquivo leia-me
* LICENSE:	 				arquivo de licença
* src:						pasta de origem para o próprio perfidix
* pom.xml:					pom simples (sim, usamos Maven)

##Licença

Este trabalho foi lançado em domínio público sob a licença BSD de 3 cláusulas

##Outras informações

A documentação até agora está acessível em http://perfidix.org (apontando para http://sebastiangraf.github.com/perfidix/ ).

A estrutura foi apresentada no Jazoon '07 como um trabalho em andamento. O jornal pode ser encontrado [aqui](http://nbn-resolving.de/urn:nbn:de:bsz:352-opus-84446).

Qualquer dúvida, basta entrar em contato com sebastian.graf AT uni-konstanz.de

##Pessoas Envolvidas

Perfidix é mantido por:

* Sebastian Graf (núcleo da Perfidix e líder de Projeto)

Os subprojetos concluídos foram:

* [Nico Haase](mailto:nico@nicohaase.de) (benchmarks parametrizados)
* Nuray Gürler (Mocking e Maven-Website)
* Bastian Lemke (saída do gráfico)
* Alexander Onea (primeiro lançamento do núcleo)
* Tim Petrowski (primeiro lançamento do núcleo)
* Marc Kramis (líder de projeto até 2007)


