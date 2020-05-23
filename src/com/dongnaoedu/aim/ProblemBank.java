package com.dongnaoedu.aim;

import com.dongnaoedu.vo.ProblemDBVo;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * xulinfei
 * 创建日期：2017/12/05
 * 创建时间: 10:57
 * 题库的模拟
 */
public class ProblemBank {

    //题库数据存储
    private static ConcurrentHashMap<Integer,ProblemDBVo> problemBankMap = new ConcurrentHashMap<>();
    //定时任务池，负责定时更新题库数据
    private static ScheduledExecutorService updateProblemBank = new ScheduledThreadPoolExecutor(1);

    //初始化题库
    public static void initBank(){
        for(int i=0;i<Consts.PROBLEM_BANK_COUNT;i++){
            String problemContent = getRandomString(700);
            problemBankMap.put(i,new ProblemDBVo(i,problemContent,EncryptTools.EncryptBySHA1(problemContent)));
        }
        updateProblemTimer();
    }

    //生成随机字符串
    //length表示生成字符串的长度
    private static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    //获得题目，我们假设一次数据库的读耗时在一般在20ms左右，所以休眠20ms
    public static ProblemDBVo getProblem(int i) {
        BusiMock.buisness(20);
        return problemBankMap.get(i);
    }

    public static String getProblemSha(int i){
        BusiMock.buisness(10);
        return problemBankMap.get(i).getSha();
    }


    //更新题库的定时任务
    private static class UpdateProblem implements Runnable{

        @Override
        public void run() {
            Random random = new Random();
            int problemId = random.nextInt(Consts.PROBLEM_BANK_COUNT);
            String problemContent = getRandomString(700);
            problemBankMap.put(problemId,new ProblemDBVo(problemId,problemContent,EncryptTools.EncryptBySHA1(problemContent)));
            //System.out.println("题目【"+problemId+"】被更新！！");
        }
    }

    //定期更新题库数据
    private static void updateProblemTimer(){
        System.out.println("开始定时更新题库..........................");
        updateProblemBank.scheduleAtFixedRate(new UpdateProblem(),15,5, TimeUnit.SECONDS);
    }
}
