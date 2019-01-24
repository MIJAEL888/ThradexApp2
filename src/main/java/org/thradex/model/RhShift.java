package org.thradex.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * The persistent class for the tt_types database table.
 * 
 */
/**
 * @author developer
 *
 */
@Entity
@Table(name="rh_shift")
@NamedQuery(name="RhShift.findAll", query="SELECT t FROM RhShift t")
public class RhShift implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	private Integer id;

	private String comment;
	
	private String reason;
	
	private String response;
	
	private int duration;

	private boolean processed;
	
	private boolean processed2;
	
	@Column(name="DATE_START")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateStart;
	
	@Column(name="DATE_FINISH")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateFinish;

	@Column(name="DATE_REG")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateReg;
	
	@Column(name="DATE_START_SHIFT")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateStartShift;
	
	@Column(name="DATE_FINSH_SHIFT")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateFinishShift;
	
	@Column(name="DATE_START_BREAK")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateStartBreak;
	
	@Column(name="DATE_FINISH_BREAK")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateFinishBreak;
	
	@Column(name="DATE_PROCESS")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	private Date dateProcess;
	
	@ManyToOne
	@JoinColumn(name="ID_PERS_FIN")
	private RhPerson rhPersonFin;

	@ManyToOne
	@JoinColumn(name="ID_PERSON")
	private RhPerson rhPerson;
	
	@ManyToOne
	@JoinColumn(name="ID_PERSON_MNG")
	private RhPerson rhPersonMng;
	
	@ManyToOne
	@JoinColumn(name="ID_SCALE")
	private RhScheduleScale rhScheduleScale;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS")
	private RhStatus rhStatus;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS_BREAK")
	private RhStatus rhStatusBreak;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE")
	private RhType rhType;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_CHARGE")
	private RhType rhTypeCharge;
	
	@ManyToOne
	@JoinColumn(name="ID_TYPE_PERM")
	private RhType rhTypePerm;
	
	@ManyToOne
	@JoinColumn(name="ID_PERIOD")
	private RhShiftPeriod rhShiftPeriod;
	
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private SisRegion sisRegion;
	
	@ManyToOne
	@JoinColumn(name="ID_PARENT")
	private RhShift rhShiftParent;
	
	@ManyToOne
	@JoinColumn(name="ID_STATUS_SIS")
	private RhStatus rhStatusSis;
	
	@ManyToOne
	@JoinColumn(name="ID_PERSON_RESP")
	private RhPerson rhPersonResp;

	@Transient
	private List<RhFile> rhFiles;
	
	@Transient
	private List<RhShift> rhShifts;
	
	@Transient
	private List<RhShiftDetail> rhShiftDetails;
	
	@Transient
	private CommonsMultipartFile[] files;
	
	@Transient
	private int validate;
	
	@Transient
	private String durationPending;
	
	@Transient
	private int durationHour;
	
	@Transient
	private String activeTime;
	
	@Transient
	private Boolean approved;
	
	@Transient
	private Boolean toPay;
	
	@Transient
	private SisCountry sisCountry;
	
	@Transient
	private String totalTime;
	
	@Transient
	private String totalProcessedTime;
	
	@Transient
	private Map<String, Object> detail;
	
	@Transient
	private String labelPlanned;
	
	@Transient
	private String labelRegister;
	
	@Transient
	private String labelReason;
	
	@Override
	public RhShift clone(){
		RhShift rhShift = new RhShift();
		rhShift.setActiveTime(this.activeTime);
		rhShift.setComment("Event cloned to close period...");
		rhShift.setDateFinish(this.dateFinish);
		rhShift.setDateFinishBreak(this.dateFinishBreak);
		rhShift.setDateFinishShift(this.dateFinishShift);
		rhShift.setDateProcess(this.dateProcess);
		rhShift.setDateReg(this.dateReg);
		rhShift.setDateStart(this.dateStart);
		rhShift.setDateStartBreak(this.dateStartBreak);
		rhShift.setDateStartShift(this.dateStartShift);
		rhShift.setDetail(this.detail);
		rhShift.setDuration(this.duration);
		rhShift.setProcessed(this.processed);
		rhShift.setProcessed(this.processed);
		rhShift.setReason(this.reason);
		rhShift.setResponse(this.response);
		rhShift.setRhPerson(this.rhPerson);
		rhShift.setRhPersonFin(this.rhPersonFin);
		rhShift.setRhPersonMng(this.rhPersonMng);
		rhShift.setRhPersonResp(this.rhPersonResp);
		rhShift.setRhScheduleScale(this.rhScheduleScale);
		rhShift.setRhShiftParent(this.rhShiftParent);
		rhShift.setRhShiftPeriod(this.rhShiftPeriod);
		rhShift.setRhStatus(this.rhStatus);
		rhShift.setRhStatusSis(this.rhStatusSis);
		rhShift.setRhType(this.rhType);
		rhShift.setRhTypeCharge(this.rhTypeCharge);
		rhShift.setRhTypePerm(this.rhTypePerm);
		rhShift.setSisCountry(this.sisCountry);
		rhShift.setSisRegion(this.sisRegion);
		return rhShift;
	}
	public RhShift() {
	}
	
	public String getLabelReason() {
		String reason1 = (reason == null) ? "" : reason;
		String status1 = (rhStatus == null) ? "" : rhStatus.getName();
		String type1 = (rhType == null) ? "" :rhType.getName();
		labelReason = type1 + "(" + status1 + " - " + reason1 + ")";
		for (RhShift rhShift1: rhShifts) {
			String reason2 = (rhShift1.getReason() == null) ? "" : rhShift1.getReason();
			String status2 = (rhShift1.getRhStatus() == null) ? "" : rhShift1.getRhStatus().getName();
			String type2 = (rhShift1.getRhType() == null) ? "" : rhShift1.getRhType().getName();
			labelReason = labelReason + "\n -- "+  type2 + "(" + status2 + " - " + reason2 + ")"; 
		}
		return labelReason;
	}
	public void setLabelReason(String labelReason) {
		this.labelReason = labelReason;
	}
	public String getLabelRegister() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		if((this.getRhType().getCode() == 1 || this.getRhType().getCode() == 2) 
				&& this.dateStartShift != null && this.dateFinishShift != null)
			return formatter.format(this.dateStartShift) + " - " + formatter.format(this.dateFinishShift);
		else return labelRegister;
	}
	public String getLabelPlanned() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		if(this.dateStart != null && this.dateFinish != null)
			return formatter.format(this.dateStart) + " - " + formatter.format(this.dateFinish);
		else return labelPlanned;
	}
	public Boolean getApproved() {
		return approved;
	}
	public boolean isProcessed2() {
		return processed2;
	}
	public void setProcessed2(boolean processed2) {
		this.processed2 = processed2;
	}
	public void setLabelPlanned(String labelPlanned) {
		this.labelPlanned = labelPlanned;
	}
	public void setLabelRegister(String labelRegister) {
		this.labelRegister = labelRegister;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getTotalProcessedTime() {
		return totalProcessedTime;
	}
	public void setTotalProcessedTime(String totalProcessedTime) {
		this.totalProcessedTime = totalProcessedTime;
	}
	public Boolean isApproved() {
		return approved;
	}
	
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public List<RhShift> getRhShifts() {
		return rhShifts;
	}

	public void setRhShifts(List<RhShift> rhShifts) {
		this.rhShifts = rhShifts;
	}

	public List<RhShiftDetail> getRhShiftDetails() {
		return rhShiftDetails;
	}

	public void setRhShiftDetails(List<RhShiftDetail> rhShiftDetails) {
		this.rhShiftDetails = rhShiftDetails;
	}

	public RhPerson getRhPersonResp() {
		return rhPersonResp;
	}

	public void setRhPersonResp(RhPerson rhPersonResp) {
		this.rhPersonResp = rhPersonResp;
	}

	public Date getDateProcess() {
		return dateProcess;
	}

	public void setDateProcess(Date dateProcess) {
		this.dateProcess = dateProcess;
	}

	public int getDurationHour() {
		return durationHour;
	}

	public void setDurationHour(int durationHour) {
		this.durationHour = durationHour;
	}

	public Map<String, Object> getDetail() {
		return detail;
	}

	public void setDetail(Map<String, Object> detail) {
		this.detail = detail;
	}

	public List<RhFile> getRhFiles() {
		return rhFiles;
	}

	public void setRhFiles(List<RhFile> rhFiles) {
		this.rhFiles = rhFiles;
	}

	public RhShift getRhShiftParent() {
		return rhShiftParent;
	}

	public RhStatus getRhStatusSis() {
		return rhStatusSis;
	}

	public void setRhStatusSis(RhStatus rhStatusSis) {
		this.rhStatusSis = rhStatusSis;
	}

	public void setRhShiftParent(RhShift rhShiftParent) {
		this.rhShiftParent = rhShiftParent;
	}

	public CommonsMultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(CommonsMultipartFile[] files) {
		this.files = files;
	}

	public SisRegion getSisRegion() {
		return sisRegion;
	}

	public void setSisRegion(SisRegion sisRegion) {
		this.sisRegion = sisRegion;
	}

	public SisCountry getSisCountry() {
		return sisCountry;
	}

	public void setSisCountry(SisCountry sisCountry) {
		this.sisCountry = sisCountry;
	}

	public String getDurationPending() {
		return durationPending;
	}

	public void setDurationPending(String durationPending) {
		this.durationPending = durationPending;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public RhShift(int validate) {
		this.validate = validate;
	}
	public RhType getRhTypePerm() {
		return rhTypePerm;
	}
	public void setRhTypePerm(RhType rhTypePerm) {
		this.rhTypePerm = rhTypePerm;
	}
	public RhType getRhTypeCharge() {
		return rhTypeCharge;
	}
	public void setRhTypeCharge(RhType rhTypeCharge) {
		this.rhTypeCharge = rhTypeCharge;
	}
	public RhShiftPeriod getRhShiftPeriod() {
		return rhShiftPeriod;
	}
	public void setRhShiftPeriod(RhShiftPeriod rhShiftPeriod) {
		this.rhShiftPeriod = rhShiftPeriod;
	}
	
	public Boolean getToPay() {
		return toPay;
	}
	public Boolean isToPay() {
		return toPay;
	}
	public void setToPay(Boolean toPay) {
		this.toPay = toPay;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Date getDateReg() {
		return dateReg;
	}
	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}
	public RhType getRhType() {
		return rhType;
	}
	public void setRhType(RhType rhType) {
		this.rhType = rhType;
	}
	public int getValidate() {
		return validate;
	}
	public RhPerson getRhPersonMng() {
		return rhPersonMng;
	}
	public void setRhPersonMng(RhPerson rhPersonMng) {
		this.rhPersonMng = rhPersonMng;
	}
	public RhStatus getRhStatus() {
		return rhStatus;
	}
	public void setRhStatus(RhStatus rhStatus) {
		this.rhStatus = rhStatus;
	}
	public void setValidate(int validate) {
		this.validate = validate;
	}

	public Date getDateFinishShift() {
		return dateFinishShift;
	}

	public void setDateFinishShift(Date dateFinishShift) {
		this.dateFinishShift = dateFinishShift;
	}

	public Date getDateStartShift() {
		return dateStartShift;
	}

	public void setDateStartShift(Date dateStartShift) {
		this.dateStartShift = dateStartShift;
	}

	public Date getDateStartBreak() {
		return dateStartBreak;
	}

	public void setDateStartBreak(Date dateStartBreak) {
		this.dateStartBreak = dateStartBreak;
	}

	public Date getDateFinishBreak() {
		return dateFinishBreak;
	}

	public void setDateFinishBreak(Date dateFinishBreak) {
		this.dateFinishBreak = dateFinishBreak;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateFinish() {
		return dateFinish;
	}

	public void setDateFinish(Date dateFinish) {
		this.dateFinish = dateFinish;
	}

	public RhScheduleScale getRhScheduleScale() {
		return rhScheduleScale;
	}

	public void setRhScheduleScale(RhScheduleScale rhScheduleScale) {
		this.rhScheduleScale = rhScheduleScale;
	}

	public RhStatus getRhStatusBreak() {
		return rhStatusBreak;
	}

	public void setRhStatusBreak(RhStatus rhStatusBreak) {
		this.rhStatusBreak = rhStatusBreak;
	}

	public RhPerson getRhPersonFin() {
		return rhPersonFin;
	}
	public void setRhPersonFin(RhPerson rhPersonFin) {
		this.rhPersonFin = rhPersonFin;
	}
	public RhPerson getRhPerson() {
		return rhPerson;
	}
	public void setRhPerson(RhPerson rhPerson) {
		this.rhPerson = rhPerson;
	}

}