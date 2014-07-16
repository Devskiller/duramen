package eu.codearte.duramen.test;

import eu.codearte.duramen.event.Event;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
public class TestEvent implements Event {

	private String test;

	private TestEvent() {
		// just for persistence
	}

	public TestEvent(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}
