package me.seriouszyx.domain;

/**
 * @ClassName User
 * @Description User
 * @Author Yixiang Zhao
 * @Date 2018/10/4 18:56
 * @Version 1.0
 */
public class User {

    private String userName;

    private String password;

    private String realName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
