package io.codearte.duramen.benchmark.event;

import com.google.common.collect.Lists;
import io.codearte.duramen.event.Event;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author Jakub Kubrynski
 */
public class ComplexEvent implements Event {

	private String stringPayload;
	private BigDecimal bigDecimalPayload;
	private ArrayList<Integer> integers;

	public ComplexEvent() {
		stringPayload = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		bigDecimalPayload = new BigDecimal(12345.6789);
		integers = Lists.newArrayList(123, 456, 789, 1234, 2345, 3456);
	}

	public String getStringPayload() {
		return stringPayload;
	}

	public BigDecimal getBigDecimalPayload() {
		return bigDecimalPayload;
	}

	public ArrayList<Integer> getIntegers() {
		return integers;
	}
}
