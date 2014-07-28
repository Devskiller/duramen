Duramen
=======

Persistent event bus implementation for Java. Easily integrates with Spring Framework. By default uses file backed database. Guarantees that event will be dispatched.

##Usage

1) Add Duramen dependency:
  `eu.codearte.duramen:duramen:0.9.1`

2) Use ```@EnableDuramen``` annotation to import Duramen into your project:
```java
@Configuration
@ComponentScan
@EnableDuramen
public class FooConfiguration {
 
}
```

3) Implement custom event class. Remember that this class must contain (even private) default constructor
```java
public class FooEvent implements Event {
	private String message;
 
	// getters and setters
}
```

4) To produce events you have to implement producer component:
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
	 * You will invoke this method somewhere in your code
	 */
	public void produceEvent() {
		FooEvent event = new FooEvent();
		event.setMessage("Test message");
		eventBus.publish(event);
	}
}
```

5) To receive events you have to implement consumer. Generic type in ```EventHandler``` will decide which events will be processed in particular consumer:
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

All Spring beans implementing ```EventHandler``` interface will be automatically registered as handlers. It's also possible to manually register ```EventHandler``` by invoking ```eventBus.register(qualifiedEventClassName, eventHandler)``` method.

##Testing

Usually in test scope we don't want to persist our events. To achieve such behaviour we can configure custom bean:
```java
import eu.codearte.duramen.datastore.InMemory();
 
@Bean
public Datastore inMemoryDatastore() {
	return new InMemory();
}
```

##Error handling

When ```EventHandler``` processing bean throws an exception, it will be logged with event data serialized to JSON.

You can specify custom ```ExceptionHandler``` by creating bean implementing ```eu.codearte.duramen.handler.ExceptionHandler``` interface.

##Available datastores

In Duramen there are 3 ```Datastore``` objects.

###FileData

Default implementation. Backed by [HugeCollections](https://github.com/OpenHFT/HugeCollections) SharedHashMap. It stores events in binary file (by default duramen.data).
To use this implementation you don't have to do anything, as long as you accept default values (see "Specifying messages limits").
To change defaults you need create own bean:

```java
import eu.codearte.duramen.datastore.FileData;
 
@Bean
public Datastore fileDatastore() {
	return new FileData("/tmp/myfile.data", /*entries*/ 10, /*entrySize*/ 8192);
}
```

###Embedded H2

You can also use embedded H2 database.

```java
import eu.codearte.duramen.datastore.EmbeddedH2;

@Bean
public Datastore embeddedH2Datastore() {
	return EmbeddedH2();
}
 
// or

@Bean
public Datastore embeddedH2Datastore() {
	return EmbeddedH2("jdbc:h2:file:/tmp/duramen.data");
}
```

###Relational DB

If you want to use your own relational database as a ```Datastore``` it is of course possible. 
You just need to create class extending ```eu.codearte.duramen.datastore.RelationalDB```

###In memory

We've already described ```InMemory``` datastore in "Testing" section

##Customizing default configuration

As you can see to use Duramen no configuration is required. However if you want, there are some options to customize.

###Specifying messages limits

By default message size is set to 4096 bytes. You can change this value by defining bean:

```java
@Bean
public Integer maxMessageSize() {
	return 8192;
}
```

Message count limit is set to 1024 events in queue. You can change this value by defining bean:

```java
@Bean
public Integer maxMessageCount() {
	return 2048;
}
```

###Processing options

By default Duramen uses daemon threads, but it can be easily changes by declaring:

```java
@Bean
public Boolean useDaemonThreads() {
	return false;
}
```

Also number of threads processing events (default we use only one thread) can be increased:

```java
@Bean
public Integer maxProcessingThreads() {
	return 2;
}
```

Finally, if you want, there is a possibility to use own ExecutorService for processing events.
```java
@Bean
public ExecutorService duramenExecutorService() {
	return Executors.newCachedThreadPool();
}
```

##Performance

Performance tests executed using JMH on Linux-3.15.4, Intel(R) Core(TM) i7-4600U CPU @ 2.10GHz

| Datastore | Event type* | Events / second |
| --------- |-------------| ---------------:|
| Filedata  | Simple      |         232 000 |
| Filedata  | Complex     |         203 000 |
| InMemory  | Simple      |       1 036 559 |
| InMemory  | Complex     |         291 000 |
| H2        | Simple      |           4 200 |
| H2        | Complex     |             150 |

- Simple event contains one short String field
- Complex event contains 512 chars String field, one BigDecimal field and 6 element ArrayList of Integers
