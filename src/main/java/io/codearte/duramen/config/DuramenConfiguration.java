package io.codearte.duramen.config;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Jakub Kubrynski
 */
public class DuramenConfiguration {

	private final Integer maxMessageSize;
	private final Integer maxMessageCount;
	private final Integer maxProcessingThreads;
	private final Integer retryDelayInSeconds;
	private final Integer retryCount;
	private final Boolean useDaemonThreads;
	private final Set<Class<? extends Throwable>> retryableExceptions;

	private DuramenConfiguration(Integer maxMessageSize, Integer maxMessageCount, Integer maxProcessingThreads,
	                             Integer retryDelayInSeconds, Integer retryCount, Boolean useDaemonThreads,
	                             Set<Class<? extends Throwable>> retryableExceptions) {

		this.maxMessageSize = maxMessageSize;
		this.maxMessageCount = maxMessageCount;
		this.maxProcessingThreads = maxProcessingThreads;
		this.retryDelayInSeconds = retryDelayInSeconds;
		this.retryCount = retryCount;
		this.useDaemonThreads = useDaemonThreads;
		this.retryableExceptions = retryableExceptions;
	}

	public Integer getMaxMessageSize() {
		return maxMessageSize;
	}

	public Integer getMaxMessageCount() {
		return maxMessageCount;
	}

	public Integer getMaxProcessingThreads() {
		return maxProcessingThreads;
	}

	public Integer getRetryDelayInSeconds() {
		return retryDelayInSeconds;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public Boolean getUseDaemonThreads() {
		return useDaemonThreads;
	}

	public Set<Class<? extends Throwable>> getRetryableExceptions() {
		return retryableExceptions;
	}

	public static DuramenConfigurationBuilder builder() {
		return new DuramenConfigurationBuilder();
	}

	public static class DuramenConfigurationBuilder {

		private Integer maxMessageSize = 4096;
		private Integer maxMessageCount = 1024;
		private Integer maxProcessingThreads = 1;
		private Integer retryDelayInSeconds = 5;
		private Integer retryCount = 3;
		private Boolean useDaemonThreads = true;
		@SuppressWarnings("unchecked")
		private Set<Class<? extends Throwable>> retryableExceptions = Sets.<Class<? extends Throwable>>newHashSet(Throwable.class);

		public DuramenConfiguration build() {
			return new DuramenConfiguration(maxMessageSize, maxMessageCount, maxProcessingThreads,
					retryDelayInSeconds, retryCount, useDaemonThreads, retryableExceptions);
		}

		public DuramenConfigurationBuilder maxMessageSize(Integer maxMessageSize) {
			this.maxMessageSize = maxMessageSize;
			return this;
		}

		public DuramenConfigurationBuilder maxMessageCount(Integer maxMessageCount) {
			this.maxMessageCount = maxMessageCount;
			return this;
		}

		public DuramenConfigurationBuilder maxProcessingThreads(Integer maxProcessingThreads) {
			this.maxProcessingThreads = maxProcessingThreads;
			return this;
		}

		public DuramenConfigurationBuilder retryDelayInSeconds(Integer retryDelayInSeconds) {
			this.retryDelayInSeconds = retryDelayInSeconds;
			return this;
		}

		public DuramenConfigurationBuilder retryCount(Integer retryCount) {
			this.retryCount = retryCount;
			return this;
		}

		public DuramenConfigurationBuilder useDaemonThreads(Boolean useDaemonThreads) {
			this.useDaemonThreads = useDaemonThreads;
			return this;
		}

		public DuramenConfigurationBuilder retryableExceptions(Class<? extends Throwable>... retryableExceptions) {
			this.retryableExceptions = Sets.newHashSet(retryableExceptions);
			return this;
		}
	}
}
