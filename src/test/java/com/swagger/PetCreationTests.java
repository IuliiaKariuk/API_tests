package com.swagger;

import com.github.javafaker.Faker;
import com.swagger.api.controller.PetController;
import com.swagger.api.controller.PetsController;
import com.swagger.api.model.PetDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;

public class PetCreationTests {
    Faker faker = new Faker();
    PetController petController = new PetController();

    @Test
    @DisplayName("Creation pet")
    void creationPet() {
        String targetPetName = faker.name().name();
        PetDto targetPet = PetDto.builder().name(targetPetName).id((long) faker.number().randomDigit()).build();
        var createdPetResponse = petController
                .addNewPetToStore(targetPet);
        Assertions.assertEquals(200, createdPetResponse.statusCode());

        Response petByIdResponse = petController.getPetById(targetPet.getId());

        PetDto actualPet = petByIdResponse.as(PetDto.class);
        Assertions.assertEquals(targetPetName, actualPet.getName());
        Assertions.assertEquals(200, petByIdResponse.statusCode());
    }

    @Test
    @DisplayName("Creation pet2")
    void creationPet2() {
        PetsController petsController = new PetsController();
        var createdPet = new PetsController()
                .createNewPet();
        Assertions.assertEquals(200, createdPet.statusCode());

        Response availablePetsResponse = petsController.findPetsByStatus("Available");
        Assertions.assertEquals(200, availablePetsResponse.statusCode());
    }

    private static Response createPet(PetDto targetPet) {
        return petStoreApiClient()
                .body(targetPet)
                .when()
                .post("/pet");
    }
    private static RequestSpecification petStoreApiClient() {
        return given()
                .baseUri("https://petstore.swagger.io/")
                .basePath("/v2")
                .log().everything()
                .contentType(ContentType.JSON);
    }
}
