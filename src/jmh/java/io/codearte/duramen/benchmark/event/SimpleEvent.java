package io.codearte.duramen.benchmark.event;

import io.codearte.duramen.event.Event;

/**
 * @author Jakub Kubrynski
 */
public class SimpleEvent implements Event {

	String payload;

	public SimpleEvent() {
		payload = "TEST";
	}

}
