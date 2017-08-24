package io.codearte.duramen.benchmark.event;

import io.codearte.duramen.event.Event;

/**
 * @author Jakub Kubrynski
 */
public class SimpleEvent implements Event {

	private String payload;

	public SimpleEvent() {
		payload = "TEST";
	}

	public String getPayload() {
		return payload;
	}
}
