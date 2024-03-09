package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Testduygu {
    @Test
    public void createboardsenaryo() {


        String apiKey = "6147bbb037b19983176313f3c458a786";
        String apiToken = "ATTA523590103700174c8af7a3d5289469b6b8f07f641bbec4cd02ba350bb2ff607dE7FA9F4F";

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("name", "DuyguBoard")
                .queryParam("key", "6147bbb037b19983176313f3c458a786")
                .queryParam("token", "ATTA523590103700174c8af7a3d5289469b6b8f07f641bbec4cd02ba350bb2ff607dE7FA9F4F")
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200).extract().response();
        response.prettyPrint();

        //Board icerisinde  yeni bir list olusturulur
        Response response2 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("id", response.path("id"))
                .queryParam("name", "Duygulist")
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .header("Accept", "application/json")
                .post("https://api.trello.com/1/boards/{id}/lists")
                .then()
                .statusCode(200).extract().response();
        response2.prettyPrint();

        //List icerisinde 2 adet card olusturulur
        Response response3 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("idList", response2.path("id").toString())
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("name", "duygucard1")
                .header("Accept", "application/json")
                .post("https://api.trello.com/1/cards")
                .then()
                .statusCode(200).extract().response();
        response3.prettyPrint();

        Response response4 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("idList", response2.path("id").toString())
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("name", "duygucard2")
                .header("Accept", "application/json")
                .post("https://api.trello.com/1/cards")
                .then()
                .statusCode(200).extract().response();
        response4.prettyPrint();
        //Random Card update
        Random random = new Random();

        String cardId1 = response3.path("id");
        String cardId2 = response4.path("id");

        int randomNumber = random.nextInt(2);

        String randomCardId;
        if (randomNumber == 0) {
            randomCardId = cardId1;
        } else {
            randomCardId = cardId2;
        }
        Response response5 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .pathParam("id", randomCardId)
                .queryParam("key", apiKey)
                .queryParam("name", "updatename")
                .queryParam("token", apiToken)
                .header("Accept", "application/json")
                .put("https://api.trello.com/1/cards/{id}")
                .then()
                .statusCode(200).extract().response();
        response5.prettyPrint();
        //Olusturulan cardlar silinir
        Response response6 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .delete("https://api.trello.com/1/cards/{id}", cardId1)
                .then()
                .statusCode(200).extract().response();
        response6.prettyPrint();
        Response response7 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .delete("https://api.trello.com/1/cards/{id}", cardId2)
                .then()
                .statusCode(200).extract().response();
        response7.prettyPrint();

//Olusturulan board silinir

        String boardid = response.path("id");
        Response response8 = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .delete("https://api.trello.com/1/boards/{id}", boardid)
                .then()
                .statusCode(200).extract().response();
        response8.prettyPrint();
    }
}
