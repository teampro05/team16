package com.pro06.service;

import com.pro06.entity.Faq;
import com.pro06.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface FaqService {
    public Faq faqInsert(Faq faq);
    public Faq faqUpdate(Faq faq);
    public void faqDelete(Integer no);
    public List<Faq> faqList();

}
