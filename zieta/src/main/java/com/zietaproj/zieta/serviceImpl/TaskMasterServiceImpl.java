package com.zietaproj.zieta.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zietaproj.zieta.dto.TaskMasterDTO;
import com.zietaproj.zieta.model.ProjectInfo;
import com.zietaproj.zieta.model.TaskInfo;
import com.zietaproj.zieta.model.TaskMaster;
import com.zietaproj.zieta.model.TasksByUser;
import com.zietaproj.zieta.model.UserInfo;
import com.zietaproj.zieta.repository.ProjectInfoRepository;
import com.zietaproj.zieta.repository.StatusMasterRepository;
import com.zietaproj.zieta.repository.TaskInfoRepository;
import com.zietaproj.zieta.repository.TaskMasterRepository;
import com.zietaproj.zieta.repository.TasksByUserRepository;
import com.zietaproj.zieta.repository.UserInfoRepository;
import com.zietaproj.zieta.response.TasksByClientProjectResponse;
import com.zietaproj.zieta.response.TasksByUserModel;
import com.zietaproj.zieta.response.TasktypesByClientResponse;
import com.zietaproj.zieta.service.TaskMasterService;
import com.zietaproj.zieta.util.TSMUtil;

@Service
public class TaskMasterServiceImpl implements TaskMasterService {

	@Autowired
	TaskMasterRepository taskMasterRepository;

	@Autowired
	TasksByUserRepository tasksByUserRepository;

	@Autowired
	ProjectInfoRepository projectInfoRepository;

	@Autowired
	TaskInfoRepository taskInfoRepository;
	
	@Autowired
	StatusMasterRepository statusRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public List<TaskMasterDTO> getAllTasks() {
		List<TaskMaster> taskMasters = taskMasterRepository.findAll();
		List<TaskMasterDTO> taskMasterDTOs = new ArrayList<TaskMasterDTO>();
		TaskMasterDTO taskMasterDTO = null;
		for (TaskMaster taskMaster : taskMasters) {
			taskMasterDTO = new TaskMasterDTO();
			taskMasterDTO.setId(taskMaster.getId());
			taskMasterDTO.setTask_type(taskMaster.getType_name());
			taskMasterDTO.setClient_id(taskMaster.getClientId());
			taskMasterDTO.setModified_by(taskMaster.getModified_by());
			taskMasterDTO.setCreated_by(taskMaster.getCreated_by());
			taskMasterDTOs.add(taskMasterDTO);
		}
		return taskMasterDTOs;
	}

	@Override
	public void addTaskmaster(TaskMaster taskmaster) {
		taskMasterRepository.save(taskmaster);
	}

	@Override
	public List<TasksByUserModel> findByClientIdAndUserId(Long clientId, Long userId) {

		List<TasksByUserModel> tasksByUserModelList = new ArrayList<>();

		List<TasksByUser> tasksByUserList = tasksByUserRepository.findByClientIdAndUserId(clientId, userId);
		TasksByUserModel tasksByUserModel = null;
		for (TasksByUser tasksByUser : tasksByUserList) {
			tasksByUserModel = new TasksByUserModel();
			long projectId = tasksByUser.getProject_id();
			long taskId = tasksByUser.getTask_id();
			long userIdent = tasksByUser.getUserId();
			TaskInfo taskInfo = taskInfoRepository.findById(taskId).get();
			String taskName = taskInfo.getTask_name();
			ProjectInfo projectInfo = projectInfoRepository.findById(projectId).get();
			String projectName = projectInfo.getProject_name();
			tasksByUserModel.setProjectId(projectId);
			tasksByUserModel.setProject_name(projectName);
			tasksByUserModel.setTask_name(taskName);
			tasksByUserModel.setTaskId(taskId);
			tasksByUserModel.setUserId(userIdent);
			tasksByUserModel.setProject_code(tasksByUser.getProject_code());
			tasksByUserModel.setTask_code(tasksByUser.getTask_code());
			tasksByUserModelList.add(tasksByUserModel);
		}

		return tasksByUserModelList;

	}

	@Override
	public List<TasksByClientProjectResponse> findByClientIdAndProjectId(Long clientId, Long projectId) {
		List<TaskInfo> taskInfoList = taskInfoRepository.findByClientIdAndProjectId(clientId, projectId);
		
		List<TasksByClientProjectResponse> tasksByClientProjectResponseList = new ArrayList<>();
		
		for(TaskInfo taskInfo: taskInfoList) {
			TasksByClientProjectResponse tasksByClientProjectResponse = new TasksByClientProjectResponse();
			ProjectInfo projectInfo = projectInfoRepository.findById(taskInfo.getProjectId()).get();
			TaskMaster taskmaster = taskMasterRepository.findById(taskInfo.getTask_type()).get();
			tasksByClientProjectResponse.setId(taskInfo.getId());
			tasksByClientProjectResponse.setProject_id(taskInfo.getProjectId());
			tasksByClientProjectResponse.setTaskCode(taskInfo.getTask_code());
			tasksByClientProjectResponse.setTask_type(taskInfo.getTask_type());
			tasksByClientProjectResponse.setTask_parent(taskInfo.getTask_parent());
			tasksByClientProjectResponse.setTask_status(taskInfo.getTask_status());
			tasksByClientProjectResponse.setTaskDescription(taskInfo.getTask_name());
			tasksByClientProjectResponse.setTasktypeDescription(taskmaster.getType_name());
			tasksByClientProjectResponse.setProjectCode(projectInfo.getProject_code());
			tasksByClientProjectResponse.setProjectDescription(projectInfo.getProject_name());	
			UserInfo userInfo = userInfoRepository.findById(taskInfo.getTask_manager()).get();
			String userName = TSMUtil.getFullName(userInfo);
			tasksByClientProjectResponse.setTaskManager(userName);
			tasksByClientProjectResponse
					.setTaskStatusDescription(statusRepository.findById(taskInfo.getTask_status()).get().getStatus());
			tasksByClientProjectResponseList.add(tasksByClientProjectResponse);
			
		}
		return tasksByClientProjectResponseList;
	}

	
	public List<TasktypesByClientResponse> getTasksByClient(Long clientId) {
		List<TaskMaster> tasksByClientList = taskMasterRepository.findByClientId(clientId);
		List<TasktypesByClientResponse> tasksByClientResponseList = new ArrayList<>();
		TasktypesByClientResponse tasksByClientResponse = null;
		for (TaskMaster tasksByClient : tasksByClientList) {
			tasksByClientResponse = modelMapper.map(tasksByClient, 
					TasktypesByClientResponse.class);
		
			tasksByClientResponseList.add(tasksByClientResponse);
	}
		return tasksByClientResponseList;
}

	@Override
	@Transactional
	public boolean saveTaskInfo(TaskInfo taskInfo) {
		try {
			TaskInfo taskInfoDB = taskInfoRepository.save(taskInfo);
			System.out.println("Task ID: "+taskInfoDB.getId());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

	}
}
