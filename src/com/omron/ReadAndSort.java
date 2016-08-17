package com.omron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ReadAndSort {
	
	static float threshold_low = (float) 0.05;
	static float threshold_high = (float) 0.95;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        File inFile = new File("process.csv"); // 读取的CSV文件
        //File outFile = new File(".csv");//写出的CSV文件
        String inString = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            //BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            ArrayList<ArrayList<Integer>> time = new ArrayList<>();
            for(int i = 0;i < 10;i++){
            	ArrayList<Integer> al = new ArrayList<>();
            	time.add(al);
            }
            while((inString = reader.readLine())!= null){
            	String[] sep = inString.split(",");
            	if(sep.length < 20)continue;
            	int total = Integer.parseInt(sep[19]);
            	if(total < 100 || total > 5000)continue;
            	for(int i = 2;i <= 18;i+=2){
            		int t = Integer.parseInt(sep[i]);
            		time.get(i/2-1).add(t);
            	}
            	time.get(9).add(total);
            }
            reader.close();
            
            for(int i = 0;i < 10;i++){
            	Collections.sort(time.get(i));
            	int low_bound = time.get(i).get((int) (time.get(i).size()*threshold_low));
            	int high_bound = time.get(i).get((int) (time.get(i).size()*threshold_high));
            	System.out.println(time.get(i).size()+"\t"+low_bound+"\t"+high_bound);
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("没找到文件！");
        }
        catch (IOException ex) {
            System.out.println("读写文件出错！");
        }
    }

}
