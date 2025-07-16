package com.example.marketingagencymanagement.service;

import com.example.marketingagencymanagement.dto.ClientDto;
import dev.samstevens.totp.exceptions.QrGenerationException;

public interface IClientService {
    public String create(ClientDto clientDto) throws QrGenerationException;
}
