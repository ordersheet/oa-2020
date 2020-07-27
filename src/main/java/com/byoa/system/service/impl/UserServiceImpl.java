package com.byoa.system.service.impl;

import com.byoa.common.config.Constant;
import com.byoa.common.domain.FileDO;
import com.byoa.common.domain.Tree;
import com.byoa.common.service.FileService;
import com.byoa.common.utils.BuildTree;
import com.byoa.common.utils.FileType;
import com.byoa.common.utils.FileUtil;
import com.byoa.common.utils.ImageUtils;
import com.byoa.system.config.JackSonUtils;
import com.byoa.system.config.StringUtils;
import com.byoa.system.dao.DeptDTOMapper;
import com.byoa.system.dao.UserDTOMapper;
import com.byoa.system.dao.UserRoleMapper;
import com.byoa.system.domain.DeptDTO;
import com.byoa.system.domain.UserDTO;
import com.byoa.system.domain.UserRoleDTO;
import com.byoa.system.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDTOMapper userMapper;

    @Autowired
    DeptDTOMapper deptMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    FileService fileService;

    @Override
    public List<UserDTO> list(Map<String, Object> map) {
        return userMapper.list(map);
    }

    @Override
    public UserDTO get(Long userId) {
        UserDTO user = userMapper.getByPrimaryKey(userId);
        DeptDTO dept = deptMapper.getDeptById(user.getDeptId());
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(dept));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        user.setDeptName(dept.getName());
        return user;
    }

    @Override
    public Long[] listAllDept() {
        return userMapper.listAllDept();
    }

    @Override
    public int count(Map<String, Object> map) {
        return 0;
    }

    public boolean exit(Map<String, Object> params) {
        boolean exit;
        exit = userMapper.list(params).size() > 0;
        return exit;
    }

    @Override
    public int save(Map<String, Object> map) {
        System.out.println("userServiceImpl.save:map" + JackSonUtils.toJackString(map));
        UserDTO record = new UserDTO();
        if (map.get("name") != null) {
            record.setName(map.get("name").toString());
        }
        if (map.get("username") != null) {
            record.setUsername(map.get("username").toString());
        }
        if (map.get("password") != null) {

            record.setPassword(map.get("password").toString());
        }
        if (map.get("deptId") != null) {

            record.setDeptId(Long.valueOf(map.get("deptId").toString()));
        }
        if (map.get("email") != null) {

            record.setEmail(map.get("email").toString());
        }
        if (map.get("status") != null) {

            record.setStatus(Integer.valueOf(map.get("status").toString()));
        }

        System.out.println("userServiceImpl.save:record:" + JackSonUtils.toJackString(record));


        int save = userMapper.save(record);
        if (save <= 0) {
            return 0;
        }
        Long userId = record.getUserId();

        Long[] roleIds = StringUtils.getLongArray(map.get("roleIds").toString());
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        List<UserRoleDTO> list = new ArrayList<>();
        for (int i = 0; i < roleIds.length; i++) {
            userRoleDTO.setRoleId(roleIds[i]);
            userRoleDTO.setUserId(record.getUserId());
            list.add(userRoleDTO);
        }

        int i = userRoleMapper.batchSave(list);
        return i;
    }

    @Override
    public int update(Map<String, Object> map) {

        System.out.println("userService.update:map:" + JackSonUtils.toJackString(map));
        Long userId = Long.valueOf(map.get("userId").toString());
        Long deptId = Long.valueOf(map.get("deptId").toString());
        String name = map.get("name").toString();
        String username = map.get("username").toString();
        String email = map.get("email").toString();
        Integer status = Integer.valueOf(map.get("status").toString());
        Long[] roleIds = StringUtils.getLongArray(map.get("roleIds").toString());
        UserDTO record = new UserDTO();
        record.setUserId(userId);
        record.setDeptId(deptId);
        record.setName(name);
        record.setUsername(username);
        record.setEmail(email);
        record.setStatus(status);

//        System.out.println("userService.update:record:" + JackSonUtils.toJackString(record));

        int r = userMapper.update(record);

        int removeByUserId = userRoleMapper.removeByUserId(userId);

        List<UserRoleDTO> list = new ArrayList<>();
        System.out.println("userServiceImpl.update:roleIds:" + JackSonUtils.toJackString(roleIds));
        for (Long roleId : roleIds) {
            UserRoleDTO roleDTO = new UserRoleDTO();
//            System.out.println(roleId);
            roleDTO.setUserId(userId);
            roleDTO.setRoleId(roleId);
            list.add(roleDTO);
        }
//        System.out.println("userServiceImpl.update:list<UserRoleDTO>:" + list);
        int i = userRoleMapper.batchSave(list);
        if (i < 0 || r < 0 || removeByUserId < 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int remove(Long userId) {
        int remove = userMapper.remove(userId);
        //删除user - role  user_role表中对应的数据
        int i = userRoleMapper.removeByUserId(userId);
        if (i >= 0 && remove >= 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int batchRemove(Long[] userIds) {
        int batchremove = userMapper.batchremove(userIds);
        int i = userRoleMapper.batchRemoveByUserId(userIds);
        if (batchremove <= 0 || i <= 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public Tree<DeptDTO> getTree() {
        List<Tree<DeptDTO>> trees = new ArrayList<>();
        List<DeptDTO> depts = deptMapper.list(new HashMap<>(16));
        Long[] dDepts = deptMapper.listParentDept();
        Long[] uDepts = userMapper.listAllDept();
        Long[] allDepts = (Long[]) ArrayUtils.addAll(dDepts, uDepts);

        for (DeptDTO dept : depts) {
            if (!ArrayUtils.contains(allDepts, dept.getDeptId())) {
                continue;
            }
            Tree<DeptDTO> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<UserDTO> users = userMapper.list(new HashMap<>(16));

        for (UserDTO user : users) {
            Tree<DeptDTO> tree = new Tree<DeptDTO>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        Tree<DeptDTO> t = BuildTree.build(trees);
        return t;
    }


    public int resetPwd(UserDTO userDTO) {
        return userMapper.update(userDTO);
    }


    @Override
    public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception  {
        String fileName = file.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        FileDO sysFile = new FileDO(FileType.fileType(fileName), Constant.FileUploadPath+ fileName, new Date());

        //获取图片后缀
        String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
        String[] str = avatar_data.split(",");
        //获取截取的x坐标
        int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
        //获取截取的y坐标
        int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
        //获取截取的高度
        int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
        //获取截取的宽度
        int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
        //获取旋转的角度
        int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
        try {
            BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
            BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(rotateImage, prefix, out);
            //转换后存入数据库
            byte[] b = out.toByteArray();
            FileUtil.uploadFile(b, Constant.FileUploadPath, fileName);
        } catch (Exception e) {
            throw new Exception("图片裁剪错误！！");
        }
            Map<String, Object> result = new HashMap<>();
            if (fileService.save(sysFile) > 0) {
                UserDTO userDO = new UserDTO();
                userDO.setUserId(userId);
                userDO.setPicId(sysFile.getId());
                if (userMapper.update(userDO) > 0) {
                    String url = sysFile.getUrl().substring(Constant.FileUploadPath.length());
                    result.put("url", Constant.FileAbPath+url);
                }
            }
            return result;
    }

    @Override
    public int updateSal(Map<String, Object> map) {
        String userId = map.get("userId").toString();
        String sal = map.get("sal").toString();
        UserDTO record = new UserDTO();
        record.setUserId(Long.valueOf(userId));
        record.setSal(Integer.valueOf(sal));
        System.out.println(record.toString());
        return userMapper.update(record);
    }

}
