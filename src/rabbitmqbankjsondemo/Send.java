package rabbitmqbankjsondemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author Rick
 */
public class Send {

    private static final String EXCHANGE_NAME = "cphbusiness.bankJSON";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");
        factory.setPort(5672);
        factory.setUsername("student");
        factory.setPassword("cph");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        
        String json = "{\"ssn\":1605789787,\"creditScore\":598,\"loanAmount\":10.0,\"loanDuration\":360}";
        
        channel.basicPublish(EXCHANGE_NAME, "", null, json.getBytes());
        System.out.println("Sent...... " + json);
        
        channel.close();
        connection.close();
    }

}
