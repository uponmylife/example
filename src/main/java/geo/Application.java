package geo;

import akka.actor.ActorSystem;
import akka.actor.Props;
import kamon.Kamon;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Application {
	public static void main(String[] args) {
		Kamon.start();
		log.info("kamon started");
		Kamon.metrics().subscribe("**", "**", ActorSystem.create("system1").actorOf(Props.create(SimpleSubscriber.class), "actor1"));

		SpringApplication.run(Application.class, args);

		Histogram histogram1 = Kamon.metrics().histogram("histogram1");
		Counter counter1 = Kamon.metrics().counter("counter1");
		histogram1.record(9);
		histogram1.record(1);
		histogram1.record(1);
		histogram1.record(2);
		histogram1.record(4);
		counter1.increment();
		counter1.increment();
		counter1.increment(5);
	}
}
