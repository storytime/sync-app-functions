package com.github.storytime.lambda.starter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.github.storytime.lambda.FunctionStarterHandler;
import com.github.storytime.lambda.common.model.db.DbUser;
import com.github.storytime.lambda.common.service.UserService;
import com.github.storytime.lambda.stater.StarterConfig;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FunctionStarterHandlerTest {

    @Mock
    private Logger logger;

    @Mock
    private SqsClient sqsClient;

    @Mock
    private UserService userService;

    @Mock
    private StarterConfig starterConfig;

    @Mock
    private Context context;

    @InjectMocks
    private FunctionStarterHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(starterConfig.getQueueUrl()).thenReturn("test-queue-url");
        when(context.getAwsRequestId()).thenReturn(UUID.randomUUID().toString());
    }

    @Test
    void testHandleRequestSuccess() {
        DbUser user1 = new DbUser();
        user1.setId("user1");

        DbUser user2 = new DbUser();
        user2.setId("user2");

        PageIterable<DbUser> usersPage = mock(PageIterable.class);
        SdkIterable<DbUser> sdkIterable = mock(SdkIterable.class);

        when(sdkIterable.stream()).thenReturn(List.of(user1, user2).stream());
        when(usersPage.items()).thenReturn(sdkIterable);
        when(userService.findAll()).thenReturn(usersPage);

        int result = handler.handleRequest(new ScheduledEvent(), context);

        verify(sqsClient, times(1)).sendMessageBatch(any(SendMessageBatchRequest.class));
        assertEquals(200, result);
    }

    @Test
    void testHandleRequestFailure() {
        ScheduledEvent scheduledEvent = new ScheduledEvent();
        when(context.getAwsRequestId()).thenReturn("test-request-id");
        when(userService.findAll()).thenThrow(new RuntimeException("DB Error"));

        Integer result = handler.handleRequest(scheduledEvent, context);
        assertEquals(Integer.valueOf(500), result);

        verify(logger).errorf(anyString(), anyString(), anyLong(), anyString(), any(Exception.class));
    }

    @Test
    void testHandleRequestException() {
        when(userService.findAll()).thenThrow(new RuntimeException("Database error"));

        int result = handler.handleRequest(new ScheduledEvent(), context);

        verify(logger, times(1)).errorf(anyString(), anyString(), anyLong(), anyString(), any(Exception.class));
        assertEquals(500, result);
    }

    @Test
    void testHandleRequestNoUsers() {
        PageIterable<DbUser> usersPage = mock(PageIterable.class);
        SdkIterable<DbUser> sdkIterable = mock(SdkIterable.class);

        when(sdkIterable.stream()).thenReturn(Stream.of());
        when(usersPage.items()).thenReturn(sdkIterable);
        when(userService.findAll()).thenReturn(usersPage);

        int result = handler.handleRequest(new ScheduledEvent(), context);

        verify(sqsClient, times(1)).sendMessageBatch(any(SendMessageBatchRequest.class));
        assertEquals(200, result);
    }

    @Test
    void testHandleRequestWithSingleUser() {
        DbUser user = new DbUser();
        user.setId("user1");

        PageIterable<DbUser> usersPage = mock(PageIterable.class);
        SdkIterable<DbUser> sdkIterable = mock(SdkIterable.class);

        when(sdkIterable.stream()).thenReturn(Stream.of(user));
        when(usersPage.items()).thenReturn(sdkIterable);
        when(userService.findAll()).thenReturn(usersPage);

        int result = handler.handleRequest(new ScheduledEvent(), context);

        verify(sqsClient, times(1)).sendMessageBatch(any(SendMessageBatchRequest.class));
        assertEquals(200, result);
    }


    @Test
    void testHandleRequestSendMessageBatchException() {
        DbUser user1 = new DbUser();
        user1.setId("user1");

        DbUser user2 = new DbUser();
        user2.setId("user2");

        PageIterable<DbUser> usersPage = mock(PageIterable.class);
        SdkIterable<DbUser> sdkIterable = mock(SdkIterable.class);

        when(sdkIterable.stream()).thenReturn(Stream.of(user1, user2));
        when(usersPage.items()).thenReturn(sdkIterable);
        when(userService.findAll()).thenReturn(usersPage);
        doThrow(new RuntimeException("SQS error")).when(sqsClient).sendMessageBatch(any(SendMessageBatchRequest.class));

        int result = handler.handleRequest(new ScheduledEvent(), context);

        verify(logger, times(1)).errorf(anyString(), anyString(), anyLong(), anyString(), any(Exception.class));
        assertEquals(500, result);
    }

    @Test
    void testHandleRequestNullInput() {
        ScheduledEvent input = null;
        int result = handler.handleRequest(input, context);
        assertEquals(500, result);
    }

    @Test
    void testHandleRequestEmptyUserId() {
        DbUser user = new DbUser();
        user.setId("");

        PageIterable<DbUser> usersPage = mock(PageIterable.class);
        SdkIterable<DbUser> sdkIterable = mock(SdkIterable.class);

        when(sdkIterable.stream()).thenReturn(Stream.of(user));
        when(usersPage.items()).thenReturn(sdkIterable);
        when(userService.findAll()).thenReturn(usersPage);

        int result = handler.handleRequest(new ScheduledEvent(), context);

        verify(sqsClient, times(1)).sendMessageBatch(any(SendMessageBatchRequest.class));
        assertEquals(200, result);
    }
}
