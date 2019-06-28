package com.hisense.serverestimate.entity;

/**
 * @Author Huang.bingzhi
 * @Date 2019/6/26 15:44
 * @Version 1.0
 */
public class WjxExam {
    /**
     * ID
     */
    private String qid;
    /**
     * 调查名称
     */
    private String name;
    /**
     * 起始日期
     */
    private String begindate;
    /**
     * 回复的调查数量
     */
    private String answercount;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getAnswercount() {
        return answercount;
    }

    public void setAnswercount(String answercount) {
        this.answercount = answercount;
    }

    public WjxExam(String qid, String name, String begindate, String answercount) {
        this.qid = qid;
        this.name = name;
        this.begindate = begindate;
        this.answercount = answercount;
    }

    public WjxExam() {
    }

    public static void main(String[] args) {
        String textTypeIndexs = "1";
        String[] scoreTypeQuestionNo = textTypeIndexs.split(",");
        for (int i = 0; i <scoreTypeQuestionNo.length ; i++) {
            scoreTypeQuestionNo[i]="q"+scoreTypeQuestionNo[i];
        }
        System.out.println(scoreTypeQuestionNo);
    }
}
