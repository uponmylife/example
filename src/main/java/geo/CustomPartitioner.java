package geo;

import kafka.producer.Partitioner;
import org.springframework.stereotype.Component;

@Component
public class CustomPartitioner implements Partitioner {
	@Override
	public int partition(Object key, int numPartitions) {
		if (!(key instanceof String)) return 0;
		String str = (String) key;
		if (str.length() < 1) return 0;
		return str.getBytes()[0] % numPartitions;
	}
}
