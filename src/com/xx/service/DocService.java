package com.xx.service;

import com.xx.aim.BusiMock;
import com.xx.service.problem.ProblemMultiService;
import com.xx.vo.MultiProblemVo;
import com.xx.vo.PendingDocVo;
import com.xx.service.problem.ProblemService;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * xulinfei
 * 创建日期：2017/12/04
 * 创建时间: 22:16
 * 文档处理服务
 */
public class DocService {

    /**
     * 上传文档到网络
     * @param docFileName 实际文档在本地的存储位置
     * @return 上传后的网络存储地址
     */
    public static String upLoadDoc(String docFileName){
        Random r = new Random();
        BusiMock.buisness(5000+r.nextInt(400));
        return "http://www.xxxx.com/file/upload/"+docFileName;
    }

    /**
     * 将待处理文档处理为本地实际文档
     * @param pendingDocVo 待处理文档
     * @return 实际文档在本地的存储位置
     */
    public static String makeDoc(PendingDocVo pendingDocVo){
        System.out.println("开始处理文档："+ pendingDocVo.getDocName());
        StringBuffer sb = new StringBuffer();
        for(Integer problemId: pendingDocVo.getProblemVoList()){
            sb.append(ProblemService.makeProblem(problemId));
        }
        return "complete_"+System.currentTimeMillis()+"_"
                +pendingDocVo.getDocName()+".pdf";
    }

    /**
     * 异步并行处理文档中的题目
     * @param pendingDocVo
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String makeAsyn(PendingDocVo pendingDocVo) throws ExecutionException, InterruptedException {
        System.out.println("开始处理文档："+ pendingDocVo.getDocName());

        //对题目处理结果的缓存
        Map<Integer,MultiProblemVo> multiProblemVoMap = new HashMap<>();
        //并行处理文档中的每个题目
        for(Integer problemId:pendingDocVo.getProblemVoList()){
            multiProblemVoMap.put(problemId,ProblemMultiService.makeProblem(problemId));
        }

        //获取题目的结果
        StringBuffer sb = new StringBuffer();
        for(Integer problemId:pendingDocVo.getProblemVoList()){
            MultiProblemVo multiProblemVo = multiProblemVoMap.get(problemId);
            sb.append(
             multiProblemVo.getProblemText()==null
                     ? multiProblemVo.getProbleFuture().get().getProcessedContent()
                     : multiProblemVo.getProblemText());
        }
        return "complete_"+System.currentTimeMillis()+"_"
                +pendingDocVo.getDocName()+".pdf";

    }

}
