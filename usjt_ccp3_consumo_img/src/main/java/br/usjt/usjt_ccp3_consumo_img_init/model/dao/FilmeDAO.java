package br.usjt.usjt_ccp3_consumo_img_init.model.dao;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import br.usjt.usjt_ccp3_consumo_img_init.model.entity.Filme;
@Repository
public class FilmeDAO {
	@PersistenceContext 
	EntityManager manager;
	
	public int inserirFilme(Filme filme) throws IOException {
		manager.persist(filme);
		return filme.getId();
	}

	public Filme buscarFilme(int id) throws IOException{
		return manager.find(Filme.class, id);
	}
	
	public void atualizarFilme(Filme filme) throws IOException{
		manager.merge(filme);
	}
	
	public void excluirFilme(int id) throws IOException{
		manager.remove(manager.find(Filme.class, id));
	}

	@SuppressWarnings("unchecked")
	public List<Filme> listarFilmes(String chave) throws IOException {
		Query query = manager.createQuery("select f from Filme f where f.titulo like :chave");
		query.setParameter("chave", "%"+chave+"%");
		return query.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Filme> listarFilmes() throws IOException {
		return manager.createQuery("select f from Filme f").getResultList();
	}
	
	public boolean buscarFilmePorTituloEData(Filme filme) {
		Query query = manager.createQuery("select f from Filme f");
		
		List<Filme> filmes = query.getResultList();
		
		for(Filme f : filmes) {

			Date cal1 = filme.getDataLancamento();
			Calendar cal2 = Calendar.getInstance();
			
			cal2.setTime(f.getDataLancamento());
			
			String ano = cal2.getTime().toString().substring(24, 28);
			String data = cal2.getTime().toString().substring(0, 11).concat(ano);

			String ano2 = cal1.toString().substring(24, 28);
			String data2 = cal1.toString().substring(0, 11).concat(ano2);
			

			if(f.getTitulo().equals(filme.getTitulo()) && data.equals(data2)) {
				
				return false;
			}
		}
		return true;
		
	}

}
