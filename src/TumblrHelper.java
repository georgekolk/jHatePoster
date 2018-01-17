import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TumblrHelper {

    private static String charset = "UTF-8";

    public static boolean postSinglePhoto(String blog, String tags,String API_KEY, String API_SECRET, String oauth_token, String oauth_token_secret, File image)throws Exception{
        String PROTECTED_RESOURCE_URL = "http://api.tumblr.com/v2/blog/" + blog + "/post";

        final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback("http://www.tumblr.com/connect/login_success.html")
                .build(TumblrApi.instance());

        OAuth1AccessToken accessToken666 = new OAuth1AccessToken(oauth_token, oauth_token_secret);
        byte[] bytes = Files.readAllBytes(image.toPath());

        String base64encodedString = Base64.getEncoder().encodeToString(bytes);

        //System.out.println("base64encodedString: " + base64encodedString);

        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);

        //request.addHeader("Content-type","application/x-www-form-urlencoded"); //from https://github.com/saeros/tumblrpost/blob/master/tumblrpost.php

        request.addParameter("type", "photo");
        //request.addParameter("title", "kYk");
        //request.addParameter("body", "text message");
        request.addParameter("tags", tags);
        request.addParameter("data64" , base64encodedString); //должно было без URLdecode, поэтому не работало

        service.signRequest(accessToken666, request);
        Response response = service.execute(request);

        System.out.println(response.getBody());

        //{"meta":{"status":201,"msg":"Created"},"response":{"id":162501469814}}
        //{"meta":{"status":400,"msg":"Bad Request"},"response":{"errors":["Nice image, but we don't support that format. Try resaving it as a gif, jpg, or png."]}}

        JSONParser pars = new JSONParser();

        try {

            JSONObject obj = (JSONObject) pars.parse(response.getBody());
            //System.out.println("obj to string looks like: " + obj.toString());
            JSONObject jsonResponse = (JSONObject) obj.get("meta");
            //System.out.println("response: " + jsonResponse.toString());

            if (jsonResponse.get("status").equals((long)201)) return true;

        }catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException npe) {
            npe.printStackTrace();

            throw new Exception(response.getBody());
        }

        return false;

    }

    public static boolean postSingleVideo(String blog, String tags,String API_KEY, String API_SECRET, String oauth_token, String oauth_token_secret, File image)throws Exception{
        String PROTECTED_RESOURCE_URL = "http://api.tumblr.com/v2/blog/" + blog + "/post";

        final OAuth10aService service = new ServiceBuilder()
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .callback("http://www.tumblr.com/connect/login_success.html")
                .build(TumblrApi.instance());

        OAuth1AccessToken accessToken666 = new OAuth1AccessToken(oauth_token, oauth_token_secret);
        byte[] bytes = Files.readAllBytes(image.toPath());

        String base64encodedString = Base64.getEncoder().encodeToString(bytes);

        //System.out.println("base64encodedString: " + base64encodedString);

        OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);

        //request.addHeader("Content-type","application/x-www-form-urlencoded"); //from https://github.com/saeros/tumblrpost/blob/master/tumblrpost.php

        request.addParameter("type", "video");
        //request.addParameter("title", "kYk");
        //request.addParameter("body", "text message");
        request.addParameter("tags", tags);
        request.addParameter("data64" , base64encodedString); //должно было без URLdecode, поэтому не работало

        service.signRequest(accessToken666, request);
        Response response = service.execute(request);

        System.out.println(response.getBody());

        //{"meta":{"status":201,"msg":"Created"},"response":{"id":162501469814}}
        //{"meta":{"status":400,"msg":"Bad Request"},"response":{"errors":["Nice image, but we don't support that format. Try resaving it as a gif, jpg, or png."]}}

        JSONParser pars = new JSONParser();

        try {

            JSONObject obj = (JSONObject) pars.parse(response.getBody());
            //System.out.println("obj to string looks like: " + obj.toString());
            JSONObject jsonResponse = (JSONObject) obj.get("meta");
            //System.out.println("response: " + jsonResponse.toString());


            if (jsonResponse.get("status").equals((long)201)) return true;

        }catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException npe) {
            npe.printStackTrace();

            throw new Exception(response.getBody());
        }

        return false;

    }
}


