package test.codearte.duramen;

import io.codearte.duramen.event.RetryableEvent;

import java.util.Date;

/**
 * Created by jkubrynski@gmail.com / 2014-02-10
 */
public class TestRetryableEvent implements RetryableEvent {

	private String test;

	private Date date1;

	private TestRetryableEvent() {
//		just for persistence
	}

	public TestRetryableEvent(String test) {
		this.test = test;
		this.date1 = new Date();
	}

	public String getTest() {
		return test;
	}

	public Date getDate1() {
		return date1;
	}
}