package com.eleks.groupservice.controller;

import com.eleks.groupservice.advisor.ResponseExceptionHandler;
import com.eleks.groupservice.dto.ErrorDto;
import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import com.eleks.groupservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    PaymentController controller;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PaymentService service;

    private PaymentRequestDto requestDto;
    private PaymentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        requestDto = PaymentRequestDto.builder()
                .paymentDescription("paymentDescription")
                .price(200.50)
                .coPayers(Arrays.asList(1L, 2L, 3L))
                .build();

        responseDto = PaymentResponseDto.builder()
                .id(1L)
                .paymentDescription(requestDto.getPaymentDescription())
                .price(requestDto.getPrice())
                .coPayers(requestDto.getCoPayers())
                .creatorId(1L)
                .groupId(1L)
                .timestamp(LocalDateTime.now())
                .build();
    }


    @Test
    void createPayment_AllDataIsSet_ReturnOkAndSavedPayment() throws Exception {
        when(service.createPayment(anyLong(), anyLong(), any(PaymentRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/groups/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void createPayment_PaymentWithoutDescription_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setPaymentDescription(null);

        postPaymentAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "paymentDescription is required");
    }

    @Test
    void createPayment_PaymentWithBlankDescription_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setPaymentDescription("");

        postPaymentAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "paymentDescription length should be between 1 and 200");
    }

    @Test
    void createPayment_PaymentWithTooLongDescription_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setPaymentDescription(RANDOM_201_STRING);

        postPaymentAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "paymentDescription length should be between 1 and 200");
    }

    @Test
    void createPayment_PaymentWithoutPrice_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setPrice(null);

        postPaymentAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "price is required");
    }

    @Test
    void createPayment_PaymentWithoutCoPayers_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setCoPayers(null);

        postPaymentAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "coPayers is required");
    }

    @Test
    void createPayment_CoPayersIdsAreInvalid_ShouldReturnBadRequestAndErrorWithMsgFromException() throws Exception {
        Exception error = new UsersIdsValidationException("msg");

        when(service.createPayment(anyLong(), anyLong(), any(PaymentRequestDto.class))).thenThrow(error);

        postPaymentAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                error.getMessage());
    }

    private void postPaymentAndExpectStatusAndErrorWithMessage(String content, int expectedStatus, String expectedMsg) throws Exception {
        String errorJson = mockMvc.perform(post("/groups/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = objectMapper.readValue(errorJson, ErrorDto.class);

        assertEquals(error.getStatusCode(), expectedStatus);
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertEquals(expectedMsg, error.getMessages().get(0));
        assertNotNull(error.getTimestamp());
    }

    @Test
    void getPayment_GettingExistingPayment_ReturnOkAndPaymentData() throws Exception {
        when(service.getPayment(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(responseDto));

        mockMvc.perform(get("/groups/1/payments/" + responseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void getPayment_PaymentDoesntExist_ReturnNotFoundAndError() throws Exception {
        when(service.getPayment(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        String responseBody = mockMvc.perform(get("/groups/1/payments/" + responseDto.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString();

        ErrorDto error = objectMapper.readValue(responseBody, ErrorDto.class);
        assertEquals(error.getStatusCode(), HttpStatus.NOT_FOUND.value());
        assertEquals("payment does't exist", error.getMessages().get(0));
        assertNotNull(error.getTimestamp());
    }

    @Test
    void getPayments_GroupExistsPaymentsExist_ReturnOkListOfPayments() throws Exception {
        List<PaymentResponseDto> list = Arrays.asList(PaymentResponseDto.builder().id(1L).build(),
                PaymentResponseDto.builder().id(2L).build(),
                PaymentResponseDto.builder().id(3L).build());

        when(service.getPayments(anyLong())).thenReturn(Optional.of(list));

        mockMvc.perform(get("/groups/1/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void getPayments_GroupExistsPaymentsDontExist_ReturnOkAndEmptyList() throws Exception {
        List<PaymentResponseDto> list = Collections.emptyList();

        when(service.getPayments(anyLong())).thenReturn(Optional.of(list));

        mockMvc.perform(get("/groups/1/payments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void getPayments_GroupDoesntExist_ReturnNotFoundAndError() throws Exception {
        when(service.getPayments(anyLong())).thenReturn(Optional.empty());

        String responseBody = mockMvc.perform(get("/groups/1/payments"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString();

        ErrorDto error = objectMapper.readValue(responseBody, ErrorDto.class);
        assertEquals(error.getStatusCode(), HttpStatus.NOT_FOUND.value());
        assertEquals("group does't exist", error.getMessages().get(0));
        assertNotNull(error.getTimestamp());
    }

    @Test
    public void deletePayment_GroupAndPaymentExist_ReturnOk() throws Exception {
        mockMvc.perform(delete("/groups/1/payments/" + responseDto.getId()))
                .andExpect(status().isOk());

        verify(service).deletePayment(anyLong(), anyLong());
    }

    @Test
    public void deletePayment_GroupOrPaymentDontExist_ReturnNotFoundAndError() throws Exception {
        ResourceNotFoundException ex = new ResourceNotFoundException("msg");
        doThrow(ex).when(service).deletePayment(anyLong(), anyLong());

        String responseBody = mockMvc.perform(delete("/groups/1/payments/" + responseDto.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = objectMapper.readValue(responseBody, ErrorDto.class);

        assertEquals(error.getStatusCode(), HttpStatus.NOT_FOUND.value());
        assertNotNull(error.getMessages());
        assertEquals(ex.getMessage(), error.getMessages().get(0));
        assertNotNull(error.getTimestamp());
    }

    private static String RANDOM_201_STRING = "JZSfzulP8b9whnGwqRKuJZSfzulP8b9whnGwqRKuJZSfzulP8b" +
            "JZSfzulP8b9whnGwqRKuJZSfzulP8b9whnGwqRKuJZSfzulP8b" +
            "JZSfzulP8b9whnGwqRKuJZSfzulP8b9whnGwqRKuJZSfzulP8b" +
            "JZSfzulP8b9whnGwqRKuJZSfzulP8b9whnGwqRKuJZSfzulP8bq";
}