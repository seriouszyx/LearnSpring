package me.seriouszyx;

import me.seriouszyx.domain.User;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author Yixiang Zhao
 * @Date 2018/10/4 18:55
 * @Version 1.0
 */
@Service
public class UserService {

    public void createUser(User user) {
        System.out.println("Saved.....");
    }
}
