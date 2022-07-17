package com.viettelpost.core.controller;

import com.viettelpost.core.base.BaseController;
import com.viettelpost.core.controller.forms.GomBillForm;
import com.viettelpost.core.controller.responses.*;
import com.viettelpost.core.services.GiaoHangService;
import com.viettelpost.core.services.domains.UserInfo;
import com.viettelpost.core.utils.LanguageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("giao-hang")
public class GiaoHangController extends BaseController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LanguageUtils languageUtils;

    @Autowired
    GiaoHangService giaoHangService;

    @RequestMapping(value = "gom-bill-txxh", method = RequestMethod.POST)
    public ResponseEntity gomBillTxxh(@RequestBody GomBillForm data) throws Exception {
        UserInfo user = getCurrentUser();
        GomBillResponse gomBillReponse = giaoHangService.gomBillTxxh(user, data);
        return new ResponseEntity<>(gomBillReponse, HttpStatus.OK);
    }

    private String ok(String Em) {
        return "";
    }
}
