package org.acme.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpendingCategoryDTO {
    private Long id;
    private String name;
    private Long iconId;
    private String iconName;
}
