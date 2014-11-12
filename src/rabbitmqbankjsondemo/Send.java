package rabbitmqbankjsondemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author Rick
 */
public class Send {

    private static final String JSON = "{\"ssn\":1605789787,\"creditScore\":598,\"loanAmount\":10.0,\"loanDuration\":360}";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.CONNECTION_HOST);
        factory.setPort(Constants.CONNECTION_PORT);
        factory.setUsername(Constants.CONNECTION_USERNAME);
        factory.setPassword(Constants.CONNECTION_PASSWORD);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Constants.EXCHANGE_NAME, "fanout");

        channel.basicPublish(Constants.EXCHANGE_NAME, "", null, JSON.getBytes());
        System.out.println("Sent...... " + JSON);

        channel.close();
        connection.close();
    }

}
