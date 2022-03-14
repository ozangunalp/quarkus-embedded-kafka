package com.ozangunalp;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.smallrye.reactive.messaging.kafka.companion.test.EmbeddedKafkaBroker;

@ApplicationScoped
public class Startup {

    @Inject
    KafkaProps props;

    private EmbeddedKafkaBroker broker;

    void startup(@Observes StartupEvent event) {
        broker = new EmbeddedKafkaBroker()
                .withDeleteLogDirsOnClose(props.deleteDirsOnClose())
                .withKafkaPort(props.kafkaPort())
                .withControllerPort(props.controllerPort());
        props.logDir().ifPresent(Unchecked.consumer(dir -> {
            Files.createDirectories(Paths.get(dir));
            broker.withAdditionalProperties(p -> p.put("log.dir", dir));
        }));
        props.advertisedListeners().ifPresent(listeners -> broker.withAdvertisedListeners(listeners));
        broker.start();
    }

    void shutdown(@Observes ShutdownEvent event) {
        broker.close();
    }
}