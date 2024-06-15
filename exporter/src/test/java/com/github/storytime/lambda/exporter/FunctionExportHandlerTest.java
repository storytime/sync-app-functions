package com.github.storytime.lambda.exporter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.storytime.lambda.FunctionExportHandler;
import com.github.storytime.lambda.common.service.DbUserService;
import com.github.storytime.lambda.exporter.configs.ExportConfig;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FunctionExportHandlerTest {


    @Mock
    private DbUserService userService;

    @Mock
    private Logger logger;


    @Mock
    private ExportConfig exportConfig;

    @Mock
    private ObjectMapper jsonMapper;
    @InjectMocks
    private FunctionExportHandler handler;

    @Mock
    private Context context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(context.getAwsRequestId()).thenReturn("test-req-id");
        when(exportConfig.getStartFrom()).thenReturn(123456789L);
    }


    @Test
    void testHandleRequestException() {
        SQSEvent sqsEvent = new SQSEvent();
        SQSEvent.SQSMessage message = new SQSEvent.SQSMessage();
        message.setBody("userId");
        sqsEvent.setRecords(List.of(message));

        when(userService.findUserById("userId")).thenThrow(new RuntimeException("Database error"));
        int result = handler.handleRequest(sqsEvent, context);

        assertEquals(500, result);
        verify(logger, times(1)).errorf(anyString(), anyString(), anyLong(), anyString(), any(Exception.class));
    }
}
