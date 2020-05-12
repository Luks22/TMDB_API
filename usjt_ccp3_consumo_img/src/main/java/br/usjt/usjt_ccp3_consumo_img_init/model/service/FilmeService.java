package br.usjt.usjt_ccp3_consumo_img_init.model.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import br.usjt.usjt_ccp3_consumo_img_init.model.dao.FilmeDAO;
import br.usjt.usjt_ccp3_consumo_img_init.model.entity.Filme;
import br.usjt.usjt_ccp3_consumo_img_init.model.entity.Genero;
import br.usjt.usjt_ccp3_consumo_img_init.model.javabeans.Creditos;
import br.usjt.usjt_ccp3_consumo_img_init.model.javabeans.Crew;
import br.usjt.usjt_ccp3_consumo_img_init.model.javabeans.Movie;
import br.usjt.usjt_ccp3_consumo_img_init.model.javabeans.Populares;


@Service
public class FilmeService {

	public static final String BASE_URL = "https://api.themoviedb.org/3";
	public static final String POPULAR = "/movie/popular";
	public static final String POPULAR_PARAM = "&language=pt-BR";
	public static final String API_KEY = "api_key=f6333e90154e11cefba601b0430df44b";
	public static final String POSTER_URL = "https://image.tmdb.org/t/p/w300";
	public static final String UPCOMING = "/movie/upcoming";
	public static final String CREDITS_INIT = "/movie/";
	public static final String CREDITS_FINAL = "/credits"; 
	

	@Autowired
	private FilmeDAO dao;
	

	public Filme buscarFilme(int id) throws IOException {
		return dao.buscarFilme(id);
	}

	@Transactional
	public Filme inserirFilme(Filme filme) throws IOException {
		int id = dao.inserirFilme(filme);
		filme.setId(id);
		return filme;
	}

	@Transactional
	public void atualizarFilme(Filme filme) throws IOException {
		dao.atualizarFilme(filme);
	}

	@Transactional
	public void excluirFilme(int id) throws IOException {
		dao.excluirFilme(id);
	}

	public List<Filme> listarFilmes(String chave) throws IOException {
		return dao.listarFilmes(chave);
	}

	public List<Filme> listarFilmes() throws IOException {
		return dao.listarFilmes();
	}

	@Transactional
	public List<Filme> baixarFilmesMaisPopulares() throws IOException {
		RestTemplate rest = new RestTemplate();
		String url = BASE_URL + POPULAR + "?" + API_KEY + POPULAR_PARAM;
		Populares resultado = rest.getForObject(url, Populares.class);

		for (Movie movie : resultado.getResults()) {
			Filme filme = new Filme();
			filme.setFilme_id(movie.getId());
			filme.setDataLancamento(movie.getRelease_date());
			filme.setDescricao(movie.getOverview());
			filme.setPopularidade(movie.getPopularity());
			filme.setPosterPath(POSTER_URL + movie.getPoster_path());
			filme.setTitulo(movie.getTitle());

			Genero genero = new Genero();
			if(movie.getGenre_ids().length <= 0) {
				genero.setId(1);
			}else {
				genero.setId(movie.getGenre_ids()[0]);
			}
			

			filme.setGenero(genero);

			if (dao.buscarFilmePorTituloEData(filme)) {
				dao.inserirFilme(filme);
			}

		}

		return dao.listarFilmes();

	}

	@Transactional
	public List<Filme> baixarLancamentos() throws IOException {
		RestTemplate rest = new RestTemplate();
		String url = BASE_URL + UPCOMING + "?" + API_KEY + POPULAR_PARAM;
		Populares resultado = rest.getForObject(url, Populares.class);

		for (Movie movie : resultado.getResults()) {
			Filme filme = new Filme();
			filme.setFilme_id(movie.getId());
			filme.setDataLancamento(movie.getRelease_date());
			filme.setDescricao(movie.getOverview());
			filme.setPopularidade(movie.getPopularity());
			filme.setPosterPath(POSTER_URL + movie.getPoster_path());
			filme.setTitulo(movie.getTitle());

			Genero genero = new Genero();
			if(movie.getGenre_ids().length <= 0) {
				genero.setId(1);
			}else {
				genero.setId(movie.getGenre_ids()[0]);
			}
			

			filme.setGenero(genero);

			if (dao.buscarFilmePorTituloEData(filme)) {
				dao.inserirFilme(filme);
			}

		}

		return dao.listarFilmes();

	}
	
	@Transactional
	public void atualizaDiretor(Filme filme) throws IOException {
		RestTemplate rest = new RestTemplate();
		String url = BASE_URL + CREDITS_INIT + filme.getFilme_id() + CREDITS_FINAL + "?" + API_KEY;
		Creditos resultado = rest.getForObject(url, Creditos.class);
		for(Crew c : resultado.getCrew()) {
			if(c.getJob().equals("Director")) {
				filme.setDiretor(c.getName());
				dao.atualizarFilme(filme);
			}
		}

		
		
		
	}
	
	@Transactional
	public void insereImagem(ServletContext servletContext, Filme filme, MultipartFile file) throws IOException {
		if(!file.isEmpty()) {
			BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
			String path = servletContext.getRealPath(servletContext.getContextPath());
			path = path.substring(0, path.lastIndexOf(File.separatorChar));
			String nomeArquivo = "img"+filme.getId()+".jpg";
			filme.setPosterPath(nomeArquivo);
			filme.setImagem(nomeArquivo);
			dao.atualizarFilme(filme);
			File destination = new File(path + File.separatorChar + "img" + File.separatorChar + nomeArquivo);
			if(destination.exists()) {
				destination.delete();
			}
			
			ImageIO.write(src, "jpg", destination);
		}
	}
}










