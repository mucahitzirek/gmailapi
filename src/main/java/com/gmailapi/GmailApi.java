package com.gmailapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Settings.GetAutoForwarding;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

public class GmailApi {
    private static final String url="https://accounts.google.com/o/oauth2/token";
    private static final String APPNAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String user = "me";
    static Gmail service = null;
    private static File filePath=new File(System.getProperty("user.dir")+"/credentials.json");

   public static void main(String[] args) throws FileNotFoundException, IOException, GeneralSecurityException {
//	   		getGmailService();
//	   		getMailBody("Deneme");
   }

   public static void getMailBody(String searchString) throws IOException {
		Gmail.Users.Messages.List request = service.users().messages().list(user).setQ(searchString);
		ListMessagesResponse messagesResponse = request.execute();
		request.setPageToken(messagesResponse.getNextPageToken());
		// Get ID of the email you are looking for
		String messageId = messagesResponse.getMessages().get(0).getId();
		Message message = service.users().messages().get(user, messageId).execute();
		String emailBody = StringUtils
				.newStringUtf8(Base64.decodeBase64(message.getPayload().getParts().get(0).getBody().getData()));

	}

	public static Gmail getGmailService() throws IOException, GeneralSecurityException {
		InputStream in = new FileInputStream(filePath); // Read credentials.json
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		Credential authorize = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
				.setJsonFactory(JSON_FACTORY)
				.setClientSecrets(clientSecrets.getDetails().getClientId().toString(),
						clientSecrets.getDetails().getClientSecret().toString())
				.build().setAccessToken(getAccessToken()).setRefreshToken(
						accessToken);
		
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize)
				.setApplicationName(GmailApi.APPNAME).build();

		return service;
	}

   private static String getAccessToken(){
		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("grant_type", "refresh_token");
			params.put("client_id", "clientid); //Replace this
			params.put("client_secret", client_secret); //Replace this
			params.put("refresh_token",refreshtoken); 
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (postData.length() != 0) {
					postData.append('&');
				}
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			URL url = new URL(url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.getOutputStream().write(postDataBytes);

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				buffer.append(line);
			}

			JSONObject json = new JSONObject(buffer.toString());
			String accessToken = json.getString("access_token");
			return accessToken;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
   }
}
