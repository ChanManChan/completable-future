package com.u4.domain.cars;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("car")
    private String car;
    @JsonProperty("car_model")
    private String carModel;
    @JsonProperty("car_color")
    private String carColor;
    @JsonProperty("car_model_year")
    private Integer carModelYear;
    @JsonProperty("car_vin")
    private String carVin;
    @JsonProperty("price")
    private String price;
    @JsonProperty("availability")
    private Boolean availability;
}
