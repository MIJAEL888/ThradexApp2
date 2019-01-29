package org.thradex.rrhh.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thradex.config.MvcConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfiguration.class)
public class ShiftReportServiceTest {

    @Autowired
    private ShiftReportService shiftReportService;

    @Test
    public void exportReportShiftDetail() {
        shiftReportService.exportReportShiftDetail(62);
    }
}