package com.dongnaoedu.vo;

/**
 * 动脑学院-Mark老师
 * 创建日期：2017/12/05
 * 创建时间: 11:39
 * 题目在数据库中存放实体类
 */
public class ProblemDBVo {
    //题目id
    private final int problemId;
    //题目内容，平均长度700字节
    private final String content;
    //题目的sha串
    private final String sha;

    public ProblemDBVo(int problemId, String content, String sha) {
        this.problemId = problemId;
        this.content = content;
        this.sha = sha;
    }

    public int getProblemId() {
        return problemId;
    }

    public String getContent() {
        return content;
    }

    public String getSha() {
        return sha;
    }
}
