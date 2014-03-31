Duramen
=======

Persistent event bus implementation for Java. Easily integrates with Spring Framework. By default uses file backed embedded H2 database. Guarantees that event will be dispatched.

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
```
3. Implement custom event class:
 ```java
 public class SampleEvent extends Event {
    private String message;
 
    // getters and setters
 }
```
4. Implement producer component:
 ```java
 import eu.codearte.duramen.EventBus;
 
 @Component
 public class EventProducer {
 
    private final EventBus eventBus;
   
    @Autowired
    public EventProducer(EventBus eventBus) {
	    this.eventBus = eventBus;
    }

    /** 
     * This method will be called from your production code
     */
    public void produce() {
	    SampleEvent event = new SampleEvent();
	    event.setMessage("Test message");
	    eventBus.publish(event);
 	  }
 }
```
5. Implement consumer:
 ```java
 import eu.codearte.duramen.handler.EventHandler;

 @Component
 public class EventConsumer implements EventHandler<SampleEvent> {

 	 @Override
	 public void onEvent(SampleEvent event) {
	   System.out.println("Received message: " + event.getMessage());
	 }

 }
```
