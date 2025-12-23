package com.wangkang.goodwillghservice.feature.user.tiler;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TilerService {

    Page<User> getTilerPage(Pageable pageable);
}
