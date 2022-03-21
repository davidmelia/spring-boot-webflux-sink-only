package example;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class StaticInstrumentEventConsumer {

  @Bean
  public Function<Flux<Message<Map<String, BigDecimal>>>, Mono<Void>> fxRates() {
    return events -> events.flatMapSequential(event -> {
      System.out.println("kafka_offset="+event.getHeaders().get("kafka_offset"));
      //ack.acknowledge();
      return Mono
          .defer(() -> Mono.just("A thing"))
          .delayElement(Duration.ofMillis(10)) // The delay hands off to another thread, easy WebClient simulation
          .then();
    }, 1).then();
  }
  
}
