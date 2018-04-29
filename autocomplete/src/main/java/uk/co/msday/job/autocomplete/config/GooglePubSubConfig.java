package uk.co.msday.job.autocomplete.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubOperations;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.co.msday.job.autocomplete.model.GoogleStoragePayload;

@Configuration
@RequiredArgsConstructor
public class GooglePubSubConfig {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@NonNull
	private FileStorageConfig config;

	@NonNull
	private AutocompleteMap map;

	@Value("${autocomplete.subscription-name}")
	private String subscriptionName;

	@Bean
	public PubSubInboundChannelAdapter messageChannelAdapter(
			@Qualifier("pubsubInputChannel") MessageChannel inputChannel, PubSubOperations pubSubTemplate) {
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName);
		adapter.setOutputChannel(inputChannel);
		adapter.setAckMode(AckMode.MANUAL);
		return adapter;
	}

	@Bean
	public MessageChannel pubsubInputChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "pubsubInputChannel")
	public MessageHandler messageReceiver() {
		return message -> {
			AckReplyConsumer consumer = (AckReplyConsumer) message.getHeaders().get(GcpPubSubHeaders.ACKNOWLEDGEMENT);
			consumer.ack();
			GoogleStoragePayload event = parseMessage(message.getPayload().toString());
			// Only care about new object events
			if ((event != null) && (message.getHeaders().get("eventType").equals("OBJECT_FINALIZE"))) {
				if (event.getName().equals(config.getFileName())) {
					log.info("Autocomplete list updated - rebuilding database");
					try {
						map.update();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	private GoogleStoragePayload parseMessage(String message) {
		ObjectMapper mapper = new ObjectMapper();
		GoogleStoragePayload payload;
		try {
			payload = mapper.readValue(message, GoogleStoragePayload.class);
		} catch (Exception e) {
			throw new RuntimeException("Cannot read file from storage");
		}
		return payload;
	}

}
