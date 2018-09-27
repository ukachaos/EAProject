package gettit;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Uka
 */

public interface PostController extends CrudRepository<Post, Integer> {

}
