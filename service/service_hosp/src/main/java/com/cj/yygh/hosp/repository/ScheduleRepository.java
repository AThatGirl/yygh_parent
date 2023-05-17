package com.cj.yygh.hosp.repository;

import com.cj.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule findByHoscodeAndHosScheduleId(String hoscode, String hoscode1);
}