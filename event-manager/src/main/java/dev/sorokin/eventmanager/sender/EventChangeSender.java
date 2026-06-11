package dev.sorokin.eventmanager.sender;

import dev.sorokin.eventcommon.EventChangedMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class EventChangeSender {

    private static final String TOPIC = "event-changes";

    private final KafkaTemplate<Long, EventChangedMessage> kafkaTemplate;

    public EventChangeSender(KafkaTemplate<Long, EventChangedMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EventChangedMessage change) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send(change);
                }
            });
            return;
        }

        send(change);
    }

    private void send(EventChangedMessage change) {
        kafkaTemplate.send(TOPIC, change.eventId(), change);
    }
}
