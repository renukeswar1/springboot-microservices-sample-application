package com.example.taskmgmtkafkaconsumer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collation = "daily_task")
@AllArgsConstructor
@NoArgsConstructor
public class DailyTaskEntity {
    @Id
    private String id;
    private String title;
    private String status;
}
