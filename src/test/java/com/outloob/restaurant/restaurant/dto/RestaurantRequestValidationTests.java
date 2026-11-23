package com.outloob.restaurant.restaurant.dto;

import com.outloob.restaurant.restaurant.entity.Address;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantRequestValidationTests {

    @Test
    void openingHoursAllowsClearingButRejectsMissingAndInvalidEntries() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            assertThat(validator.validate(new UpdateOpeningHoursRequest(List.of()))).isEmpty();
            assertThat(validator.validate(new UpdateOpeningHoursRequest(Collections.singletonList(null))))
                    .anyMatch(violation -> violation.getPropertyPath().toString().startsWith("openingHours[0]"));

            var invalidHours = new OpeningHoursDto(8, LocalTime.of(9, 0), LocalTime.of(17, 0));
            assertThat(validator.validate(new UpdateOpeningHoursRequest(List.of(invalidHours))))
                    .anyMatch(violation -> violation.getPropertyPath().toString().equals("openingHours[0].dayOfWeek"));
        }
    }

    @Test
    void profilePhoneRequiresADigitAndKeepsExistingFormatLimits() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            for (String phone : List.of("+(---) --", "123456", "+" + "1".repeat(21), "123-ABC", "12+3456")) {
                assertThat(validator.validate(profileWithPhone(phone)))
                        .as("invalid phone: %s", phone)
                        .anyMatch(violation -> violation.getPropertyPath().toString().equals("phoneNumber"));
            }
            for (String phone : List.of("+1 (555) 123-4567", "1234567", "+" + "1".repeat(20))) {
                assertThat(validator.validate(profileWithPhone(phone))).as("valid phone: %s", phone).isEmpty();
            }
        }
    }

    private static UpdateRestaurantProfileRequest profileWithPhone(String phone) {
        return new UpdateRestaurantProfileRequest("Restaurant", null, "restaurant@example.com", phone,
                0, 0, 100, new Address("Main Street", "City", null, "Country"));
    }
}
