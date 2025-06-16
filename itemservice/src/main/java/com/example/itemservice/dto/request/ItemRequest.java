package com.example.itemservice.dto.request;

import com.example.itemservice.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @NotBlank
    @Size(min = 2, max = 16)
    private String title;

    @NotBlank
    @Size(min = 40, max = 9000)
    private String description;

    @Size(max = 5)
    private List<String> imageUrls;

    @NotBlank
    private String cityName;

    private Double price;

    private Category category;
}
