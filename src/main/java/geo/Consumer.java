package geo;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Consumer {
	public void receive(Map<String, Map<String, List>> map) {
		map.keySet().forEach(topic -> {
			Map<String, List> map2 = map.get(topic);
			map2.keySet().forEach(key2 -> System.out.println("topic=" + topic + ", key2=" + key2 + ", list=" + map2.get(key2)));
		});
	}
}
