package com.zietaproj.zieta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zietaproj.zieta.model.ProjectInfo;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {
	
	List<ProjectInfo> findByClientId(long clientId);
	
	List<ProjectInfo> findByProjectManager(long projectManager);

	List<ProjectInfo> findByIsDelete(short notDeleted);

	List<ProjectInfo> findByProjectManagerAndIsDelete(long projectManagerId, short notDeleted);

	List<ProjectInfo> findByClientIdAndIsDelete(Long clientId, short notDeleted);

	

}
