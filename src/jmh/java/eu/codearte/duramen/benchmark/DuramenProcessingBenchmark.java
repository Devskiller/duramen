package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.EventBus;
import eu.codearte.duramen.event.Event;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by jkubrynski@gmail.com / 2014-05-20
 */
@State(Scope.Benchmark)
@SuppressWarnings({"unchecked", "unused"})
public class DuramenProcessingBenchmark {

	private EventBus eventBus;
	private AnnotationConfigApplicationContext applicationContext;

	@Param({"SimpleEvent", "ComplexEvent"})
	private String eventTestCase;

	@Param({"Filedata", "H2", "InMemory"})
	private String datastoreTestCase;

	private Constructor<Event> eventConstructor;


	@Benchmark
	public int testSimpleEvent() throws IllegalAccessException, InvocationTargetException, InstantiationException {
		Event test = eventConstructor.newInstance();
		eventBus.publish(test);
		return test.hashCode();
	}

	@Setup(Level.Iteration)
	public void init() throws IOException, ClassNotFoundException, NoSuchMethodException {
		Class<?> eventConfigClass = getClassForSimpleName("config." + eventTestCase + "Config");
		Class<?> datastoreConfigClass = getClassForSimpleName("config." + datastoreTestCase + "Config");
		eventConstructor = (Constructor<Event>) getClassForSimpleName("event." + eventTestCase).getDeclaredConstructor();
		applicationContext = new AnnotationConfigApplicationContext(datastoreConfigClass, eventConfigClass);
		eventBus = applicationContext.getBean(EventBus.class);
	}

	private Class<?> getClassForSimpleName(String simpleName) throws ClassNotFoundException {
		return Class.forName("eu.codearte.duramen.benchmark." + simpleName);
	}

	@TearDown(Level.Iteration)
	public void cleanup() throws IOException, InterruptedException {
		// wait for event to process before closing context
		Thread.sleep(1000);

		applicationContext.close();

		// cleanup files
		for (File f : new File("/tmp").listFiles()) {
			if (f.getName().startsWith("perf_duramen")) {
				f.delete();
			}
		}
	}

}
