package geo;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.common.collect.Maps;
import kamon.Kamon;
import kamon.metric.Entity;
import kamon.metric.EntitySnapshot;
import kamon.metric.SubscriptionsDispatcher;
import kamon.metric.instrument.Counter;
import kamon.metric.instrument.Histogram;
import lombok.extern.slf4j.Slf4j;
import scala.collection.JavaConversions;

import java.util.Map;

@Slf4j
public class SimpleSubscriber extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof SubscriptionsDispatcher.TickMetricSnapshot) {
			SubscriptionsDispatcher.TickMetricSnapshot snapshot = (SubscriptionsDispatcher.TickMetricSnapshot) message;
			log.info("TickMetricSnapshot {} ~ {}", snapshot.from(), snapshot.to());

			filterCategory("counter", snapshot).forEach((e, s) -> {
				log.info("Counter [{}]: {}", e.name(), s.counter("counter").get().count());
			});

			filterCategory("histogram", snapshot).forEach((e, s) -> {
				Histogram.Snapshot h = s.histogram("histogram").get();
				log.info("Histogram [{}]: records={} max={} min={} sum={} percentile={}", e.name(), h.numberOfMeasurements(),
						h.max(), h.min(), h.sum(), h.percentile(50));
			});
		} else unhandled(message);
	}

	private Map<Entity, EntitySnapshot> filterCategory(String categoryName, SubscriptionsDispatcher.TickMetricSnapshot snapshot) {
		return Maps.filterKeys(JavaConversions.mapAsJavaMap(snapshot.metrics()), (e) -> e.category().equals(categoryName));
	}

	public static void main(String[] args) throws Exception {
		Kamon.start();
		log.info("kamon started");

		Histogram someHistogram = Kamon.metrics().histogram("histogram1");
		Counter someCounter = Kamon.metrics().counter("counter1");
		someHistogram.record(9);
		someHistogram.record(1);
		someHistogram.record(1);
		someHistogram.record(2);
		someHistogram.record(4);
		someCounter.increment();
		someCounter.increment();
		someCounter.increment(5);

		Kamon.metrics().subscribe("**", "**", ActorSystem.create("system1").actorOf(Props.create(SimpleSubscriber.class), "actor1"));

		Thread.sleep(20_000);
		Kamon.shutdown();
		System.exit(0);
	}
}
