package rabbitmqbankjsondemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 *
 * @author Rick
 */
public class Receive {

    private static final String QUEUE_NAME = "rh11_test01";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");
        factory.setPort(5672);
        factory.setUsername("student");
        factory.setPassword("cph");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare("cphbusiness.bankJSON", "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "cphbusiness.bankJSON", "");
        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(".... Waiting for response ....");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String json = new String(delivery.getBody());
            System.out.println("Received: " + json);
        }
    }

}
