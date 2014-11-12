package rabbitmqbankjsondemo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 *
 * @author Rick
 */
public class Receive {



    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.CONNECTION_HOST);
        factory.setPort(Constants.CONNECTION_PORT);
        factory.setUsername(Constants.CONNECTION_USERNAME);
        factory.setPassword(Constants.CONNECTION_PASSWORD);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(Constants.EXCHANGE_NAME, "fanout");
        
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, Constants.EXCHANGE_NAME, "");
        
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
