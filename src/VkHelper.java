import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VkHelper {
    private static String requestURL = null;
	private static String charset = "UTF-8";

    public static List<String> uploadPhoto(String uploadPage, String charset, File fileToProcess)throws Exception {
        List<String> serverHashPhoto = new ArrayList<String>();
        MultipartUtility multipart = new MultipartUtility(uploadPage, charset);
        multipart.addHeaderField("User-Agent", "jHateSMM");
        multipart.addFormField("description", "Cool Pictures");
        multipart.addFilePart("file", fileToProcess);
        List<String> response2 = multipart.finish();
        JSONParser pars = new JSONParser();
        Object obj = null;
        try {
            obj = pars.parse(response2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray uploadedPhotoArray = (JSONArray) obj;

        for (int i = 0; i < uploadedPhotoArray.size(); i++) {
            JSONObject uploadedPhoto = (JSONObject) uploadedPhotoArray.get(i);
            serverHashPhoto.add(uploadedPhoto.get("server").toString());
            serverHashPhoto.add(uploadedPhoto.get("hash").toString());
            serverHashPhoto.add(uploadedPhoto.get("photo").toString());
        }
            return serverHashPhoto;
    }

	public static String saveWallPhoto(String ACCESS_TOKEN, String GROUP_ID, String server, String hash, String photo)throws Exception{
		URL saveWallPhoto = new URL("https://api.vk.com/method/photos.saveWallPhoto?group_id=" + GROUP_ID + "&server=" + server + "&hash=" + hash + "&photo=" + photo + "&access_token=" + ACCESS_TOKEN + "&v=5.52");
		String multipleAttachments = "";

			HttpURLConnection connection = (HttpURLConnection) saveWallPhoto.openConnection();
			StringBuilder content = new StringBuilder();

			connection.setRequestProperty("Accept-Charset", charset);
			connection.setUseCaches(false);
			connection.setRequestProperty("User-Agent", "jHateSMM");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

			while ((line = bufferedReader.readLine()) != null)
			{
				content.append(line + "\n");
			}
			bufferedReader.close();
			JSONParser pars = new JSONParser();

			try {
				JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONArray jsonResponse = (JSONArray) obj.get("response");

				for(int i = 0; i < jsonResponse.size(); i++){
					JSONObject jsonObj1 = (JSONObject) jsonResponse.get(i);
					System.out.println("PHOTO_ID: "+ jsonObj1.get("id").toString());
					multipleAttachments = multipleAttachments + "photo" + jsonObj1.get("owner_id").toString() + "_" + jsonObj1.get("id").toString()+",";
				}

			} catch (NullPointerException e) {
				e.printStackTrace();

				JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONObject jsonError = (JSONObject) obj.get("error");
				System.out.println("jsonError: " + jsonError);
				String errorMsg = (String) jsonError.get("error_msg");
				System.out.println("errorMsg: " + errorMsg);

				throw new Exception(errorMsg);
			}

		return multipleAttachments;
	}

	public static Boolean postWallPhoto(String ACCESS_TOKEN, String GROUP_ID, String multipleAttachments, String message)throws Exception{
		URL postWallPhoto = new URL("https://api.vk.com/method/wall.post?owner_id=-" + GROUP_ID  + "&attachments=" + multipleAttachments + "&access_token=" + ACCESS_TOKEN + "&message="
				+ message + "&v=5.62");

		System.out.println("postWallPhoto: " + postWallPhoto);

		boolean savePhotoResult = false;
			HttpURLConnection connection = (HttpURLConnection) postWallPhoto.openConnection();
			StringBuilder content = new StringBuilder();

			connection.setRequestProperty("Accept-Charset", charset);
			connection.setUseCaches(false);
			connection.setRequestProperty("User-Agent", "jHateSMM");
			connection.setRequestProperty("Connection","close");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;

			while ((line = bufferedReader.readLine()) != null){
				content.append(line + "\n");
			}
			bufferedReader.close();
			savePhotoResult = content.toString().contains("post_id");

		return savePhotoResult;
	}

	public static String getWallUploadServer(String ACCESS_TOKEN, String GROUP_ID)throws Exception{
		URL getWallUploadServer = new URL("https://api.vk.com/method/photos.getWallUploadServer?group_id=" + GROUP_ID + "&access_token=" + ACCESS_TOKEN + "&count=1&v=5.62");
			HttpURLConnection connection = (HttpURLConnection) getWallUploadServer.openConnection();
			StringBuilder content = new StringBuilder();

			connection.setRequestProperty("Accept-Charset", charset);
			connection.setUseCaches(false);
			connection.setRequestProperty("User-Agent", "jHateSMM");

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;

			while ((line = bufferedReader.readLine()) != null){
				content.append(line + "\n");
			}
			bufferedReader.close();

			JSONParser pars = new JSONParser();

			try {
				JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONObject jsonResponse = (JSONObject) obj.get("response");
				requestURL = (String) jsonResponse.get("upload_url");

			}catch (NullPointerException e) {
				e.printStackTrace();
				JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONObject jsonError = (JSONObject) obj.get("error");
				String errorMsg = (String) jsonError.get("error_msg");
				System.out.println("errorMsg: " + errorMsg);
				throw new Exception(errorMsg);
			}

		return requestURL;

	}

    public static String docsGetWallUploadServer(String ACCESS_TOKEN, String GROUP_ID)throws Exception{
        URL getWallUploadServer = new URL("https://api.vk.com/method/docs.getWallUploadServer?group_id=" + GROUP_ID + "&access_token=" + ACCESS_TOKEN + "&v=5.62");

            HttpURLConnection connection = (HttpURLConnection) getWallUploadServer.openConnection();
            StringBuilder content = new StringBuilder();

            connection.setRequestProperty("Accept-Charset", charset);
            connection.setUseCaches(false);
            connection.setRequestProperty("User-Agent", "jHateSMM");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();

            JSONParser pars = new JSONParser();

            try {
                JSONObject obj = (JSONObject) pars.parse(content.toString());
                JSONObject jsonResponse = (JSONObject) obj.get("response");
                requestURL = (String) jsonResponse.get("upload_url");

			}catch (NullPointerException e) {
				e.printStackTrace();

				JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONObject jsonError = (JSONObject) obj.get("error");
				String errorMsg = (String) jsonError.get("error_msg");
				throw new Exception(errorMsg);
			}
        return requestURL;

    }

    public static String uploadDoc(String uploadPage, String charset, File fileToProcess)throws Exception {
        MultipartUtility multipart = new MultipartUtility(uploadPage, charset);
        multipart.addHeaderField("User-Agent", "jHateSMM");
        multipart.addFormField("description", "Cool Pictures");

        multipart.addFilePart("file", fileToProcess);
        List<String> response2 = multipart.finish();

        JSONParser pars = new JSONParser();

        try {
            JSONArray obj = (JSONArray) pars.parse(response2.toString());

            for (int i = 0; i < obj.size(); i++) {
                JSONObject uploadedFile = (JSONObject) obj.get(i);
                return uploadedFile.get("file").toString();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String docsSave (String ACCESS_TOKEN, String uploadFile)throws Exception{
        URL getWallUploadServer = new URL("https://api.vk.com/method/docs.save?file=" + uploadFile + "&access_token=" + ACCESS_TOKEN +"&v=5.63");

            HttpURLConnection connection = (HttpURLConnection) getWallUploadServer.openConnection();
            StringBuilder content = new StringBuilder();

            connection.setRequestProperty("Accept-Charset", charset);
            connection.setUseCaches(false);
            connection.setRequestProperty("User-Agent", "jHateSMM");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();

            JSONParser pars = new JSONParser();

            try {
                JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONArray jsonResponse = (JSONArray) obj.get("response");

				for (int i = 0; i < obj.size(); i++) {
					JSONObject uploadedDoc = (JSONObject) jsonResponse.get(i);
					return "doc" + uploadedDoc.get("owner_id") + "_" + uploadedDoc.get("id") + ",";
				}

			} catch (ParseException e) {
                e.printStackTrace();

				JSONObject obj = (JSONObject) pars.parse(content.toString());
				JSONObject jsonError = (JSONObject) obj.get("error");
				String errorMsg = (String) jsonError.get("error_msg");
				throw new Exception(errorMsg);

			}
        return null;
    }
}






