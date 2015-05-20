package geo;

import geo.service.KeyStore;
import geo.service.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

@RestController
@Slf4j
public class Controller {
	@Autowired
	private KeyStore keyStore;
	@Autowired
	private Repository repository;

	@RequestMapping("/base/{query}")
	public String get(@PathVariable String query) {
		String key;
		try {
			key = keyStore.getKey(query);
		} catch (Exception e) {
			return "matching key not found.";
		}

		String result = null;
		try {
			result = repository.find(key);
		} catch (Exception e) {
			return "";
		}

		return result;
	}

	@RequestMapping("/rxjava/{query}")
	public DeferredResult<String> getByRxjava(@PathVariable String query) {
		DeferredResult deferredResult = new DeferredResult();
		Observable.from(keyStore.getKeyLater(query), Schedulers.io()).subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				deferredResult.setResult("matching key not found.");
			}

			@Override
			public void onNext(String key) {
				Observable.from(repository.findLater(key)).subscribe(new Subscriber<String>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						deferredResult.setResult("");
					}

					@Override
					public void onNext(String result) {
						deferredResult.setResult(result);
					}
				});
			}
		});

		return deferredResult;
	}
}
