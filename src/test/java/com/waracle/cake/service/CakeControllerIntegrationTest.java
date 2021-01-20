package com.waracle.cake.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake.api.Cake;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class CakeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private int cakeId;
    private Cake cakeRequest;
    private Cake cakeResponse;
    private List<Cake> cakesResponse;

    private static <T> String serializeRequest(T request) throws Exception {
        return new ObjectMapper()
                .writeValueAsString(request);
    }

    private static <T> T deserializeResponseAs(MvcResult mvcResult, TypeReference<T> typeReference) throws Exception {
        return new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), typeReference);
    }

    @Test
    public void shouldReturnNotFound_whenWeGetCakeWithAnInvalidId() throws Exception {
        givenWeHaveAnInvalidCakeId();
        thenTheGetCakeEndpointReturnsANotFoundError();
    }

    @Test
    public void shouldGetCakeWithId() throws Exception {
        givenWeHaveACakeId();
        whenWeCallTheGetCakeEndpoint();
        thenACakeIsReturned();
    }

    @Test
    public void shouldGetAllCakes() throws Exception {
        whenWeCallTheGetCakesEndpoint();
        thenAListOfCakesIsReturned();
    }

    @Test
    public void shouldCreateACake() throws Exception {
        givenWeHaveACakeToCreate();
        whenWeCallTheCreateCakeEndpoint();
        thenTheExpectedCakeIsReturned();
    }

    @Test
    public void shouldReturnNotFound_whenWeUpdateACakeWithAnInvalidId() throws Exception {
        givenWeHaveAnInvalidCakeId();
        andWeHaveACakeToUpdate();
        thenTheUpdateCakeEndpointReturnsANotFoundError();
    }

    @Test
    public void shouldUpdateACakeWithId() throws Exception {
        givenWeHaveACakeId();
        andWeHaveACakeToUpdate();
        whenWeCallTheUpdateCakeEndpoint();
        thenTheExpectedCakeIsReturned();
    }

    @Test
    public void shouldReturnNotFound_whenWeDeleteACakeWithAnInvalidId() throws Exception {
        givenWeHaveAnInvalidCakeId();
        thenTheDeleteCakeEndpointReturnsANotFoundError();
    }

    @Test
    public void shouldDeleteACakeWithId() throws Exception {
        givenWeHaveACakeId();
        whenWeCallTheDeleteCakeEndpoint();
        thenTheCakeIsDeleted();
    }

    private void givenWeHaveAnInvalidCakeId() {
        cakeId = 100;
    }

    private void givenWeHaveACakeId() {
        cakeId = 1;
    }

    private void givenWeHaveACakeToCreate() {
        cakeRequest =
                new Cake(null,
                         "New Cake", "New Description", "New Image");
    }

    private void andWeHaveACakeToUpdate() {
        cakeRequest =
                new Cake(null,
                         "Updated Cake", "Updated Description", "Updated Image");
    }

    private void whenWeCallTheGetCakeEndpoint() throws Exception {
        cakeResponse = deserializeResponseAs(
                mockMvc.perform(get("/cakes/" + cakeId))
                       .andExpect(status().isOk())
                       .andReturn(), new TypeReference<Cake>() {
                });
    }

    private void whenWeCallTheGetCakesEndpoint() throws Exception {
        cakesResponse = deserializeResponseAs(
                mockMvc.perform(get("/cakes"))
                       .andExpect(status().isOk())
                       .andReturn(), new TypeReference<List<Cake>>() {
                });
    }

    private void whenWeCallTheCreateCakeEndpoint() throws Exception {
        cakeResponse = deserializeResponseAs(
                mockMvc.perform(
                        post("/cakes")
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .content(serializeRequest(cakeRequest))
                               )
                       .andExpect(status().isCreated())
                       .andReturn(), new TypeReference<Cake>() {
                });
    }

    private void whenWeCallTheUpdateCakeEndpoint() throws Exception {
        cakeResponse = deserializeResponseAs(
                mockMvc.perform(
                        put("/cakes/" + cakeId)
                                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                .content(serializeRequest(cakeRequest))
                               )
                       .andExpect(status().isOk())
                       .andReturn(), new TypeReference<Cake>() {
                });
    }

    private void whenWeCallTheDeleteCakeEndpoint() throws Exception {
        mockMvc.perform(delete("/cakes/" + cakeId))
               .andExpect(status().isNoContent());
    }

    private void thenTheGetCakeEndpointReturnsANotFoundError() throws Exception {
        mockMvc.perform(get("/cakes/" + cakeId))
               .andExpect(status().isNotFound())
               .andExpect(this::assertNotFoundError);
    }

    private void thenTheUpdateCakeEndpointReturnsANotFoundError() throws Exception {
        mockMvc.perform(
                put("/cakes/" + cakeId)
                        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .content(serializeRequest(cakeRequest)))
               .andExpect(status().isNotFound())
               .andExpect(this::assertNotFoundError);
    }

    private void thenTheDeleteCakeEndpointReturnsANotFoundError() throws Exception {
        mockMvc.perform(delete("/cakes/" + cakeId))
               .andExpect(status().isNotFound())
               .andExpect(status().isNotFound())
               .andExpect(this::assertNotFoundError);
    }

    private void thenACakeIsReturned() {
        assertThat(cakeResponse)
                .isNotNull()
                .satisfies(this::assertValidCakeResponse);
    }

    private void thenAListOfCakesIsReturned() {
        assertThat(cakesResponse)
                .hasSize(16)
                .allSatisfy(this::assertValidCakeResponse);
    }

    private void thenTheExpectedCakeIsReturned() {
        assertThat(cakeResponse)
                .isNotNull()
                .satisfies(cake -> assertExpectedCakeValues(cake, cakeRequest));
    }

    private void thenTheCakeIsDeleted() throws Exception {
        whenWeCallTheGetCakesEndpoint();
        assertThat(cakesResponse)
                .hasSize(15)
                .allSatisfy(this::assertValidCakeResponse);
    }

    private void assertNotFoundError(MvcResult result) {
        assertThat(result.getResolvedException())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(format("No cake found with id '%d'", cakeId));

    }

    private void assertValidCakeResponse(Cake cake) {
        assertThat(cake.getId()).isNotNull();
        assertThat(cake.getTitle()).isNotNull();
        assertThat(cake.getDescription()).isNotNull();
        assertThat(cake.getImage()).isNotNull();
    }

    private void assertExpectedCakeValues(Cake actual, Cake expected) {
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getImage()).isEqualTo(expected.getImage());
    }
}