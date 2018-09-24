package showcase;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostControllerImpl implements PostController {

    List<Post> posts;

    public PostControllerImpl() {
        posts = new ArrayList<Post>();

        posts.add(new Post(100, "Post1", "Testing post 1"));
        posts.add(new Post(101, "Post2", "Testing post 2"));

        Post p = new Post(102, "Post with comment", "Test post with comments");


        Comment comment = new Comment(1, "Uka", "this is a comment");

        Comment comment1 = new Comment(2, "User", "this is a user comment");

        System.out.println("---------->" + p.addComment(comment));
        System.out.println("---------->" + p.addComment(comment1));

        posts.add(p);

        System.out.println("---------->" + p.getComments().size());
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

        for (Post p : posts) {
            if (p.getId() == id) {
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
                p.setDownvote(p.getDownvote() + 1);
            }
        }
    }
}
