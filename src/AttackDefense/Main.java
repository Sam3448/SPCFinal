package AttackDefense;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		/*
		Crawler c = new Crawler();
		c.init();
		c.fetch(179990739l, 1);
		c.formRecommendationSet();
		*/
		//HashMap net = c.getFriends();
		/*
		Defense df=new Defense();
		HashMap net = df.readHash("hashmap.txt");
		System.out.println(net.toString());
		System.out.println(df.Perturb(net).toString());
		System.out.println();
		*/
		Defense df=new Defense();
		HashMap net = df.readHash("hashmap.txt");
		System.out.println(net.toString());
		System.out.println(df.Perturb(net).toString());
		System.out.println();
		Attack attack=new Attack("dataset.txt", "history.txt");
		System.out.println(Arrays.toString(attack.ranking()));
		System.out.println();
		attack=new Attack("dataset_perturbed.txt", "history.txt");
		System.out.println(Arrays.toString(attack.ranking()));
	}
}
