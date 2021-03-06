package com.statful.client.core.message;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.Tags;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MessageBuilderTest {

    private final static String NAMESPACE = "TEST_NS";
    private final static String NAME = "response_time";
    private final static Tags TAGS = new Tags();
    private final static Aggregations AGGREGATIONS = new Aggregations();
    private final static long TIMESTAMP = 121232323;
    private static final int SAMPLE_RATE = 100;

    static {
        TAGS.putTag("unit", "s");
        TAGS.putTag("app", "statful");

        AGGREGATIONS.put(Aggregation.AVG);
        AGGREGATIONS.put(Aggregation.COUNT);
    }

    @Test
    public void shouldBuildMessageWithAllAttributes() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertThat(message, anyOf(
                is("TEST_NS.response_time,unit=s,app=statful 3 121232323 avg,count,10 100"),
                is("TEST_NS.response_time,app=statful,unit=s 3 121232323 avg,count,10 100")));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildMessageWithoutName() throws Exception {
        MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildMessageWithoutValue() throws Exception {
        MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();
    }

    @Test
    public void shouldBuildMessageWithoutTags() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertEquals(message, ("TEST_NS.response_time 3 121232323 avg,count,10 100"));
    }


    @Test
    public void shouldBuildMessageWithEmptyTags() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withTags(new Tags())
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertEquals(message, ("TEST_NS.response_time 3 121232323 avg,count,10 100"));
    }

    @Test
    public void shouldBuildMessageWithoutAggregations() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertThat(message, anyOf(
                is("TEST_NS.response_time,unit=s,app=statful 3 121232323 100"),
                is("TEST_NS.response_time,app=statful,unit=s 3 121232323 100")));
    }

    @Test
    public void shouldBuildMessageWithoutAggregationFreq() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(null)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertEquals("TEST_NS.response_time 3 121232323 avg,count,10 100", message);
    }

    @Test
    public void shouldBuildMessageWithEmptyAggregations() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(new Aggregations())
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertThat(message, anyOf(
                is("TEST_NS.response_time,unit=s,app=statful 3 121232323 100"),
                is("TEST_NS.response_time,app=statful,unit=s 3 121232323 100")));
    }

    @Test
    public void shouldBuildMessageWithoutNamespace() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withName(NAME)
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertEquals(message, ("response_time 3 121232323 avg,count,10 100"));
    }

    @Test
    public void shouldBuildMessageWithSampleRate() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withName(NAME)
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .withSampleRate(25)
                .build();

        assertEquals(message, ("response_time 3 121232323 avg,count,10 25"));
    }

    @Test
    public void shouldBuildMessageWithoutSampleRate() {
        String message = MessageBuilder.newBuilder()
                .withName(NAME)
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(AggregationFrequency.FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();

        assertEquals(message, ("response_time 3 121232323 avg,count,10"));
    }

    @Test
    public void shouldEscapeMessage() {
        String message = MessageBuilder.newBuilder()
                .withNamespace("a namespace, with comma")
                .withName("a name, with =equal")
                .withValue("a value, with comma and =equal")
                .withTags(Tags.from(new String[]{"tag, key=", "tag, value="}))
                .withTimestamp(TIMESTAMP)
                .withSampleRate(SAMPLE_RATE)
                .build();

        assertEquals("a\\ namespace\\,\\ with\\ comma.a\\ name\\,\\ with\\ =equal,tag\\,\\ key\\==tag\\,\\ value\\= a value, with comma and =equal 121232323 100", message);
    }
}