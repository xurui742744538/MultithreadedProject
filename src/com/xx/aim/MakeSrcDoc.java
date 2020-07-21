package com.xx.aim;

import com.xx.vo.PendingDocVo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * xulinfei
 * 创建日期：2017/12/04
 * 创建时间: 21:37
 */
public class MakeSrcDoc {

    /**
     * 形成待处理文档
     * @param docCount 生成的文档数量
     * @return 待处理文档列表
     */
    public static List<PendingDocVo> makeDoc(int docCount){
        Random r = new Random();
        Random rProblemCount = new Random();
        List<PendingDocVo> docList = new LinkedList<>();//文档列表
        for(int i=0;i<docCount;i++){
            List<Integer> problemList = new LinkedList<Integer>();//文档中题目列表
            int docProblemCount = rProblemCount.nextInt(60)+60;
            for(int j=0;j< docProblemCount;j++){
                int problemId = r.nextInt(Consts.PROBLEM_BANK_COUNT);
                problemList.add(problemId);
            }
            PendingDocVo pendingDocVo = new PendingDocVo("pending_"+i,problemList);
            docList.add(pendingDocVo);
        }
        return docList;
    }

}
