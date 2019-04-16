package com.kafka.productor;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductorApplication {

	private static String kafka_servers = System.getenv("KAFKA_SERVER");
	private static String kafka_topic = System.getenv("KAFKA_TOPIC");
	private static String kafka_count_msg = System.getenv("KAFKA_COUNT_MSG");
	private static String kafka_msg = System.getenv("KAFKA_MSG");

	private static String mensagem = null;

	private final static String TOPIC = kafka_topic;
	private final static String BOOTSTRAP_SERVERS = kafka_servers;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ProductorApplication.class, args);

		if (args.length == 0) {
			int num_msg_kafka = Integer.parseInt(kafka_count_msg);
			runProducer(num_msg_kafka);
		} else {
			runProducer(Integer.parseInt(args[0]));
		}

	}

	private static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}

	static void runProducer(final int sendMessageCount) throws Exception {
		final Producer<Long, String> producer = createProducer();
		long time = System.currentTimeMillis();
		if (kafka_msg != null) {
			mensagem = kafka_msg;
		} else {
			mensagem = "Mensagem enviada para o kafka com hash:";
		}
		try {
			for (long index = time; index < time + sendMessageCount; index++) {
				final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, mensagem + " " + index);
				RecordMetadata metadata = producer.send(record).get();
				long elapsedTime = System.currentTimeMillis() - time;
				System.out.printf("-------------- message productor ----------- \n");
				System.out.println("Record Key: " + record.key());
				System.out.println("Record value: " + record.value());
				System.out.println("Record partition: " + metadata.partition());
				System.out.println("Record Time: " + elapsedTime);
				System.out.println("Record offset: " + metadata.offset());
				System.out.println("Server Kafka: " + kafka_servers);
				System.out.printf("-------------------------------------------- \n");
			}
		} finally {
			producer.flush();
			producer.close();
		}
	}
}
