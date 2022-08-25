package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 16:16 2020/10/16
 * @Modified By:
 */
public interface IExportService {
    /**
     *
     * @param baseDoc
     * @param baseUrl
     * @param oid
     * @return
     */
    ResponseModel exportRegPdf(String baseDoc, String baseUrl, Long oid);

    /**
     *
     * @param yearId
     * @return
     */
    boolean exportResult(Long yearId, InputStream inputStream, HttpServletResponse response);

    /**
     *
     * @param yearId
     * @param deptId
     * @param inputStream
     * @param response
     * @return
     */
    boolean exportStaff(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);
}
