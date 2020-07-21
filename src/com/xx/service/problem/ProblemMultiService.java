package com.xx.service.problem;

import com.xx.aim.Consts;
import com.xx.aim.ProblemBank;
import com.xx.vo.MultiProblemVo;
import com.xx.vo.ProblemCacheVo;
import com.xx.vo.ProblemDBVo;

import java.util.concurrent.*;

/**
 * xulinfei
 * 创建日期：2017/12/12
 * 创建时间: 21:03
 * 并行异步的处理题目
 */
public class ProblemMultiService {

    //存放处理过题目内容的缓存
    private static ConcurrentHashMap<Integer, ProblemCacheVo> problemCache
            = new ConcurrentHashMap<>();

    //存放正在处理的题目的缓存，防止多个线程同时处理一个题目
    private static ConcurrentHashMap<Integer, Future<ProblemCacheVo>>
            processingProblemCache = new ConcurrentHashMap<>();

    //处理的题目的线程池
    private static ExecutorService makeProblemExec =
            Executors.newFixedThreadPool(Consts.THREAD_COUNT_BASE * 2);

    //供调用者使用，返回题目的内容或者任务
    public static MultiProblemVo makeProblem(Integer problemId) {
        //检查缓存中是否存在
        ProblemCacheVo problemCacheVo = problemCache.get(problemId);
        if (null == problemCacheVo) {
            System.out.println("题目【" + problemId + "】在缓存中不存在，需要新启任务");
            return new MultiProblemVo(getProblemFuture(problemId));
        } else {
            //拿摘要，一篇文档中的所有题目的摘要其实可以一次性取得，以减少对数据库的访问
            String problemSha = ProblemBank.getProblemSha(problemId);
            if (problemCacheVo.getProblemSha().equals(problemSha)) {
                System.out.println("题目【" + problemId + "】在缓存中存在且没有修改过，可以直接使用。");
                return new MultiProblemVo(problemCacheVo.getProcessedContent());
            } else {
                System.out.println("题目【" + problemId + "】的摘要发生了变化，启动任务更新缓存。");
                return new MultiProblemVo(getProblemFuture(problemId));
            }
        }

    }

    //返回题目的工作任务
    private static Future<ProblemCacheVo> getProblemFuture(Integer problemid) {
        Future<ProblemCacheVo> problemFuture = processingProblemCache.get(problemid);
        if (problemFuture == null) {
            ProblemDBVo problemDBVo = ProblemBank.getProblem(problemid);
            ProblemTask problemTask = new ProblemTask(problemDBVo, problemid);
            //当前线程新启了一个任务
            FutureTask<ProblemCacheVo> ft = new FutureTask<ProblemCacheVo>(problemTask);
            problemFuture = processingProblemCache.putIfAbsent(problemid, ft);
            if (problemFuture == null) {
                //表示没有别的线程正在处理当前题目
                problemFuture = ft;
                makeProblemExec.execute(ft);
                System.out.println("题目【" + problemid + "】计算任务启动，请等待完成>>>>>>>>>>>>>。");
            } else {
                System.out.println("刚刚有其他线程启动了题目【" + problemid + "】的计算任务，任务不必开启");
            }
        } else {
            System.out.println("当前已经有了题目【" + problemid + "】的计算任务，不必重新开启");
        }
        return problemFuture;
    }

    //处理题目的任务
    private static class ProblemTask implements Callable<ProblemCacheVo> {

        private ProblemDBVo problemDBVo;
        private Integer problemId;

        public ProblemTask(ProblemDBVo problemDBVo, Integer problemId) {
            this.problemDBVo = problemDBVo;
            this.problemId = problemId;
        }

        @Override
        public ProblemCacheVo call() throws Exception {
            try {
                ProblemCacheVo problemCacheVo = new ProblemCacheVo();
                problemCacheVo.setProcessedContent(
                        BaseProblemService.makeProblem(problemId, problemDBVo.getContent()));
                problemCacheVo.setProblemSha(problemDBVo.getSha());
                problemCache.put(problemId, problemCacheVo);
                return problemCacheVo;
            } finally {
                //无论正常还是异常，都需要将生成的题目的任务从缓存移除
                processingProblemCache.remove(problemId);
            }
        }
    }

}
