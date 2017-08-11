package br.edu.fiap.microservice.app;

import org.apache.openejb.jee.jpa.unit.Persistence;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.testing.Module;

import br.edu.fiap.microservice.bean.Filme;

public class Modules {
	@Module // a persistence.xml
	public Persistence persistence() {
	   final PersistenceUnit unit = new PersistenceUnit("dbUnit");
	   unit.addClass(Filme.class);
	   unit.setProperty("openjpa.RuntimeUnenhancedClasses", "supported");
	   unit.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
	   unit.setExcludeUnlistedClasses(true);
	 
	   final Persistence persistence = new Persistence(unit);
	   persistence.setVersion("2.0");
	   return persistence;
	}
}
