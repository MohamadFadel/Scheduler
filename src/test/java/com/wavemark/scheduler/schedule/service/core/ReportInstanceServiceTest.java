package com.wavemark.scheduler.schedule.service.core;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.ReportInstanceRepository;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportInstanceServiceTest {

    @Mock
    private ReportInstanceRepository reportInstanceRepository;

    @InjectMocks
    private ReportInstanceService reportInstanceService;

    @Test
    void testSaveReportInstance() {
        ReportInstanceConfig reportInstanceConfig = DataUtil.generateReportInstanceConfig();
        reportInstanceService.saveReportInstance(reportInstanceConfig);
        verify(reportInstanceRepository, times(1)).save(reportInstanceConfig);
    }

    @Test
    void testGetNewReportId() {
        Long expectedId = 1L;
        when(reportInstanceRepository.getReportIdNextValueSequence()).thenReturn(expectedId);
        Long reportId = reportInstanceService.getNewReportId();
        assertEquals(expectedId, reportId);
    }

    @Test
    void testBuildReport(){

//        ReportInstanceService reportInstanceServiceMock = spy(reportInstanceService);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthUser).thenReturn("authUser");
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("authDepartment");

            ReportInstanceConfig result = reportInstanceService.buildReportInstanceConfig(DataUtil.generateReportInstanceInput());
//                    reportInstanceServiceMock.buildReportInstanceConfig(DataUtil.generateReportInstanceInput());

            assertNotNull(result);
        }
    }

    @Test
    void testUpdateReport() {

//        ReportInstanceService reportInstanceServiceMock = spy(reportInstanceService);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthUser).thenReturn("authUser");

            ReportInstanceConfig result = reportInstanceService.updateReportInstanceConfig(DataUtil.generateReportInstanceConfig(), DataUtil.generateReportInstanceInput());
//                    reportInstanceServiceMock.updateReportInstanceConfig(DataUtil.generateReportInstanceConfig(), DataUtil.generateReportInstanceInput());

            assertNotNull(result);
        }
    }

    @Test
    void testFindReportById() throws EntryNotFoundException {
        Long reportId = 1L;
        ReportInstanceConfig reportInstanceConfig = DataUtil.generateReportInstanceConfig();
        when(reportInstanceRepository.findById(reportId)).thenReturn(Optional.of(reportInstanceConfig));
        ReportInstanceConfig result = reportInstanceService.findReportById(reportId);
        assertEquals(reportInstanceConfig, result);
    }

    @Test
    void testFindReportByIdThrowsEntryNotFoundException() {
        Long reportId = 1L;
        when(reportInstanceRepository.findById(reportId)).thenReturn(Optional.empty());
        assertThrows(EntryNotFoundException.class, () -> reportInstanceService.findReportById(reportId));
    }

}
