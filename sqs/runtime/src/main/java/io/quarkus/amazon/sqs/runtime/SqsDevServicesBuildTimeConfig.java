package io.quarkus.amazon.sqs.runtime;

import io.quarkus.amazon.common.runtime.DevServicesBuildTimeConfig;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ConfigGroup
public class SqsDevServicesBuildTimeConfig extends DevServicesBuildTimeConfig {

  /**
   * The queues to create on startup.
   */
  @ConfigItem
  public Optional<List<QueueConfig>> queues = Optional.empty();

  @ConfigGroup
  public static class QueueConfig {

    /**
     * Name of the queue (required)
     */
    @ConfigItem
    public String name;

    /**
     * Attributes of the queue (optional)
     */
    @ConfigItem
    public Map<String, String> attributes;

    public CreateQueueRequest createQueueRequest() {
      CreateQueueRequest.Builder builder = CreateQueueRequest.builder();
      builder.queueName(name);
      builder.attributesWithStrings(attributes);
      return builder.build();
    }
  }

}
