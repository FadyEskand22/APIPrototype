package com.APIprototype.APIprototype;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/catfacts")
public class RestAPIController {
    private final String API_URL = "https://meowfacts.herokuapp.com/";
    
    @GetMapping("/random")
    public Object getRandomCatFact() {
        try {

            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonResponse = restTemplate.getForObject(API_URL, String.class);
            JsonNode root = mapper.readTree(jsonResponse);

            // pulls the first fact from the array
            String fact = root.get("data").get(0).asText();
            
            return fact;
        } catch (Exception ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return "Error in /random";
        }
    }


    @GetMapping("/multiple")
    public Object getMultipleCatFacts(@RequestParam(defaultValue = "5") int count) {
        try {
            String url = API_URL + "?count=" + count;
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(jsonResponse);

            // puts facts into a list
            List<CatFact> catFacts = new ArrayList<>();
            for (JsonNode factNode : root.get("data")) {
                catFacts.add(new CatFact(factNode.asText()));
            }

            return catFacts;
        } catch (Exception ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return "Error in /multiple";
        }
    }

}