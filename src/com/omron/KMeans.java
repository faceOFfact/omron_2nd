package com.omron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class KMeans {

	static int iterBound = 100;
	static float gapBound = (float)0.0001;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        File inFile = new File("process.csv"); // 读取的CSV文件
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
            		if(t == 32767 || t <= 0)continue;
            		time.get(i/2-1).add(t);
            	}
            	time.get(9).add(total);
            }
            reader.close();
            
            for(int i = 0;i < 10;i++){
            	Collections.sort(time.get(i));
            	kMeans(time.get(i), 4);
            	//if(i == 0)break;
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("没找到文件！");
        }
        catch (IOException ex) {
            System.out.println("读写文件出错！");
        }
    }

	private static void kMeans(ArrayList<Integer>al, int k){
		int n = al.size();
		int inc = n/k;
		float centra[] = new float[k];
		float last[] = new float[k];
		
		float limit = 0;
		for(int i = 0;i < k;i++){
			centra[i] = (float)al.get((int)limit);
			last[i] = (float)al.get((int)limit);
			limit += inc;
			//System.out.println(centra[i]);
		}
		
		ArrayList<OneCase> points = new ArrayList<>();
		float dist[] = new float[k];
		for(int i = 0;i < n;i++){
			OneCase oc = new OneCase(al.get(i));
			
			float min = Integer.MAX_VALUE;
			int tag = -1;
			for(int j = 0;j < k;j++){
				dist[j] = Math.abs((float)al.get(i)-centra[j]);
				if(dist[j] < min){
					min = dist[j];
					tag = j;
				}
			}
			oc.tag = tag;
			//if(i%10 == 0)System.out.println(oc.pos+"\t"+oc.tag);
			points.add(oc);
		}
		
		int iter = 0;
		int sums[] = new int[k];
		int count[] = new int[k];
		int lowerBound[] = new int[k];
		int upperBound[] = new int[k];
		for(int i = 0;i < k;i++){
			lowerBound[i] = Integer.MAX_VALUE;
			upperBound[i] = Integer.MIN_VALUE;
		}
		while(iter++ < iterBound){
			for(int i = 0;i < k;i++){
				sums[i] = 0;
				count[i] = 0;
			}
			for(int i = 0;i < n;i++){
				//OneCase oc = points.get(i);
				float min = Integer.MAX_VALUE;
				int tag = -1;
				for(int j = 0;j < k;j++){
					dist[j] = Math.abs(points.get(i).pos-centra[j]);
					if(dist[j] < min){
						min = dist[j];
						tag = j;
					}
				}
				sums[tag] += points.get(i).pos;
				count[tag]++;
				points.get(i).tag = tag;
				//if(i%10 == 0)System.out.println(points.get(i).pos+"\t"+points.get(i).tag);
			}
			
			for(int i = 0;i < k;i++)
				centra[i] = (float)sums[i]/count[i];
			boolean isSame = true;
			for(int i = 0;i < k;i++)
				if(Math.abs(centra[i]-last[i]) > gapBound){
					isSame = false;
					break;
				}
			if(isSame)break;
			
			for(int i = 0;i < k;i++)
				last[i] = (float)sums[i]/count[i];
			//if(iter == 1)break;
		}
		for(int i = 0;i < n;i++){
			if(points.get(i).pos > upperBound[points.get(i).tag])upperBound[points.get(i).tag] = points.get(i).pos;
			if(points.get(i).pos < lowerBound[points.get(i).tag])lowerBound[points.get(i).tag] = points.get(i).pos;
		}
		
		for(int i = 0;i < k;i++)
			System.out.println(centra[i]+"\t"+count[i]+"\t"+lowerBound[i]+"\t"+upperBound[i]);
		System.out.println();
	}
}
