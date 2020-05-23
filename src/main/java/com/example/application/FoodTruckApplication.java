package com.example.application;

import com.example.model.Resource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * The FoodTruckApplication prints out a list of food trucks that are open at the current date and current time
 */
public class FoodTruckApplication {

    private Logger logger = LoggerFactory.getLogger(FoodTruckApplication.class);

    public static void main(String[] args) {
        FoodTruckApplication foodTruckApplication = new FoodTruckApplication();
        try {
            foodTruckApplication.getCurrentDateTimeAndFetchFoodTrucks();
        } catch (JsonProcessingException | JSONException ex) {
            System.out.println("There is an error occurred while parsing data");
        }
    }

    /**
     * It fetches the current date and time from system and extracts day of week and time(hours and minutes). it
     * call fetchFoodTrucks method to get food truck details
     *
     * @throws JsonProcessingException
     * @throws JSONException
     */
    private void getCurrentDateTimeAndFetchFoodTrucks() throws JsonProcessingException, JSONException {

        logger.info("getCurrentDateTimeAndFetchFoodTrucks method started execution");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm a");
        Date date = Calendar.getInstance().getTime();
        String dateAndTime = dateFormat.format(date.getTime());
        String[] dateAndTimeArr = dateAndTime.split(" ");
        String[] dateArr = dateAndTimeArr[0].split("/");
        LocalDate localDate = LocalDate.of(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]),
                Integer.parseInt(dateArr[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        fetchFoodTrucks(dateAndTimeArr[1], dayOfWeek.getValue());
    }

    /**
     * It pulls the food truck information based on offset and current time. It continues to fetch the results based
     * on user's input. It returns only 10 records at a time.
     *
     * @param currTime
     * @param dayOrder
     *
     * @throws JsonProcessingException
     * @throws JSONException
     */
    public void fetchFoodTrucks(String currTime, int dayOrder) throws JsonProcessingException, JSONException {

        logger.info("fetchFoodTrucks method started execution");

        List<Resource> resourceList = getResources(currTime, dayOrder, 0);
        if (resourceList.size() == 0) {
            return;
        }
        int offset = 0;
        int limit = 10;
        int listOffset = 0;
        char input;
        int listSize = resourceList.size();
        Scanner scanner = new Scanner(System.in);
        do {
            List<Resource> output;
            if (listSize == 0) {
                return;
            }
            if (listSize <= 10) {
                output = resourceList.subList(listOffset, listOffset + listSize);
                listSize = 0;
            } else {
                output = resourceList.subList(listOffset, listOffset + limit);
            }
            listOffset = listOffset + limit;
            listSize = (listSize >= limit) ? (listSize - limit) : listSize;
            printTheResources(output);
            System.out.println("Press $ to continue seeing other food trucks");
            input = scanner.next().trim().charAt(0);
            if (input == '$' && listSize == 0 && resourceList.size() == 40) {
                resourceList = getResources(currTime, dayOrder, ++offset);
                listSize = resourceList.size();
                listOffset = 0;
                if (resourceList.size() == 0) {
                    System.out.println("Sorry, You have seen all the food trucks that are open..!");
                    return;
                }
            }
        } while (input == '$');

        scanner.close();
        logger.info("fetchFoodTrucks method ended execution");
    }

    /**
     * It prints the resources in console.
     *
     * @param output
     */
    private void printTheResources(List<Resource> output) {
        logger.info("printTheResources method started execution");
        Collections.sort(output, Comparator.comparing(Resource::getApplicant));
        String format = "%-40s%s%n";
        System.out.printf(format, "Name", "Address");
        output.forEach(resource -> System.out.format(format, resource.getApplicant(), resource.getLocation()));
        logger.info("printTheResources method ended execution");
    }

    /**
     * This is the main entry point for performing web requests.it fetches only 40 records in a call.so that it will
     * be fast. it also converts string data to custom Resource object.
     *
     * @param currTime
     * @param dayOrder
     * @param offset
     *
     * @return
     *
     * @throws JSONException
     * @throws JsonProcessingException
     */
    private List<Resource> getResources(String currTime, int dayOrder, int offset)
            throws JSONException, JsonProcessingException {

        logger.info("getResources method started execution");
        WebClient webClient = WebClient.create(
                "https://data.sfgov.org/resource/jjew-r69b.json?$where=start24 <= '" + currTime + "' and end24 >= "
                        + "'" + currTime + "'&dayorder=" + dayOrder + "&$limit=40&$offset=" + offset);
        System.out.println(offset + "---");
        Mono<ClientResponse> result = webClient.get().exchange();
        String data = result.flatMap(res -> res.bodyToMono(String.class)).block();
        List<Resource> resourceList = new ArrayList<>();
        JSONArray jsonArr = new JSONArray(data);
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            resourceList.add(new ObjectMapper().readValue(jsonObj.toString(), Resource.class));
        }
        logger.info("getResources method ended execution");
        return resourceList;
    }
}
