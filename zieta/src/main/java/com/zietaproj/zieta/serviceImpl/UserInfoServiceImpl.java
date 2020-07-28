package com.zietaproj.zieta.serviceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zietaproj.zieta.dto.UserInfoDTO;
import com.zietaproj.zieta.model.ScreensMaster;
import com.zietaproj.zieta.model.UserAccessType;
import com.zietaproj.zieta.model.UserInfo;
import com.zietaproj.zieta.repository.AccessTypeScreenMappingRepository;
import com.zietaproj.zieta.repository.ClientInfoRepository;
import com.zietaproj.zieta.repository.UserInfoRepository;
import com.zietaproj.zieta.response.LoginResponse;
import com.zietaproj.zieta.response.UserDetailsResponse;
import com.zietaproj.zieta.service.AccessTypeMasterService;
import com.zietaproj.zieta.service.ScreensMasterService;
import com.zietaproj.zieta.service.UserAccessTypeService;
import com.zietaproj.zieta.service.UserInfoService;



@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	UserInfoRepository userInfoRepositoryRepository;
	
	@Autowired
	AccessTypeScreenMappingRepository accessControlConfigRepository;
	
	@Autowired
	UserAccessTypeService userAccessTypeService;
	
	@Autowired
	ScreensMasterService screensMasterService;
	
	@Autowired
	AccessTypeMasterService accessTypeMasterService;
	
	@Autowired
	ClientInfoRepository clientInfoRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<UserInfoDTO> getAllUserInfoDetails() {
		List<UserInfo> userInfoList= userInfoRepositoryRepository.findAll();
		List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
		mapUserInfoModelToDTO(userInfoList, userInfoDTOs);
		return userInfoDTOs;
	}

	private void mapUserInfoModelToDTO(List<UserInfo> userInfoList, List<UserInfoDTO> userInfoDTOs) {
		UserInfoDTO userInfoDTO = null;
		for (UserInfo userInfo : userInfoList) {
			userInfoDTO =  modelMapper.map(userInfo, UserInfoDTO.class);
			userInfoDTO.setPassword("********");
			userInfoDTO.setClientCode(clientInfoRepo.findById(userInfo.getClientId()).get().getClientCode());
			userInfoDTO.setClientDescription(clientInfoRepo.findById(userInfo.getClientId()).get().getClientName());
			 userInfoDTOs.add(userInfoDTO);
		}
	}

	@Override
	public UserInfoDTO findByEmail(String email) {
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		UserInfo userInfo = userInfoRepositoryRepository.findByEmail(email);
		if(userInfo !=null) {
			userInfoDTO =  modelMapper.map(userInfo, UserInfoDTO.class);
		}
		return userInfoDTO;
		
	}

	@Override
	public UserDetailsResponse getUserData(String emailId) {
		UserInfo userInfo = userInfoRepositoryRepository.findByEmail(emailId);
		
		List<UserAccessType> userAccessTypeList = userAccessTypeService.
				findByClientIdAndUserId(userInfo.getClientId(), userInfo.getId());
		List<Long> accessIdList = userAccessTypeList.stream().map(UserAccessType::getAccessTypeId)
										.collect(Collectors.toList());
				
		 List<Long> accessControlConfigList = accessControlConfigRepository.
				 findByClientIdANDAccessTypeId(userInfo.getClientId(), accessIdList);
		 List<ScreensMaster> screensListByClientId = screensMasterService.getScreensByIds(accessControlConfigList);
		 List<String> accessTypes = accessTypeMasterService.findByClientIdANDAccessTypeId(userInfo.getClientId(), accessIdList);
		 UserDetailsResponse userDetails = fillUserData(userInfo);
		 userDetails.setClientCode(clientInfoRepo.findById(userInfo.getClientId()).get().getClientCode());
		 userDetails.setClientDescription(clientInfoRepo.findById(userInfo.getClientId()).get().getClientName());
		 userDetails.setScreensByClient(screensListByClientId);
		 userDetails.setAccessTypesByClient(accessTypes);
		
		return userDetails;
	}

	
	private UserDetailsResponse fillUserData(UserInfo userInfo) {
		
		UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
		userDetailsResponse.setClientId(userInfo.getClientId());
		userDetailsResponse.setFirstName(userInfo.getUserFname());
		userDetailsResponse.setMiddleName(userInfo.getUserMname());
		userDetailsResponse.setLastName(userInfo.getUserLname());
		userDetailsResponse.setUserEmailId(userInfo.getEmail());
		userDetailsResponse.setStatus(userInfo.getIsActive());
		userDetailsResponse.setUserId(userInfo.getId());
		userDetailsResponse.setInfoMessage("User details after successful login");
		
		return userDetailsResponse;
	}
	
	
	public LoginResponse authenticateUser(String email, String password) {
		LoginResponse loginResponse = LoginResponse.builder().infoMessage("").build();
		
		UserInfoDTO dbUserInfo = findByEmail(email);
		if (dbUserInfo != null) {
			if (password.equals(dbUserInfo.getPassword())) {
				loginResponse.setActive(dbUserInfo.getIsActive() !=0);
				loginResponse.setInfoMessage("Valid credentials are provided !!");
				loginResponse.setLoginValid(true);
				
				return loginResponse;
				
			} else {
				loginResponse.setInfoMessage("Provided credentials are wrong... "
						+ email);
				return loginResponse;
			}
		}else {
			loginResponse.setInfoMessage("Invalid emailId provided: "+ email);
			return loginResponse;
		}
		
		
	}

	@Override
	public List<UserInfoDTO> findByClientId(Long client_id) {
		List<UserInfo> userInfoList = userInfoRepositoryRepository.findByClientId(client_id);
		List<UserInfoDTO> userInfoDTOs = new ArrayList<UserInfoDTO>();
		mapUserInfoModelToDTO(userInfoList, userInfoDTOs);
		return userInfoDTOs;
	}

	

	
}
