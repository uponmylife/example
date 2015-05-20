package geo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Future;

import static geo.Util.sleep;

@Service
@Slf4j
public class KeyStore {
	public String getKey(String query) {
		sleep(200);
		log.info("getKey " + query);
		if (query.length() > 3) throw new RuntimeException();
		return query.substring(0, 1).toLowerCase();
	}

	@Async
	public Future<String> getKeyLater(String query) {
		return new AsyncResult<>(getKey(query));
	}

	@Async
	public ListenableFuture<String> getKeyLater2(String query) {
		return new AsyncResult<>(getKey(query));
	}
}
