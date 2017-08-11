package br.edu.fiap.microservice.app;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.DataSource;

import br.edu.fiap.microservice.DataSourceRestEJB;
import br.edu.fiap.microservice.bean.Filme;





public class Application {

	private static Context context;
	private static DataSourceRestEJB restEJB;
	private static EJBContainer container;
	
	public static void main(String[] args) throws Exception {
		
		Properties propContainer = new Properties();
		
		//OpenEJB
		propContainer.put("httpejbd", "cxf-rs");
		propContainer.put("openejb.embedded.remotable", "true");
		
		//Datasource
		propContainer.put(context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContainerFactoy");
		
		//Criar Conexao
		propContainer.put("test", "new://Resource?type=DataSource");
		propContainer.put("test.JdbcDriver", "org.hsqldb.JdbcDriver");
		propContainer.put("test.JdbcUrl", "jdbc:hsqldb.filme");
		
		//Persistencia
		propContainer.put("test.hibernate.dialect", "or.hibernate.dialect.HSQLDialect");
		
		container = EJBContainer.createEJBContainer(propContainer);
		
		context = container.getContext();
		Object object = context.lookup("java:global/OpenEJB/DataSourceRestEJB");
		restEJB = (DataSourceRestEJB) object;
		
		Filme filme = new Filme();
		filme.setTitulo("Matrix");
		
		restEJB.addFilme(filme);
		
		String filmeDBRest = (String) WebClient.create("http://localhost:4204").path("/OpenEJB/");
		
		
		
		

	}
	

}
