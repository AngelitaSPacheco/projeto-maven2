package services;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Aluno;
import model.Endereco;
import repository.AlunoRepository;
import repository.EnderecoRepository;

@Path("/aluno")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlunoService {

	@Inject
	private AlunoRepository alunoRepository;
	
	@Inject
	private EnderecoRepository enderecoRepository;
	
	@POST
	public Response cadastrarAluno(Aluno novo) {
		
		Aluno existente = alunoRepository.consultar(novo.getNome());
		if (existente != null) {
			return Response.ok().entity(-1).build();
		}
		
		Endereco endereco = enderecoRepository.consultar(novo.getEndereco().getCep());
		if (endereco == null) {
			enderecoRepository.cadastrar(novo.getEndereco());
		}
		
		novo.setEndereco(endereco);
		
		return Response.ok().entity(alunoRepository.cadastrar(novo)).build();
	}

	
	@PUT
	public Response atualizarAluno(Aluno aluno) {
	
		Aluno existente = alunoRepository.consultar(aluno.getId());
		if (existente == null) {
			
			return Response.ok().entity(null).build();
		}
		
		Endereco endereco = enderecoRepository.consultar(aluno.getEndereco().getCep());
		if (endereco == null) {
		
			enderecoRepository.cadastrar(aluno.getEndereco());
		}
	
		aluno.setEndereco(endereco);
		return Response.ok().entity(alunoRepository.atualizar(aluno)).build();
	}

	@Path("/{alunoId}")
	@DELETE
	public Response removerAluno(@PathParam("alunoId") int alunoId) {
		
		Aluno aluno = alunoRepository.consultar(alunoId);
		if (aluno == null) {
			
			return Response.ok().entity(false).build();
		}
		
		try {
			alunoRepository.remover(aluno);
			
			return Response.ok().entity(true).build();
		} catch (Exception e) {
			
			return Response.ok().entity(false).build();
		}
	}

	@Path("/nome/{nome}")
	@GET
	public Response consultarAluno(@PathParam("nome") String nome) {
		
		Aluno aluno = alunoRepository.consultar(nome);
		if (aluno == null) {
			
			return Response.ok().entity(null).build();
		}
		
		return Response.ok().entity(aluno).build();
	}
}
