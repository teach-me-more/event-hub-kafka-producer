package com.rbasystems.kafka.eventhub.event_hub_kafka_producer;

import com.rbasystems.kafka.eventhub.Employee;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;



@SpringBootApplication
@EnableKafka
public class EventHubKafkaProducerApplication {
	@Autowired
    private KafkaProperties kafkaProperties;
    @Value("${employee.topic_name}")
    private String topicName;
  
	public static void main(String[] args) {
		SpringApplication.run(EventHubKafkaProducerApplication.class, args);
	}



    // Producer configuration

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        // props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        //         StringSerializer.class);
        // props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        //         JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, Employee> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Employee> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic adviceTopic() {
        return new NewTopic(topicName, 3, (short) 1);
    }
}
