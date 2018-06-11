import java.io.File;
import java.util.List;

public class TelegramHelper {

    public static boolean postSinglePhoto(String blog, String API_KEY, File image)throws Exception{
        String urlPhotoString = "https://api.telegram.org/bot" + API_KEY + "/sendPhoto?chat_id=" + blog;
        MultipartUtility multipart = new MultipartUtility(urlPhotoString, "UTF-8");
        multipart.addFilePart("photo", image);
        List<String> response2 = multipart.finish();
        //System.out.println(response2.toString());
        if (response2.toString().contains("\"ok\":true")){
            return true;
        }else{
            return false;
        }
    }

}
