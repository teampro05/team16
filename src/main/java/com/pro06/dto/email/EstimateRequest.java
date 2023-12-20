package com.pro06.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//여행 견적 요청서 항목
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstimateRequest {
    private String name;
    private String phone;
    private String email;
    private String travelType;
    private String vehicleType;
    private String vehicleNumber;
    private String departPlace;
    private String arrivalPlace;
    private String stopPlace;
    private String departDate;
    private String arrivalDate;
}
