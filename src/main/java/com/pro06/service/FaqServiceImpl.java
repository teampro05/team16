package com.pro06.service;

import com.pro06.entity.Faq;
import com.pro06.entity.Role;
import com.pro06.entity.Status;
import com.pro06.entity.User;
import com.pro06.repository.FaqRepository;
import com.pro06.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class FaqServiceImpl implements FaqService {

    @Autowired
    private FaqRepository faqRepository;

    @Override
    public Faq faqInsert(Faq faq) {
        return faqRepository.save(faq);
    }

    @Override
    public Faq faqUpdate(Faq faq) {
        return faqRepository.save(faq);
    }

    @Override
    public void faqDelete(Integer no) {
        faqRepository.deleteById(no);
    }

    @Override
    public List<Faq> faqList() {
        return faqRepository.findAll();
    }
}
