/*
 * Copyright 2010-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package showcase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Handles requests for the application home page.
 *
 * @author Roy Clarkson
 */
@Controller
@RequestMapping("/*")
public class HomeController {

    long id = 0;

    @Resource
    PostController controller;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("Spring Android Basic Auth");
        return "home";
    }

    @RequestMapping(value = "/getmessage", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Message getMessage() {
        logger.info("Accessing protected resource");
        return new Message(id++, "Congratulations!", "You have accessed a Basic Auth protected resource.");
    }

    @RequestMapping(value = "/getposts", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Post> getPosts() {
        return controller.getPostList();
    }

    @RequestMapping(value = "/getpost/{postid}*", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Post getPost(@PathVariable("postid") int postid) {
        logger.info("Accessing protected resource");
        return controller.getPost(postid);
    }

    @RequestMapping(value = "/upvote/{postid}*", method = RequestMethod.GET)
    public @ResponseBody
    Message upvotePost(@PathVariable("postid") int postid) {

        try {
            controller.upvotePost(postid);

            return new Message(id++, "Success", "Success");
        } catch (Exception ex) {
            ex.printStackTrace();

            return new Message(id++, "Fail", "Fail");
        }
    }

    @RequestMapping(value = "/downvote/{postid}*", method = RequestMethod.GET)
    public @ResponseBody
    Message downvotePost(@PathVariable("postid") int postid) {
        try {
            controller.downvotePost(postid);

            return new Message(id++, "Success", "Success");
        } catch (Exception ex) {
            ex.printStackTrace();

            return new Message(id++, "Fail", "Fail");
        }
    }

    @RequestMapping(value = "/addcomment/{postid}*", method = RequestMethod.POST)
    //public @ResponseBody Message addComment(@RequestBody Comment comment/*, @PathVariable("postid") int postid*/) {
    public @ResponseBody Message addComment(@RequestBody Comment comment/*, @PathVariable("postid") int postid*/) {
        System.out.println("-------->" + "STARTED");
        controller.addComment(102, comment);
        return new Message(id++, "Success", "Success");
    }
}