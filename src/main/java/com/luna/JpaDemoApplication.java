package com.luna;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.luna.modelo.Categoria;
import com.luna.modelo.Perfil;
import com.luna.modelo.Usuario;
import com.luna.modelo.Vacante;
import com.luna.repositorio.CategoriasRepository;
import com.luna.repositorio.PerfilesRepository;
import com.luna.repositorio.UsuariosRepository;
import com.luna.repositorio.VacantesRepository;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner {

	@Autowired
	private CategoriasRepository repoCategorias;
	
	@Autowired
	private VacantesRepository   repoVacantes;
	
	@Autowired
	private UsuariosRepository repoUsuarios;
	
	@Autowired
	private PerfilesRepository   repoPerfiles;
	
	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		
		buscarVacantesVariosEstatus();
		
	}
	
	public void buscarVacantesVariosEstatus() {
		
		String[] estatus= new String[]{"aprobada","eliminada"};
		
		List<Vacante> lista= repoVacantes.findByEstatusIn(estatus);
		System.out.println("Registros encontrados "+lista.size());
		for(Vacante v:lista) {
			
			System.out.println(v.getId()+" : "+v.getNombre()+" : "+v.getEstatus());	
		}
		
	}
	
	
	public void buscarVacantesPorSalario() {
		List<Vacante> lista= repoVacantes.findBySalarioBetweenOrderBySalarioDesc(7000.0, 14000.0);
		System.out.println("Registros encontrados "+lista.size());
		for(Vacante v:lista) {
			
			System.out.println(v.getId()+" "+v.getNombre()+" : $"+v.getSalario());	
		}
		
	}
	
	public void buscarVacantesDestacadosEstatus() {
		List<Vacante> lista= repoVacantes.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
		System.out.println("Registros encontrados "+lista.size());
		for(Vacante v:lista) {
			
			System.out.println(v.getId()+" "+v.getNombre());	
		}
		
	}
	
	public void buscarVacantesPorEstatus() {
		
		List<Vacante> lista= repoVacantes.findByEstatus("Aprobada");
		System.out.println("Registros encontrados "+lista.size());
		for(Vacante v:lista) {
			
			System.out.println(v.getId()+" "+v.getNombre());	
		}
		
	}
	
	public void buscarUsuario() {
		
		Optional<Usuario> optional= repoUsuarios.findById(1);
		
		if(optional.isPresent()) {
			Usuario u= optional.get();
			System.out.println("Nombre :"+u.getNombre());
			System.out.println("Perfiles asignados :");
			for(Perfil p:u.getPerfiles()) {
				
				System.out.println(p.getPerfil());
			}
			
			
		}
		else
			System.out.println("Usuario no encontrado");
		
		
		
	}
	
	
	public void crearUsuarioPerfil() {
		Usuario user= new Usuario();
		
		user.setNombre("Hector Luna");
		user.setEmail("bizarro@gmail.com");
		user.setFechaRegistro(new Date());
		user.setUsername("erickeane");
		user.setPassword("12345");
		user.setEstatus(1);
		
		Perfil p1= new Perfil();
		p1.setId(2);
		Perfil p2=new Perfil();
		p2.setId(3);
		
		user.agregar(p1);
		user.agregar(p2);
		
		repoUsuarios.save(user);
		
	}
	
	public void crearPerfilesAplicacion() {
		
		repoPerfiles.saveAll(getPerfiles());
	}
	
	public void guardarVacante() {
		Vacante v= new Vacante();
		v.setNombre("Profesor de matematicas");
		v.setDescripcion("Para escuela de nivel primaria");
		v.setFecha(new Date());
		v.setSalario(8500.0);
		v.setEstatus("Aprobada");
		v.setDestacado(0);
		v.setImagen("escuela.png");
		v.setDetalle("<h1> Los requisitos para profesor de matematicas     </h1>");
		
		Categoria c = new Categoria();
		c.setId(15);
		v.setCategoria(c);
		repoVacantes.save(v);
		
	}
	
	
	private void buscarVacantes() {
		
		List<Vacante> listaVacante=repoVacantes.findAll();
		
		for(Vacante v:listaVacante) {
			
			System.out.println("Vacante: "+v.getId()+" "+v.getNombre()+", Categoria: "+v.getCategoria().getNombre());
		}
	}
	
	
	private void buscarTodosPaginacionOrdenados() {
		
		Pageable pageable = PageRequest.of(0, 5,Sort.by("nombre").descending());
		Page<Categoria> page= repoCategorias.findAll(pageable);
	
		System.out.println("Total registros "+ page.getTotalElements());
		System.out.println("Total Paginas "+ page.getTotalPages());
		for(Categoria c:page.getContent()){
					
					System.out.println(c.getId() +" "+ c.getNombre());
					
				}
		
		
		
		
	}
	
	
	
	
	private void buscarTodosPaginacion(){
		
		
		Page<Categoria> page= repoCategorias.findAll(PageRequest.of(0, 5));
		System.out.println("Total registros "+ page.getTotalElements());
		System.out.println("Total Paginas "+ page.getTotalPages());
		for(Categoria c:page.getContent()){
					
					System.out.println(c.getId() +" "+ c.getNombre());
					
				}
		
		
	}
	
	
	private void obtenerTodosOrdenados() {
		                                             //Nombre de la propiedad declarada en el modelo
		List<Categoria> lista= repoCategorias.findAll(Sort.by("nombre").descending());
		for(Categoria c:lista) {
			
			System.out.println(c.getId()+" "+ c.getNombre());
			
		}
		
	}
	
	private void borrarTodoEnBloque() {
		repoCategorias.deleteAllInBatch();
	}
	
	
	private void buscarTodosJpa() {
		
		List<Categoria> lista= repoCategorias.findAll();
		for(Categoria c:lista) {
			
			System.out.println(c.getId()+" "+ c.getNombre());
			
		}
	}
	
	
	private void guardar() {
		Categoria cat= new Categoria();
		cat.setNombre("Finanzas");
		cat.setDescripcion("Trabajo relacionado con finanzas de contabilidad");
		
		repoCategorias.save(cat);
		
		System.out.println(cat);
	}
	
	private void buscarPorId(int id) {
		
		Optional<Categoria> ca= repoCategorias.findById(id);
		
		if(ca.isPresent()) {
			
			System.out.println(ca.get().toString());
		}else {
			System.out.println("No se encontro el objeto");
		}
		
		
	}
	
	/**
	 * Metodo para actualizar 
	 */
	private void actualizar(int id) {
		Optional<Categoria> ca= repoCategorias.findById(id);
		
		if(ca.isPresent()) {
			Categoria cActualizar= ca.get();
			cActualizar.setNombre("Ingenieria de Sotfware");
			cActualizar.setDescripcion("Desarollo de sistemas");
			repoCategorias.save(cActualizar);
			
			System.out.println(ca.get().toString());
		}else {
			System.out.println("No se encontro el objeto");
		}
		
		
	}
	
	private void eliminar(int id) {
		repoCategorias.deleteById(id);
	}
	
	private void conteo() {
		long contador=repoCategorias.count();
		System.out.println("Numero de registros de la tabla categoria "+contador);
		
	}
	
	private void eliminarTodos() {
		repoCategorias.deleteAll();
	}
	
	private void encontrarPorIds() {
		List<Integer>  ids= new ArrayList<>();
		ids.add(1);
		ids.add(4);
		ids.add(10);
		
		Iterable<Categoria> categorias= repoCategorias.findAllById(ids);
		
		for(Categoria c:categorias) {
			
			System.out.println(c.toString());
		}
		
	}
	
	private void buscarTodos() {
		
		Iterable<Categoria> categorias= repoCategorias.findAll();
		for(Categoria c:categorias) {
			
			System.out.println(c.toString());
		}
	}
	
	private void verificar(int id) {
		
		if(repoCategorias.existsById(id)) {
			System.out.println("La categoria existe");
		}
		else
			System.out.println("El elemento no existe");
		
	}
	
	private void guardarTodos() {
		
		repoCategorias.saveAll(getListaCategorias());
	}
	
	
	private List<Categoria> getListaCategorias(){
		List<Categoria> lista= new LinkedList<>();
		
		Categoria c1= new Categoria();
		Categoria c2= new Categoria();
		Categoria c3= new Categoria();
		
		c1.setNombre("Trabajador de BolckChain");
		c1.setDescripcion("Trabajos relacionados con Bit coin");
		
		c2.setNombre("Soldadura/Pintura");
		c2.setDescripcion("Trabajos relacionados con soldadura y pintura");

		c3.setNombre("Ingeriero industrial");
		c3.setDescripcion("Trabajos relacionados con ingenieria industrial");

		lista.add(c1);
		lista.add(c2);
		lista.add(c3);
		
		return lista;
		
	}
	
	private List<Perfil> getPerfiles(){
		
		List<Perfil> perfiles= new LinkedList<>();
		
		Perfil p1= new Perfil();
		Perfil p2= new Perfil();
		Perfil p3= new Perfil();
		
		p1.setPerfil("SUPERVISOR");
		p2.setPerfil("ADMINISTRADOR");
		p3.setPerfil("USUARIO");
		
		
		perfiles.add(p1);
		perfiles.add(p2);
		perfiles.add(p3);
		
		return perfiles;
	}
	

}
