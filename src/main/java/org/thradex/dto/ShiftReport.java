package org.thradex.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

public class ShiftReport {

    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date timeStart;

    @DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
    private Date timeFinish;

    private int order;

    private String day;

    private String status;

    private Double additionalMinutes;

    private Double compensationMinutes;

    private Double discountMinutes;

    private Double workedMinutes;

    private Double totalMinutes;

    private Double totalRateMinutes;

    private Double totalMonthMinutes;

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(Date timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getDiscountMinutes() {
        return discountMinutes;
    }

    public void setDiscountMinutes(Double discountMinutes) {
        this.discountMinutes = discountMinutes;
    }

    public Double getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Double totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public Double getTotalRateMinutes() {
        return totalRateMinutes;
    }

    public void setTotalRateMinutes(Double totalRateMinutes) {
        this.totalRateMinutes = totalRateMinutes;
    }

    public Double getTotalMonthMinutes() {
        return totalMonthMinutes;
    }

    public void setTotalMonthMinutes(Double totalMonthMinutes) {
        this.totalMonthMinutes = totalMonthMinutes;
    }

    public Double getAdditionalMinutes() {
        return additionalMinutes;
    }

    public void setAdditionalMinutes(Double additionalMinutes) {
        this.additionalMinutes = additionalMinutes;
    }

    public Double getCompensationMinutes() {
        return compensationMinutes;
    }

    public void setCompensationMinutes(Double compensationMinutes) {
        this.compensationMinutes = compensationMinutes;
    }

    public Double getWorkedMinutes() {
        return workedMinutes;
    }

    public void setWorkedMinutes(Double workedMinutes) {
        this.workedMinutes = workedMinutes;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
