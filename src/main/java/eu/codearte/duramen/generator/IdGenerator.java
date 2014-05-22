package eu.codearte.duramen.generator;

/**
 * @author Jakub Kubrynski
 */
public interface IdGenerator {

	/**
	 * Generates unique event identifier
	 *
	 * @return generated event id
	 */
	Long getNextId();
}
