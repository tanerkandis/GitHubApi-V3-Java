package hitit;

import java.util.List;

import org.json.JSONObject;

import hitit.GitHub;;

public class User {
	
	public User(String username) {
		
		this.username = username;
		GetUserInfo();
	}
	String username;
	String location;
	String company;
	int contributionCount;
	
	private void GetUserInfo()
	{
		String url = "https://api.github.com/users/" + username;
		System.out.println("GetUserInfo request url: " + url);
		location = "";// response["location"];
		company = "";//response["company"];

		try {
			List<JSONObject> jsonObjects = GitHub.ReadUrl(url, 1);
			//System.out.println("object count: " + jsonObjects.size());
			for (int i = 0; i < jsonObjects.size(); i++) {
			    JSONObject jsonObj = jsonObjects.get(i);
			    location = jsonObj.get("location").toString();
			    company = jsonObj.get("company").toString();
			}
		}
		catch (Exception e) {
			System.out.println("GetUserInfo Error!");
			e.printStackTrace();
		}
	}
}
