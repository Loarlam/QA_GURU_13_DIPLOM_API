package in.reqres.tests;

import in.reqres.models.lombok.CreateUserBodyLombokModel;
import in.reqres.models.lombok.CreateUserResponseLombokModel;
import in.reqres.models.lombok.UpdateUserBodyLombokModel;
import in.reqres.models.lombok.UpdateUserResponseLombokModel;
import in.reqres.models.pojo.CreateUserBodyPOJOModel;
import in.reqres.models.pojo.CreateUserResponsePOJOModel;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.CreateUserWithSpec.requestSpecificationCreate;
import static in.reqres.specs.CreateUserWithSpec.responseSpecificationCreate;
import static in.reqres.specs.DeleteUserWithSpec.requestSpecificationDelete;
import static in.reqres.specs.DeleteUserWithSpec.responseSpecificationDelete;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static in.reqres.helpers.CustomApiListener.withCustomTemplates;


public class SeveralTestsForReqresAPI extends BaseTest {
    int userId;

    @Test
    @Tag("reqres")
    @Owner("Loarlam")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создает нового юзера")
    @Description("Создает нового юзера методом /api/users с POJO моделью и проверяет параметры ответа созданного юзера")
    void creatingUserWithPOJO() {
        CreateUserBodyPOJOModel createUserBodyPOJOModel = new CreateUserBodyPOJOModel();
        createUserBodyPOJOModel.setName(dataForTheTest.userName);
        createUserBodyPOJOModel.setJob(dataForTheTest.userJob);

        CreateUserResponsePOJOModel createUserResponsePOJOModel = given().
                filter(withCustomTemplates()).
                contentType(JSON)
                .body(createUserBodyPOJOModel)
                .log().all()
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(CreateUserResponsePOJOModel.class);

        assertEquals(createUserResponsePOJOModel.getName(), dataForTheTest.userName);
        assertEquals(createUserResponsePOJOModel.getJob(), dataForTheTest.userJob);
        assertNotNull(createUserResponsePOJOModel.getId());
        assertThat(createUserResponsePOJOModel.getCreatedAt()).isGreaterThan(dataForTheTest.timeBeforeStartTest);
    }

    @Test
    @Tag("reqres")
    @Owner("Loarlam")
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("Создаёт юзера, затем обновляет информацию юзеру")
    @Description("Создаёт юзера, затем обновляет информацию по созданному юзеру методом /api/users/{id юзера} с Lombok моделью")
    void updatingUserInfoWithLombok() {
        CreateUserBodyLombokModel createUserBodyLombokModel = new CreateUserBodyLombokModel();
        createUserBodyLombokModel.setName(dataForTheTest.userName);
        createUserBodyLombokModel.setJob(dataForTheTest.userJob);

        UpdateUserBodyLombokModel updateUserBodyLombokModel = new UpdateUserBodyLombokModel();
        updateUserBodyLombokModel.setUpdateName(dataForTheTest.userNameToUpdate);
        updateUserBodyLombokModel.setUpdateJob(dataForTheTest.userJobToUpdate);

        CreateUserResponseLombokModel createUserResponseLombokModel = given().
                filter(withCustomTemplates()).
                contentType(JSON)
                .body(createUserBodyLombokModel)
                .log().all()
                .when()
                .post("/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().as(CreateUserResponseLombokModel.class);

        UpdateUserResponseLombokModel updateUserResponseLombokModel = given().
                filter(withCustomTemplates()).
                contentType(JSON)
                .body(updateUserBodyLombokModel)
                .log().all()
                .when()
                .put("/api/users/" + createUserResponseLombokModel.getId())
                .then()
                .log().all()
                .statusCode(200)
                .extract().as(UpdateUserResponseLombokModel.class);

        assertThat(updateUserResponseLombokModel.getUpdateName()).isEqualTo(dataForTheTest.userNameToUpdate);
        assertThat(updateUserResponseLombokModel.getUpdateJob()).isEqualTo(dataForTheTest.userJobToUpdate);
        assertThat(updateUserResponseLombokModel.getUpdatedAt()).isGreaterThan(dataForTheTest.timeBeforeStartTest);
    }


    @Test
    @Tag("reqres")
    @Owner("Loarlam")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создаёт юзера, затем удаляем юзерау")
    @Description("Создаёт юзера, затем удаляем информацию по юзеру /api/users/{id юзера}")
    void deletingUserWithSpec() {
        CreateUserBodyLombokModel createUserBodyLombokModel = new CreateUserBodyLombokModel();
        createUserBodyLombokModel.setName(dataForTheTest.userName);
        createUserBodyLombokModel.setJob(dataForTheTest.userJob);

        given().
                filter(withCustomTemplates()).
                spec(requestSpecificationCreate)
                .body(createUserBodyLombokModel)
                .when()
                .post()
                .then()
                .spec(responseSpecificationCreate)
                .extract().as(CreateUserResponseLombokModel.class);

        given().
                contentType(JSON)
                .spec(requestSpecificationDelete)
                .when()
                .delete("/" + userId)
                .then()
                .spec(responseSpecificationDelete);
    }
}