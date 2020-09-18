package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RsEventService {

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;
}
