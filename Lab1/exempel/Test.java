import java.io.*;
import java.util.*;

class Test{

    public static void main(String[] args)throws IOException{
    BufferedReader bf;
    bf=new BufferedReader(new InputStreamReader(System.in));

	MinTrad t1=new MinTrad("Kalle");
	MinTrad t2=new MinTrad("Pelle");
	Thread tt1=new Thread(t1);
	Thread tt2=new Thread(t2);
	tt1.start();
	tt2.start();
	System.out.println("hej, ange ditt namn:");
	String namn=bf.readLine();
	while (!namn.equals("sluta")){
	    namn=bf.readLine();
	}
	    t1.keepRunning=false;
	    t2.keepRunning=false;


	System.out.println(namn+" fint namn!");
	
    }
}

class MinTrad  implements Runnable{
    boolean keepRunning=true;
    String name=null;
    BufferedReader bf;
    
    MinTrad(String name){

	this.name=name;
	bf=new BufferedReader(new InputStreamReader(System.in));	
    }
    
    
    public void run(){
	while (keepRunning!=false){
	    
	    System.out.println(Thread.currentThread().getName()+": "+name);
	    System.out.println(name+" Ange en text:");
	    try{
		name=bf.readLine();
		Thread.sleep(3000);
	    }catch(Exception ie){}
	}
    }
}
