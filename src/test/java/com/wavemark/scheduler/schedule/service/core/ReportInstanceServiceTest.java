package com.wavemark.scheduler.schedule.service.core;

import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.ReportInstanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportInstanceServiceTest {

    @Mock
    private ReportInstanceRepository reportInstanceRepository;

    @InjectMocks
    private ReportInstanceService reportInstanceService;

    @Test
    void testSaveReportInstance() {
        ReportInstanceConfig reportInstanceConfig = new ReportInstanceConfig();
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
    void testFindReportById() throws EntryNotFoundException {
        Long reportId = 1L;
        ReportInstanceConfig reportInstanceConfig = new ReportInstanceConfig();
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

    @Test
    void testDeleteReportInstance() {
        String reportId = "1";
        reportInstanceService.deleteReportInstance(reportId);
        // Verify that no interaction occurs with the repository since the method is empty
        verifyNoMoreInteractions(reportInstanceRepository);
    }
}
