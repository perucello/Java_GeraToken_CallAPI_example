package api.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {

    public static void main(String[] args) {

        String id = "xxxxxxxxxxxx";
        String apiUrl = "https://xxxxxxxxxxxx";
        String token = "xxxxxxxxxxxx";
        callApi(apiUrl, token);
    }

    public static void callApi(String apiUrl, String bearerToken) {
        try {

            
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { try ( // 200 OK
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }   System.out.println(content.toString());
                    //return content.toString();
                }
            } else {
                System.out.println("Erro: Código de resposta HTTP " + responseCode);
                //return "Erro: Código de resposta HTTP " + responseCode;
            }
        } catch (IOException e) {
            System.out.println("Erro ao chamar API: " + e.getMessage());
            //return "Erro ao chamar API: " + e.getMessage();
        }
    }
    
    
}
