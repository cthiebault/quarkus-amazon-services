package io.quarkus.amazon.sqs.deployment;

import io.quarkus.amazon.common.runtime.RuntimeConfigurationError;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static software.amazon.awssdk.services.sqs.model.QueueAttributeName.CONTENT_BASED_DEDUPLICATION;
import static software.amazon.awssdk.services.sqs.model.QueueAttributeName.FIFO_QUEUE;

class SqsDevServicesWithQueueAttributesTest {

    @Inject
    Instance<SqsClient> client;

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
        .setExpectedException(RuntimeConfigurationError.class)
        .withApplicationRoot((jar) -> jar
            .addAsResource("devservices-queue-attributes.properties", "application.properties"));

    @Test
    void test() {
        assertNotNull(client.get());
        List<String> queueUrls = client.get().listQueues().queueUrls();
        assertEquals(2, queueUrls.size());
        String queue1Url = queueUrls.get(0);
        assertTrue(queue1Url.endsWith("/queue1"));
        assertTrue(queueUrls.get(1).endsWith("/queue2"));

        GetQueueAttributesResponse queue1Attributes = client.get().getQueueAttributes(b -> b.queueUrl(queue1Url));
        assertEquals(2, queue1Attributes.attributesAsStrings().size());
        assertEquals("true", queue1Attributes.attributesAsStrings().get(FIFO_QUEUE.toString()));
        assertEquals("true", queue1Attributes.attributesAsStrings().get(CONTENT_BASED_DEDUPLICATION.toString()));
    }
}
