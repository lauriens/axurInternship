package InternshipChallenge;

import java.util.regex.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SafetyLists {
	
	private static final String WORKINGDIR = System.getProperty("user.dir");
	private static final String DATADIR = "data";
	private static final String SLASH = System.getProperty("file.separator");
	private String filePath;
	private String listType;
	
	public SafetyLists(String listType)
	{
		this.listType = listType;
		this.filePath = WORKINGDIR + SLASH + DATADIR + SLASH + listType + ".txt";
	}
	
	private boolean checkURL(String url)
	{		
		//Regex expression for valid URLs
		String pattern = "(?:(?:https?|ftp):\\/\\/|\\b(?:[a-z\\d]+\\.))(?:(?:[^\\s()<>]+|\\((?:[^\\s()<>]+|(?:\\([^\\s()<>]+\\)))?\\))+(?:\\((?:[^\\s()<>]+|(?:\\(?:[^\\s()<>]+\\)))?\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))?";
		
		Pattern match = Pattern.compile(pattern);
		Matcher testing = match.matcher(url);
		
		if (testing.find())
		{
			if (testing.group(0).equals(url))
			{
				return true;
			}
		}
	
		return false;
	}
	
	public void addElement(String url) throws FileNotFoundException, IOException
	{
		if (!checkURL(url))
		{
			System.out.println("Invalid URL. Please, try again.");
			return;
		}
		
		try
		{
			if (this.onTheList(url))
			{
				System.out.println("URL is already on the list!");
				return;
			}
		} catch (FileNotFoundException e)
		{
			System.out.println(this.listType + " file not found. Creating one...");
		}
		
		try
		{
			FileWriter list = new FileWriter (filePath, true);
			
			list.write(url + "\n");
			list.close();
		} catch (IOException e)
		{
			String error = "Error while writing on the list!";
			throw new IOException(error);
		}
		System.out.println("Ok!");
	}
	
	public void removeElement(String url) throws IOException
	{
		String line;
		BufferedReader inputFile;
		FileWriter newList;
		boolean found = false;
		
		try
		{
			inputFile = new BufferedReader(new FileReader(filePath));
			//Create a new list file
			newList = new FileWriter("tmp.txt");
			
			//Copy the list to the new file, minus the URL we want to remove
			while ((line = inputFile.readLine()) != null)
				if (line.equals(url))
					found = true;
				else
					newList.write(line + "\n");
				
			//Check if URL existed
			if(!found)
				System.out.println("URL not found. " + listType + " remains the same.");
			
			//Delete the previous version
			inputFile.close();
			File oldList = new File(filePath);
			oldList.delete();
			
			//Rename the new file
			newList.close();
			File list = new File("tmp.txt");
			list.renameTo(oldList);
		} catch (FileNotFoundException e)
		{
			String error = "List doesn't exist. Please, create it before trying to remove something from it.";
			throw new FileNotFoundException(error);
			
		} catch (IOException e)
		{
			String error = "Couldn't remove the URL. Closing...";
			throw new IOException(error);
		}
		
	}
	
	public boolean onTheList(String url) throws IOException
	{
		String line;
		BufferedReader inputFile;
		boolean found = false;
		
		try
		{
			inputFile = new BufferedReader(new FileReader(filePath));
			
			while ((line = inputFile.readLine()) != null && found == false)
			{
				if (line.equals(url))
				{
					inputFile.close();
					return true;
				}
			}
			
			inputFile.close();
		} catch (FileNotFoundException e)
		{
			throw e;
		} catch (IOException e)
		{
			String error = "Error while reading from the list.";
			throw new IOException(error);
		}
		
		return false;
	}
	
	public void showList() throws FileNotFoundException, IOException
	{
		String line;
		BufferedReader inputFile;
		
		try
		{
			inputFile = new BufferedReader(new FileReader(filePath));
			
			while ((line = inputFile.readLine()) != null)
				System.out.println(line);
		} catch (FileNotFoundException e)
		{
			String error = listType + " file not found. Please, create a list before trying to print it!";
			throw new FileNotFoundException(error);
		} catch (IOException e)
		{
			String error = "Error while reading from the list.";
			throw new IOException(error);
		}
		
	}
	
}
