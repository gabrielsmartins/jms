
package br.message.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.message.jms.model.Pedido;

public class TopicConsumerEstoqueObject {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws NamingException, JMSException {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection();
		connection.setClientID("estoque");
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topico = (Topic) context.lookup("loja");
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-estoque");
		
		System.out.println("*** Estoque ***");
		
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					ObjectMessage objectMessage = (ObjectMessage) message;
					Pedido pedido = (Pedido) objectMessage.getObject();
					System.out.println("Recebendo mensagem : ");
					System.out.println(pedido);
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
