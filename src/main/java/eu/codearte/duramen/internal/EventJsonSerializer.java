package eu.codearte.duramen.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.codearte.duramen.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * @author Jakub Kubrynski
 */
@Component
public class EventJsonSerializer {

	private final static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private ObjectMapper objectMapper;

	public EventJsonSerializer() {
		this.objectMapper = new ObjectMapper();
	}

	public String serializeToJson(Event event) {
		String json;
		try {
			json = objectMapper.writeValueAsString(event);
		} catch (JsonProcessingException jsonException) {
			LOG.error("Error during serializing event to json", jsonException);
			json = "ERROR";
		}
		return json;
	}
}
