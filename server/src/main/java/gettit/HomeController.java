package gettit;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Handles requests for the application home page.
 *
 * @author Uka
 */
@Controller
@RequestMapping("/*")
public class HomeController {

    long id = 0;

    @Autowired
    private UserService userService;

    @Resource
    @Autowired
    PostController controller;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Root page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("Spring Android Basic Auth");
        return "home";
    }

    /**
     * Used for authentication
     *
     * @return
     */
    @RequestMapping(value = "/getmessage", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Message getMessage() {
        logger.info("Accessing protected resource");
        return new Message(id++, "Congratulations!", "You have accessed a Basic Auth protected resource.");
    }

    /**
     * Creates new user
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    Message createNewUser(@RequestBody User user) {
        User userExists = userService.findUserByUsername(user.getUsername());
        System.out.println("------>" + user.getRole());
        try {
            if (userExists != null) {
                return new Message(id++, "Fail", "userexists");
            } else {
                userService.saveUser(user);
                return new Message(id++, "Success", "Success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(id++, "Fail", "Fail");
        }
    }

    /**
     * Return all posts
     *
     * @return
     */
    @RequestMapping(value = "/getposts", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Post> getPosts() {
        return Lists.newArrayList(controller.findAll());
    }

    /**
     * Get post by id
     *
     * @param postid
     * @return
     */
    @RequestMapping(value = "/getpost/{postid}*", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Post getPost(@PathVariable("postid") int postid) {
        logger.info("Accessing protected resource");
        return controller.findById(postid).get();
    }

    /**
     * Add upvote to post
     *
     * @param postid
     * @return
     */
    @RequestMapping(value = "/upvote/{postid}*", method = RequestMethod.GET)
    public @ResponseBody
    Message upvotePost(@PathVariable("postid") int postid) {

        try {
            Post p = controller.findById(postid).get();
            if (p != null) {
                p.setUpvote(p.getUpvote() + 1);
                controller.save(p);
                return new Message(id++, "Success", "Success");
            } else {
                return new Message(id++, "Fail", "Fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            return new Message(id++, "Fail", "Fail");
        }
    }

    /**
     * Add downvote to post
     *
     * @param postid
     * @return
     */
    @RequestMapping(value = "/downvote/{postid}*", method = RequestMethod.GET)
    public @ResponseBody
    Message downvotePost(@PathVariable("postid") int postid) {
        try {
            Post p = controller.findById(postid).get();
            if (p != null) {
                p.setDownvote(p.getDownvote() + 1);
                controller.save(p);
                return new Message(id++, "Success", "Success");
            } else {
                return new Message(id++, "Fail", "Fail");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            return new Message(id++, "Fail", "Fail");
        }
    }

    /**
     * Adds comment to post
     *
     * @param comment
     * @param postid
     * @return
     */
    @RequestMapping(value = "/addcomment/{postid}*", method = RequestMethod.POST)
    public @ResponseBody
    Message addComment(@RequestBody Comment comment, @PathVariable("postid") int postid) {
        try {
            Post p = controller.findById(postid).get();

            if (p != null) {
                p.addComment(comment);
                controller.save(p);
                return new Message(id++, "Success", "Success");
            }

            return new Message(id++, "Success", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(id++, "Fail", "Fail");
        }
    }

    /**
     * Creates new post
     *
     * @param post
     * @return
     */
    @RequestMapping(value = "/addpost", method = RequestMethod.POST)
    public @ResponseBody
    Message addPost(@RequestBody Post post) {
        try {
            controller.save(post);

            return new Message(id++, "Success", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Message(id++, "Fail", "Fail");
        }
    }
}