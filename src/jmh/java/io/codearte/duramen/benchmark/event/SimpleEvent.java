package io.codearte.duramen.benchmark.event;

import eu.codearte.duramen.event.Event;

/**
 * @author Jakub Kubrynski
 */
public class SimpleEvent implements Event {

	String payload;

	public SimpleEvent() {
		payload = "TEST";
	}

}
