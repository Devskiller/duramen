Duramen
=======

Java persistent event bus

##Usage:

1. Add duramen dependency:
  eu.codearte.duramen:duramen:0.5.0
2. Use ```@EnableDuramen``` annotation to import Duramen into your project:
```java
@Configuration
@ComponentScan
@EnableDuramen
public class SampleConfiguration {

}

