package com.itdajman.tuneheavenanalytics.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
class FixedDateScheduleTest {

    public static final String IMPORT_CSV_DATA_SCHEDULER = "com.itdajman.tuneheavenanalytics.service.CsvDataImportScheduler.importCsvData";

    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;

    @Test
    public void testYearlyCronTaskScheduled() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        scheduledTasks.forEach(scheduledTask -> scheduledTask.getTask().getRunnable().getClass().getDeclaredMethods());
        long count = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals("0 0 23 * * ?") &&
                        cronTask.toString().equals(IMPORT_CSV_DATA_SCHEDULER))
                .count();
        assertThat(count).isEqualTo(1L);
    }
}
