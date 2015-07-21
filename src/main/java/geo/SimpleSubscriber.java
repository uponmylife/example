package geo;

import akka.actor.UntypedActor;
import com.google.common.collect.Maps;
import kamon.metric.Entity;
import kamon.metric.EntitySnapshot;
import kamon.metric.SubscriptionsDispatcher;
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

			filterCategory("trace", snapshot).forEach((e, s) -> {
				Histogram.Snapshot h = s.histogram("elapsed-time").get();
				log.info("Histogram [{}]: records={} max={} min={} sum={} percentile={}", e.name(), h.numberOfMeasurements(),
						nano2Milli(h.max()), nano2Milli(h.min()), nano2Milli(h.sum()), nano2Milli(h.percentile(50)));
			});

			filterCategory("trace-segment", snapshot).forEach((e, s) -> {
				Histogram.Snapshot h = s.histogram("elapsed-time").get();
				log.info("Histogram [{}]: records={} max={} min={} sum={} percentile={}", e.name(), h.numberOfMeasurements(),
						nano2Milli(h.max()), nano2Milli(h.min()), nano2Milli(h.sum()), nano2Milli(h.percentile(50)));
			});
		} else unhandled(message);
	}

	private Map<Entity, EntitySnapshot> filterCategory(String categoryName, SubscriptionsDispatcher.TickMetricSnapshot snapshot) {
		return Maps.filterKeys(JavaConversions.mapAsJavaMap(snapshot.metrics()), (e) -> e.category().equals(categoryName));
	}

	private long nano2Milli(long nano) {
		return nano / 1_000_000;
	}
}
