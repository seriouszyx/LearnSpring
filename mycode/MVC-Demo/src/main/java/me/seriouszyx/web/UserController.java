package me.seriouszyx.web;

import me.seriouszyx.UserService;
import me.seriouszyx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author Yixiang Zhao
 * @Date 2018/10/4 18:23
 * @Version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView createUser(User user) {
        userService.createUser(user);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/createSuccess");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping("/register")
    public String register() {
        return "user/register";
    }

}
