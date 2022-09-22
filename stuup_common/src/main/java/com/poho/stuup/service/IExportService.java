package com.poho.stuup.service;



import com.poho.stuup.model.dto.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;


public interface IExportService {

    boolean exportReward(InputStream inputStream, HttpServletResponse response, RewardSearchDTO searchDTO);

    boolean exportContest(InputStream inputStream, HttpServletResponse response, ContestSearchDTO searchDTO);

    boolean exportCertificate(InputStream inputStream, HttpServletResponse response, CertificateSearchDTO searchDTO);

    boolean exportMilitary(InputStream inputStream, HttpServletResponse response, MilitarySearchDTO searchDTO);

    boolean exportPolitical(InputStream inputStream, HttpServletResponse response, PoliticalSearchDTO searchDTO);

    boolean exportVolunteer(InputStream inputStream, HttpServletResponse response, VolunteerSearchDTO searchDTO);
}
