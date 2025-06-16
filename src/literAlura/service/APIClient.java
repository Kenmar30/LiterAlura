package literAlura.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class APIClient {

    public String get(String urlString) {
        try {
            // Si la URL contiene parámetros con texto del usuario, codificamos
            if (urlString.contains("search=")) {
                String base = urlString.substring(0, urlString.indexOf("search=") + 7);
                String query = urlString.substring(urlString.indexOf("search=") + 7);
                String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
                urlString = base + encodedQuery;
            }

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            StringBuilder response = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                response.append((char) c);
            }

            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al realizar la solicitud HTTP: " + e.getMessage(), e);
        }
    }
}
