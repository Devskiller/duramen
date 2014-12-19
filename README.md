Duramen
=======

Persistent event bus implementation for Java. Easily integrates with Spring Framework. By default uses file backed database. Guarantees that event will be dispatched.

##Usage

1) Add Duramen dependency:
  `io.codearte.duramen:duramen:1.0.0`

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
import io.codearte.duramen.EventBus;
 
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
import io.codearte.duramen.handler.EventHandler;

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
import io.codearte.duramen.datastore.InMemory();
 
@Bean
public Datastore inMemoryDatastore() {
	return new InMemory();
}
```

##Error handling

When ```EventHandler``` processing bean throws an exception, it will be logged with event data serialized to JSON.

You can specify custom ```ExceptionHandler``` by creating bean implementing ```io.codearte.duramen.handler.ExceptionHandler``` interface.

##Available datastores

In Duramen there are 3 ```Datastore``` objects.

###FileData

Default implementation. Backed by [HugeCollections](https://github.com/OpenHFT/HugeCollections) SharedHashMap. It stores events in binary file (by default duramen.data).
To use this implementation you don't have to do anything, as long as you accept default values (see "Specifying messages limits").
To change defaults you need create own bean:

```java
import io.codearte.duramen.datastore.FileData;
 
@Bean
public Datastore fileDatastore() {
	return new FileData("/tmp/myfile.data", /*entries*/ 10, /*entrySize*/ 8192);
}
```

###Embedded H2

You can also use embedded H2 database.

```java
import io.codearte.duramen.datastore.EmbeddedH2;

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
You just need to create class extending ```io.codearte.duramen.datastore.RelationalDB```

###In memory

We've already described ```InMemory``` datastore in "Testing" section

##Customizing default configuration

As you can see to use Duramen no configuration is required. However if you want, there are some options to customize.
To do that just provide ```DuramenConfiguration``` bean:

```java
@Bean
public DuramenConfiguration duramenConfiguration() {
	return DuramenConfiguration.builder().retryDelayInSeconds(30).build()
}
```

###Specifying messages limits

By default message size is set to 4096 bytes. You can change this value by defining bean:

```java
DuramenConfiguration.builder().maxMessageSize(8192).build()
```

Message count limit is set to 1024 events in queue. You can change this value by defining bean:

```java
DuramenConfiguration.builder().maxMessageCount(2048).build()
}
```

###Retrying options

By default Duramen reties all ```RetryableEvent``` events after any exception 3 times with 5 seconds delay. Of course you can customize
those settings by ```DuramenConfiguration```:

```java
DuramenConfiguration.builder().retryDelayInSeconds(5).retryCount(10)
		.retryableExceptions(SocketTimeoutException.class).build()
```

###Processing options

By default Duramen uses daemon threads, but it can be easily changes by declaring:

```java
DuramenConfiguration.builder().useDaemonThreads(false).build()
```

Also number of threads processing events (default we use only one thread) can be increased:

```java
DuramenConfiguration.builder().maxProcessingThreads(2).build()
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
