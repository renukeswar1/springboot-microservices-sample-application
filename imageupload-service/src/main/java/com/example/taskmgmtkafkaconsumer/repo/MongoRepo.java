package com.example.taskmgmtkafkaconsumer.repo;


import com.example.taskmgmtkafkaconsumer.entity.DailyTaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRepo extends MongoRepository<DailyTaskEntity,Long> {
}
