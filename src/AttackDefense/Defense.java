package AttackDefense;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Defense {
	
	HashMap<Long, List<Long>> newNet=new HashMap();
	Random r=new Random(1234l);
	final int walkStep=3;
	final int reWalkLimit=5;
	
	public HashMap<Long, List<Long>> Perturb(HashMap<Long, List<Long>> net){
		for(Long u:net.keySet()){
			newNet.put(u, new ArrayList());
			List<Long> connection=net.get(u);
			int conSize=connection.size();
			for(int i=0;i<connection.size();i++){
				Long v=connection.get(i);
				Long cur=u;
				List<Long> curu=connection;
				int curConSize=conSize;
				int countWalkTimes=0;
				while((cur==u||cur==v||newNet.get(u).contains(cur))&&countWalkTimes<reWalkLimit){
					for(int j=0;j<walkStep;j++){
						if(curu == null)
							continue;
						cur=curu.get(r.nextInt(curConSize));
						curu=net.get(cur);
						if(curu == null)
							continue;
						curConSize=curu.size();
					}
					countWalkTimes++;
				}
				if(cur!=u&&cur!=v&&!newNet.get(u).contains(cur))newNet.get(u).add(cur);
			}
		}
		return newNet;
	}
	
	public HashMap<Long, List<Long>> readHash(String file) throws Exception{
		HashMap<Long, List<Long>> net=new HashMap();
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String temp="";
		while((temp=br.readLine())!=null){
			String[] sp=temp.split("\t");
			Long u=Long.parseLong(sp[0]);
			String[] temp2=sp[1].split(","); 
			List<Long> connection=new ArrayList();
			for(String s:temp2) connection.add(Long.parseLong(s));
			net.put(u, connection);
		}
		return net;
	}
}
