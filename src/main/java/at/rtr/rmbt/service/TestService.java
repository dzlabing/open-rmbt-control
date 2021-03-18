package at.rtr.rmbt.service;

import at.rtr.rmbt.model.Test;
import at.rtr.rmbt.request.TestResultDetailRequest;
import at.rtr.rmbt.request.TestResultRequest;
import at.rtr.rmbt.response.TestResponse;
import at.rtr.rmbt.response.TestResultContainerResponse;
import at.rtr.rmbt.response.TestResultDetailResponse;

import java.util.List;
import java.util.UUID;

public interface TestService {
    Test save(Test test);

    String getRmbtSetProviderFromAs(Long testUid);

    Integer getRmbtNextTestSlot(Long testUid);

    List<String> getDeviceHistory(Long clientId);

    List<String> getGroupNameByClientId(Long clientId);

    TestResponse getTestByUUID(UUID testUUID);

    TestResultDetailResponse getTestResultDetailByTestUUID(TestResultDetailRequest testResultDetailRequest);

    TestResultContainerResponse getTestResult(TestResultRequest testResultRequest);
}
