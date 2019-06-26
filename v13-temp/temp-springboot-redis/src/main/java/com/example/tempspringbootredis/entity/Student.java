package com.example.tempspringbootredis.entity;

import java.io.Serializable;

/**
 * @author huangguizhao
 */
public class Student implements Serializable{

    private String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
