package rabbitmqbankjsondemo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.util.UUID;

import static rabbitmqbankjsondemo.Constants.*;

/**
 *
 * @author Richard Haley III
 */
public class BankJSONClient {

    private static final String JSON = "{\"ssn\":1605789787,\"creditScore\":598,\"loanAmount\":10.0,\"loanDuration\":360}";

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rh11_rpc_queue";
    private String replyQueueName;
    private QueueingConsumer consumer;

    public BankJSONClient() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CONNECTION_HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish(JSON_BANK_EXCHANGE, requestQueueName, props, message.getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody(), "UTF-8");
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connection.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BankJSONClient client = null;
        String response = null;

        try {
            client = new BankJSONClient();

            System.out.println(" [X] Sending request: " + JSON);
            response = client.call(JSON);
            System.out.println(" [.] Got '" + response + "'");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
}
