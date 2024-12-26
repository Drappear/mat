package com.example.mat.dto.diner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DinerCategoryDto {
  
  private Long dcid;

  private String name;
  
}
