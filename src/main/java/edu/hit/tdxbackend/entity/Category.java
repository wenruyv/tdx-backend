package edu.hit.tdxbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;
    private String name;
    private String urlPath;
}
