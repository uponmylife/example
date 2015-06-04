package geo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "twit")
public interface TwitRepository extends PagingAndSortingRepository<Twit, Long> {
}
