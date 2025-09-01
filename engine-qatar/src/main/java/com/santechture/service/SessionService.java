package com.santechture.service;

import com.santechture.request.Activity;
import jakarta.enterprise.context.ApplicationScoped;
import org.kie.api.runtime.KieSession;

import java.util.List;

@ApplicationScoped
public class SessionService {
//    public void addListToSession(List<Object> list, KieSession session) {
//        if(list != null)
//        {
//            for (Object a : list) {
//                session.insert(a);
//            }
//        }
//    }


    public <T> void addListToSession(List<T> list, KieSession session) {
        if (list != null) {
            for (T item : list) {
                session.insert(item);
            }
        }
    }

}
