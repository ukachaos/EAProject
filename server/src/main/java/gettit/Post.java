package gettit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Uka
 */
@Entity
public class Post {
    @Id
    @GeneratedValue
    private int id;

    private String author;

    private int upvote;
    private int downvote;

    private String title;
    private String content;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

    private String tag;

    public Post() {

    }

    public Post(int id, String title, String content) {
        this.title = title;
        this.content = content;

        upvote = 0;
        downvote = 0;

        comments = new ArrayList<Comment>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }

    public boolean removeComment(Comment comment) {
        if (comments.contains(comment)) {
            return comments.remove(comment);
        }

        return false;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
