package co.edu.eafit;

import co.edu.eafit.mongodb.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping(value = "/api")
public class WeatherApiController {

    @GetMapping("/weather/{location}")
    public String getWeather(@PathVariable String location) {
        return checkWeather(location);
    }

    public String checkWeather(String location) {
        return "{\n" +
                "    \"queryCost\": 1,\n" +
                "    \"latitude\": 6.24589,\n" +
                "    \"longitude\": -75.5746,\n" +
                "    \"resolvedAddress\": \"Medellín, Colombia\",\n" +
                "    \"address\": \"Medellin,ANT,CO\",\n" +
                "    \"timezone\": \"America/Bogota\",\n" +
                "    \"tzoffset\": -5.0,\n" +
                "    \"description\": \"Similar temperatures continuing with a chance of rain multiple days.\"\n"+
                "}";
    }
}
