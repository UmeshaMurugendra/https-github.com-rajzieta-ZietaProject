package com.zietaproj.zieta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zietaproj.zieta.model.ProjectMaster;
import com.zietaproj.zieta.model.RoleMaster;


@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, Long>{

	List<ProjectMaster> findByClientId(long client_id);

	List<ProjectMaster> findByClientIdAndIsDelete(Long clientId, short notDeleted);
	
}
