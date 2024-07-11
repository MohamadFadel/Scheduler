package com.wavemark.scheduler.fire.http.response.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResponseMessageFactoryTest {

    @Test
    void testGenerateMessage_Default() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(400);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Unexpected error");
        assertEquals(responseMessage.getDescription(), "Task has failed due to an unexpected error.");
    }

    @Test
    void testGenerateMessage_401() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(401);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Department authentication error");
        assertEquals(responseMessage.getDescription(), "Task has failed due to an authentication error.");
    }

    @Test
    void testGenerateMessage_408() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(408);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Timeout error");
        assertEquals(responseMessage.getDescription(), "Task has failed due to a timeout error.");
    }

    @Test
    void testGenerateMessage_412() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(412);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Department configuration error");
        assertEquals(responseMessage.getDescription(), "Task has failed because your department is not configured to run this task type.");
    }

    @Test
    void testGenerateMessage_503() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(503);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Service unavailable error");
        assertEquals(responseMessage.getDescription(), "Task has failed due to service unavailability.");
    }

    @Test
    void testGenerateMessage_505() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(505);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Database error");
        assertEquals(responseMessage.getDescription(), "Task has failed due to a database error.");
    }

    @Test
    void testGenerateMessage_506() {
        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(506);

        assertNotNull(responseMessage);
        assertEquals(responseMessage.getType(), "Database connection error");
        assertEquals(responseMessage.getDescription(), "Task has failed due to a database connection error.");
    }

}