package com.dongnaoedu.service.problem;

import com.dongnaoedu.aim.BusiMock;
import com.dongnaoedu.aim.ProblemBank;

import java.util.Random;

/**
 * xulinfei
 * 创建日期：2017/12/08
 * 创建时间: 18:57
 * 题目处理的基础服务,模拟解析题目文本，下载图片等操作，
 * 返回解析后的文本
 */
public class BaseProblemService {
    /**
     * 对题目进行处理，如解析文本，下载图片等等工作
     * @param problemId 题目id
     * @return 题目解析后的文本
     */
    public static String makeProblem(Integer problemId,String problemSrc){
        Random r = new Random();
        BusiMock.buisness(450+r.nextInt(100));
        return "CompleteProblem[id="+problemId
                +" content=:"+ problemSrc+"]";
    }
}
