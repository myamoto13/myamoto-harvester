package com.extia.socialnetharvester.http.viadeo;
import java.util.Arrays;
import java.util.List;


public class ViadeoProperties {
	public String getViadeoUrl(){
		return "http://www.viadeo.com";
	}

	public String getViadeoSigninUrl() {
		return "https://secure.viadeo.com/r/account/authentication/signin";
	}

	public String getViadeoSearchUrl() {
		return "http://www.viadeo.com/v/search/members";
	}

	public List<String> getThemesDisjointsList() {
		/**thèmes non disjoints : Secteur ("Conseil", "High-tech" ...), Mots-clés ("Oracle", "Informatique"), Langue du profil**/
		return Arrays.asList(new String[]{"Région", "Ville", "Fonction"});
	}
	
	public int getMaximumSearchResults(){
		return 1000;
	}
}
