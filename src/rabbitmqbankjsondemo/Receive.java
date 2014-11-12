package rabbitmqbankjsondemo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.util.Map;

/**
 *
 * @author Rick
 */
public class Receive {

    private static final String EXCHANGE_NAME = "cphbusiness.bankJSON";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");
        factory.setPort(5672);
        factory.setUsername("student");
        factory.setPassword("cph");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        
        System.out.println(".... Waiting for response ....");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            
            String exchange = delivery.getEnvelope().getExchange();
            System.out.println("Exchange: " + exchange);
            
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println("Routing key: " + routingKey);
            
            AMQP.BasicProperties properties = delivery.getProperties();
            System.out.println("ReplyTo: " + properties.getReplyTo());
            
            String json = new String(delivery.getBody());
            System.out.println("Received: " + json);
        }
    }

}
