package com.poho.stuup.service;



import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;


public interface IExportService {

    boolean exportReward(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);

    boolean exportContest(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);

    boolean exportCertificate(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);

    boolean exportMilitary(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);

    boolean exportPolitical(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);

    boolean exportVolunteer(String yearId, String deptId, InputStream inputStream, HttpServletResponse response);
}
