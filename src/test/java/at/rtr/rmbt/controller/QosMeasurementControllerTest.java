package at.rtr.rmbt.controller;

import at.rtr.rmbt.TestFixtures;
import at.rtr.rmbt.TestUtils;
import at.rtr.rmbt.advice.RtrAdvice;
import at.rtr.rmbt.constant.URIConstants;
import at.rtr.rmbt.response.ErrorResponse;
import at.rtr.rmbt.response.MeasurementQosResponse;
import at.rtr.rmbt.response.QosMeasurementsResponse;
import at.rtr.rmbt.service.QosMeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static at.rtr.rmbt.TestConstants.DEFAULT_TEST_UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class QosMeasurementControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private QosMeasurementService qosMeasurementService;

    @Before
    public void setUp() {
        QosMeasurementController qosMeasurementController = new QosMeasurementController(qosMeasurementService);
        mockMvc = MockMvcBuilders.standaloneSetup(qosMeasurementController)
            .setControllerAdvice(new RtrAdvice())
            .build();
    }

    @Test
    public void provideMeasurementQosParameters_whenCommonData_expectMeasurementQosResponse() throws Exception {
        var response = getMeasurementQosResponse();
        when(qosMeasurementService.getQosParameters(any(), any())).thenReturn(response);

        mockMvc.perform(post(URIConstants.MEASUREMENT_QOS_REQUEST))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void saveQosMeasurementResult_whenCommonData_expectEmptyErrorResponse() throws Exception {
        ErrorResponse expected = new ErrorResponse();

        when(qosMeasurementService.saveQosMeasurementResult(any())).thenReturn(expected);

        mockMvc.perform(
            post(URIConstants.RESULT_QOS_URL)
                .content(TestUtils.mapper.writeValueAsString(TestFixtures.qosResultRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(content().json("{error:[]}"));

        verify(qosMeasurementService).saveQosMeasurementResult(any());
    }


    @Test
    public void getQosTestResults_whenCommonData_expectQosResults() throws Exception {
        QosMeasurementsResponse response = new QosMeasurementsResponse(
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList(),
            new QosMeasurementsResponse.EvalTimes(1L, 2L),
            new ErrorResponse()
        );

        when(qosMeasurementService.getQosResult(DEFAULT_TEST_UUID, "en", null)).thenReturn(response);

        mockMvc.perform(
            post(URIConstants.MEASUREMENT_QOS_RESULT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"test_uuid\": \"%s\", \"language\": \"en\"}", DEFAULT_TEST_UUID))
        ).andExpect(status().isOk())
            .andExpect(content().json(
                "{" +
                    "testresultdetail:[]," +
                    "testresultdetail_desc:[]," +
                    "testresultdetail_testdesc:[]," +
                    "eval_times: {" +
                    "   eval: 1," +
                    "   full: 2" +
                    "}," +
                    "error: {" +
                    "   error:[]" +
                    "}}"
            ));
    }

    private MeasurementQosResponse getMeasurementQosResponse() {
        return MeasurementQosResponse.builder()
            .build();
    }
}
