package com.example.itemservice.service;

import com.example.itemservice.client.NominatimClient;
import com.example.itemservice.domain.City;
import com.example.itemservice.dto.response.NominatimResponse;
import com.example.itemservice.exceptions.notfound.CityNotFoundException;
import com.example.itemservice.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final NominatimClient nominatimClient;

    public City findCityByName(String cityName) {
        return cityRepository.findByName(cityName)
                .orElseGet(() -> {
                    List<NominatimResponse> results = nominatimClient.searchCity(cityName, "json", 1);
                    if (results.isEmpty()) {
                        throw new CityNotFoundException(cityName);
                    }
                    City city = createCity(results.getFirst());
                    return cityRepository.save(city);
                });
    }

    public City createCity(NominatimResponse nominatimResponse) {
        return City.builder()
                .name(nominatimResponse.getName())
                .latitude(nominatimResponse.getLat())
                .longitude(nominatimResponse.getLon())
                .build();
    }
}
