package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.CertificateExcelDTO;
import com.poho.stuup.model.dto.CertificateSearchDTO;

import java.util.List;
import java.util.Map;

public interface ICertificateService {

    ResponseModel findDataPageResult(CertificateSearchDTO searchDTO);

    Map<String, Object> importList(List<CertificateExcelDTO> list);
}
