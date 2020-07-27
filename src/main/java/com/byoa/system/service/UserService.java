package com.byoa.system.service;

import com.byoa.common.domain.Tree;
import com.byoa.system.domain.DeptDTO;
import com.byoa.system.domain.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {

    List<UserDTO> list(Map<String, Object> map);

    UserDTO get(Long userId);

    Long[] listAllDept();

    int count(Map<String, Object> map);

    boolean exit(Map<String, Object> params);

    int save(Map<String, Object> map) ;

    int update(Map<String, Object> map);

    int remove(Long userId);

    int batchRemove(Long[] userIds);

    Tree<DeptDTO> getTree();

    int resetPwd(UserDTO record);

    Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;

    int updateSal(Map<String, Object> map);
}
