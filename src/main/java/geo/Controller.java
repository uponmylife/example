package geo;

import geo.service.KeyStore;
import geo.service.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;

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

		log.info("servlet end.");
		return deferredResult;
	}

	@RequestMapping("/callback/{query}")
	public DeferredResult<String> getByCallback(@PathVariable String query) {
		DeferredResult deferredResult = new DeferredResult();
		keyStore.getKeyLater2(query).addCallback(new ListenableFutureCallback<String>() {
			@Override
			public void onFailure(Throwable ex) {
				deferredResult.setResult("matching key not found.");
			}

			@Override
			public void onSuccess(String key) {
				repository.findLater2(key).addCallback(new ListenableFutureCallback<String>() {
					@Override
					public void onFailure(Throwable ex) {
						deferredResult.setResult("");
					}

					@Override
					public void onSuccess(String result) {
						deferredResult.setResult(result);
					}
				});
			}
		});

		log.info("servlet end.");
		return deferredResult;
	}

	@RequestMapping("/messageDriven/{query}")
	public DeferredResult<String> getByMessage(@PathVariable String query) {
		DeferredResult deferredResult = new DeferredResult();
		AsyncSubject<String> querySubject = AsyncSubject.create();
		AsyncSubject<String> keySubject = AsyncSubject.create();
		AsyncSubject<String> resultSubject = AsyncSubject.create();

		querySubject.subscribe(query1 -> {
			keyStore.getKeyLater2(query1).addCallback(new ListenableFutureCallback<String>() {
				@Override
				public void onSuccess(String key) {
					keySubject.onNext(key);
					keySubject.onCompleted();
				}

				@Override
				public void onFailure(Throwable ex) {
					deferredResult.setResult("matching key not found.");
				}
			});
		});

		keySubject.subscribe(key -> {
			repository.findLater2(key).addCallback(new ListenableFutureCallback<String>() {
				@Override
				public void onSuccess(String result) {
					resultSubject.onNext(result);
					resultSubject.onCompleted();
				}

				@Override
				public void onFailure(Throwable ex) {
					deferredResult.setResult("");
				}
			});
		});

		resultSubject.subscribe(result -> {
			deferredResult.setResult(result);
		});

		querySubject.onNext(query);
		querySubject.onCompleted();

		log.info("servlet end.");
		return deferredResult;
	}
}
