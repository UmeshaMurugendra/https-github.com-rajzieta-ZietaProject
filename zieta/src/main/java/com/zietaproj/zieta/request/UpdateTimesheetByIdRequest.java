package com.zietaproj.zieta.request;

import java.util.Date;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTimesheetByIdRequest {

	
		private Long id;
		private Long clientId;
		private Long projectId;
	    private Long activityId;
	    private Long taskActivityId;
	    private Long userId;
	    private Long taskId;
	    private Long statusId;
	    private boolean plannedActivity;
	    private Date tsDate;
	    private  float tsTotalTime;
	    private String createdBy;
	    private String modifiedBy;
	    private short isDelete;
	    
}
