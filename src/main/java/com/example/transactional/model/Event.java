package com.example.transactional.model;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "events")
@AllArgsConstructor
@Setter
public class Event {
    @Id
    private String id;
    private String message;
}
