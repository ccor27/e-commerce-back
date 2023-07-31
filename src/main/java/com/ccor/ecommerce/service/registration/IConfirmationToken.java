package com.ccor.ecommerce.service.registration;

import com.ccor.ecommerce.model.ConfirmationToken;

public interface IConfirmationToken {
    public void saveConfirmationToken(ConfirmationToken token);
}
