package showcase;

import java.util.List;

public interface PostController {
    void addPost(Post post);

    void deletePost(Post post);

    void updatePost(Post post);

    List<Post> getPostList();

    Post getPost(int id);

    void addComment(int id, Comment comment);

    void upvotePost(int id);

    void downvotePost(int id);
}
