package com.jlpay.kotlindemo.study_java;

/**
 * 面试常考的编程代码整理
 */
public class InterviewCoding {

    /**
     * 单例模式
     * 腾讯面过，小鱼易连面过
     */
    public static volatile InterviewCoding instance;

    public InterviewCoding singleInstance() {
        if (instance == null) {
            synchronized (InterviewCoding.class) {
                if (instance == null) {
                    instance = new InterviewCoding();
                }
            }
        }
        return instance;
    }
}
