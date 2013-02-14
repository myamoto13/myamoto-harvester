package com.extia.webscraper.scraper.linkedinTest;

import java.io.IOException;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class LinkedInExample
{
	private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~/connections:(id,last-name)";

	
	
	
	public static void main(String[] args) throws IOException
	{
		OAuthService service = new ServiceBuilder()
		.provider(LinkedInApi.class)
		.apiKey("n97yvokjb06u")
		.apiSecret("IvQxGkjq6VsCTtwC")
		.scope("r_network")
		.build();
		Scanner in = new Scanner(System.in);
		//BareBonesBrowserLaunch.openURL("www.google.com");
		System.out.println("=== LinkedIn's OAuth Workflow ===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println();

		System.out.println("Now go and authorize Scribe here:");
		String authURL = service.getAuthorizationUrl(requestToken);
		System.out.println(authURL);
		BareBonesBrowserLaunch.openURL(authURL);
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
		System.out.println();
		
		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
		
		request = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~");
		service.signRequest(accessToken, request);
		response = request.send();
		System.out.println("profile :");
		System.out.println();
		System.out.println(response.getBody());
		
		String searchURL ="http://api.linkedin.com/v1/people-search?keywords=Java";
		
		request = new OAuthRequest(Verb.GET, searchURL);
		service.signRequest(accessToken, request);
		response = request.send();
		System.out.println("Java search :");
		System.out.println();
		System.out.println(response.getBody());
		
	}

}