package br.com.thiago.test;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.thiago.servicos.Servicos;
import br.com.thiago.test.entidate.PessoaRequest;
import br.com.thiago.test.entidate.PessoaResponse;
import br.com.thiago.test.entidate.PessoaUpdate;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

public class ReqResExample {

	@Before
	public void configuraApi() {
		baseURI = 	"https://reqres.in/";

	}
	
	@Test
	public void methodGet() {
		basePath= "/api/users";
		given()
			.when()
				.get(Servicos.getUsers_ID.getValor(), 2)
			.then().contentType(ContentType.JSON)
				.statusCode(HttpStatus.SC_OK)
				.and()
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("Schemas/barbaExample.json"))
				.log().all();
	}

	@Test
	public void methodPost() {
		PessoaRequest pessoaRequest = new PessoaRequest("barba", "QA");
		basePath= "/api/users/1";		
		PessoaResponse as = given()
			.contentType("application/json")
		.body(pessoaRequest)
		.when()
			.post("/")
		.then()
			.statusCode(HttpStatus.SC_CREATED)
			.extract().response().as(PessoaResponse.class);
		
		Assert.assertNotNull(as);
		Assert.assertNotNull(as);
		Assert.assertEquals(pessoaRequest.getNome(), as.getNome());
		Assert.assertEquals(pessoaRequest.getJob(), as.getJob());
		
		System.out.println("ID"+as.getId());
		System.out.println("getCreatedAt"+as.getCreatedAt());
	}
	
	@Test
	public void methodPost2() {
		PessoaRequest pessoaRequest = new PessoaRequest("barba 2", "QA 2");

		basePath= "/api/users/2";		
		String as = given()
			.contentType("application/json")
		.body(pessoaRequest)
		.when()
			.post("/")
		.then()
			.statusCode(HttpStatus.SC_CREATED).log().all()
			.and().extract().response().path("nome");

		System.out.println();
		Assert.assertNotNull(as);
	}
	
	@Test
	public void methodPut() {
		
		String pathSchema = "Schemas/barbaExample.json";
		
		basePath = Servicos.getUsers_ID.getValor();
	
		PessoaRequest request = new PessoaRequest();
		request.setNome("Teste");
		request.setJob("Analista de Dados");
		
		int codigoUsuario = 2;
		
			PessoaUpdate response = given()
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.pathParam("id", codigoUsuario)
				.log().all()
				.body(request)
			.when()
				.put()
			.then()
				.statusCode(HttpStatus.SC_OK)
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathSchema))
				.extract()
				.response()
				.as(PessoaUpdate.class);
	}
	
	@Test
	public void methodPatch() {
		
		Integer idUsuario = 2;
		basePath = Servicos.getUsers_ID.getValor();
		
		PessoaRequest request = new PessoaRequest("Mauricio", "Auxiliar Administrativo");
		
		String pathToSchemaInClasspath = "Schemas/patchUserResponse.json";
		
		PessoaUpdate as = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", idUsuario)
			.log().all()
		.when()
			.body(request)
			.patch()
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(pathToSchemaInClasspath))
			.extract()
			.response()
			.as(PessoaUpdate.class);
	}
	
	@Test
	public void methodDelete() {
		
		basePath = Servicos.getUsers_ID.getValor();
		
		given()
			.pathParam("id", 3)
			.log().all()
		.when()
			.delete()
		.then()
			.statusCode(HttpStatus.SC_NO_CONTENT);
			
	}
}
