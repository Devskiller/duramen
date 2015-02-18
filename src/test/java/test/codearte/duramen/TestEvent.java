package test.codearte.duramen;

import io.codearte.duramen.event.Event;

import java.util.Date;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
public class TestEvent implements Event {

	private String test;

	private Date date1;

	private TestEvent() {
//		just for persistence
	}

	public TestEvent(String test) {
		this.test = test;
		this.date1 = new Date();
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
}