package populator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

@SpringBootTest
@ActiveProfiles("populator")
public class KafkaTopicPopulator {

  @Autowired
  private StreamBridge streamBridge;
  
  @Test
  void testName() throws Exception {
    
     Mono.just(MessageBuilder.withPayload(Map.of("GBPUSD",BigDecimal.valueOf(0.109631).setScale(6, RoundingMode.HALF_UP)))
        .setHeaderIfAbsent(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString()).build()).flatMap(kafkaEvent -> {
          System.out.println("sending");
          streamBridge.send("fxRates-out-0", kafkaEvent);
          return Mono.just(Map.of("dave", "melia"));
        }).repeat(100).collectList().map(m -> m.get(0)).block();
  }
}
