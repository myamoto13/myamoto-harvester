package com.extia.webscraper.scraper;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class Loginexample{
	private static OAuthService service;
	private Token requestToken;
	private Verifier verifier;
	private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~/connections:(id,last-name)";
	private static Boolean authInProgress = false;

	/** Called when the activity is first created. */
	public void onCreate() {
		if ( ! authInProgress ) {
			authInProgress = true;
			service = new ServiceBuilder()
			.provider(LinkedInApi.class)
			.apiKey("XXXXX")
			.apiSecret("YYYYY")
			.callback("callback://whodunit")
			.build();

			System.out.println("=== LinkedIn's OAuth Workflow ===");
			System.out.println();

			// Obtain the Request Token
			System.out.println("Fetching the Request Token...");
			requestToken = service.getRequestToken();
			System.out.println("Got the Request Token!");
			System.out.println();

			System.out.println("Now go and authorize Scribe here:");
			System.out.println(service.getAuthorizationUrl(requestToken));

			service.getAuthorizationUrl(requestToken);
		}
	}

	protected void onResume() {  
		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		System.out.println(" ---> Request Token: " + requestToken.getToken());
		System.out.println(" ---> Request Token Secret: " + requestToken.getSecret());
		System.out.println(" ---> Verifier: " + verifier.getValue());
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if you're curious it looks like this: " + accessToken + " )");
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
	} 
}