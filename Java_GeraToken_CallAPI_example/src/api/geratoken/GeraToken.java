package api.geratoken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GeraToken {

    public static void main(String[] args) throws IOException {
        geraToken();
    }

    public static void geraToken() throws IOException {

        String clientId = "xxxxxxxxxxxx";
        String clientSecret = "xxxxxxxxxxxx";
        String tokenUrl = "https://xxxxxxxxxxxx.com/oauth/token";

        URL url = new URL(tokenUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "client_credentials");

        String form;
        form = parameters.entrySet().stream().map(entry -> {
            try {
                return URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining("&"));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(form.getBytes("UTF-8"));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    System.out.println(response.toString());
                }
            }
        } else {
            throw new RuntimeException("HTTP Status Code: " + responseCode);
        }
    }
}
