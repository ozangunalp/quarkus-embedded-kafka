package com.ozangunalp;

import java.util.Optional;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "kafka")
public interface KafkaProps {
    @WithDefault("0")
    int kafkaPort();

    @WithDefault("0")
    int controllerPort();

    @WithDefault("localhost")
    String host();

    @WithDefault("false")
    boolean deleteDirsOnClose();

    Optional<String> logDir();

    Optional<String> advertisedListeners();
}
