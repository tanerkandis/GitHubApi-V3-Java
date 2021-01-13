package hitit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays; 

import org.json.JSONArray;
import org.json.JSONObject;

public class GitHub {
	public static List<JSONObject> ReadUrl(String urlString, int count) throws Exception {
		List<JSONObject> values = new ArrayList<JSONObject>();
		
		//güvenlik önlemi.
		if (urlString == "" || count < 1)
			return values;

		BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        //System.out.println("url: " + url);
	    	reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        // json output okunuyor.
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read);

	        String data  = buffer.toString();
	        //System.out.println("data: " + data);
	        
	        // user info çekileceði zaman json array deðil json obje oluþur
	        JSONArray jsonArr = new JSONArray();
	        if (data.startsWith("{", 0) && data.endsWith("}")) {
	        	JSONObject obj = new JSONObject(data);
	    	    jsonArr.put(obj);
	        }
	        else {
	    	    jsonArr = new JSONArray(data);
	        }       	
	        
			//values json array istenilen count kadar dolduruluyor.
	        //eðer values json array,istenilen count dan küçükse bulduðu kadar gönderir.
			for (int i = 0; i < jsonArr.length(); i++) {
			    JSONObject jsonObj = jsonArr.getJSONObject(i);
			    values.add(jsonObj);			
				if (values.size() == count) {
			        if (reader != null)
			            reader.close();
			        
					break;
				}
			}
	    } 
		catch (IOException e) {
			System.out.println("ReadUrl Error!");
			e.printStackTrace();
		}
	    finally {
	        if (reader != null)
	            reader.close();
	    }
	    Thread.sleep(100); // api tarafýndan cok sýk sorgu gönderildiðinde banlamamasý için delay eklendi
	    return values;
	}
	
	//en fazla indirileren reposlarý döndür.	
	public static List<String> MostDownloadedRepos (String organisation, int count) {
		List<String> repositories = new ArrayList<String>();
		
		if (count < 1)
			return repositories;		
		else if (count > 5)
			count = 5; // cunku sabit olarak girdiðim liste 5 eleman içeriyor
		
		//https://github.com/search?utf8=%E2%9C%93&q=org%3Aapache&type=Repositories?qt=stars:%3E1&sort=stars
		List<String> populerRepos = Arrays.asList("echarts", "dubbo", "superset", "spark", "airflow"); 
		for (int i = 0; i < count; i++)
			repositories.add(populerRepos.get(i));
			
		return repositories;
	}
	//en fazla commit yapa userlar ý çekme.
	public static List<User> MostPopulerUser (String organisation, String repoName, int count) {
		List<User> users = new ArrayList<User>();
		//String url = "curl -H \"Accept: application/vnd.github.v3+json\"  https://api.github.com/repos/"+ organisation + "/" + repoName + "/contributors?sort=commit";
		String url = "https://api.github.com/repos/"+ organisation + "/" + repoName + "/contributors?sort=commit";
		System.out.println("MostPopulerUser request url: " + url);

		try {
			List<JSONObject> jsonObjects = ReadUrl(url, count);
			for (int i = 0; i < jsonObjects.size(); i++) {
			    JSONObject jsonObj = jsonObjects.get(i);
				String userName = jsonObj.get("login").toString();
				User user = new User(userName);
				user.contributionCount = Integer.parseInt(jsonObj.get("contributions").toString());
				users.add(user);
			}
		}
		catch (Exception e) {
			System.out.println("MostPopulerUser Error!");
			e.printStackTrace();
		}
		
		return users;
	}
}
