package com.viettelpost.core.base;

import com.viettelpost.core.services.domains.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {
    protected UserInfo getCurrentUser() throws Exception {
        UserInfo info = null;
        try {
            info = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
        }
        if (info == null || info.getUserId() == null || !info.verify()) {
            throw new VtException(401, "Tài khoản đã được đăng nhập ở một nơi khác");
        }
        return info;
    }
}
