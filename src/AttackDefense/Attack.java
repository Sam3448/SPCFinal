package AttackDefense;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Attack {
	
	//History set, recommendation set, lambda, # of connections
	final double lambda=1e-2;
	
	//******************************
	List<Integer> H=new ArrayList();
	List<List<Integer>> C=new ArrayList(); //the collection of R(all candidates)
	List<Integer> numConnections=new ArrayList();//index should be the same as C
	List<Long> index2Id=new ArrayList();
	HashMap<String, Integer> post2num=new HashMap();
	//******************************
	
	String usersInput, history;
	
	public Attack(String userPosts, String history) throws Exception{
		this.usersInput=userPosts;
		this.history=history;
		readInPosts();
		readH();
	}
	
	public void readInPosts() throws Exception{
		//ID(long)\t\t# of connections(int)\t\t(post(String)(\t))
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(usersInput)));
		String temp="";
		while((temp=br.readLine())!=null){
			String[] sp=temp.split("\t\t");
			index2Id.add(Long.parseLong(sp[0]));
			numConnections.add(Integer.parseInt(sp[1]));
			String[] rec=sp[2].split("\t");
			for(String temp2:rec) post2num.put(temp2, post2num.getOrDefault(temp2, post2num.size()));
			C.add(new ArrayList());
			for(String temp2:rec) C.get(C.size()-1).add(post2num.get(temp2));
		}
	}
	
	public void readH() throws Exception{
		//one post per row
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(history)));
		String temp="";
		while((temp=br.readLine())!=null){
			H.add(post2num.get(temp));
		}
		System.out.println(H.toString());
	}
	
	public long[] ranking(){  //return the ranking of Id
		int candidates=index2Id.size();
		long[] retUserId=new long[candidates];
		PriorityQueue<double[]> usernscore=new PriorityQueue(new Comparator<double[]>(){
			public int compare(double[] a, double[] b){
				return (int)(b[1]-a[1]);
			}
		});
		for(int user=0;user<candidates;user++){
			double pR=lambda*numConnections.get(user);
			List<Integer> R=C.get(user);
			double count=0;
			for(int temp:R) if(H.contains(temp)) count++;
			double qR=count/H.size();
			double userScore=Integer.MIN_VALUE;
			if(qR!=0) userScore=qR*Math.log(qR/pR)+(1-qR)*Math.log((1-qR)/(1-pR));
			usernscore.offer(new double[]{user, userScore});
		}
		int count=0;
		while(!usernscore.isEmpty()){
			double[] temp=usernscore.poll();
			System.out.println(temp[0]+" "+temp[1]);
			retUserId[count++]=index2Id.get((int)temp[0]);
		}
		return retUserId;
	}
}
