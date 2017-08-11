package br.edu.fiap.microservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.edu.fiap.microservice.bean.Filme;

@Singleton
@Lock(LockType.READ)
public class DataSourceRestEJB {
	
	@Resource
	private DataSource dbUnit;
	
	@PostConstruct
	private void construct() throws Exception{
		Connection cnx = dbUnit.getConnection();
		
		try {
			PreparedStatement ps = cnx.prepareStatement("CREATE TABLE Filme ("
					+"diretor VARCHAR(255), "
					+"titulo VARCHAR(255), "
					+"ano INTEGER)");
			ps.execute();
					
		} finally {
			cnx.close();
		}
		
	}
	
	@Path("/db/insert")
	public void addFilme(Filme filme) throws Exception {
		Connection cnx = dbUnit.getConnection();
		
		try {
			PreparedStatement ps = cnx.prepareStatement("INSERT INTO Filme(diretor, titulo, ano)"
					+ "VALUES(?,?,?)");
			ps.setString(1, filme.getDiretor());
			ps.setString(2, filme.getTitulo());
			ps.setInt(3, filme.getAno());
			ps.execute();
		} finally {
			cnx.close();
		}
	}
	
	@Path("/db/filme/{id}")
	@GET
	@Produces
	public String getFilmeTitulo(@PathParam("id") String id) throws Exception{
		Connection cnx = dbUnit.getConnection();
		
		List<Filme> filmes = new ArrayList<Filme>();
		
		try {
			PreparedStatement ps = cnx.prepareStatement("SELECT diretor, titulo, ano FROM Filme");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Filme filme = new Filme();
				filme.setDiretor(rs.getString("diretor"));
				filme.setTitulo(rs.getString("titulo"));
				filme.setAno(rs.getInt("ano"));
				filmes.add(filme);
			}
		} finally {
			cnx.close();
		}
		return filmes.get(Integer.parseInt(id)).getTitulo();
		
	}
	
}