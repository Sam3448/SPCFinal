package AttackDefense;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Crawler {
	
	private Twitter twitter;
	private HashMap<Long, ArrayList<Long>> friends;
	private HashMap<Long, ArrayList<String>> posts;
	
	public HashMap getFriends(){
		return friends;
	}
	
	public void init(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey("sGakE2fSMVDLdi3x4dmC5Spxl");
		builder.setOAuthConsumerSecret("tTYGpBS2XeqzZ4te8GyqHjQMxARdBsQiuYphRqFm26UwmiUpVJ");
		builder.setOAuthAccessToken("383166892-gut2GtqciktJLqowSIBl3SKUvpRPnKEP9ZPcoDuI");
		builder.setOAuthAccessTokenSecret("m8NCttw4GMOzWJAlhKNRJaoPA2jPSnz2PWFF5V1dpuGmr");
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();
		friends = new HashMap<Long, ArrayList<Long>>();
		posts = new HashMap<Long, ArrayList<String>>();
	}
	
	public void fetch(Long entryId, int level){
		try {
			ArrayList<PagableResponseList> list = new ArrayList<PagableResponseList>();
			ArrayList<PagableResponseList> list1 = new ArrayList<PagableResponseList>();
			PagableResponseList<User> users = twitter.getFriendsList(twitter.getId(), -1);
			list.add(users);
			int i=0;
			while(i<level){
				for(PagableResponseList<User> l : list){
					for(User u : l){
						if(!friends.keySet().contains(u.getId())){
							ArrayList f =  new ArrayList<Long>();
							PagableResponseList<User> userList = twitter.getFriendsList(u.getId(), -1);
							for(User uu : userList){
								try{
									f.add(uu.getId());
									
									ArrayList statuses = new ArrayList<String>();
									for(Status s : twitter.getUserTimeline(uu.getId())){
										statuses.add(s.getText().replaceAll("\n", " "));
									}
									posts.put(uu.getId(), statuses);
									
								} catch (TwitterException e) {
									e.printStackTrace();
								}
							}
							friends.put(u.getId(), f);
							list1.add(userList);
						}
					}
				}
				list = new ArrayList<PagableResponseList>(list1);
				list1.clear();
				i++;
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public void formRecommendationSet(){
		StringBuilder sb = new StringBuilder();
		for(long i : friends.keySet()){
			sb.append(i + "\t\t" + friends.get(i).size() + "\t");
			for(long j : friends.get(i)){
				if(posts.get(j) != null){
					for(String s : posts.get(j)){
						sb.append("\t" + s);
					}
				}
			}
			sb.append("\n");
		}
		try {
			FileWriter fw = new FileWriter(new File("recommendations.txt"));
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
