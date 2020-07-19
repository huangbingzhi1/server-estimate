package com.hisense.serverestimate.service.impl;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @Author Huang.bing.zhi
 * @Description 汇总每个分公司的记录
 * @Date 2019/10/31 18:25
 * @Version 1.0
 */
public class ExamCollectionVO {
    private String company;
    private Set<String>  serverCodeSet;
    private Set<String> cis;
    //每个题目的分数
//    private List<Double> totalScoreByNoList;
    private List<HashMap<String,ArrayList<Double>>> scoreByNoMap;
    private Double meanScore;
    private int num;

    public ExamCollectionVO(Map<String, Object> data){
        final String[] scoreArrays = data.get("scoreArray").toString().split(",");

        serverCodeSet=new HashSet<>(20);
        cis=new HashSet<>(20);
        scoreByNoMap= new ArrayList<HashMap<String, ArrayList<Double>>>();
        for(int i=0;i<scoreArrays.length;i++){
            scoreByNoMap.add(new HashMap<String, ArrayList<Double>>());
        }
        for(int i=0;i<scoreArrays.length;i++){
            if(scoreByNoMap.get(i).containsKey(data.get("serverCode").toString())){
                 ArrayList<Double> serverCodeScors = scoreByNoMap.get(i).get(data.get("serverCode").toString());
                serverCodeScors.add(Double.valueOf(scoreArrays[i]));
            }else{
                ArrayList<Double> cisScoreList=new ArrayList<Double>();
                cisScoreList.add(Double.valueOf(scoreArrays[i]));
                scoreByNoMap.get(i).put(data.get("serverCode").toString(),cisScoreList);
            }
        }
//        totalScoreByNoList =new ArrayList<>(10);
//        for(String temp:scoreArrays){
//            totalScoreByNoList.add(new Double(temp));
//        }
        meanScore=Double.valueOf(data.get("meanScore").toString());
        cis.add( data.get("enterpriseCis").toString());
        serverCodeSet.add(data.get("serverCode").toString());
        company=data.get("companyName").toString();
        num=1;
    }

    public void addItem(Map<String, Object> data){
        num++;
        cis.add( data.get("enterpriseCis").toString());
        serverCodeSet.add(data.get("serverCode").toString());
        final String[] scoreArrays = data.get("scoreArray").toString().split(",");

        for(int i=0;i<scoreArrays.length;i++){
            if(scoreByNoMap.get(i).containsKey(data.get("serverCode").toString())){
                ArrayList<Double> serverCodeScors = scoreByNoMap.get(i).get(data.get("serverCode").toString());
                serverCodeScors.add(Double.valueOf(scoreArrays[i]));
            }else{
                ArrayList<Double> cisScoreList=new ArrayList<Double>();
                cisScoreList.add(Double.valueOf(scoreArrays[i]));
                scoreByNoMap.get(i).put(data.get("serverCode").toString(),cisScoreList);
            }
        }
    }
    public String getCompany(){
        return company;
    }
    public  List<String> getMeanScoreByNoList(){
        DecimalFormat df = new DecimalFormat("#.00");
        List<String> result=new ArrayList<>();
        for(HashMap<String, ArrayList<Double>> temp:this.scoreByNoMap){
            final Collection<ArrayList<Double>> values = temp.values();
            double total=0.0d;
            for(ArrayList<Double> item:values){
                final OptionalDouble average = item.stream().mapToDouble(s->s).average();
                total+=average.getAsDouble();
            }
            result.add(df.format(total/serverCodeSet.size()));
        }
        return result;
    }
    public String getMeanScore(){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(this.meanScore);
    }
    public String getCisNum(){
        return String.valueOf(cis.size());
    }
    public String getServerNum(){
        return String.valueOf(serverCodeSet.size());
    }
}
