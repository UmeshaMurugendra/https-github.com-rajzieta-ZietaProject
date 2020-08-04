package com.zietaproj.zieta.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zietaproj.zieta.dto.ProcessMasterDTO;
import com.zietaproj.zieta.dto.ProcessStepsDTO;
import com.zietaproj.zieta.model.ProcessMaster;
import com.zietaproj.zieta.model.ProcessSteps;
import com.zietaproj.zieta.repository.ProcessMasterRepository;
import com.zietaproj.zieta.repository.ProcessStepsRepository;
import com.zietaproj.zieta.service.ProcessService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProcessServiceImpl implements ProcessService {
	
	@Autowired
	ProcessMasterRepository processMasterRepository;
	
	@Autowired
	ProcessStepsRepository processStepsRepository;

	@Autowired
	ModelMapper modelMapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessServiceImpl.class);
	
	
public List<ProcessMasterDTO> getAllProcess() {
		
		List<ProcessMaster> processmasters = processMasterRepository.findAll();
		List<ProcessMasterDTO> processmasterDTOs = new ArrayList<ProcessMasterDTO>();
		ProcessMasterDTO processMasterDTO = null;
		for (ProcessMaster processmaster : processmasters) {
			processMasterDTO = modelMapper.map(processmaster,ProcessMasterDTO.class);
			processmasterDTOs.add(processMasterDTO);
	}
	
		return processmasterDTOs;
}
	@Override
	public void addProcess(ProcessMaster processmaster) {
		processMasterRepository.save(processmaster);
	}


	public void editProcessById(@Valid ProcessMasterDTO processmasterDto) throws Exception {
		
		Optional<ProcessMaster> processMasterEntity = processMasterRepository.findById(processmasterDto.getId());
		if(processMasterEntity.isPresent()) {
			ProcessMaster processMaster = modelMapper.map(processmasterDto, ProcessMaster.class);
			processMasterRepository.save(processMaster);
			
		}else {
			throw new Exception("Process not found with the provided ID : "+processmasterDto.getId());
		}
		
	}
	
	
	public void deleteProcessById(Long id) throws Exception {
		
		Optional<ProcessMaster> processmaster = processMasterRepository.findById(id);
		if (processmaster.isPresent()) {
			processMasterRepository.deleteById(id);

		}else {
			log.info("No Process found with the provided ID{} in the DB",id);
			throw new Exception("No Process found with the provided ID in the DB :"+id);
		}
		
		
	}
	
	
	
	
	//Implementation for Process Steps API's
	
	
	
	
	public List<ProcessStepsDTO> getAllProcessSteps() {
		
		List<ProcessSteps> processsteps = processStepsRepository.findAll();
		List<ProcessStepsDTO> processstepsDTOs = new ArrayList<ProcessStepsDTO>();
		ProcessStepsDTO processStepDTO = null;
		for (ProcessSteps processstep : processsteps) {
			processStepDTO = modelMapper.map(processstep,ProcessStepsDTO.class);
			processstepsDTOs.add(processStepDTO);
	}
	
		return processstepsDTOs;
}
	@Override
	public void addProcessSteps(ProcessSteps processstep) {
		processStepsRepository.save(processstep);
	}


	public void editProcessStepsById(@Valid ProcessStepsDTO processstepsDto) throws Exception {
		
		Optional<ProcessSteps> processStepsEntity = processStepsRepository.findById(processstepsDto.getId());
		if(processStepsEntity.isPresent()) {
			ProcessSteps processStep = modelMapper.map(processstepsDto, ProcessSteps.class);
			processStepsRepository.save(processStep);
			
		}else {
			throw new Exception("ProcessStep not found with the provided ID : "+processstepsDto.getId());
		}
		
	}
	
	
	public void deleteProcessStepsById(Long id) throws Exception {
		
		Optional<ProcessSteps> processstep = processStepsRepository.findById(id);
		if (processstep.isPresent()) {
			processStepsRepository.deleteById(id);

		}else {
			log.info("No ProcessStep found with the provided ID{} in the DB",id);
			throw new Exception("No ProcessStep found with the provided ID in the DB :"+id);
		}
		
		
	}
	
}