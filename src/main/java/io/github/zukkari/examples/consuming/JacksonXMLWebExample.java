package io.github.zukkari.examples.consuming;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.zukkari.examples.consuming.weather.Observations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class JacksonXMLWebExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Create the mapper that will transform XML into our Java object
        ObjectMapper mapper = new XmlMapper();
        // Disable feature that fails on unknown properties
        // This is needed because we dont need all of the properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Create HTTP client
        var client = HttpClient.newHttpClient();

        // Create a request that we will send over the network
        var request = HttpRequest.newBuilder()
                // URL that we will fetch the data from
                .uri(URI.create("http://www.ilmateenistus.ee/ilma_andmed/xml/observations.php"))
                // We have to set this header for this call, otherwise
                // Estonian Weather Service will not send us the response
                .header("User-Agent", "Mozilla/5.0")
                // HTTP method that specifies that we want to get the information
                .GET()
                .build();

        // Send the request over the network
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        // Read the Inputstream of the response into our Observations class
        Observations observations = mapper.readValue(response.body(), Observations.class);

        // Print out the result
        System.out.println(observations);
    }
}
