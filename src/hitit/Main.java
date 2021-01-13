package hitit;
import hitit.GitHub;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	private static final int repoCount = 5; 
	private static final int userCount = 10;
	private static final String organisation = "apache";
	private static final String fileName = "result.txt";
	
	private static void AddLog(String repoName, User user)//txt dosyasýna line ý yazýyor.
	{
		File f = new File(fileName);
		if(f.exists() == false || f.isDirectory()) 
			return;

		String line = "repo: " + repoName + ", user: " + user.username + ", location: " + user.location + ", company: " + user.company + ", contributors: " + user.contributionCount;  
		
		try (FileWriter myWriter = new FileWriter(fileName, true);
			BufferedWriter bufWriter = new BufferedWriter(myWriter);
			PrintWriter out = new PrintWriter(bufWriter)) {
			out.println(line);
		} 
		catch (IOException e) {
			System.out.println("AddLog Error!");
			e.printStackTrace();
		}
	}
	
	
	public static void main (String[] args) throws Exception
	{		
		// eski log siliniyor, yenisi olusturuluyor
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) 
		    f.delete();		
		if (f.createNewFile())
	    	System.out.println("File created: " + f.getName());

		//result.txt sonuçlar yazýlýyor
		List<String> repos = GitHub.MostDownloadedRepos(organisation, repoCount);
		for	(int repoIndex = 0; repoIndex < repos.size(); repoIndex++)
		{
			String repoName = repos.get(repoIndex);		
			List<User> users = GitHub.MostPopulerUser(organisation, repoName, userCount); 	// reading most populer users 
			//System.out.println(users.size());
			for	(int userIndex = 0; userIndex < users.size(); userIndex++)
				AddLog(repoName, users.get(userIndex));
 		}
		System.out.println("DONE. (press any key to exit)");
		System.in.read();
	}
}
