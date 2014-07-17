package eu.codearte.duramen.benchmark;

import eu.codearte.duramen.event.Event;

/**
* @author Jakub Kubrynski
*/
class SimpleEvent implements Event {
	String payload;

	SimpleEvent() {
	}

	SimpleEvent(String payload) {
		this.payload = payload;
	}
}
