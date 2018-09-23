package showcase;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostControllerImpl implements PostController {

    List<Post> posts;

    public PostControllerImpl(){
        posts = new ArrayList<Post>();

        posts.add(new Post(100, "Post1", "Testing post 1"));
        posts.add(new Post(101, "Post2", "Testing post 2"));
    }

    @Override
    public void addPost(Post post) {
        posts.add(post);
    }

    @Override
    public void deletePost(Post post) {
        if (posts.contains(post)) {
            posts.remove(post);
        }
    }

    @Override
    public void updatePost(Post post) {
        if (posts.contains(post)) {
            posts.set(posts.indexOf(post), post);
        }
    }

    @Override
    public List<Post> getPostList() {
        return posts;
    }

    @Override
    public Post getPost(int id) {

        for(Post p : posts){
            if(p.getId() == id){
                return p;
            }
        }

        return null;
    }

    @Override
    public void addComment(int id, Comment comment) {
        for (Post p : posts) {
            if (p.getId() == id) {
                p.addComment(comment);
            }
        }
    }

    @Override
    public void upvotePost(int id) {
        for (Post p : posts) {
            if (p.getId() == id) {
                p.setUpvote(p.getUpvote() + 1);
            }
        }
    }

    @Override
    public void downvotePost(int id) {
        for (Post p : posts) {
            if (p.getId() == id) {
                if (p.getUpvote() > 0)
                    p.setUpvote(p.getUpvote() - 1);
            }
        }
    }
}
