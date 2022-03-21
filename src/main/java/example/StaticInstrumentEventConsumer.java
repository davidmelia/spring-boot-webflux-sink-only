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
      System.out.println(event.getHeaders().get("kafka_offset") + "***"+ event.getPayload() + "***");
      //ack.acknowledge();
      return Mono
          .defer(() -> Mono.just("A thing"))
          .doOnNext(r -> log.info("Before delay: {}", r)) // container-0-C-1
          .delayElement(Duration.ofSeconds(1)) // The delay hands off to another thread, easy WebClient simulation
          .doOnNext(r -> log.info("After delay: {}", r)) // parallel-1
          .then();
    }, 1).then();
  }
  
}
