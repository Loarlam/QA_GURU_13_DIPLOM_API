package in.reqres.tests;

import com.github.javafaker.Faker;

import java.time.Instant;
import java.util.Locale;

public class DataForTheTest {
    Faker fakerData = new Faker(Locale.FRANCE);

    String baseUrl = "https://reqres.in",
            userName = fakerData.funnyName().name(),
            userNameToUpdate = fakerData.funnyName().name(),
            userJob = fakerData.job().position(),
            userJobToUpdate = fakerData.job().position(),
            timeBeforeStartTest = Instant.now().toString();
}