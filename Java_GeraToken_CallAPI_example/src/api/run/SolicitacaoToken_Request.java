package api.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SolicitacaoToken_Request {

    public static void main(String[] args) throws IOException {
        String id = "xxxxxxxxxxxx";
        String apiUrl = "xxxxxxxxxxxx/" + id;
        run(apiUrl);
    }

    public static void run(String apiUrl) throws IOException {

        String clientId = "xxxxxxxxxxxx";
        String clientSecret = "xxxxxxxxxxxx";
        String tokenUrl = "https://xxxxxxxxxxxx/oauth/token";

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
                    String token = response.toString();
                    System.out.println(token);

                    String regex = "\"access_token\":\"([^\"]+)\"";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(token);

                    if (matcher.find()) {
                        String accessToken = matcher.group(1);

                        System.out.println("Chamando API: " + apiUrl);
                        System.out.println("Credencial Token: " + accessToken);
                        chamaApi(apiUrl, accessToken);

                    } else {
                        System.out.println("Sem acesso.");
                    }
                }
            }
        } else {
            throw new RuntimeException("HTTP Status Code: " + responseCode);
        }
    }

    public static String chamaApi(String apiUrl, String bearerToken) throws MalformedURLException, IOException {

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        connection.setRequestProperty("Authorization", "Bearer " + bearerToken);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try ( // 200 OK
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println("Response: " + content.toString());
                return content.toString();
            }
        } else {
            System.out.println("Erro: Código de resposta HTTP " + responseCode);
            return "Erro: Código de resposta HTTP " + responseCode;
        }
    }

}
