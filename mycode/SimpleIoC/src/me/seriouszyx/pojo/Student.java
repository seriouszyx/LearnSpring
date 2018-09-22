package me.seriouszyx.pojo;

/**
 * @ClassName Student
 * @Description 学生实体类
 * @Author Yixiang Zhao
 * @Date 2018/9/22 9:19
 * @Version 1.0
 */
public class Student {

    private String name;

    private String gender;

    public void intro() {
        System.out.println("My name is " + name + " and I'm " + gender + " .");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
