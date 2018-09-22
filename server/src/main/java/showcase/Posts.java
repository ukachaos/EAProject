package showcase;

public class Posts {
    Post[] posts;

    public Posts(){

    }

    public Posts(Post[] posts){
        this.posts = posts;
    }

    public void setPosts(Post[] posts){
        this.posts = posts;
    }

    public Post[] getPosts(){
        return posts;
    }
}
