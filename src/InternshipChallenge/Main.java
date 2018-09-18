package InternshipChallenge;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	
	private static String url;
	private static SafetyLists whitelist;
	private static SafetyLists blacklist;
	
	public static void main(String args[]) throws IllegalArgumentException, IOException
	{
		parameterParser(args);
		
	}
	
	public static boolean checkAddOperation (String url, String list) throws IOException
	{
		System.out.println("Checking if URL already exists");
		if (list.equals("whitelist"))
		{
			try
			{
				if (blacklist.onTheList(url))
				{
					System.out.println("URL already exists on the blacklist. Removing it from the blacklist...");
					blacklist.removeElement(url);
				}
			} catch (FileNotFoundException e)
			{
				System.out.println("Warning: list file for blacklist doesn't exists.");
			}
			return true;
		}
		else
		{
			try
			{
				if (whitelist.onTheList(url))
				{
					System.out.println("URL already exists on the whitelist. Removing it...");
					whitelist.removeElement(url);
				}
			} catch (FileNotFoundException e)
			{
				System.out.println("Warning: list file for whitelist doesn't exists.");
			}
			return true;
		}
	}
	
	private static void verifyOperation(String url) throws IOException
	{
		boolean found = false;
		try {
			if (whitelist.onTheList(url))
			{
				System.out.println("Safe");
				found = true;
			}
		} catch (FileNotFoundException e)
		{
			System.out.println("Whitelist not created.");
		}
		try
		{
			if (!found && blacklist.onTheList(url))
			{
				System.out.println("Unsafe");
				found = true;
			}
		} catch (FileNotFoundException e)
		{
			System.out.println("Blacklist not created.");
		}
			if (!found)
				System.out.println("Unknown");
		
	}
	
	private static void parameterParser(String args[]) throws IllegalArgumentException, IOException
	{		
		String argumentError = "Invalid number or type of argument.\nUse: Main --help to see the options";
		if (args.length < 1)
			throw new IllegalArgumentException(argumentError);
		else if (args.length == 1)
			switch (args[0])
			{
				case "show-whitelist":
					whitelist = new SafetyLists("whitelist");
					whitelist.showList();
					break;
				
				case "show-blacklist":
					blacklist = new SafetyLists("blacklist");
					blacklist.showList();
					
				case "--help":
					System.out.println("Usage: Main <parameter> [<url>]\n\nParameters:\n"
									+  "add-whitelist <url> : Adds the url to the list of trusted websites\n"
									+  "add-blacklist <url> : Adds the url to the list of unsafe websites\n"
									+  "verify <url> : Checks if the url is safe; returns Unknown, if not present in both lists\n"
									+  "remove-whitelist <url> : removes the url from the list of trusted websites\n"
									+  "remove-blacklist <url> : removes the url from the list of unsafe websites\n"
									+  "show-whitelist : prints the list of trusted websites\n"
									+  "show-blacklist : prints the list of unsafe websites\n");
					System.exit(0);
					break;
				default:
					throw new IllegalArgumentException(argumentError);
			}
		else if (args.length == 2)
		{
			url = args[1];
			switch (args[0])
			{
			case "add-whitelist":
				whitelist = new SafetyLists("whitelist");
				blacklist = new SafetyLists("blacklist");
				if (checkAddOperation(url, "whitelist"))
					whitelist.addElement(url);
				break;
			case "add-blacklist":
				whitelist = new SafetyLists("whitelist");
				blacklist = new SafetyLists("blacklist");
				if (checkAddOperation(url, "blacklist"))
					blacklist.addElement(url);
				break;
			case "verify":
				whitelist = new SafetyLists("whitelist");
				blacklist = new SafetyLists("blacklist");
				verifyOperation(url);
				break;
			case "remove-whitelist":
				whitelist = new SafetyLists("whitelist");
				whitelist.removeElement(url);
				break;
			case "remove-blacklist":
				blacklist = new SafetyLists("blacklist");
				blacklist.removeElement(url);
				break;
			default:
				throw new IllegalArgumentException(argumentError);
			}
		}
		else
			throw new IllegalArgumentException(argumentError);
	}
	
}
