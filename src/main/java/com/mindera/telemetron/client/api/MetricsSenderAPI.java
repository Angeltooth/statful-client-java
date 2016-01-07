package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.sender.MetricsSender;

import java.util.logging.Logger;

/**
 * This is the metrics sender API, which is exposed to allow building metrics and sending them to Telemetron.
 */
public final class MetricsSenderAPI {

    private static final Logger LOGGER = Logger.getLogger(MetricsSenderAPI.class.getName());

    private static final long TIMESTAMP_DIVIDER = 1000L;

    private final MetricsSender metricsSender;

    private String metricName;
    private String value;
    private String namespace;
    private Tags tags;
    private Aggregations aggregations;
    private AggregationFreq aggregationFreq;
    private Integer sampleRate;

    public MetricsSenderAPI(final MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    /**
     * Just a syntax sugar method.
     *
     * @return A reference to this builder
     */
    public MetricsSenderAPI with() {
        return this;
    }

    /**
     * Sets the metric name.
     *
     * @param metricName Metric name as string
     * @return A reference to this builder
     */
    public MetricsSenderAPI metricName(final String metricName) {
        if (isStringSafe(metricName)) {
            this.metricName = metricName;
        }
        return this;
    }

    /**
     * Sets the metric value.
     *
     * @param value The value as string
     * @return A reference to this builder
     */
    public MetricsSenderAPI value(final String value) {
        if (isStringSafe(value)) {
            this.value = value;
        }
        return this;
    }

    /**
     * Sets the Telemetron client configuration to use.
     *
     * @param configuration Client configuration
     * @return A reference to this builder
     */
    public MetricsSenderAPI configuration(final ClientConfiguration configuration) {
        if (configuration != null) {
            this.withNamespace(configuration.getNamespace()).withSampleRate(configuration.getSampleRate());
        }
        return this;
    }

    /**
     * Sets the metric sample rate.
     *
     * @param sampleRate Sample rate as integer
     * @return A reference to this builder
     */
    private MetricsSenderAPI withSampleRate(final Integer sampleRate) {
        if (sampleRate != null) {
            this.sampleRate = sampleRate;
        }
        return this;
    }

    /**
     * Sets a tag to the metric
     *
     * @param type The tag type
     * @param value The tag value
     * @return A reference to this builder
     */
    public MetricsSenderAPI tag(final String type, final String value) {
        if (!Tags.isEmptyOrNull(type, value)) {
            getSafeTags().putTag(type, value);
        }
        return this;
    }

    /**
     * Sets a Tags object to the metric. Which can be a collection.
     *
     * @param tags Tags to use
     * @return A reference to this builder
     */
    public MetricsSenderAPI tags(final Tags tags) {
        if (tags != null) {
            getSafeTags().merge(tags);
        }
        return this;
    }

    /**
     * Sets aggregations to the metric.
     *
     * @param aggregations Array of aggregations to use
     * @return A reference to this builder
     */
    public MetricsSenderAPI aggregations(final Aggregation... aggregations) {
        if (aggregations != null) {
            for (Aggregation aggregation : aggregations) {
                withAggregation(aggregation);
            }
        }
        return this;
    }

    /**
     * Sets an Aggregations object to the metric, which can be a collection.
     *
     * @param aggregations Aggregations to use
     * @return A reference to this builder
     */
    public MetricsSenderAPI aggregations(final Aggregations aggregations) {
        if (aggregations != null) {
            getSafeAggregations().merge(aggregations);
        }
        return this;
    }

    private MetricsSenderAPI withAggregation(final Aggregation aggregation) {
        if (aggregation != null) {
            getSafeAggregations().put(aggregation);
        }
        return this;
    }

    /**
     * Sets the aggregation frequency of the metric.
     *
     * @param aggFreq Aggregation frequency (10, 30, 60, 120, 180, 300)
     * @return A reference to this builder
     */
    public MetricsSenderAPI aggFreq(final AggregationFreq aggFreq) {
        if (aggFreq != null) {
            aggregationFreq = aggFreq;
        }
        return this;
    }

    /**
     * Sets the namespace of the metric.
     *
     * @param namespace Namespace as string
     * @return A reference to this builder
     */
    public MetricsSenderAPI namespace(final String namespace) {
        withNamespace(namespace);
        return this;
    }

    private MetricsSenderAPI withNamespace(final String namespace) {
        if (isStringSafe(namespace)) {
            this.namespace = namespace;
        }
        return this;
    }

    /**
     * Sends the metric to Telemetron.
     */
    public void send() {
        try {
            if (isValid()) {
                long unixTimestamp = getUnixTimestampAsString();
                metricsSender.put(metricName, value, tags, aggregations, aggregationFreq, sampleRate, namespace, unixTimestamp);
            } else {
                LOGGER.warning("Unable to send metric because it's not valid. Please send metric name and value.");
            }
        } catch (Exception e) {
            LOGGER.warning("An exception has occurred while sending the metric to Telemetron: " + e.toString());
        }
    }

    private boolean isValid() {
        return isStringSafe(metricName) && isStringSafe(value);
    }

    private long getUnixTimestampAsString() {
        return System.currentTimeMillis() / TIMESTAMP_DIVIDER;
    }

    private Tags getSafeTags() {
        if (tags == null) {
            tags = new Tags();
        }
        return tags;
    }

    private Aggregations getSafeAggregations() {
        if (aggregations == null) {
            aggregations = new Aggregations();
        }
        return aggregations;
    }

    private boolean isStringSafe(final String string) {
        return string != null && !string.isEmpty();
    }

    String getMetricName() {
        return metricName;
    }

    String getValue() {
        return value;
    }

    String getNamespace() {
        return namespace;
    }

    Tags getTags() {
        return tags;
    }

    Aggregations getAggregations() {
        return aggregations;
    }

    AggregationFreq getAggregationFreq() {
        return aggregationFreq;
    }

    Integer getSampleRate() {
        return sampleRate;
    }
}
