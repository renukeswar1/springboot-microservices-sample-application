package com.example.taskmgmtkafkaconsumer.consumer;

import com.example.taskmanagement.avro.DailyTask;
import com.example.taskmgmtkafkaconsumer.entity.DailyTaskEntity;
import com.example.taskmgmtkafkaconsumer.repo.MongoRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaskConsumerService {

    private final MongoRepo mongoRepo;

    public TaskConsumerService(MongoRepo mongoRepo) {
        this.mongoRepo = mongoRepo;
    }


    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, DailyTask> record) {
        DailyTask dailyTask = record.value();
        DailyTaskEntity dailyTaskEntity = new DailyTaskEntity();
        dailyTaskEntity.setTitle(dailyTask.getTitle().toString());
        dailyTaskEntity.setStatus(dailyTask.getStatus().toString());

        mongoRepo.save(dailyTaskEntity);
        System.out.println(dailyTask.getTitle());
    }
}
