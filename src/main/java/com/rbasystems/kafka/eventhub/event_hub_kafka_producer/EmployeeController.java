package com.rbasystems.kafka.eventhub.event_hub_kafka_producer;

import com.rbasystems.kafka.eventhub.Employee;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private KafkaTemplate<String, Employee> kafkaTemplate;
    
    @Value("${employee.topic_name}")
    private String topicName;

    Logger LOGGER = Logger.getLogger("EmployeeController");
    @PostMapping
    public String createEmployeeEvent(@RequestBody Employee employee){
        LOGGER.info("Recived an employee record to create a new employee");
        String employeeId = UUID.randomUUID().toString();
        employee.setEmployeeId(employeeId);
        sendEvent(employee);
        LOGGER.info("Employee event is published with employee id = "+employeeId);
        return employeeId;
    }


    @PutMapping("/{id}")
    public void updateEmployeeEvent(@PathVariable String id , @RequestBody Employee employee){
        LOGGER.info("Recived an employee record to update an existing employee "+employee.getEmployeeId());
        sendEvent(employee);
        LOGGER.info("Employee event is published after update for employee id = "+employee.getEmployeeId());
        return ;
    }
    private void sendEvent(Employee employee){
        kafkaTemplate.send(topicName, employee.getEmployeeId(),employee);
    }
}
