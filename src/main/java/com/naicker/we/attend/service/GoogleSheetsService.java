package com.naicker.we.attend.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.naicker.we.attend.data.payloads.MessageResponse;

public interface GoogleSheetsService {

    MessageResponse loadTimesheet();

}
