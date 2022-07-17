package com.viettelpost.core.cfgs.exception;

import com.viettelpost.core.base.BaseResponse;
import com.viettelpost.core.base.VtException;
import com.viettelpost.core.utils.LanguageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private LanguageUtils languageUtils;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<BaseResponse> handleException(Exception ex) {
        return handleMessageException(ex);
    }

    private ResponseEntity<BaseResponse> handleMessageException(Exception ex) {
        if (ex instanceof VtException) {
            return processPopulateException((VtException) ex);
        } else {
            logger.error(ex.getLocalizedMessage(), ex);
            BaseResponse errorDetails = new BaseResponse();
            errorDetails.setError(true);
            errorDetails.setMessage("Hệ thống đang bận vui lòng thử lại sau");
            return new ResponseEntity<>(errorDetails, HttpStatus.OK);
        }
    }

    private ResponseEntity<BaseResponse> processPopulateException(VtException ex) {
        if (ex.getCode() == 401) {
            BaseResponse errorDetails = new BaseResponse();
            errorDetails.setError(true);
            errorDetails.setMessage("Tài khoản đã được đăng nhập ở một nơi khác");
            return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
        } else {
            logger.error(ex.getMessage());
            BaseResponse errorDetails = new BaseResponse();
            errorDetails.setError(true);
            errorDetails.setMessage(ex.getLocalizedMessage());
            return new ResponseEntity<>(errorDetails, HttpStatus.OK);
        }
    }
}
