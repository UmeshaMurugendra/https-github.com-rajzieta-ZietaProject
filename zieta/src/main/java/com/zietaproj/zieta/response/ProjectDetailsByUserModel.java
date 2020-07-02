package com.zietaproj.zieta.response;

import com.zietaproj.zieta.model.CustInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDetailsByUserModel {
	
	//project info details starts
	long projectInfoId;
	long clientId;
	String projectCode;
	String projectName;
	long projectType;
	long projectOrgnode;
	long projectManager;
	short allowUnplanned;
	long custId;
	long projectStatus;
	String createdBy;
	String modifiedBy;
	//project info details ends
	
	//additional details starts
	String projectTypeName;
	String orgNodeName;
	String projectManagerName;
	String projectStatusDescription;
	String clientCode;
	CustInfo custInfo;
	//additional details ends
	
}
