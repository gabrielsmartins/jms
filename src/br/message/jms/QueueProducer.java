
package br.message.jms;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class QueueProducer {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("financeiro");
		QueueBrowser browser = session.createBrowser((Queue) fila);
		MessageProducer producer = session.createProducer(fila);
		
		for(int i=1; i <= 100; i++) {
			Message message = session.createTextMessage("<pedido><id>" + i + "</id></pedido>");
			producer.send(message);
		}
		
		Enumeration<?> msgs = browser.getEnumeration();
		while (msgs.hasMoreElements()) { 
		    TextMessage msg = (TextMessage) msgs.nextElement(); 
		    System.out.println("Mensagem: \n" + msg.getText()); 
		}
		
		session.close();
		connection.close();
		context.close();

	}

}
