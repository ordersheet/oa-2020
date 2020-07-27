package com.byoa.activiti.service;

import org.activiti.engine.repository.Model;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *@autuor ysk@bysj
 */
@Service
public interface ProcessService {
    Model convertToModel(String procDefId) throws Exception;

    InputStream resourceRead(String id, String resType) throws Exception;
}
