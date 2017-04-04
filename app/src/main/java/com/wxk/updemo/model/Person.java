package com.wxk.updemo.model;

/**
 * Created by Administrator on 2017/4/3
 */

public class Person {

    private String name;
    private int age;
    private boolean flag;
    private PhoneModel model;

    public Person() {
    }

    public Person(String name, int age, boolean flag) {
        this.name = name;
        this.age = age;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", flag=" + flag +
                '}';
    }
}
