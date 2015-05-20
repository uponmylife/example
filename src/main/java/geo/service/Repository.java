package geo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static geo.Util.*;

@Service
@Slf4j
public class Repository {
	private Map<String, String> map;

	@PostConstruct
	public void init() {
		map = new HashMap<>();
		map.put("a", "Apple");
		map.put("b", "Banana");
		map.put("c", "Cherry");
		map.put("g", "Guava");
	}

	public String find(String key) {
		sleep(200);
		log.info("find " + key);
		String value = map.get(key);
		if (value == null) throw new RuntimeException();
		return value;
	}

	@Async
	public Future<String> findLater(String key) {
		return new AsyncResult<>(find(key));
	}
}
