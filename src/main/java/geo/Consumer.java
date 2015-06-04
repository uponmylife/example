package geo;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Consumer {
	public void receive(Map<String, Map<String, List>> topicMap) {
		topicMap.keySet().forEach(topic -> {
			System.out.println("topic=" + topic);
			Map<String, List> partitionMap = topicMap.get(topic);
			partitionMap.keySet().forEach(partition -> {
				List list = partitionMap.get(partition);
				System.out.println("partition=" + partition + ", list=" + list);
			});
		});
	}
}
