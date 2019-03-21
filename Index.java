import java.io.*;
import java.util.*;
import java.lang.*;

public class Index{
	
	public static void writer(HashMap<String, HashSet<String>> ind2, String fp2){
		
		Set<String> keys = ind2.keySet();
		ArrayList<String> word_list = new ArrayList<>();
		for(String w : keys){
			word_list.add(w);
		}
		
		Collections.sort(word_list);
		
		try{
		File file = new File(fp2);
		
		if(!file.exists()){
			file.createNewFile();
		}
		
		PrintWriter pw = new PrintWriter(file);
		
		for(String w2 : word_list){
			HashSet<String> pages = ind2.get(w2);
			String p_str = w2 + " " + String.join(", ", pages);
			pw.println(p_str);
		}
		pw.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, HashSet<String>> word_reader(String file, int nc) throws Exception{
		
		int count = 0;
		ArrayList<String> words = new ArrayList<>();
		int index = 0;
		HashMap<String, HashSet<String>> ind = new HashMap<>();
		int page_count = 1;
		
		Scanner input = new Scanner(new File(file));
		
		ArrayList<String> data_list = new ArrayList<>();
		
		while(input.hasNext()){
			String w = input.next();
			data_list.add(w);
		}
		
		//Finding largest string. Program will break if max length of word exceeds nc.
		//int max_len = data_list.get(0).length();
		//int ind_str = 0;
		//for(int i = 0; i < data_list.size(); i++){
		//	if(data_list.get(i).length() > max_len){
		//		max_len = data_list.get(i).length();
		//		ind_str = i;
		//	}
		//}
		//System.out.println("Index " + ind_str + " "+ data_list.get(ind_str) + " " + "is the largest and is size " + max_len);
		
		//Finding total length of all the letters.
		ArrayList<String> letters = new ArrayList<>();
		for(String a : data_list){
			for(int i = 0; i < a.length(); i++){
				char c = a.charAt(i);
				String c_str = Character.toString(c);
				letters.add(c_str);
			}
		}
		
		int counter = letters.size();
		
		while(counter > 0){
			for(String word : data_list){
				for(int i = 0; i < word.length(); i++){
					count++;
				}
				
				words.add(word.toLowerCase());
				
				if(count > nc){
					words.remove(words.size() - 1);
					index = words.size();
					break;
				}
			}
			
			//Finding length of letters in compiled word list.
			ArrayList<String> letters2 = new ArrayList<>();
			for(String c : words){
				for(int i = 0; i < c.length(); i++){
					char c2 = c.charAt(i);
					String c2_str = Character.toString(c2);
					letters2.add(c2_str);
				}
			}
			counter = counter - letters2.size();
			
			for(String x : words){
				HashSet<String> value = ind.get(x);
				if(value == null){
					HashSet<String> pg_starter = new HashSet<>();
					String pg_str = Integer.toString(page_count);
					pg_starter.add(pg_str);
					ind.put(x, pg_starter);
				} else{
					String pg_str2 = Integer.toString(page_count);
					value.add(pg_str2);
					ind.put(x, value);
				}
			}
			data_list = new ArrayList<String>(data_list.subList(index, data_list.size()));
			count = 0;
			words.clear();
			index = 0;
			page_count++;
		}
		return ind;
	}
	
	public static void main(String[] args) throws Exception{
		long start = System.currentTimeMillis();
		
		File[] theFiles = new File(args[0]).listFiles();
		//File[] theFiles2 = new File(System.getProperty("user.dir") + "/" + args[0]).listFiles();
		//System.out.println(System.getProperty("user.dir"));
		
		File o_dir = new File(args[1]);
		if(!o_dir.exists()){
			o_dir.mkdir();
		}
		
		String o_dir_str = o_dir.getName();
		
		int nc = Integer.parseInt(args[2]);
		
		for(File path : theFiles){
			String fp = path.getName();
			String parent = path.getParent();
			String[] parts = fp.split("\\."); //escaping special character.
			String f_alone = parts[0];
			String fp_amend = f_alone + "_output.txt";
			HashMap<String, HashSet<String>> w_ind = word_reader(parent + "/" + fp, nc);
			writer(w_ind, o_dir_str + "/" + fp_amend);
		}
		
		long end = System.currentTimeMillis();
		System.out.println(end - start + " milliseconds");
	}
}