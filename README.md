Duramen
=======

Persistent event bus implementation for Java. Easily integrates with Spring Framework. By default uses file backed embedded H2 database. Guarantees that event will be dispatched.

##Usage:

1. Add duramen dependency:
  eu.codearte.duramen:duramen:0.6.2
2. Use ```@EnableDuramen``` annotation to import Duramen into your project:
 ```java
 @Configuration
 @ComponentScan
 @EnableDuramen
 public class FooConfiguration {
 
 }
```
3. Implement custom event class:
 ```java
 public class FooEvent extends Event {
    private String message;
 
    // getters and setters
 }
```
4. To produce events you have to implement producer component:
 ```java
 import eu.codearte.duramen.EventBus;
 
 @Component
 public class FooEventProducer {
 
    private final EventBus eventBus;
   
    @Autowired
    public FooEventProducer(EventBus eventBus) {
	    this.eventBus = eventBus;
    }

    /** 
     * This method will be called from your production code
     */
    public void produce() {
	    FooEvent event = new FooEvent();
	    event.setMessage("Test message");
	    eventBus.publish(event);
 	  }
 }
```
5. To receive events you have to implement consumer. Generic type in ```EventHandler``` will decide which events will be processed in particular consumer:
 ```java
 import eu.codearte.duramen.handler.EventHandler;

 @Component
 public class FooEventConsumer implements EventHandler<FooEvent> {

 	 @Override
	 public void onEvent(FooEvent event) {
	   System.out.println("Received message: " + event.getMessage());
	 }

 }
```
