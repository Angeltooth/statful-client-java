package com.mindera.telemetron.client.api;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public class Tags {

    private final Map<String, String> tags = new HashMap<String, String>();

    public static Tags from(final String type, final String name) {
        Tags result = new Tags();
        result.putTag(type, name);
        return result;
    }

    public static Tags from(final Tags tags) {
        Tags result = new Tags();
        result.merge(tags);
        return result;
    }

    public final void putTag(final String type, final String value) {
        tags.put(type, value);
    }

    public final Map<String, String> getTags() {
        return tags;
    }

    public final String getTagValue(final String type) {
        return tags.get(type);
    }

    public final Tags merge(final Tags tags) {
        if (nonNull(tags)) {
            this.tags.putAll(tags.getTags());
        }
        return this;
    }
}
