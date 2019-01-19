
package br.message.jms;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;

import br.message.jms.model.Pedido;
import br.message.jms.model.PedidoFactory;

public class TopicProducer {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

		Connection connection = factory.createConnection();
		connection.start();

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination topico = (Destination) context.lookup("loja");

		MessageProducer producer = session.createProducer(topico);

		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		StringWriter writer=  new StringWriter();
		JAXB.marshal(pedido, writer );
		String xml = writer.toString();
		System.out.println(xml);
		Message message = session.createTextMessage(xml);
		message.setBooleanProperty("ebook", false);
		producer.send(message);

		System.out.println("Mensagem enviada com sucesso");

		session.close();
		connection.close();
		context.close();

	}

}
