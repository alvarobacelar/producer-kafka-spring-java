package com.kafka.productor.controller;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
@RestController
public class EventStreamsController {
    @Autowired
    private KafkaTemplate<String, String> template;
    private List<String> messages = new CopyOnWriteArrayList<>();
    @KafkaListener(topics = "${listener.topic}", groupId = "channel1")
    public void listen(ConsumerRecord<String, String> record) throws Exception {
        messages.add(record.value());
    }
    @GetMapping(value = "/send/{msg}")
    public void send(String msg) throws Exception {
        this.template.send("topic", msg);
    }
    @GetMapping("/messages")
    public String received() throws Exception {
        String result = messages.toString();
        messages.clear();
        return result;
    }
}