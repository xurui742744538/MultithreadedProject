package com.dongnaoedu.vo;

import java.util.concurrent.Future;

/**
 * 动脑学院-Mark老师
 * 创建日期：2017/12/12
 * 创建时间: 20:59
 * 并发题目处理时，返回处理的题目结果
 */
public class MultiProblemVo {

    private final String problemText;//要么就是题目处理后的文本;
    private final Future<ProblemCacheVo> probleFuture;//处理题目的任务

    public MultiProblemVo(String problemText) {
        this.problemText = problemText;
        this.probleFuture = null;
    }

    public MultiProblemVo(Future<ProblemCacheVo> probleFuture) {
        this.probleFuture = probleFuture;
        this.problemText = null;
    }

    public String getProblemText() {
        return problemText;
    }

    public Future<ProblemCacheVo> getProbleFuture() {
        return probleFuture;
    }
}
