
package br.message.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueConsumer {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
		Destination fila = (Destination) context.lookup("financeiro");
		MessageConsumer consumer = session.createConsumer(fila);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					System.out.println("Recebendo mensagem : ");
					System.out.println(textMessage.getText());
					session.commit();
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		
		
		new Scanner(System.in);
		
		session.close();
		connection.close();
		context.close();

	}

}
