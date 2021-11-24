package co.edu.eafit.events;

import co.edu.eafit.events.dto.*;
import co.edu.eafit.model.weather.*;

import java.util.stream.Collectors;

public class Converter {

    public static Weather toDomain(WeatherDTO weatherDTO) {
        return Weather.builder()
                .address(weatherDTO.getAddress())
                .currentConditions(toDomain(weatherDTO.getCurrentConditions()))
                .days(weatherDTO.getDays().stream().map(Converter::toDomain).collect(Collectors.toList()))
                .description(weatherDTO.getDescription())
                .stations(toDomain(weatherDTO.getStations()))
                .latitude(weatherDTO.getLatitude())
                .queryCost(weatherDTO.getQueryCost())
                .resolvedAddress(weatherDTO.getResolvedAddress())
                .timezone(weatherDTO.getTimezone())
                .longitude(weatherDTO.getLongitude())
                .tzoffset(weatherDTO.getTzoffset())
                .build();
    }

    private static Day toDomain(DayDTO dayDTO) {
        return Day.builder()
                .datetime(dayDTO.getDatetime())
                .datetimeEpoch(dayDTO.getDatetimeEpoch())
                .tempmax(dayDTO.getTempmax())
                .tempmin(dayDTO.getTempmin())
                .temp(dayDTO.getTemp())
                .feelslikemax(dayDTO.getFeelslikemax())
                .feelslikemin(dayDTO.getFeelslikemin())
                .feelslike(dayDTO.getFeelslike())
                .dew(dayDTO.getDew())
                .humidity(dayDTO.getHumidity())
                .precip(dayDTO.getPrecip())
                .precipprob(dayDTO.getPrecipprob())
                .preciptype(dayDTO.getPreciptype())
                .snow(dayDTO.getSnow())
                .snowdepth(dayDTO.getSnowdepth())
                .windgust(dayDTO.getWindgust())
                .windspeed(dayDTO.getWindspeed())
                .winddir(dayDTO.getWinddir())
                .pressure(dayDTO.getPressure())
                .cloudcover(dayDTO.getCloudcover())
                .visibility(dayDTO.getVisibility())
                .solarradiation(dayDTO.getSolarradiation())
                .solarenergy(dayDTO.getSolarenergy())
                .uvindex(dayDTO.getUvindex())
                .severerisk(dayDTO.getSevererisk())
                .sunrise(dayDTO.getSunrise())
                .sunriseEpoch(dayDTO.getSunriseEpoch())
                .sunset(dayDTO.getSunset())
                .sunsetEpoch(dayDTO.getSunsetEpoch())
                .moonphase(dayDTO.getMoonphase())
                .conditions(dayDTO.getConditions())
                .description(dayDTO.getDescription())
                .icon(dayDTO.getIcon())
                .stations(dayDTO.getStations())
                .source(dayDTO.getSource())
                .hours(dayDTO.getHours().stream().map(Converter::toDomain).collect(Collectors.toList()))
                .build();
    }

    private static Stations toDomain(StationsDTO stationsDTO) {
        return Stations.builder()
                .F0688(toDomain(stationsDTO.getF0688()))
                .SKMD(toDomain(stationsDTO.getSKMD()))
                .SKRG(toDomain(stationsDTO.getSKRG()))
                .build();
    }

    private static Hour toDomain(HourDTO hourDTO){
        return Hour.builder()
                .cloudcover(hourDTO.getCloudcover())
                .conditions(hourDTO.getConditions())
                .datetime(hourDTO.getDatetime())
                .datetimeEpoch(hourDTO.getDatetimeEpoch())
                .dew(hourDTO.getDew())
                .feelslike(hourDTO.getFeelslike())
                .humidity(hourDTO.getHumidity())
                .icon(hourDTO.getIcon())
                .precip(hourDTO.getPrecip())
                .precipprob(hourDTO.getPrecipprob())
                .preciptype(hourDTO.getPreciptype())
                .pressure(hourDTO.getPressure())
                .severerisk(hourDTO.getSevererisk())
                .snow(hourDTO.getSnow())
                .snowdepth(hourDTO.getSnowdepth())
                .solarenergy(hourDTO.getSolarenergy())
                .stations(hourDTO.getStations())
                .solarradiation(hourDTO.getSolarradiation())
                .source(hourDTO.getSource())
                .temp(hourDTO.getTemp())
                .uvindex(hourDTO.getUvindex())
                .visibility(hourDTO.getVisibility())
                .winddir(hourDTO.getWinddir())
                .windgust(hourDTO.getWindgust())
                .windspeed(hourDTO.getWindspeed())
                .build();
    }

    private static F0688 toDomain(F0688DTO f0688DTO) {
        return F0688.builder()
                .contribution(f0688DTO.getContribution())
                .id(f0688DTO.getId())
                .latitude(f0688DTO.getLatitude())
                .distance(f0688DTO.getDistance())
                .name(f0688DTO.getName())
                .quality(f0688DTO.getQuality())
                .longitude(f0688DTO.getLongitude())
                .useCount(f0688DTO.getUseCount())
                .build();
    }

    private static SKMD toDomain(SKMDDTO skmddto) {
        return SKMD.builder()
                .contribution(skmddto.getContribution())
                .id(skmddto.getId())
                .latitude(skmddto.getLatitude())
                .distance(skmddto.getDistance())
                .name(skmddto.getName())
                .quality(skmddto.getQuality())
                .longitude(skmddto.getLongitude())
                .useCount(skmddto.getUseCount())
                .build();
    }

    private static SKRG toDomain(SKRGDTO skrgdto) {
        return SKRG.builder()
                .contribution(skrgdto.getContribution())
                .id(skrgdto.getId())
                .latitude(skrgdto.getLatitude())
                .distance(skrgdto.getDistance())
                .name(skrgdto.getName())
                .quality(skrgdto.getQuality())
                .longitude(skrgdto.getLongitude())
                .useCount(skrgdto.getUseCount())
                .build();
    }

    private static CurrentCondition toDomain(CurrentConditionDTO currentConditionDTO) {
        return CurrentCondition.builder()
                .cloudcover(currentConditionDTO.getCloudcover())
                .conditions(currentConditionDTO.getConditions())
                .datetime(currentConditionDTO.getDatetime())
                .datetimeEpoch(currentConditionDTO.getDatetimeEpoch())
                .dew(currentConditionDTO.getDew())
                .feelslike(currentConditionDTO.getFeelslike())
                .humidity(currentConditionDTO.getHumidity())
                .icon(currentConditionDTO.getIcon())
                .moonphase(currentConditionDTO.getMoonphase())
                .precip(currentConditionDTO.getPrecip())
                .pressure(currentConditionDTO.getPressure())
                .snowdepth(currentConditionDTO.getSnowdepth())
                .stations(currentConditionDTO.getStations())
                .sunrise(currentConditionDTO.getSunrise())
                .sunriseEpoch(currentConditionDTO.getSunriseEpoch())
                .sunset(currentConditionDTO.getSunset())
                .sunsetEpoch(currentConditionDTO.getSunsetEpoch())
                .temp(currentConditionDTO.getTemp())
                .visibility(currentConditionDTO.getVisibility())
                .winddir(currentConditionDTO.getWinddir())
                .windgust(currentConditionDTO.getWindgust())
                .windspeed(currentConditionDTO.getWindspeed())
                .build();
    }
}
